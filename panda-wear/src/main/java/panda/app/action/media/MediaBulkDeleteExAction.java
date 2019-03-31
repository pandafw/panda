package panda.app.action.media;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Media;
import panda.app.media.MediaDataStore;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/media")
@Auth(AUTH.SUPER)
public class MediaBulkDeleteExAction extends MediaBulkDeleteAction {

	@IocInject
	private MediaDataStore mds;
	
	public String getMediaLink(Media m) {
		return "<img class=\"p-mwh32px\" src=\"icon/" + m.getSlug() + "\">";
	}

	@Override
	protected void afterBulkDelete(List<Media> dataList) {
		mds.delete(getDao(), dataList.toArray(new Media[dataList.size()]));
		super.afterBulkDelete(dataList);
	}
}
