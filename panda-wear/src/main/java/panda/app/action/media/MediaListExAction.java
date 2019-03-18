package panda.app.action.media;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Media;
import panda.mvc.annotation.At;

@At("${super_path}/media")
@Auth(AUTH.SUPER)
public class MediaListExAction extends MediaListAction {
	public String getMediaLink(Media m) {
		if (m == null || m.getSize() <= 0) {
			return "";
		}
		return "<a href=\"view?id=" + m.getId() + "\"><img class=\"p-mwh32px\" src=\"icon/" + m.getId() + "\"></a>";
	}
}
