package panda.ex.wordpress.bean;


/**
 * Comment Count object for a blog post.
 */
public class CommentCount extends BaseBean {

	public Integer approved;
	public Integer awaiting_moderation;
	public Integer spam;
	public Integer total_comments;

}
