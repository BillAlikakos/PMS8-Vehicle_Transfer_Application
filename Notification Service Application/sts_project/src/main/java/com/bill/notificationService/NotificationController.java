package com.bill.notificationService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bill.notificationService.dataTypes.NotificationRequest;
import com.bill.notificationService.dataTypes.NotificationResponse;
import com.bill.notificationService.service.NotificationService;
import com.bill.notificationService.util.Constants;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
public class NotificationController
{

	@Autowired
	private NotificationService notificationService;
	
	@PostMapping("/notify")
	@ResponseBody
	public ResponseEntity<NotificationResponse> submitNotification(@Valid @RequestBody NotificationRequest request)
	{
		try
		{
			notificationService.sendNotification(request);
			
			return new ResponseEntity<>(new NotificationResponse(Constants.SUCCESS_RESULT_CODE, "", Constants.EMAIL_SUBMITTED_MSG), HttpStatus.CREATED);
		}
		catch(MessagingException | IOException ex)
		{
			ex.printStackTrace();
			return new ResponseEntity<>(new NotificationResponse(Constants.ERROR_RESULT_CODE, Constants.EMAIL_ERROR_CODE, Constants.EMAIL_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}
