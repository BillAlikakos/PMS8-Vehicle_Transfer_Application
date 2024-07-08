package com.example.demo.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.datatypes.DeleteFormRequest;
import com.example.demo.datatypes.Response;
import com.example.demo.db.Form;
import com.example.demo.db.FormRepository;
import com.example.demo.util.Constants;

@Service
public class HandleFormDeletion
{
	private Logger logger;
	private Response response;
	
	@Autowired
	private FormRepository formRepo;

	public HandleFormDeletion() 
	{
		this.logger = LogManager.getLogger();
		this.response = new Response();
	}
	
	/**
	 * Handles the deletion of the given form, provided that it exists.
	 * @param request - The incoming request.
	 * @return - The result of the operation.
	 */
	public Response deleteForm(DeleteFormRequest request)
	{
		String resultMsg = "";
		Optional<Form> existingEntityOptional = formRepo.findById(request.getFormID());
		if(existingEntityOptional.isPresent())
		{
			Form existingForm = existingEntityOptional.get();
			
			if(existingForm.getOwner_ID().equals(request.getUserID()))
			{
				if(!existingForm.getStatus().equalsIgnoreCase(Constants.FORM_STATUS.PROCESSED.toString()))
				{
					formRepo.delete(existingForm);
					resultMsg = MessageFormat.format(Constants.FORM_DELETION_SUCCESS, request.getFormID());
					this.response.setResult(Constants.SUCCESS_STATUS_CODE);
				}
				else
				{
					formRepo.delete(existingForm);
					resultMsg = MessageFormat.format(Constants.FORM_DELETION_ERROR, request.getFormID());
					this.response.setResult(Constants.FAILURE_STATUS_CODE);
					this.response.setErrorCode(Constants.STATUS_ERR);
				}
				
				logger.info(resultMsg);
			}
			else
			{
				resultMsg = MessageFormat.format(Constants.FORM_DOES_NOT_BELONG_TO_USER, request.getUserID());
				this.response.setResult(Constants.FAILURE_STATUS_CODE);
				this.response.setErrorCode(Constants.DB_ERR);
			}
		}
		else
		{
			resultMsg = Constants.FORM_DOES_NOT_EXIST;
			this.response.setResult(Constants.SUCCESS_STATUS_CODE);
			
			logger.warn(resultMsg);
		}
		
		this.response.setMessage(resultMsg);
		
		return this.response;
	}
	
}
