package panda.tube.wordpress;

import panda.bind.json.Jsons;

/**
 * Media Object, as the result of an upload;
 */
public class MediaObject {
	public String id;
	
	public String url;

	public String file;

	public String type;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
