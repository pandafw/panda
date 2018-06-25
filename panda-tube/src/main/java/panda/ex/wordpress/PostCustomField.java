package panda.ex.wordpress;

import panda.bind.json.Jsons;

public class PostCustomField {
	public String id;
	public String key;
	public String value;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
