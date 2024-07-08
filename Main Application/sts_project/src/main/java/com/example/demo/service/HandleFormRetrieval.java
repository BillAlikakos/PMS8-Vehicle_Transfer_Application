package com.example.demo.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.example.demo.datatypes.FormsResponse;
import com.example.demo.datatypes.Response;
import com.example.demo.db.Form;
import com.example.demo.db.FormRepository;
import com.example.demo.util.Constants;

@Service
public class HandleFormRetrieval
{

	@Autowired
	private FormRepository formRepo;
	private FormsResponse response;
	private List<Form> userForms;
	private Logger logger;
	
	public HandleFormRetrieval()
	{
		this.response = new FormsResponse();
		this.userForms = new ArrayList<Form>();
		this.logger = LogManager.getLogger();
	}
	
	/**
	 * Retrieves the forms of the given user.
	 * @param id - The id of the user.
	 * @param isOwner - Flag that denotes if the submitted or the assigned forms of the user should be retrieved.
	 * @param status - The status of the forms to retrieve. If there is no given status, all of the user's forms will be retrieved.
	 * @return
	 */
	public Response retrieveForms(String id, boolean isOwner, String status)
	{

		if(isOwner)
		{
			this.getOwnerForms(id, status);
		}
		else
		{
			this.getBuyerForms(id, status);
		}
		
		String messageToLog = "Retireved "+userForms.size()+" forms for user";
		
		if(status!=null)
		{
			messageToLog = "Retireved "+userForms.size()+" "+status+" forms for user";
		}

		logger.info(messageToLog);
		
		userForms.forEach(f->logger.info("Form value: "+f.toString()));
		
		if(userForms!=null)
		{
			response.setForms(userForms);
			response.setResult(Constants.SUCCESS_STATUS_CODE);	
		}
		else
		{
			String resultMsg = MessageFormat.format(Constants.USER_HAS_NO_FORMS, id);
			
			logger.error(resultMsg);
			response.setResult(Constants.FAILURE_STATUS_CODE);
			response.setMessage(resultMsg);
		}
		
		return response;
	}
	
	private void getOwnerForms(String userId, String status)
	{
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("owner_id", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Form form = new Form("", userId, "", null, status, null);//Submitted forms case
		Example<Form> example = Example.of(form,customExampleMatcher);
		
		logger.info("Retrieving submitted forms for owner: "+userId);
		if(status==null)
		{
			this.userForms = formRepo.findAll(example);
		}
		else
		{
			this.userForms = formRepo.findByStatusAndOwnerId(userId, status);
		}
	}
	
	/**
	 * 
	 * @param taxId
	 * @param status
	 */
	private void getBuyerForms(String taxId, String status)
	{
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("tax_id", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Form form = new Form("", "", "", null, status, taxId);//Incoming forms case
		Example<Form> example = Example.of(form,customExampleMatcher);
		
		logger.info("Retrieving submitted forms for buyer: "+taxId);
		if(status==null)
		{
			this.userForms = formRepo.findAll(example);
		}
		else
		{
			this.userForms = formRepo.findByStatusAndBuyerId(taxId, status);
		}
		
	}
}
