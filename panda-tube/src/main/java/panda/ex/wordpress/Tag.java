package panda.ex.wordpress;

import panda.bind.json.Jsons;

/**
 * Tag object for a blog.
 */
public class Tag {
	public String tagId;

	public String name;

	public String slug;

	public Integer count;

	public String htmlUrl;

	public String rssUrl;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
