package panda.tube.wordpress;

import panda.bind.json.Jsons;

public class PostThumbnailImageMeta {
	public Integer aperture;
	public String credit;
	public String camera;
	public String caption;
	public Integer created_timestamp;
	public String copyright;
	public Integer focal_length;
	public Integer iso;
	public Integer shutter_speed;
	public String title;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
