package panda.app.action.media;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Media;
import panda.app.media.Medias;
import panda.mvc.annotation.At;

@At("${super_path}/media")
@Auth(AUTH.SUPER)
public class MediaListExAction extends MediaListAction {
	public String getMediaLink(Media m) {
		return Medias.getMediaLink(m);
	}
}
