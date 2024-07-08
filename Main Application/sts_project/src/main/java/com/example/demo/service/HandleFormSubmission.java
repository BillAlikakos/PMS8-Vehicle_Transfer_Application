package com.example.demo.service;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.datatypes.AuthRequest;
import com.example.demo.datatypes.AuthResponse;
import com.example.demo.datatypes.FormData;
import com.example.demo.datatypes.FormRequest;
import com.example.demo.datatypes.NotificationRequest;
import com.example.demo.datatypes.Response;
import com.example.demo.datatypes.RuntimeException;
import com.example.demo.datatypes.UpdateVehicleRequest;
import com.example.demo.datatypes.VehiclesResponse;
import com.example.demo.db.Form;
import com.example.demo.db.FormRepository;
import com.example.demo.db.Vehicle;
import com.example.demo.util.CommonMethods;
import com.example.demo.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class HandleFormSubmission
{

	@Value("${NOTIFICATION_SERVICE_ENDPOINT}")
	private String notificationServiceEndpoint;
	@Value("${AUTH_SERVICE_ENDPOINT}")
	private String userAuthEndpoint;
	@Autowired
	private FormRepository formRepo;
	@Autowired
	private CommonMethods commonMethods;
	@Autowired
	private HandleVehicleRetrieval vehicleRetrieval;
	@Autowired
	private HandleVehicleUpdate vehicleUpdate;

	private Logger logger = LogManager.getLogger();

	/**
	 * Handles the submission of new and updated forms.
	 * @param request - The incoming request object.
	 * @param accessToken - The keycloak accessToken of the user.
	 * @return - The result of the operation.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws RuntimeException
	 */
	public Response submitForm(FormRequest request, String accessToken) throws JsonMappingException, JsonProcessingException, RuntimeException
	{
		
		logger.info("[submitForm] isUpdate? "+request.getIsUpdate());
		
		Optional<Form> existingEntityOptional = formRepo.findById(request.getFormID());
		Form f = null;
		
		if(!request.getIsUpdate())
		{
			
			Pattern pattern = Pattern.compile(Constants.TAX_ID_REGEX);
	        Matcher m = pattern.matcher(request.getTaxID());
	        
	        if(!m.matches())
	        {
	        	return new Response(Constants.FAILURE_STATUS_CODE, Constants.TAX_ID_ERR, Constants.INVALID_TAX_ID);
	        }
			
			logger.debug("[submitForm] Insert case, will check if form already exists");
			
	        if(existingEntityOptional.isPresent()) 
	        {
	        	return new Response(Constants.FAILURE_STATUS_CODE, Constants.DB_ERR, Constants.ENTITY_ALREADY_EXISTS);
	        }
			else
			{
				Vehicle v = this.vehicleRetrieval.retrieveVehicleById(request.getVehicleID()).getVehicles().get(0);
				
				if(v.getStatus().equalsIgnoreCase(Constants.VEHICLE_STATUS.TRANSFER_IN_PROGRESS.toString()))
				{
					String resultMsg = MessageFormat.format(Constants.VEHICLE_TRANSFER_IN_PROGRESS, request.getVehicleID());
					
					return new Response(Constants.FAILURE_STATUS_CODE, Constants.DB_ERR, resultMsg);
				}
			}
	        
	        f = new Form(request.getFormID(), request.getOwnerID(), request.getVehicleID(), request.getDate(), request.getStatus(), request.getTaxID());
	        
	        logger.debug("[submitForm] Retrieving buyer data");
	        String buyerEmail = callUserAuthService(accessToken, request.getTaxID());
	        logger.debug("[submitForm] Retrieved user email: "+buyerEmail);
	        request.setEmail(buyerEmail);
	        
	        callNotificationService(accessToken, request, Optional.of(f), false);//Initial user notification of new assigned form
		}
		else
		{	
			if(!existingEntityOptional.isPresent()) 
	        {
	           return new Response(Constants.FAILURE_STATUS_CODE, Constants.DB_ERR, Constants.FORM_DOES_NOT_EXIST);
	        }
			else
			{
				Form existing =  existingEntityOptional.get();
				
				if(!existing.getTax_ID().equals(request.getTaxID()))
				{
					return new Response(Constants.FAILURE_STATUS_CODE, Constants.DB_ERR, MessageFormat.format(Constants.FORM_DOES_NOT_BELONG_TO_USER, request.getTaxID()));
				}
				else if(existing.getStatus().equalsIgnoreCase(Constants.FORM_STATUS.PROCESSED.name()))
				{
					return new Response(Constants.FAILURE_STATUS_CODE, Constants.DB_ERR, MessageFormat.format(Constants.FORM_ALREADY_PROCESSED, request.getFormID()));
				}
			}
			
			Form existing = existingEntityOptional.get();
			f = new Form(existing.getForm_ID(), existing.getOwner_ID(), existing.getVehicle_ID(), existing.getDate(), request.getStatus(), existing.getTax_ID());
			
			if(accessToken!=null && !accessToken.isEmpty())
			{
				if(request.getStatus().equalsIgnoreCase(Constants.FORM_STATUS.PROCESSED.name()))
				{	
					callNotificationService(accessToken, request, existingEntityOptional, false);//Send report to initial owner
				}
				
				request.setEmail(existing.getOwner_ID());
				
				callNotificationService(accessToken, request, existingEntityOptional, true);//Notification is sent to buyer for the registration here
			}
		}
		
		formRepo.save(f);

		String resultMsg = "";
		
		if(!request.getIsUpdate())
		{
			resultMsg = MessageFormat.format(Constants.SUCCESSFULLY_INSERTED, Constants.ENTITY_FORM, f.getForm_ID());
		}
		else
		{
			resultMsg = MessageFormat.format(Constants.SUCCESSFULLY_UPDATED, Constants.ENTITY_FORM, f.getForm_ID());
		}
		
		return new Response(Constants.SUCCESS_STATUS_CODE,"", resultMsg);
	}
	
	/**
	 * Retrieves the information of the vehicle, in order to populate the notification service request.
	 * @param formId - The id of the form
	 * @return
	 */
	private Vehicle getVehicleDataFromDB(String formId)
	{
		Form currentForm = this.formRepo.findByFormId(formId).get(0);
		
		VehiclesResponse resp = this.vehicleRetrieval.retrieveVehicleById(currentForm.getVehicle_ID());
		
		logger.info("[getVehicleDataFromDB] "+resp.toString());
		return resp.getVehicles().get(0);
	}
	
	/**
	 * Sends the appropriate notification request to the notification service depending on the input parameters.
	 * @param accessToken - The keycloak access token of the user.
	 * @param request - The request to be sent towards the notification service.
	 * @param existingEntityOptional - The form that was updated.
	 * @param isOwner - Flag that denotes if the recipient of the notification is the owner of a vehicle.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws RuntimeException 
	 */
	private void callNotificationService(String accessToken, FormRequest request, Optional<Form> existingEntityOptional, boolean isOwner) throws JsonMappingException, JsonProcessingException, RuntimeException
	{
		NotificationRequest notificationRequest = null;

		if(!isOwner && request.getIsUpdate() && request.getStatus().equalsIgnoreCase(Constants.FORM_STATUS.PROCESSED.toString()))
		{
			Optional<String> email = commonMethods.retrieveTokenData(accessToken, Constants.JWT_EMAIL_ATTR);
			
			if(email.isPresent() && !isOwner)
			{
				if(existingEntityOptional.isPresent())
				{
					Form form = existingEntityOptional.get();
					notificationRequest = new NotificationRequest(email.get(), request.getFormID(), false, false, request.getIsUpdate(), null);	
					
					Optional<String> firstName = commonMethods.retrieveTokenData(accessToken, Constants.JWT_FIRST_NAME_ATTR);
					Optional<String> lastName = commonMethods.retrieveTokenData(accessToken, Constants.JWT_LAST_NAME_ATTR);
					
					if(firstName.isPresent() && lastName.isPresent())
					{
						Vehicle vehicle = this.getVehicleDataFromDB(form.getForm_ID());
						FormData formData = new FormData(form.getVehicle_ID(), firstName.get(), lastName.get(), vehicle.getMake(), vehicle.getYear(), vehicle.getDisplacement(), vehicle.getModel(), vehicle.getColor(), vehicle.getDateOfInspection());
						
						notificationRequest.setData(formData);
					}
					
					updateVehicle(Constants.VEHICLE_STATUS.AVAILABLE.toString(), form.getVehicle_ID(), email.get(), false);
				}
				else
				{
					notificationRequest = new NotificationRequest(email.get(), request.getFormID(), false, false, request.getIsUpdate(), null);
				}

				commonMethods.callNotificationService(notificationRequest, notificationServiceEndpoint);
			}
		}
		else if(!isOwner)
		{
			//Initial notification
			if(existingEntityOptional.isPresent())
			{
				Form form = existingEntityOptional.get();
				if(request.getIsUpdate() && request.getStatus().equalsIgnoreCase(Constants.FORM_STATUS.ABORTED.toString()))
				{
					updateVehicle(Constants.VEHICLE_STATUS.AVAILABLE.toString(), form.getVehicle_ID(), form.getOwner_ID(),true);
					notificationRequest = new NotificationRequest(form.getOwner_ID(), request.getFormID(), true, false, request.getIsUpdate(), null);
				}
				else
				{
					updateVehicle(Constants.VEHICLE_STATUS.TRANSFER_IN_PROGRESS.toString(), form.getVehicle_ID(), request.getOwnerID(),false);
					notificationRequest = new NotificationRequest(request.getEmail(), request.getFormID(), false, false, request.getIsUpdate(), null);
				}
			
				commonMethods.callNotificationService(notificationRequest, notificationServiceEndpoint);
			}
		}
		else
		{
			//Form submitted
			if(!request.getStatus().equalsIgnoreCase(Constants.FORM_STATUS.PROCESSED.toString()))//Rejected
			{
				if(existingEntityOptional.isPresent())
				{
					Form form = existingEntityOptional.get();
					updateVehicle(Constants.VEHICLE_STATUS.AVAILABLE.toString(), form.getVehicle_ID(),null, true);/*Set vehicle back to available 
					if the form was rejected by the buyer*/
					if(request.getEmail()==null)
					{
						request.setEmail(form.getOwner_ID());
					}
				}
				
				notificationRequest = new NotificationRequest(request.getEmail(), request.getFormID(), true, false, request.getIsUpdate(), null);
			}
			else
			{
				//Approved
				notificationRequest = new NotificationRequest(request.getEmail(), request.getFormID(), true, true, request.getIsUpdate(), null);
			}

			commonMethods.callNotificationService(notificationRequest, notificationServiceEndpoint);
		}
	}
	
	/**
	 * Calls the user management application in order to retrieve the email address of the buyer that will be notified on the submission of
	 * the owner's form.
	 * @param accessToken - The keycloak access token of the user.
	 * @param taxId - The taxId of the buyer.
	 * @return the email address of the buyer.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	private String callUserAuthService(String accessToken, String taxId) throws JsonMappingException, JsonProcessingException
	{
		logger.info("[callUserAuthService] accessToken: "+accessToken);
		
		AuthRequest request = new AuthRequest();
		request.setTaxID(taxId);

		RestTemplate client = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.AUTHORIZATION, Constants.BEARER_TOKEN_PREFIX+accessToken);
		
		HttpEntity<AuthRequest> requestEntity = new HttpEntity<>(request, headers);
		
		String getDataEndpoint = userAuthEndpoint+"getData";
		logger.debug("[callUserAuthService] userAuth service endpoint: "+getDataEndpoint);
		
		HttpEntity<AuthResponse> response =  client.postForEntity(getDataEndpoint,requestEntity, AuthResponse.class);
		
		
        logger.info("[callUserAuthService] Response entity body: "+response.getBody());
        logger.info("[callUserAuthService] Response entity headers size: "+response.getHeaders().size());
		
		return response.getBody().getEmailAddress();
	}
	
	/**
	 * Updates the given vehicle's data.
	 * @param status - The new status of the vehicle.
	 * @param vehicleId - The id of the vehicle to update. 
	 * @param ownerId - The id of the new owner of the vehicle (in case the form was accepted).
	 * @param isRejected - Flag that denotes if the form was rejected, so that the vehicle's status is reset to <b>available</b>
	 * @throws RuntimeException
	 */
	private void updateVehicle(String status, String vehicleId, String ownerId, boolean isRejected) throws RuntimeException
	{	
		UpdateVehicleRequest request = new UpdateVehicleRequest();
		
		if(status != null)
		{
			if(isRejected)
			{
				request.setStatus(Constants.VEHICLE_STATUS.AVAILABLE.toString());
			}
			else
			{
				request.setStatus(status);
			}
		}
		
		if(vehicleId != null)
			request.setVehicleId(vehicleId);
		if(ownerId != null && !isRejected)
			request.setOwnerId(ownerId);
		
		Response resp = this.vehicleUpdate.updateVehicle(request);
		
		if(!resp.getResult().equals(Constants.SUCCESS_STATUS_CODE))
		{
			throw new RuntimeException(resp.getErrorCode(), resp.getErrorCode());
		}
	}
}
