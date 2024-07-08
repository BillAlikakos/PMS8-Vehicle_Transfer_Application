package com.bill.notificationService.util;

public class Constants
{
	public static final String SUCCESS_RESULT_CODE = "0";
	public static final String ERROR_RESULT_CODE = "1";
	public static final String FORM_STATUS_APPROVED = "approved";
	public static final String FORM_STATUS_REJECTED = "rejected";
//	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String PDF_EXTENSION = ".pdf";
	
	public static final String OWNER_SUBJECT = "Vehicle transfer form status update";
	public static final String BUYER_SUBJECT = "New vehicle transfer form assigned";
	public static final String COMPLETE_SUBJECT = "Vehicle transfer complete";
	public static final String EMAIL_SUBMITTED_MSG = "Email notification submitted";
	public static final String EMAIL_ERROR_MSG = "An error occurred while submitting the email notification";
	public static final String EMAIL_ERROR_CODE = "EMAILERR";
	
	public static final String OWNER_FORM_TEMPLATE = "<html><body>Your form with ID: <b>{0}</b> was <b>{1}</b></body></html>";
	public static final String BUYER_FORM_TEMPLATE = "<html><body>A vehicle transfer form with ID: <b>{0}</b> has been assigned to you.\nPlease review the form {1}</body></html>";
	public static final String BUYER_REDIRECT = "<a href=\"{0}\">here</a>";
	public static final String BUYER_FORM_COMPLETE_TEMPLATE = "<html><body>Your vehicle transfer has been completed successfully.<br>You may find the new vehicle registration document attached.</body></html>";
	
}
