package com.example.demo;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.datatypes.AddVehicleRequest;
import com.example.demo.datatypes.DeleteFormRequest;
import com.example.demo.datatypes.FormRequest;
import com.example.demo.datatypes.ModifyFormRequest;
import com.example.demo.datatypes.Response;
import com.example.demo.datatypes.RuntimeException;
import com.example.demo.service.HandleFormDeletion;
import com.example.demo.service.HandleFormRetrieval;
import com.example.demo.service.HandleFormSubmission;
import com.example.demo.service.HandleVehicleAddition;
import com.example.demo.service.HandleVehicleRetrieval;
import com.example.demo.util.CommonMethods;
import com.example.demo.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://127.0.0.1:8080", allowCredentials = "true", allowedHeaders = "*")
@RestController
public class DemoController 
{
	@Autowired
	private HandleFormRetrieval retrievalOperations;
	@Autowired
	private HandleFormSubmission submissionOperations;
	@Autowired
	private HandleFormDeletion deletionOperations;
	@Autowired
	private HandleVehicleAddition addVehicleOperations;
	@Autowired
	private HandleVehicleRetrieval retrieveVehicleOperations;
	@Autowired
	private CommonMethods cm;
	
	private Logger logger =  LogManager.getLogger();
	
	private Response response = new Response();
	
	@GetMapping(value="/retrieve")
	public ResponseEntity<Response> retrieveForms(@Valid @RequestParam String userId, @RequestParam(required = false) boolean isOwner, @RequestParam(required = false) String status, @RequestHeader(value = Constants.AUTHORIZATION_HEADER, required = false) String accessToken)
	{
		logger.info("[retrieveForms] Incoming user: "+userId+"\nisOwner: "+isOwner+"\nStatus: "+status);
		
		if(!isOwner)
		{
			try
			{
				 Optional<String> taxIdOpt = cm.retrieveTokenData(accessToken, Constants.JWT_TAX_ID);
				 if(taxIdOpt.isPresent())
				 {
					 userId = taxIdOpt.get();
				 }
			} 
			catch (JsonProcessingException e)
			{
				e.printStackTrace();
			}
		}
		
		response = retrievalOperations.retrieveForms(userId, isOwner, status);
		
		return handleResponse(response);
	}
	
	
	@PostMapping(value="/submit")
	public ResponseEntity<Response> submitForm(@Valid @RequestBody FormRequest form, @RequestHeader(value = Constants.AUTHORIZATION_HEADER, required = false) String accessToken) throws JsonProcessingException
	{
		String formStatus = form.getStatus();
		if(!formStatus.equalsIgnoreCase(Constants.FORM_STATUS.NEW.toString()))
		{
			return handleResponse(new Response(Constants.FAILURE_STATUS_CODE,Constants.STATUS_ERR, Constants.INVALID_FORM_STATUS));
		}
		
		try
		{
			form.setFormID(java.util.UUID.randomUUID().toString());
			response = submissionOperations.submitForm(form, accessToken);
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
			response.setErrorCode(e.getErrorCode());
			response.setMessage(e.getMessage());
			response.setResult(Constants.FAILURE_STATUS_CODE);
		}
		
		return handleResponse(response);
	}
	
	@PutMapping(value="/updateStatus")
	public ResponseEntity<Response> updateStatus(@Valid @RequestBody ModifyFormRequest request, @RequestHeader(value = Constants.AUTHORIZATION_HEADER, required = false) String accessToken) throws JsonMappingException, JsonProcessingException, RuntimeException
	{	
		String formStatus = request.getStatus();
		if(!formStatus.equalsIgnoreCase(Constants.FORM_STATUS.ABORTED.toString())
		&& !formStatus.equalsIgnoreCase(Constants.FORM_STATUS.IN_PROGRESS.toString())
		&& !formStatus.equalsIgnoreCase(Constants.FORM_STATUS.PROCESSED.toString()))
		{
			return handleResponse(new Response(Constants.FAILURE_STATUS_CODE,Constants.STATUS_ERR, Constants.INVALID_FORM_STATUS));
		}
		
		FormRequest req = new FormRequest();
		
		try
		{
			 Optional<String> taxIdOpt = cm.retrieveTokenData(accessToken, Constants.JWT_TAX_ID);
			 if(taxIdOpt.isPresent())
			 {
				 req.setTaxID(taxIdOpt.get());
			 }
			 else
			 {
				 req.setTaxID(request.getUserID());
			 }
		} 
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		
		req.setFormID(request.getFormID());
		req.setIsUpdate(Boolean.TRUE.toString());
		req.setStatus(request.getStatus());
		
		response = submissionOperations.submitForm(req, accessToken);
		
		return handleResponse(response);
	}
	
	@DeleteMapping(value="/delete")
	public ResponseEntity<Response> deleteForm(@Valid @RequestBody DeleteFormRequest request)
	{
		response = this.deletionOperations.deleteForm(request);
		
		return handleResponse(response);
	}
	
	@PostMapping(value="/addVehicle")
	public ResponseEntity<Response> addVehicle(@Valid @RequestBody AddVehicleRequest request)
	{
		response = addVehicleOperations.addVehicle(request);
		
		return handleResponse(response);
	}
	
	@GetMapping(value="/getVehicles")
	public ResponseEntity<Response> getVehicles(@Valid @RequestParam String userId)
	{
		response = retrieveVehicleOperations.retrieveVehicles(userId);
		
		return handleResponse(response);
	}
	
	/**
	 * Returns the appropriate response entity, depending on the response object. 
	 * @param response - The result of the incoming request's processing.
	 * @return the <b>ResponseEntity</b> object of the given response.
	 */
	private ResponseEntity<Response> handleResponse(Response response)
	{

		String errorCode = response.getErrorCode();
		String result = response.getResult();
		String errorMsg = response.getMessage();
		ObjectMapper om = new ObjectMapper();
		
		try
		{
			logger.info("Response: "+om.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		
		if(result.equals(Constants.FAILURE_STATUS_CODE) && errorCode!=null && !errorCode.isEmpty() && errorMsg!=null && !errorMsg.isEmpty())
		{

			if(errorMsg.equalsIgnoreCase(Constants.ENTITY_ALREADY_EXISTS)
			|| errorMsg.contains(Constants.FORM_ALREADY_PROCESSED.substring(0, Constants.FORM_ALREADY_PROCESSED.indexOf(Constants.TEMPLATE_PLACEHOLDER_END)+1))
			|| (errorMsg.contains(Constants.VEHICLE_TRANSFER_IN_PROGRESS.substring(0, Constants.VEHICLE_TRANSFER_IN_PROGRESS.indexOf(Constants.TEMPLATE_PLACEHOLDER_START)))
					&& errorMsg.contains(Constants.VEHICLE_TRANSFER_IN_PROGRESS.substring(Constants.VEHICLE_TRANSFER_IN_PROGRESS.indexOf(Constants.TEMPLATE_PLACEHOLDER_END)+1)))
			|| errorMsg.contains(Constants.FORM_DELETION_ERROR.substring(Constants.FORM_DELETION_ERROR.indexOf(Constants.TEMPLATE_PLACEHOLDER_END)+1)))
			{
				return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
			}
			else if(errorMsg.contains(Constants.FORM_DOES_NOT_BELONG_TO_USER.substring(0, Constants.FORM_DOES_NOT_BELONG_TO_USER.indexOf(Constants.USER_PREFIX)))
			|| errorMsg.equalsIgnoreCase(Constants.INVALID_FORM_STATUS))
			{
				return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
			}
			else if(errorMsg.equalsIgnoreCase(Constants.FORM_DOES_NOT_EXIST)
			|| errorMsg.contains(Constants.USER_HAS_NO_FORMS.substring(Constants.USER_HAS_NO_FORMS.indexOf(Constants.TEMPLATE_PLACEHOLDER_END)+1))
			|| (errorMsg.contains(Constants.VEHICLE_DOES_NOT_EXIST.substring(0, Constants.VEHICLE_DOES_NOT_EXIST.indexOf(Constants.TEMPLATE_PLACEHOLDER_START)))
				&& errorMsg.contains(Constants.VEHICLE_DOES_NOT_EXIST.substring(Constants.VEHICLE_DOES_NOT_EXIST.indexOf(Constants.TEMPLATE_PLACEHOLDER_END)+1))))
			{
				return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
			}
			else if(errorCode.equalsIgnoreCase(Constants.TAX_ID_ERR))
			{
				return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
			}
			else
			{
				return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else
		{
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
	}
	
}
