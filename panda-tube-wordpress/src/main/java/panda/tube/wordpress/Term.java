package panda.tube.wordpress;

import panda.bind.json.Jsons;

public class Term {
	public String term_id;
	public String name;
	public String slug;
	public String term_group;
	public String term_taxonomy_id;
	public String taxonomy;
	public String description;
	public String parent;
	public Integer count;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
