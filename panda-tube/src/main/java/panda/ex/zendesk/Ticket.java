package panda.ex.zendesk;

import java.util.Date;
import java.util.List;

import panda.bind.json.Jsons;

public class Ticket {
	/** Unique ID of the ticket*/
	private Long id;
	
	/** The API url of this ticket */
	private String url;
	
	/** An id you can use to link Zendesk Support tickets to local records */
	private String external_id;
	
	/** The type of this ticket. Possible values: "problem", "incident", "question" or "task" */
	private String type;
	
	/** The value of the subject field for this ticket */
	private String subject;
	
	/** The dynamic content placeholder, if present, or the "subject" value, if not.  */
	private String raw_subject;
	
	/** The first comment on the ticket */
	private String description;
	
	/** The urgency with which the ticket should be addressed. Possible values: "urgent", "high", "normal", "low" */
	private String priority;
	
	/** The state of the ticket. Possible values: "new", "open", "pending", "hold", "solved", "closed" */
	private String status;
	
	/** The original recipient e-mail address of the ticket */
	private String recipient;
	
	/** The user who requested this ticket */
	private Long requester_id;
	
	/** The user who submitted the ticket. The submitter always becomes the author of the first comment on the ticket */
	private Long submitter_id;
	
	/** The agent currently assigned to the ticket */
	private Long assignee_id;
	
	/** The organization of the requester. You can only specify the ID of an organization associated with the requester */
	private Long organization_id;

	/** The group this ticket is assigned to */
	private Long group_id;

	/** The ids of users currently cc'ed on the ticket */
	private List<Long> collaborator_ids;

	/** POST requests only. Users to add as cc's when creating a ticket */
	private List<Object> collaborators;
	
	/** Agents currently following the ticket */
	private List<Long> follower_ids;

	private List<Long> email_cc_ids;

	/** The topic this ticket originated from, if any */
	private Long forum_topic_id;

	/** For tickets of type "incident", the ID of the problem the incident is linked to */
	private Long problem_id;
	
	/** Is true of this ticket has been marked as a problem, false otherwise */
	private Boolean has_incidents;
	
	/** If this is a ticket of type "task" it has a due date */
	private Date due_at;

	/** Tags that have been associated with the ticket*/
	private List<String> tags;

	/** This object explains how the ticket was created */
	private Object via;
	
	/** Custom fields for the ticket. */
	private List<CustomField> custom_fields;

	/** Custom fields for the ticket. */
	private List<CustomField> fields;
	
	/** The satisfaction rating of the ticket, if it exists, or the state of satisfaction, 'offered' or 'unoffered' */
	private SatisfactionRating satisfaction_rating;

	/** The ids of the sharing agreements used for this ticket */
	private List<Long> sharing_agreement_ids;
	
	/** The ids of the followups created from this ticket. Ids are only visible once the ticket is closed */
	private List<Long> followup_ids;
	
	/** POST requests only. The id of a closed ticket when creating a follow-up ticket */
	private Long via_followup_source_id;
	
	/** POST requests only. List of macro IDs to be recorded in the ticket audit */
	private List<Long> macro_ids;
	
	/** Enterprise only. The id of the ticket form to render for the ticket */
	private Long ticket_form_id;
	
	/** Enterprise only. The id of the brand this ticket is associated with */
	private Long brand_id;

	/** Is false if channelback is disabled, true otherwise. Only applicable for channels framework ticket */
	private Boolean allow_channelback;

	/** When an agent responds, are they allowed to add attachments? Defaults to true */
	private Boolean allow_attachments;
	
	/** Is true if any comments are public, false otherwise */
	private Boolean is_public;

	/** Ticket creation timestamp*/
	private Date created_at;

	/** Ticket updated timestamp*/
	private Date updated_at;

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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the external_id
	 */
	public String getExternal_id() {
		return external_id;
	}

	/**
	 * @param external_id the external_id to set
	 */
	public void setExternal_id(String external_id) {
		this.external_id = external_id;
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
	 * @return the raw_subject
	 */
	public String getRaw_subject() {
		return raw_subject;
	}

	/**
	 * @param raw_subject the raw_subject to set
	 */
	public void setRaw_subject(String raw_subject) {
		this.raw_subject = raw_subject;
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
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
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
	 * @return the submitter_id
	 */
	public Long getSubmitter_id() {
		return submitter_id;
	}

	/**
	 * @param submitter_id the submitter_id to set
	 */
	public void setSubmitter_id(Long submitter_id) {
		this.submitter_id = submitter_id;
	}

	/**
	 * @return the assignee_id
	 */
	public Long getAssignee_id() {
		return assignee_id;
	}

	/**
	 * @param assignee_id the assignee_id to set
	 */
	public void setAssignee_id(Long assignee_id) {
		this.assignee_id = assignee_id;
	}

	/**
	 * @return the organization_id
	 */
	public Long getOrganization_id() {
		return organization_id;
	}

	/**
	 * @param organization_id the organization_id to set
	 */
	public void setOrganization_id(Long organization_id) {
		this.organization_id = organization_id;
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
	 * @return the collaborator_ids
	 */
	public List<Long> getCollaborator_ids() {
		return collaborator_ids;
	}

	/**
	 * @param collaborator_ids the collaborator_ids to set
	 */
	public void setCollaborator_ids(List<Long> collaborator_ids) {
		this.collaborator_ids = collaborator_ids;
	}

	/**
	 * @return the collaborators
	 */
	public List<Object> getCollaborators() {
		return collaborators;
	}

	/**
	 * @param collaborators the collaborators to set
	 */
	public void setCollaborators(List<Object> collaborators) {
		this.collaborators = collaborators;
	}

	/**
	 * @return the follower_ids
	 */
	public List<Long> getFollower_ids() {
		return follower_ids;
	}

	/**
	 * @param follower_ids the follower_ids to set
	 */
	public void setFollower_ids(List<Long> follower_ids) {
		this.follower_ids = follower_ids;
	}

	/**
	 * @return the email_cc_ids
	 */
	public List<Long> getEmail_cc_ids() {
		return email_cc_ids;
	}

	/**
	 * @param email_cc_ids the email_cc_ids to set
	 */
	public void setEmail_cc_ids(List<Long> email_cc_ids) {
		this.email_cc_ids = email_cc_ids;
	}

	/**
	 * @return the forum_topic_id
	 */
	public Long getForum_topic_id() {
		return forum_topic_id;
	}

	/**
	 * @param forum_topic_id the forum_topic_id to set
	 */
	public void setForum_topic_id(Long forum_topic_id) {
		this.forum_topic_id = forum_topic_id;
	}

	/**
	 * @return the problem_id
	 */
	public Long getProblem_id() {
		return problem_id;
	}

	/**
	 * @param problem_id the problem_id to set
	 */
	public void setProblem_id(Long problem_id) {
		this.problem_id = problem_id;
	}

	/**
	 * @return the has_incidents
	 */
	public Boolean getHas_incidents() {
		return has_incidents;
	}

	/**
	 * @param has_incidents the has_incidents to set
	 */
	public void setHas_incidents(Boolean has_incidents) {
		this.has_incidents = has_incidents;
	}

	/**
	 * @return the due_at
	 */
	public Date getDue_at() {
		return due_at;
	}

	/**
	 * @param due_at the due_at to set
	 */
	public void setDue_at(Date due_at) {
		this.due_at = due_at;
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
	 * @return the via
	 */
	public Object getVia() {
		return via;
	}

	/**
	 * @param via the via to set
	 */
	public void setVia(Object via) {
		this.via = via;
	}

	/**
	 * @return the custom_fields
	 */
	public List<CustomField> getCustom_fields() {
		return custom_fields;
	}

	/**
	 * @param custom_fields the custom_fields to set
	 */
	public void setCustom_fields(List<CustomField> custom_fields) {
		this.custom_fields = custom_fields;
	}

	/**
	 * @return the fields
	 */
	public List<CustomField> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<CustomField> fields) {
		this.fields = fields;
	}

	/**
	 * @return the satisfaction_rating
	 */
	public SatisfactionRating getSatisfaction_rating() {
		return satisfaction_rating;
	}

	/**
	 * @param satisfaction_rating the satisfaction_rating to set
	 */
	public void setSatisfaction_rating(SatisfactionRating satisfaction_rating) {
		this.satisfaction_rating = satisfaction_rating;
	}

	/**
	 * @return the sharing_agreement_ids
	 */
	public List<Long> getSharing_agreement_ids() {
		return sharing_agreement_ids;
	}

	/**
	 * @param sharing_agreement_ids the sharing_agreement_ids to set
	 */
	public void setSharing_agreement_ids(List<Long> sharing_agreement_ids) {
		this.sharing_agreement_ids = sharing_agreement_ids;
	}

	/**
	 * @return the followup_ids
	 */
	public List<Long> getFollowup_ids() {
		return followup_ids;
	}

	/**
	 * @param followup_ids the followup_ids to set
	 */
	public void setFollowup_ids(List<Long> followup_ids) {
		this.followup_ids = followup_ids;
	}

	/**
	 * @return the via_followup_source_id
	 */
	public Long getVia_followup_source_id() {
		return via_followup_source_id;
	}

	/**
	 * @param via_followup_source_id the via_followup_source_id to set
	 */
	public void setVia_followup_source_id(Long via_followup_source_id) {
		this.via_followup_source_id = via_followup_source_id;
	}

	/**
	 * @return the macro_ids
	 */
	public List<Long> getMacro_ids() {
		return macro_ids;
	}

	/**
	 * @param macro_ids the macro_ids to set
	 */
	public void setMacro_ids(List<Long> macro_ids) {
		this.macro_ids = macro_ids;
	}

	/**
	 * @return the ticket_form_id
	 */
	public Long getTicket_form_id() {
		return ticket_form_id;
	}

	/**
	 * @param ticket_form_id the ticket_form_id to set
	 */
	public void setTicket_form_id(Long ticket_form_id) {
		this.ticket_form_id = ticket_form_id;
	}

	/**
	 * @return the brand_id
	 */
	public Long getBrand_id() {
		return brand_id;
	}

	/**
	 * @param brand_id the brand_id to set
	 */
	public void setBrand_id(Long brand_id) {
		this.brand_id = brand_id;
	}

	/**
	 * @return the allow_channelback
	 */
	public Boolean getAllow_channelback() {
		return allow_channelback;
	}

	/**
	 * @param allow_channelback the allow_channelback to set
	 */
	public void setAllow_channelback(Boolean allow_channelback) {
		this.allow_channelback = allow_channelback;
	}

	/**
	 * @return the allow_attachments
	 */
	public Boolean getAllow_attachments() {
		return allow_attachments;
	}

	/**
	 * @param allow_attachments the allow_attachments to set
	 */
	public void setAllow_attachments(Boolean allow_attachments) {
		this.allow_attachments = allow_attachments;
	}

	/**
	 * @return the is_public
	 */
	public Boolean getIs_public() {
		return is_public;
	}

	/**
	 * @param is_public the is_public to set
	 */
	public void setIs_public(Boolean is_public) {
		this.is_public = is_public;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
