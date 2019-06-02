package panda.gems.media.action;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.media.action.MediaListAction;
import panda.gems.media.entity.Media;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/media")
@Auth(AUTH.SUPER)
public class MediaListExAction extends MediaListAction {
	public String getMediaLink(Media m) {
		if (m == null || m.getSize() <= 0) {
			return "";
		}
		return "<a href=\"view/?id=" + m.getId() + "\"><img class=\"p-mwh32px\" src=\"icon/" + m.getSlug() + "\"></a>";
	}
}
