package panda.tube.wordpress;

import java.util.Date;

import panda.bind.json.Jsons;

public class MediaItem {
	public String attachment_id;
	public Date date_created_gmt;
	public Integer parent;
	public String link;
	public String title;
	public String caption;
	public String description;
	public MediaItemMetadata metadata;
	public PostThumbnailImageMeta image_meta;
	public String thumbnail;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
