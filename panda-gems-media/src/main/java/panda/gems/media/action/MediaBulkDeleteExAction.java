package panda.gems.media.action;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.media.action.MediaBulkDeleteAction;
import panda.gems.media.entity.Media;
import panda.gems.media.store.MediaDataStore;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/media")
@Auth(AUTH.ADMIN)
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
