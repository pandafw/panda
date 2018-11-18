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
			if (data.getMediaFile() != null && data.getMediaFile().isExists()) {
				data.setMediaData(data.getMediaFile().getData());
				data.setMediaSize(data.getMediaData().length);
				if (Strings.isEmpty(data.getMediaName())) {
					data.setMediaName(Strings.right(data.getMediaFile().getName(), FileNames.MAX_FILENAME_LENGTH));
				}
				ImageWrapper iw = Images.i().read(data.getMediaData());
				data.setMediaWidth(iw.getWidth());
				data.setMediaHeight(iw.getHeight());
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@Override
	protected Media startInsert(Media data) {
		data =  super.startInsert(data);
		data.setMediaSize(0);
		setFileData(data);
		return data;
	}

	@Override
	protected void afterInsert(Media data) {
		super.afterInsert(data);
		
		data.setMediaFile(null);
	}

	@Override
	protected Media startUpdate(Media data, Media sd) {
		data =  super.startUpdate(data, sd);
		data.setMediaSize(sd.getMediaSize());
		setFileData(data);
		return data;
	}

	@Override
	protected Set<String> getUpdateFields(Media data, Media sd) {
		Set<String> ufs = new HashSet<String>(super.getUpdateFields(data, sd));
		if (data.getMediaData() != null) {
			ufs.add(Media.MEDIA_DATA);
		}
		return ufs;
	}

	@Override
	protected void afterUpdate(Media data, Media sd) {
		super.afterUpdate(data, sd);
		
		data.setMediaFile(null);
	}
}
