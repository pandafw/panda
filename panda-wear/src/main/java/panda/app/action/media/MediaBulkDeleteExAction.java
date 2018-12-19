package panda.app.action.media;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Media;
import panda.app.media.MediaDataDaoSaver;
import panda.app.media.Medias;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;

@At("${super_path}/media")
@Auth(AUTH.SUPER)
public class MediaBulkDeleteExAction extends MediaBulkDeleteAction {

	@IocInject
	private MediaDataDaoSaver mds;
	
	public String getMediaLink(Media m) {
		return Medias.getMediaIcon(m);
	}

	@Override
	protected void afterBulkDelete(List<Media> dataList) {
		mds.delete(dataList.toArray(new Media[dataList.size()]));
		super.afterBulkDelete(dataList);
	}
}
