package panda.app.action.media;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import panda.app.entity.Media;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.FileNames;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.annotation.At;

@At("${super_path}/media")
public class MediaEditExAction extends MediaEditAction {
	private void setFileData(Media data) {
		try {
			if (data.getFile() != null && data.getFile().isExists()) {
				data.setData(data.getFile().data());
				data.setSize(data.getData().length);
				if (Strings.isEmpty(data.getName())) {
					data.setName(Strings.right(data.getFile().getName(), FileNames.MAX_FILENAME_LENGTH));
				}
				ImageWrapper iw = Images.i().read(data.getData());
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
		data =  super.startInsert(data);
		data.setSize(0);
		setFileData(data);
		return data;
	}

	@Override
	protected void afterInsert(Media data) {
		super.afterInsert(data);
		
		data.setFile(null);
	}

	@Override
	protected Media startUpdate(Media data, Media sd) {
		data =  super.startUpdate(data, sd);
		data.setSize(sd.getSize());
		setFileData(data);
		return data;
	}

	@Override
	protected Set<String> getUpdateFields(Media data, Media sd) {
		Set<String> ufs = new HashSet<String>(super.getUpdateFields(data, sd));
		if (data.getData() != null) {
			ufs.add(Media.DATA);
		}
		return ufs;
	}

	@Override
	protected void afterUpdate(Media data, Media sd) {
		super.afterUpdate(data, sd);
		
		data.setFile(null);
	}
}
