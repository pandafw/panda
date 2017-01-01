package panda.ex.slack;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private String channel;
	private String username;
	private String icon_emoji;
	private String icon_url;
	private String text;
	private List<Attachment> attachments;
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the icon_emoji
	 */
	public String getIcon_emoji() {
		return icon_emoji;
	}
	/**
	 * @param icon_emoji the icon_emoji to set
	 */
	public void setIcon_emoji(String icon_emoji) {
		this.icon_emoji = icon_emoji;
	}
	/**
	 * @return the icon_url
	 */
	public String getIcon_url() {
		return icon_url;
	}
	/**
	 * @param icon_url the icon_url to set
	 */
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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

	public void addAttachment(Attachment attachment) {
		if (attachments == null) {
			attachments = new ArrayList<Attachment>();
		}
		attachments.add(attachment);
	}
}
