package panda.tube.wordpress;

import panda.bind.json.Jsons;

/**
 * Author object for a blog.
 */
public class Author {
	public String display_name;

	public String user_id;

	public String user_login;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
