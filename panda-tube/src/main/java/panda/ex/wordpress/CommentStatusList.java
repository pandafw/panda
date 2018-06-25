package panda.ex.wordpress;

import panda.bind.json.Jsons;

/**
 * Comment status names for the blog
 */
public class CommentStatusList {

	public String hold;

	public String approve;

	public String spam;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
