package panda.app.action.media;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Media;
import panda.app.media.MediaDataStore;
import panda.app.media.Medias;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.vfs.FileStores;

@At("${super_path}/media")
@Auth(AUTH.SUPER)
public class MediaEditExAction extends MediaEditAction {

	@IocInject
	private MediaDataStore mds;
	
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

		mds.save(getDao(), data);
	}

	@Override
	protected void finalInsert(Media data) {
		super.finalInsert(data);

		FileStores.safeDelete(data.getFile());
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
		int cnt = super.updateData(udat, sdat);
		if (udat.getFile() != null && udat.getFile().isExists()) {
			mds.save(getDao(), udat);
		}
		return cnt;
	}

	@Override
	protected void finalUpdate(Media data, Media sd) {
		super.finalUpdate(data, sd);
		
		FileStores.safeDelete(data.getFile());
		data.setFile(null);
	}

	@Override
	protected void deleteData(Media data) {
		super.deleteData(data);
		mds.delete(getDao(), data);
	}

}
