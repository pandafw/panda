package panda.app.action.media;

import panda.app.entity.Media;

public class Medias {
	public static String getMediaLink(Media m) {
		if (m == null || m.getSize() <= 0) {
			return "";
		}
		return "<a href=\"view?id=" + m.getId() + "\">"
				+ "<img class=\"p-wh32px\" src=\"mediaview?id=" + m.getId() + "\">"
				+ "</a>";
	}
}
