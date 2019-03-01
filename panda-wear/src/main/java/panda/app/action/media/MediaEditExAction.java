package panda.app.action.media;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Media;
import panda.app.media.MediaDataDaoStore;
import panda.app.media.Medias;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.vfs.FileStores;

@At("${super_path}/media")
@Auth(AUTH.SUPER)
public class MediaEditExAction extends MediaEditAction {

	@IocInject
	private MediaDataDaoStore mds;
	
	@Override
	protected Media startInsert(Media data) {
		data = super.startInsert(data);
		data.setSize(0);
		Medias.setFileMeta(data);
		return data;
	}

	@Override
	protected void insertData(Media data) {
		super.insertData(data);

		mds.save(data);
		FileStores.safeDelete(data.getFile());
	}

	@Override
	protected void afterInsert(Media data) {
		super.afterInsert(data);

		data.setFile(null);
	}

	@Override
	protected Media startUpdate(Media data, Media sd) {
		data = super.startUpdate(data, sd);
		data.setSize(sd.getSize());
		Medias.setFileMeta(data);
		return data;
	}

	@Override
	protected int updateData(Media udat, Media sdat) {
		if (udat.getFile() != null && udat.getFile().isExists()) {
			mds.save(udat);
			FileStores.safeDelete(udat.getFile());
		}
		return super.updateData(udat, sdat);
	}

	@Override
	protected void afterUpdate(Media data, Media sd) {
		super.afterUpdate(data, sd);
		
		data.setFile(null);
	}

	@Override
	protected void afterDelete(Media data) {
		mds.delete(data);

		super.afterDelete(data);
	}

}
