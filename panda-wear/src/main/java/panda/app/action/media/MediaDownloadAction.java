package panda.app.action.media;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.app.action.AbstractAction;
import panda.app.entity.Media;
import panda.dao.entity.EntityDao;
import panda.io.MimeTypes;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.servlet.HttpServletResponser;

@At("${super_path}/media")
public class MediaDownloadAction extends AbstractAction {
	@At
	public void mediaview(@Param("id") Long id) throws Exception {
		HttpServletResponse res = getResponse();
		
		if (id != null && id > 0) {
			EntityDao<Media> dao = getDaoClient().getEntityDao(Media.class);
			Media m = dao.fetch(id);
			if (m != null && m.getData() != null) {
				writeImage(m);
				return;
			}
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		res.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	private void writeImage(Media m) throws Exception {
		HttpServletRequest req = getRequest();
		HttpServletResponse res = getResponse();

		HttpServletResponser hsrs = new HttpServletResponser(req, res);
		hsrs.setFileName(Strings.isEmpty(m.getName()) ? ("media-" + m.getId() + ".jpg") : m.getName());
		hsrs.setContentType(MimeTypes.getMimeType(hsrs.getFileName()));
		hsrs.setContentLength(m.getData().length);
		hsrs.writeHeader();
		hsrs.writeBytes(m.getData());
	}
}

