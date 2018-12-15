package panda.app.media;

import panda.app.entity.Media;

public class Medias {
	public static final int MAX_FILENAME_LENGTH = 200;

	public static final int DEFAULT_ICON_SIZE = 32;

	public static final int DEFAULT_THUMB_SIZE = 192;

	public static String getMediaIcon(Media m) {
		return "<img class=\"p-mwh32px\" src=\"mediaicon?id=" + m.getId() + "\">";
	}
	
	public static String getMediaLink(Media m) {
		if (m == null || m.getSize() <= 0) {
			return "";
		}
		return "<a href=\"view?id=" + m.getId() + "\">" + getMediaIcon(m) + "</a>";
	}
}
