package panda.ex.slack;

import java.util.List;

public class Attachment {
	private String callback;
	private String color;
	private String pretext;
	private String author_name;
	private String author_link;
	private String author_icon;
	private String title;
	private String title_link;
	private String text;
	private List<Field> fields;
	private String image_url;
	private String thumb_url;
	/**
	 * @return the callback
	 */
	public String getCallback() {
		return callback;
	}
	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the pretext
	 */
	public String getPretext() {
		return pretext;
	}
	/**
	 * @param pretext the pretext to set
	 */
	public void setPretext(String pretext) {
		this.pretext = pretext;
	}
	/**
	 * @return the author_name
	 */
	public String getAuthor_name() {
		return author_name;
	}
	/**
	 * @param author_name the author_name to set
	 */
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	/**
	 * @return the author_link
	 */
	public String getAuthor_link() {
		return author_link;
	}
	/**
	 * @param author_link the author_link to set
	 */
	public void setAuthor_link(String author_link) {
		this.author_link = author_link;
	}
	/**
	 * @return the author_icon
	 */
	public String getAuthor_icon() {
		return author_icon;
	}
	/**
	 * @param author_icon the author_icon to set
	 */
	public void setAuthor_icon(String author_icon) {
		this.author_icon = author_icon;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the title_link
	 */
	public String getTitle_link() {
		return title_link;
	}
	/**
	 * @param title_link the title_link to set
	 */
	public void setTitle_link(String title_link) {
		this.title_link = title_link;
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
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	/**
	 * @return the image_url
	 */
	public String getImage_url() {
		return image_url;
	}
	/**
	 * @param image_url the image_url to set
	 */
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	/**
	 * @return the thumb_url
	 */
	public String getThumb_url() {
		return thumb_url;
	}
	/**
	 * @param thumb_url the thumb_url to set
	 */
	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

}
