package panda.ex.freshdesk;

import java.util.Date;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;

public class Contact {
	/** Set to true if the contact has been verified */
	private Boolean active;
	
	/** Address of the contact */
	private String address;
	
	/** Avatar of the contact */
	private Avatar avatar;
	
	/** ID of the primary company to which this contact belongs*/
	private Integer company_id;
	
	/** Set to true if the contact can see all tickets that are associated with the company to which he belong */
	private Boolean view_all_tickets;
	
	/** Key value pair containing the name and value of the custom fields. Read more here*/
	private Map<String, String> custom_fields;
	
	/** Set to true if the contact has been deleted. Note that this attribute will only be present for deleted contacts*/
	private Boolean deleted;
	
	/** A short description of the contact*/
	private String description;
	
	/** Primary email address of the contact. If you want to associate additional email(s) with this contact, use the other_emails attribute*/
	private String email;
	
	/** ID of the contact*/
	private Long id;
	
	/** Job title of the contact*/
	private String job_title;
	
	/** Language of the contact*/
	private String language;
	
	/** Mobile number of the contact*/
	private Long mobile;

	/** Name of the contact*/
	private String name;
	
	/** Additional emails associated with the contact*/
	private List<String> other_emails;
	
	/** Telephone number of the contact*/
	private String phone;
	
	/** Tags associated with this contact*/
	private List<String> tags;
	
	/** Time zone in which the contact resides*/
	private String time_zone;
	
	/** Twitter handle of the contact*/
	private String twitter_id;
	
	/** Additional companies associated with the contact*/
	private List<String> other_companies;
	
	/** Contact creation timestamp*/
	private Date created_at;
	
	/** Contact updated timestamp */
	private Date updated_at;

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the company_id
	 */
	public Integer getCompany_id() {
		return company_id;
	}

	/**
	 * @param company_id the company_id to set
	 */
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	/**
	 * @return the view_all_tickets
	 */
	public Boolean getView_all_tickets() {
		return view_all_tickets;
	}

	/**
	 * @param view_all_tickets the view_all_tickets to set
	 */
	public void setView_all_tickets(Boolean view_all_tickets) {
		this.view_all_tickets = view_all_tickets;
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
	 * @return the job_title
	 */
	public String getJob_title() {
		return job_title;
	}

	/**
	 * @param job_title the job_title to set
	 */
	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the mobile
	 */
	public Long getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(Long mobile) {
		this.mobile = mobile;
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
	 * @return the other_emails
	 */
	public List<String> getOther_emails() {
		return other_emails;
	}

	/**
	 * @param other_emails the other_emails to set
	 */
	public void setOther_emails(List<String> other_emails) {
		this.other_emails = other_emails;
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
	 * @return the time_zone
	 */
	public String getTime_zone() {
		return time_zone;
	}

	/**
	 * @param time_zone the time_zone to set
	 */
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
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
	 * @return the other_companies
	 */
	public List<String> getOther_companies() {
		return other_companies;
	}

	/**
	 * @param other_companies the other_companies to set
	 */
	public void setOther_companies(List<String> other_companies) {
		this.other_companies = other_companies;
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
