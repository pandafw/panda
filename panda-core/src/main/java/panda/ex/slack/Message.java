package panda.ex.slack;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private String icon_emoji;
	private String text;
	private boolean mrkdwn;
	private List<Attachment> attachments;

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
	 * @return the mrkdwn
	 */
	public boolean isMrkdwn() {
		return mrkdwn;
	}

	/**
	 * @param mrkdwn the mrkdwn to set
	 */
	public void setMrkdwn(boolean mrkdwn) {
		this.mrkdwn = mrkdwn;
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
