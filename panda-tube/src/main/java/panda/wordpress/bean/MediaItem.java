package panda.wordpress.bean;

import java.util.Date;

public class MediaItem extends BaseBean {
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
}
