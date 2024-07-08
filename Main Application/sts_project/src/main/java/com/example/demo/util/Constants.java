package com.example.demo.util;

public final class Constants
{
	public static final String ENTITY_FORM = "form";
	public static final String ENTITY_VEHICLE = "vehicle";
	public static final String ERROR_PREFIX = "Error ";
	public static final String STATUS_ERR = "STATUS_ERR";
	public static final String DB_ERR = "DB_ERR";
	public static final String TAX_ID_ERR = "TAX_ID_ERR";
	public static final String INVALID_TAX_ID = "The given tax id is invalid";
	public static final String TAX_ID_REGEX = "^\\d{1,10}$";
	public static final String SUCCESS_STATUS_CODE = "0";
	public static final String FAILURE_STATUS_CODE = "1";
	public static final String TEMPLATE_PLACEHOLDER_START = "{";
	public static final String TEMPLATE_PLACEHOLDER_END = "}";
	public static final String USER_PREFIX = "user ";
	
	//Insert
	public static final String ENTITY_ALREADY_EXISTS = "Entity with the same ID already exists and should not be updated.";
	public static final String SUCCESSFULLY_INSERTED = "Successfully inserted {0} {1}";
	
	//Retrieve
	public static final String USER_HAS_NO_FORMS = "User {0} does not have any submitted forms";
	
	//Update & delete
	public static final String SUCCESSFULLY_UPDATED = "Successfully updated {0} {1}";
	public static final String FORM_DOES_NOT_EXIST = "Requested form does not exist";
	public static final String FORM_DOES_NOT_BELONG_TO_USER = "Requested form does not belong to user {0}";
	public static final String FORM_ALREADY_PROCESSED = "Submitted form {0} has been already processed";
	public static final String INVALID_FORM_STATUS = "Invalid form status";
	public static final String VEHICLE_DOES_NOT_EXIST = "Requested vehicle with id {0} does not exist";
	public static final String VEHICLE_TRANSFER_IN_PROGRESS = "Requested vehicle with id {0} has already been assigned to a transfer form";
	
	//Delete
	public static final String FORM_DELETION_SUCCESS = "Successfully deleted requested form {0}";
	public static final String FORM_DELETION_ERROR = "Requested form {0} can not be deleted";
	
	//JWT attributes
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";
	public static final String JWT_EXPIRATION_ATTR = "exp";
	public static final String JWT_ISSUED_AT_ATTR = "iat";
	public static final String JWT_EMAIL_ATTR = "email";
	public static final String JWT_FIRST_NAME_ATTR = "given_name";
	public static final String JWT_LAST_NAME_ATTR = "family_name";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String JWT_TAX_ID = "tax_id";
	
	public static enum VEHICLE_STATUS{AVAILABLE, TRANSFER_IN_PROGRESS};
	public static enum FORM_STATUS
	{
		NEW, IN_PROGRESS, PROCESSED, ABORTED;
		public String toString()
		{
	        switch(this)
	        {
		        case NEW :
		            return "New";
		        case IN_PROGRESS :
		            return "In Progress";
		        case PROCESSED :
		            return "Processed";
		        case ABORTED :
		            return "Aborted";
	        }
	        
	        return null;
	    }
	}
}
