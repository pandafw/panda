package panda.tube.freshdesk;

import java.util.Date;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;

public class Ticket {
	/** Unique ID of the ticket*/
	private Long id;
	
	/** Name of the requester */
	private String name;
	
	/** Email address of the requester. If no contact exists with this email address in Freshdesk, it will be added as a new contact.*/
	private String email;
	
	/** Phone number of the requester. If no contact exists with this phone number in Freshdesk, it will be added as a new contact. If the phone number is set and the email address is not, then the name attribute is mandatory.*/
	private String phone;
	
	/** Facebook ID of the requester. A contact should exist with this facebook_id in Freshdesk. */
	private String facebook_id;
	
	/** Twitter handle of the requester. If no contact exists with this handle in Freshdesk, it will be added as a new contact.*/
	private String twitter_id;
	
	/** User ID of the requester. For existing contacts, the requester_id can be passed instead of the requester's email.*/
	private Long requester_id;
	
	/** ID of the agent to whom the ticket has been assigned.*/
	private Long responder_id;

	/** Helps categorize the ticket according to the different kinds of issues your support team deals with.*/
	private String type;
	
	/** Status of the ticket */
	private Integer status;
	
	/** Priority of the ticket*/
	private Integer priority;
	
	/** The channel through which the ticket was created*/
	private Integer source;
	

	/** Set to true if the ticket has been deleted/trashed. Deleted tickets will not be displayed in any views except the "deleted" filter*/
	private Boolean deleted;
	
	/** Set to true if the ticket has been marked as spam*/
	private Boolean spam;
	
	/** Timestamp that denotes when the ticket is due to be resolved*/
	private Date due_by;
	
	/** Timestamp that denotes when the first response is due*/
	private Date fr_due_by;
	
	/** Set to true if the ticket has been escalated for any reason*/
	private Boolean is_escalated;
	
	/** Set to true if the ticket has been escalated as the result of first response time being breached*/
	private Boolean fr_escalated;

	/** Email addresses to which the ticket was originally sent*/
	private List<String> to_emails;
	
	/** Email address added in the 'cc' field of the incoming ticket email*/
	private List<String> cc_emails;
	
	/** Email address(e)s added while forwarding a ticket*/
	private List<String> fwd_emails;
	
	/** Email address added while replying to a ticket */
	private List<String> reply_cc_emails;
	
	/** ID of email config which is used for this ticket. (i.e., support@yourcompany.com/sales@yourcompany.com)*/
	private Long email_config_id;

	/** Ticket creation timestamp*/
	private Date created_at;

	/** Ticket updated timestamp*/
	private Date updated_at;

	/** ID of the product to which the ticket is associated*/
	private Long product_id;

	/** ID of the group to which the ticket has been assigned*/
	private Long group_id;
	
	/** ID of the company to which this ticket belongs*/
	private Long company_id;

	/** Subject of the ticket*/
	private String subject;
	
	/** HTML content of the ticket*/
	private String description;
	
	/** Content of the ticket in plain text*/
	private String description_text;
	
	/** Tags that have been associated with the ticket*/
	private List<String> tags;
	
	/** Ticket attachments. The total size of these attachments cannot exceed 15MB.*/
	private List<Attachment> attachments;
	
	/** Key value pairs containing the names and values of custom fields. Read more here*/
	private Map<String, String> custom_fields;

	/** include=requester */
	private Contact requester;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the facebook_id
	 */
	public String getFacebook_id() {
		return facebook_id;
	}

	/**
	 * @param facebook_id the facebook_id to set
	 */
	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}

	/**
	 * @return the twitter_id
	 */
	public String getTwitter_id() {
		return twitter_id;
	}

	/**
	 * @param twitter_id the twitter_id to set
	 */
	public void setTwitter_id(String twitter_id) {
		this.twitter_id = twitter_id;
	}

	/**
	 * @return the requester_id
	 */
	public Long getRequester_id() {
		return requester_id;
	}

	/**
	 * @param requester_id the requester_id to set
	 */
	public void setRequester_id(Long requester_id) {
		this.requester_id = requester_id;
	}

	/**
	 * @return the responder_id
	 */
	public Long getResponder_id() {
		return responder_id;
	}

	/**
	 * @param responder_id the responder_id to set
	 */
	public void setResponder_id(Long responder_id) {
		this.responder_id = responder_id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @return the source
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the spam
	 */
	public Boolean getSpam() {
		return spam;
	}

	/**
	 * @param spam the spam to set
	 */
	public void setSpam(Boolean spam) {
		this.spam = spam;
	}

	/**
	 * @return the due_by
	 */
	public Date getDue_by() {
		return due_by;
	}

	/**
	 * @param due_by the due_by to set
	 */
	public void setDue_by(Date due_by) {
		this.due_by = due_by;
	}

	/**
	 * @return the fr_due_by
	 */
	public Date getFr_due_by() {
		return fr_due_by;
	}

	/**
	 * @param fr_due_by the fr_due_by to set
	 */
	public void setFr_due_by(Date fr_due_by) {
		this.fr_due_by = fr_due_by;
	}

	/**
	 * @return the is_escalated
	 */
	public Boolean getIs_escalated() {
		return is_escalated;
	}

	/**
	 * @param is_escalated the is_escalated to set
	 */
	public void setIs_escalated(Boolean is_escalated) {
		this.is_escalated = is_escalated;
	}

	/**
	 * @return the fr_escalated
	 */
	public Boolean getFr_escalated() {
		return fr_escalated;
	}

	/**
	 * @param fr_escalated the fr_escalated to set
	 */
	public void setFr_escalated(Boolean fr_escalated) {
		this.fr_escalated = fr_escalated;
	}

	/**
	 * @return the to_emails
	 */
	public List<String> getTo_emails() {
		return to_emails;
	}

	/**
	 * @param to_emails the to_emails to set
	 */
	public void setTo_emails(List<String> to_emails) {
		this.to_emails = to_emails;
	}

	/**
	 * @return the cc_emails
	 */
	public List<String> getCc_emails() {
		return cc_emails;
	}

	/**
	 * @param cc_emails the cc_emails to set
	 */
	public void setCc_emails(List<String> cc_emails) {
		this.cc_emails = cc_emails;
	}

	/**
	 * @return the fwd_emails
	 */
	public List<String> getFwd_emails() {
		return fwd_emails;
	}

	/**
	 * @param fwd_emails the fwd_emails to set
	 */
	public void setFwd_emails(List<String> fwd_emails) {
		this.fwd_emails = fwd_emails;
	}

	/**
	 * @return the reply_cc_emails
	 */
	public List<String> getReply_cc_emails() {
		return reply_cc_emails;
	}

	/**
	 * @param reply_cc_emails the reply_cc_emails to set
	 */
	public void setReply_cc_emails(List<String> reply_cc_emails) {
		this.reply_cc_emails = reply_cc_emails;
	}

	/**
	 * @return the email_config_id
	 */
	public Long getEmail_config_id() {
		return email_config_id;
	}

	/**
	 * @param email_config_id the email_config_id to set
	 */
	public void setEmail_config_id(Long email_config_id) {
		this.email_config_id = email_config_id;
	}

	/**
	 * @return the created_at
	 */
	public Date getCreated_at() {
		return created_at;
	}

	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	/**
	 * @return the updated_at
	 */
	public Date getUpdated_at() {
		return updated_at;
	}

	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	/**
	 * @return the product_id
	 */
	public Long getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	/**
	 * @return the group_id
	 */
	public Long getGroup_id() {
		return group_id;
	}

	/**
	 * @param group_id the group_id to set
	 */
	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}

	/**
	 * @return the company_id
	 */
	public Long getCompany_id() {
		return company_id;
	}

	/**
	 * @param company_id the company_id to set
	 */
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description_text
	 */
	public String getDescription_text() {
		return description_text;
	}

	/**
	 * @param description_text the description_text to set
	 */
	public void setDescription_text(String description_text) {
		this.description_text = description_text;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the attachments
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the custom_fields
	 */
	public Map<String, String> getCustom_fields() {
		return custom_fields;
	}

	/**
	 * @param custom_fields the custom_fields to set
	 */
	public void setCustom_fields(Map<String, String> custom_fields) {
		this.custom_fields = custom_fields;
	}

	/**
	 * @return the requester
	 */
	public Contact getRequester() {
		return requester;
	}

	/**
	 * @param requester the requester to set
	 */
	public void setRequester(Contact requester) {
		this.requester = requester;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}

}
