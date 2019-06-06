package panda.tube.freshdesk;

public enum ErrorCode {
	/**	A mandatory attribute is missing. For example, calling Create a Contact without the mandatory email field in the request will result in this error. */
	missing_field,
	
	/** This code indicates that a request contained an incorrect or blank value, or was in an invalid format.*/
	invalid_value,
	
	/** Indicates that this value already exists. This error is applicable to fields that require unique values such as the email address in a contact or the name in a company.*/
	duplicate_value,
	
	/** Indicates that the field value doesn't match the expected data type. Entering text in a numerical field would trigger this error.*/
	datatype_mismatch,
	
	/** An unexpected field was part of the request. If any additional field is included in the request payload (other than what is specified in the API documentation), this error will occur.*/
	invalid_field,
	
	/** Request parameter is not a valid JSON. We recommend that you validate the JSON payload with a JSON validator before firing the API request.*/
	invalid_json,
	
	/** Incorrect or missing API credentials. As the name suggests, it indicates that the API request was made with invalid credentials. Forgetting to apply Base64 encoding on the API key is a common cause of this error.*/
	invalid_credentials,
	
	/** Insufficient privileges to perform this action. An agent attempting to access admin APIs will result in this error.*/
	access_denied,
	
	/** Not allowed as a specific feature has to be enabled in your Freshdesk portal for you to perform this action.*/
	require_feature,
	
	/** Account has been suspended.*/
	account_suspended,
	
	/** HTTPS is required in the API URL.*/
	ssl_required,
	
	/** Read only field cannot be altered.*/
	readonly_field,
	
	/** An email should be associated with the contact before converting the contact to an agent.*/
	inconsistent_state,
	
	/** The account has reached the maximum number of agents.*/
	max_agents_reached,
	
	/** The agent has reached the maximum number of failed login attempts.*/
	password_lockout,
	
	/** The agent's password has expired.*/
	password_expired,
	
	/** No JSON data required.*/
	no_content_required,
	
	/** The agent is not authorized to update this field.*/
	inaccessible_field,
	
	/** The field cannot be updated due to the current state of the record.*/
	incompatible_field
}
