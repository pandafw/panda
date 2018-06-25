package panda.ex.wordpress;

import java.util.Date;

import panda.bind.json.Jsons;

/**
 * A WordPress Comment object
 */
public class Comment {
	public String comment_id;
	public String parent;
	public String user_id;
	public Date dateCreated;
	public String status;
	public String content;
	public String link;
	public String post_id;
	public String post_title;
	public String author;
	public String author_url;
	public String author_email;
	public String author_ip;
	public String type;
	
	/**
	 * @return the comment_parent
	 */
	public String getComment_parent() {
		return parent;
	}

	/**
	 * @param comment_parent the comment_parent to set
	 */
	public void setComment_parent(String comment_parent) {
		this.parent = comment_parent;
	}
	
	/**
	 * @return the date_created_gmt
	 */
	public Date getDate_created_gmt() {
		return dateCreated;
	}

	/**
	 * @param date_created_gmt the date_created_gmt to set
	 */
	public void setDate_created_gmt(Date date_created_gmt) {
		this.dateCreated = date_created_gmt;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
