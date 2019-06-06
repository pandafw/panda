package panda.tube.wordpress;

import panda.bind.json.Jsons;

/**
 * Comment Count object for a blog post.
 */
public class CommentCount {

	public Integer approved;
	public Integer awaiting_moderation;
	public Integer spam;
	public Integer total_comments;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
