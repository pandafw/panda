package panda.ex.wordpress;

import panda.bind.json.Jsons;

public class PostEnclosure {
	public String url;
	public Integer length;
	public String type;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
