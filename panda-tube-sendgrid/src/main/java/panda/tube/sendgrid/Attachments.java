package panda.tube.sendgrid;

import panda.bind.json.Jsons;

/**
 * An attachment object.
 */
public class Attachments {
	/** The attachment content. */
	public String content;

	/**
	 * The mime type of the content you are attaching. For example, “text/plain” or “text/html”.
	 */
	public String type;

	/** The attachment file name. */
	public String filename;

	/** The attachment disposition. */
	public String disposition;

	/**
	 * The attachment content ID. This is used when the disposition is set to “inline” and the attachment is an image, allowing the file to be displayed within the body of your email.
	 */
	public String content_id;

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
