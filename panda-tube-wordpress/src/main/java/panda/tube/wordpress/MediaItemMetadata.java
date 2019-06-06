package panda.tube.wordpress;

import java.util.Map;

import panda.bind.json.Jsons;


public class MediaItemMetadata {
	public Integer width;
	public Integer height;
	public String file;
	public Map<String, MediaItemSize> sizes;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
