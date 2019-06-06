package panda.tube.wordpress;

import panda.bind.json.Jsons;

public class Filter {

	public Integer number;
	public Integer offset;

	public Filter offset(Integer offset, Integer number) {
		this.offset = offset;
		this.number = number;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
