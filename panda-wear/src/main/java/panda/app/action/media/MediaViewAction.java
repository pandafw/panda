package panda.app.action.media;

import java.io.IOException;

import panda.app.action.AbstractAction;
import panda.app.constant.SET;
import panda.app.entity.Media;
import panda.app.media.MediaData;
import panda.app.media.MediaDataSaver;
import panda.app.media.Medias;
import panda.dao.Dao;
import panda.io.FileNames;
import panda.io.MimeTypes;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServletResponser;

@At("${super_path}/media")
public class MediaViewAction extends AbstractAction {
	@IocInject
	private MediaDataSaver mds;

	private void findMedia(Long id, String sc, int sz) throws IOException {
		if (id != null && id > 0) {
			Dao dao = getDaoClient().getDao();
			Media m = dao.fetch(Media.class, id);

			if (m != null) {
				MediaData md = null;
				if (sc == null) {
					md = mds.find(id);
				}
				else {
					sz = getSettings().getPropertyAsInt(sc, sz);
					md = mds.find(id, sz); 
				}
				if (md != null) {
					writeImage(m, md);
					return;
				}
			}
		}

		getResponse().setStatus(HttpStatus.SC_NOT_FOUND);
	}

	@At
	public void mediaview(@Param("id") Long id) throws Exception {
		findMedia(id, null, 0);
	}

	@At
	public void mediathumb(@Param("id") Long id) throws Exception {
		findMedia(id, SET.MEDIA_THUMB_SIZE, Medias.DEFAULT_THUMB_SIZE);
	}

	@At
	public void mediaicon(@Param("id") Long id) throws Exception {
		findMedia(id, SET.MEDIA_ICON_SIZE, Medias.DEFAULT_ICON_SIZE);
	}

	private void writeImage(Media m, MediaData md) throws IOException {
		String filename = m.getName();
		if (Strings.isEmpty(filename)) { 
			filename = "media-" + (m.getMid() == null ? m.getId() : m.getMid()) + ".jpg";
		}
		if (md.getMsz() > 0) {
			filename = FileNames.addSuffix(filename, "-" + md.getMsz());
		}

		HttpServletResponser hsrs = new HttpServletResponser(getRequest(), getResponse());
		hsrs.setFileName(filename);
		hsrs.setContentType(MimeTypes.getMimeType(hsrs.getFileName()));
		hsrs.setContentLength(md.getSize());
		hsrs.writeHeader();
		hsrs.writeStream(md.open());
	}
}

