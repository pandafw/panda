package panda.tube.sendgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;

/**
 * Class Mail builds an object that sends an email through SendGrid. Note that this object is not thread safe.
 */
public class Mail {

	/** The email's from field. */
	public Email from;

	/**
	 * The email's subject line. This is the global, or “message level”, subject of your email. This may be overridden by personalizations[x].subject.
	 */
	public String subject;

	/**
	 * The email's personalization. Each object within personalizations can be thought of as an envelope - it defines who should receive an individual message and how that message should be handled.
	 */
	public List<Personalization> personalizations;

	/** The email's content. */
	public List<Content> content;

	/** The email's attachments. */
	public List<Attachments> attachments;

	/** The email's template ID. */
	public String template_id;

	/**
	 * The email's sections. An object of key/value pairs that define block sections of code to be used as substitutions.
	 */
	public Map<String, String> sections;

	/** The email's headers. */
	public Map<String, String> headers;

	/** The email's categories. */
	public List<String> categories;

	/**
	 * The email's custom arguments. Values that are specific to the entire send that will be carried along with the email and its activity data. Substitutions will not be made on custom arguments, so
	 * any string that is entered into this parameter will be assumed to be the custom argument that you would like to be used. This parameter is overridden by personalizations[x].custom_args if that
	 * parameter has been defined. Total custom args size may not exceed 10,000 bytes.
	 */
	public Map<String, String> custom_args;

	/**
	 * A unix timestamp allowing you to specify when you want your email to be delivered. This may be overridden by the personalizations[x].send_at parameter. Scheduling more than 72 hours in advance
	 * is forbidden.
	 */
	public Long send_at;

	/**
	 * This ID represents a batch of emails to be sent at the same time. Including a batch_id in your request allows you include this email in that batch, and also enables you to cancel or pause the
	 * delivery of that batch. For more information, see https://sendgrid.com/docs/API_Reference/Web_API_v3/cancel_schedule_send.
	 */
	public String batch_id;

	/** The email's unsubscribe handling object. */
	public ASM asm;

	/** The email's IP pool name. */
	public String ip_pool_name;

	/** The email's mail settings. */
	public MailSettings mail_settings;

	/** The email's tracking settings. */
	public TrackingSettings tracking_settings;

	/** The email's reply to address. */
	public Email reply_to;

	private <T> List<T> addToList(T element, List<T> list) {
		if (list == null) {
			list = new ArrayList<T>();
		}
		list.add(element);
		return list;
	}

	private <K, V> Map<K, V> addToMap(K key, V value, Map<K, V> map) {
		if (map == null) {
			map = new HashMap<K, V>();
		}
		map.put(key, value);
		return map;
	}

	/** Construct a new Mail object. */
	public Mail() {
		return;
	}

	/**
	 * Construct a new Mail object.
	 * 
	 * @param from the email's from address.
	 * @param subject the email's subject line.
	 * @param to the email's recipient.
	 * @param content the email's content.
	 */
	public Mail(Email from, String subject, Email to, Content content) {
		this.from = from;
		this.subject = subject;
		Personalization personalization = new Personalization();
		personalization.addTo(to);
		this.addPersonalization(personalization);
		this.addContent(content);
	}

	/**
	 * Add a personalizaton to the email.
	 * 
	 * @param personalization a personalization.
	 */
	public void addPersonalization(Personalization personalization) {
		this.personalizations = addToList(personalization, this.personalizations);
	}

	/**
	 * Add content to this email.
	 * 
	 * @param content content to add to this email.
	 */
	public void addContent(Content content) {
		Content newContent = new Content();
		newContent.type = content.type;
		newContent.value = content.value;
		this.content = addToList(newContent, this.content);
	}

	/**
	 * Add attachments to the email.
	 * 
	 * @param attachments attachments to add.
	 */
	public void addAttachments(Attachments attachments) {
		Attachments newAttachment = new Attachments();
		newAttachment.content = attachments.content;
		newAttachment.type = attachments.type;
		newAttachment.filename = attachments.filename;
		newAttachment.disposition = attachments.disposition;
		newAttachment.content_id = attachments.content_id;
		this.attachments = addToList(newAttachment, this.attachments);
	}

	/**
	 * Add a section to the email.
	 * 
	 * @param key the section's key.
	 * @param value the section's value.
	 */
	public void addSection(String key, String value) {
		this.sections = addToMap(key, value, this.sections);
	}

	/**
	 * Add a header to the email.
	 * 
	 * @param key the header's key.
	 * @param value the header's value.
	 */
	public void addHeader(String key, String value) {
		this.headers = addToMap(key, value, this.headers);
	}

	/**
	 * Add a category to the email.
	 * 
	 * @param category the category.
	 */
	public void addCategory(String category) {
		this.categories = addToList(category, this.categories);
	}

	/**
	 * Add a custom argument to the email.
	 * 
	 * @param key argument's key.
	 * @param value the argument's value.
	 */
	public void addCustomArg(String key, String value) {
		this.custom_args = addToMap(key, value, this.custom_args);
	}

	/**
	 * Set the email's send at time (Unix timestamp).
	 * 
	 * @param sendAt the send at time.
	 */
	public void setSend_at(long sendAt) {
		this.send_at = sendAt;
	}

	/**
	 * Set the email's batch ID.
	 * 
	 * @param batchId the batch ID.
	 */
	public void setBatch_id(String batchId) {
		this.batch_id = batchId;
	}

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
