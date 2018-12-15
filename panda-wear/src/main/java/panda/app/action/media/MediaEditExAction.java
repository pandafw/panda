package panda.app.action.media;

import java.io.IOException;

import panda.app.entity.Media;
import panda.app.media.MediaDataDaoSaver;
import panda.app.media.Medias;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.annotation.At;

@At("${super_path}/media")
public class MediaEditExAction extends MediaEditAction {

	@IocInject
	private MediaDataDaoSaver mds;
	
	private void setFileMeta(Media data) {
		try {
			if (data.getFile() != null && data.getFile().isExists()) {
				data.setSize(data.getFile().getSize());
				if (Strings.isEmpty(data.getName())) {
					data.setName(Strings.right(data.getFile().getName(), Medias.MAX_FILENAME_LENGTH));
				}
				ImageWrapper iw = Images.i().read(data.getFile().data());
				data.setWidth(iw.getWidth());
				data.setHeight(iw.getHeight());
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@Override
	protected Media startInsert(Media data) {
		data = super.startInsert(data);
		data.setSize(0);
		setFileMeta(data);
		return data;
	}

	@Override
	protected void insertData(Media data) {
		super.insertData(data);

		mds.save(data);
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
		setFileMeta(data);
		return data;
	}

	@Override
	protected int updateData(Media udat, Media sdat) {
		if (udat.getFile() != null && udat.getFile().isExists()) {
			mds.save(udat);
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
