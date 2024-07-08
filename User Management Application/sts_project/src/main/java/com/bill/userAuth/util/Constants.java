package com.bill.userAuth.util;

public class Constants
{
	public static final String GROUPS = "groups";
	public static final String REALM_ACCESS_CLAIM = "realm_access";
	public static final String ROLES_CLAIM = "roles";
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String CLIENT_CREDENTIALS_GRANT = "client_credentials";
	public static final String PASSWORD_GRANT = "password";
	public static final String REFRESH_TOKEN_GRANT = "refresh_token";
	
	public static final String ADMIN = "Admin";
	public static final String ADMIN_CLIENT = "admin-cli";
	
	public static final String ACCESS_TOKEN_JSON_PROPERTY = "access_token";
	public static final String REFRESH_TOKEN_JSON_PROPERTY = "refresh_token";
	public static final String TAX_ID_JSON_PROPERTY = "tax_id";
	public static final String ACTIVE_TOKEN_JSON_PROPERTY = "active";
	public static final String EMAIL_JSON_PROPERTY = "email";
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";
	public static final String JWT_EXPIRATION_ATTR = "exp";
	public static final String JWT_ISSUED_AT_ATTR = "iat";
	
	public static final String SUCCESS_MESSAGE = "Success";
	public static final String TOKEN_IS_ACTIVE = "Token is active";
	public static final String TOKEN_IS_INACTIVE = "Token is inactive";
	
	public static final String ERROR_PREFIX = "Error ";
	public static final String USER_AUTH_ERROR = "An error occurred while authenticating user ";
	public static final String KEYCLOAK_JSON_ERROR = "An error occurred while parsing the Keycloak JSON response";
	public static final String KEYCLOAK_JSON_ERROR_CODE = "KCERR01";
	public static final String ADMIN_AUTH_ERROR = "An error occurred while retrieving admin access token";
	
	public static final String USER_REG_ERROR = "An error occurred while registering user ";
	public static final String USER_RET_ERROR = "An error occurred while retrieving user data";
	public static final String TOKEN_REGEN_ERROR = "An error occurred while generating new access token ";
	public static final String TOKEN_VALIDITY_ERROR = "An error occurred while checking token validity";
	
}
