package panda.ex.wordpress.bean;

import java.util.Map;


public class MediaItemMetadata extends BaseBean {
	public Integer width;
	public Integer height;
	public String file;
	public Map<String, MediaItemSize> sizes;
}
