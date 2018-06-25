package panda.ex.wordpress;

import panda.bind.json.Jsons;

public class Filter {

	public Integer number;
	public Integer offset;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
