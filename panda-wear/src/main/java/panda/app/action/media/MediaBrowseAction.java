package panda.app.action.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.SET;
import panda.app.entity.Media;
import panda.app.entity.query.MediaQuery;
import panda.app.media.MediaData;
import panda.app.media.MediaDataSaver;
import panda.app.media.Medias;
import panda.dao.Dao;
import panda.io.FileNames;
import panda.io.MimeTypes;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServletResponser;
import panda.vfs.FileItem;

@At("${super_path}/media")
@Auth(AUTH.SUPER)
public class MediaBrowseAction extends AbstractAction {
	@IocInject
	private MediaDataSaver mds;

	public static class Arg {
		public Date ds;
		public Date de;
		public String qs;
		public Long sn;
	}

	private void find(Long id, String sc, int sz) throws IOException {
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
					write(m, md);
					return;
				}
			}
		}

		getResponse().setStatus(HttpStatus.SC_NOT_FOUND);
	}

	@At
	public void media(@Param("id") Long id) throws Exception {
		find(id, null, 0);
	}

	@At
	public void thumb(@Param("id") Long id) throws Exception {
		find(id, SET.MEDIA_THUMB_SIZE, Medias.DEFAULT_THUMB_SIZE);
	}

	@At
	public void icon(@Param("id") Long id) throws Exception {
		find(id, SET.MEDIA_ICON_SIZE, Medias.DEFAULT_ICON_SIZE);
	}

	private void write(Media m, MediaData md) throws IOException {
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
	
	@At("")
	@To(Views.SFTL)
	@Redirect(toslash=true)
	public Object index(@Param Arg arg) {
		return browse(arg);
	}
	
	@At
	@To(Views.SJSON)
	public Object browse(@Param Arg arg) {
		Dao dao = getDaoClient().getDao();

		MediaQuery mq = new MediaQuery();
		if (arg.ds != null) {
			mq.createdAt().greaterThanOrEqualTo(arg.ds);
		}
		if (arg.de != null) {
			arg.de = DateTimes.zeroCeiling(arg.de);
			mq.createdAt().lessThan(arg.de);
		}
		if (Strings.isNotEmpty(arg.qs)) {
			mq.name().match(arg.qs);
		}
		if (arg.sn != null) {
			mq.id().lessThan(arg.sn);
		}
		mq.orderBy(Media.ID, false);
		mq.limit(getMediaLimit());
		
		addFilters(mq);

		return dao.select(mq);
	}

	// add filters for sub class
	protected void addFilters(MediaQuery mq) {
	}

	public int getMediaLimit() {
		return 3;
	}
	
	/**
	 * upload
	 * 
	 * @param files the upload files
	 * @return the uploaded files
	 */
	@At
	@To(Views.SJSON)
	public List<Media> uploads(final @Param("files") FileItem[] files) {
		final List<Media> medias = new ArrayList<Media>();
		
		if (Arrays.isNotEmpty(files)) {
			final Dao dao = getDaoClient().getDao();
			dao.exec(new Runnable() {
				@Override
				public void run() {
					for (FileItem fi : files) {
						if (fi != null && fi.isExists()) {
							Media m = new Media();
							m.setFile(fi);
							Medias.setFileMeta(m);
							assist().initCreatedByFields(m);
							dao.insert(m);
							mds.save(m);
							
							m.setFile(null);
							medias.add(m);
						}
					}
				}
			});
		}
		return medias;
	}
}
