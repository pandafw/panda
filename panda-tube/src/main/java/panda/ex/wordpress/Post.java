package panda.ex.wordpress;

import java.util.Date;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;


public class Post {
	public static final String PING_STATUS_OPEN = "open";
	public static final String PING_STATUS_CLOSED = "closed";

	public static final String POST_STATUS_DRAFT = "draft";
	public static final String POST_STATUS_PUBLISH = "publish";
	public static final String POST_STATUS_PEDING = "pending";
	public static final String POST_STATUS_FUTURE = "future";
	public static final String POST_STATUS_PRIVATE = "private";

	public static final String POST_TYPE_POST = "post";
	public static final String POST_TYPE_PAGE = "page";
	public static final String POST_TYPE_LINK = "link";
	public static final String POST_TYPE_NAV = "nav_menu_item";
	public static final String POST_TYPE_ATTACHMENT = "attachment";

	public String post_id;
	public String post_title;
	public Date post_date;
	public Date post_date_gmt;
	public Date post_modified;
	public Date post_modified_gmt;
	public String post_status;
	public String post_type;
	public String post_format;
	public String post_name;
	public String post_author;
	public String post_password;
	public String post_excerpt;
	public String post_content;
	public String post_parent;
	public String post_mime_type;
	public String link;
	public String guid;
	public Integer menu_order;
	public String comment_status;
	public String ping_status;
	public boolean sticky;
	public Integer post_thumbnail_id;
	public MediaItem post_thumbnail;
	public List<Term> terms;
	public Map<String, List<Integer>> terms_ids;
	public Map<String, List<String>> terms_names;
	public List<PostCustomField> custom_fields;
	public PostEnclosure enclosure;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
