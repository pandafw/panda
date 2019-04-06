package panda.app.action.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.SET;
import panda.app.entity.Media;
import panda.app.entity.query.MediaQuery;
import panda.app.media.MediaData;
import panda.app.media.MediaDataStore;
import panda.app.media.Medias;
import panda.dao.Dao;
import panda.io.FileNames;
import panda.io.MimeTypes;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServletResponser;
import panda.servlet.HttpServlets;
import panda.vfs.FileItem;
import panda.vfs.FileStores;

@At("${!!super_path|||'/super'}/media")
@Auth(AUTH.SUPER)
public class MediaBrowseAction extends AbstractAction {
	private static final Log log = Logs.getLog(MediaBrowseAction.class);
	
	@IocInject
	private MediaDataStore mds;

	public static class Arg {
		public Date ds;
		public Date de;
		public String qs;
		public Long si;
	}

	@At("media/(.*)$")
	public void media2(@PathArg String slug) throws Exception {
		media(slug);
	}

	@At("thumb/(.*)$")
	public void thumb2(@PathArg String slug) throws Exception {
		thumb(slug);
	}

	@At("icon/(.*)$")
	public void icon2(@PathArg String slug) throws Exception {
		icon(slug);
	}

	@At
	public void media(@Param("slug") String slug) throws Exception {
		find(slug, null, 0);
	}

	@At
	public void thumb(@Param("slug") String slug) throws Exception {
		find(slug, SET.MEDIA_THUMB_SIZE, Medias.DEFAULT_THUMB_SIZE);
	}

	@At
	public void icon(@Param("slug") String slug) throws Exception {
		find(slug, SET.MEDIA_ICON_SIZE, Medias.DEFAULT_ICON_SIZE);
	}

	@At("")
	@To(Views.SFTL)
	@Redirect(toslash=true)
	public Object index(@Param Arg arg) {
		return browse(arg);
	}
	
	@At
	@To(Views.SFTL)
	public Object browse_popup(@Param Arg arg) {
		return browse(arg);
	}
	
	@At
	@To(Views.SFTL)
	public Object select_popup(@Param Arg arg) {
		return browse(arg);
	}

	@At
	@To(Views.SJSON)
	public Object browse(@Param Arg arg) {
		Dao dao = getDaoClient().getDao();

		MediaQuery mq = new MediaQuery();
		if (arg.ds != null) {
			mq.createdAt().ge(arg.ds);
		}
		if (arg.de != null) {
			arg.de = DateTimes.zeroCeiling(arg.de);
			mq.createdAt().lt(arg.de);
		}
		if (Strings.isNotEmpty(arg.qs)) {
			mq.name().contains(arg.qs);
		}
		if (arg.si != null) {
			mq.id().lt(arg.si);
		}
		mq.id().desc();
		mq.limit(getMediaIndexLimit());
		
		addFilters(mq);

		return dao.select(mq);
	}

	// add filters for sub class
	protected void addFilters(MediaQuery mq) {
	}

	public int getMediaIndexLimit() {
		return getSettings().getPropertyAsInt(SET.MEDIA_INDEX_LIMIT, Medias.DEFAULT_INDEX_LIMIT);
	}
	
	@At
	@To(Views.SJSON)
	public Object deletes(final @Param("id") List<Long> ids) {
		if (Collections.isEmpty(ids)) {
			return null;
		}

		final Dao dao = getDaoClient().getDao();
		MediaQuery mq = new MediaQuery();
		mq.id().in(ids);
		List<Media> ms = dao.select(mq);
		
		final Map<Long, Object> r = new HashMap<Long, Object>();
		for (final Media m : ms) {
			try {
				dao.exec(new Runnable() {
					@Override
					public void run() {
						dao.delete(m);
						mds.delete(dao, m);
					}
				});
				r.put(m.getId(), true);
			}
			catch (Exception e) {
				log.warn("Failed to delete media " + m.getId(), e);
				r.put(m.getId(), e.getMessage());
			}
		}
		
		return r;
	}
	
	/**
	 * upload
	 * 
	 * @param files the upload files
	 * @return the uploaded media
	 */
	@At
	@To(Views.SJSON)
	public List<Media> uploads(final @Param("files") List<FileItem> files) {
		final List<Media> medias = new ArrayList<Media>();
		
		if (Collections.isNotEmpty(files)) {
			final Dao dao = getDaoClient().getDao();
			for (final FileItem fi : files) {
				if (fi != null && fi.isExists()) {
					try {
						dao.exec(new Runnable() {
							@Override
							public void run() {
								Media m = new Media();
								m.setSlug(Randoms.randUUID32());
								m.setFile(fi);
								Medias.setFileMeta(m);
								assist().setCreatedByFields(m);
								dao.insert(m);
								mds.save(dao, m);

								m.setFile(null);
								medias.add(m);
							}
						});
					}
					catch (Exception e) {
						log.error("Failed to save media " + fi.getName(), e);
						String msg = getText("media-save-failed", "Failed to save media file ${top}.", FileNames.getName(fi.getName()));
						if (getContext().isAppDebug()) {
							msg += "\n" + e.getMessage();
						}
						addActionWarning(msg);
					}
					finally {
						FileStores.safeDelete(fi);
					}
				}
			}
		}
		return medias;
	}

	protected void find(String slug, String sc, int sz) throws IOException {
		if (Strings.isNotEmpty(slug)) {
			Dao dao = getDaoClient().getDao();

			MediaQuery mq = new MediaQuery();
			mq.slug().eq(slug);
			
			Media m = dao.fetch(mq);
			if (m != null) {
				int maxage = getSettings().getPropertyAsInt(SET.MEDIA_CACHE_MAXAGE, Medias.DEFAULT_CACHE_MAXAGE);

				if (HttpServlets.checkAndSetNotModified(getRequest(), getResponse(), m.getUpdatedAt(), maxage)) {
					return;
				}
				
				MediaData md = null;
				if (sc == null) {
					md = mds.find(dao, m);
				}
				else {
					sz = getSettings().getPropertyAsInt(sc, sz);
					md = mds.find(dao, m, sz); 
				}
				if (md != null) {
					write(m, md, maxage);
					return;
				}
			}
		}

		getResponse().setStatus(HttpStatus.SC_NOT_FOUND);
	}

	protected void write(Media m, MediaData md, int maxage) throws IOException {
		String filename = m.getName();
		if (Strings.isEmpty(filename)) { 
			filename = "media-" + m.getId() + ".jpg";
		}
		if (md.getMsz() != Medias.ORIGINAL) {
			filename = FileNames.addSuffix(filename, "-" + md.getMsz());
		}

		HttpServletResponser hsrs = new HttpServletResponser(getRequest(), getResponse());
		hsrs.setFileName(filename);
		hsrs.setContentType(MimeTypes.getMimeType(hsrs.getFileName()));
		hsrs.setContentLength(md.getSize());
		hsrs.setLastModified(m.getUpdatedAt());
		hsrs.setMaxAge(maxage);
		hsrs.writeHeader();
		hsrs.writeStream(md.open());
	}
	
}

