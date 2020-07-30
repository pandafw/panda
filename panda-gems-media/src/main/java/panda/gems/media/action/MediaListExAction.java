package panda.gems.media.action;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.media.entity.Media;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/media")
@Auth(AUTH.ADMIN)
public class MediaListExAction extends MediaListAction {
	public static String getMediaImage(Media m) {
		return "<img class=\"p-mwh32px\" src=\"thumb/32/" + m.getSlug() + "\">";
	}
	
	public String getMediaLink(Media m) {
		if (m == null || m.getSize() <= 0) {
			return "";
		}
		return "<a href=\"view?id=" + m.getId() + "\">" + getMediaImage(m) + "</a>";
	}
}
