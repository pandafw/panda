package panda.ex.zendesk;

import java.util.Date;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;

public class User {
	/** Automatically assigned when the user is created */
	public Long id;
	
	/** The user's primary email address. *Writeable on create only. On update, a secondary email is added. See Email Address **/
	public String email;
	
	/** The user's name **/
	public String name;
	
	/** false if the user has been deleted */
	public Boolean active;
	
	/** An alias displayed to end users */
	public String alias;

	/** Whether or not the user is a chat-only agent */
	public Boolean chat_only;

	/** The time the user was created */
	public Date created_at;

	/** A custom role if the user is an agent on the Enterprise plan */
	public Long custom_role_id;

	/** The user's role id. 0 for custom agents, 1 for light agent and 2 for chat agent */
	public Long role_type;

	/** Any details you want to store about the user, such as an address */
	public String details;

	/** A unique identifier from another system. The API treats the id as case insensitive. Example: ian1 and Ian1 are the same user */
	public String external_id;

	/** The last time the user signed in to Zendesk Support */
	public Date last_login_at;
	
	/** The user's locale */
	public String locale;

	/** The user's language identifier */
	public Long locale_id;

	/** Designates whether the user has forum moderation capabilities */
	public Boolean moderator;

	/** Any notes you want to store about the user */
	public String notes;

	/** true if the user can only create private comments */
	public Boolean only_private_comments;

	/** The id of the organization the user is associated with */
	public Long organization_id;

	/** The id of the user's default group. *Can only be set on create, not on update */
	public Long default_group_id;

	/** The user's primary phone number. See Phone Number below */
	public String phone;

	/** Whether the phone number is shared or not. See Phone Number below */
	public Boolean shared_phone_number;

	/** The user's profile picture represented as an Attachment object */
	public Attachment photo;

	/** If the agent has any restrictions; false for admins and unrestricted agents, true for other agents */
	public Boolean restricted_agent;

	/** The user's role. Possible values are "end-user", "agent", or "admin" */
	public String role;

	/** If the user is shared from a different Zendesk Support instance. Ticket sharing accounts only */
	public Boolean shared;

	/** If the user is a shared agent from a different Zendesk Support instance. Ticket sharing accounts only */
	public Boolean shared_agent;

	/** The user's signature. Only agents and admins can have signatures */
	public String signature;

	/** If the agent is suspended. Tickets from suspended users are also suspended, and these users cannot sign in to the end user portal */
	public Boolean suspended;

	/** The user's tags. Only present if your account has user tagging enabled */
	public List<String> tags;

	/** Specifies which tickets the user has access to. Possible values are: "organization", "groups", "assigned", "requested", null */
	public String ticket_restriction;

	/** The user's time zone. See Time Zone */
	public String time_zone;

	/** If two factor authentication is enabled */
	public Boolean two_factor_auth_enabled;

	/** The time the user was last updated */
	public Date updated_at;

	/** The user's API url */
	public String url;

	/** Values of custom fields in the user's profile. See User Fields */
	public Map<String, String> user_fields;

	/** The user's primary identity is verified or not. For secondary identities, see User Identities */
	public Boolean verified;

	/** Whether or not the user can access the CSV report on the Search tab of the Reporting page in the Support admin interface. See Analyzing Help Center search results in Help Center. Only available on the Guide Professional plan */
	public Boolean report_csv;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
