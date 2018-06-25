package panda.ex.wordpress;

import panda.bind.json.Jsons;

public class MediaItemSize {
	public String file;
	public String width;
	public String height;
	public String mime_type;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
