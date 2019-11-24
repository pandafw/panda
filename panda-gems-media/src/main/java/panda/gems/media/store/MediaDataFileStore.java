package panda.gems.media.store;

import java.io.File;
import java.io.IOException;

import panda.app.util.AppSettings;
import panda.dao.Dao;
import panda.gems.media.I;
import panda.gems.media.S;
import panda.gems.media.V;
import panda.gems.media.entity.Media;
import panda.gems.media.entity.MediaData;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.FileNames;
import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FileStores;

@IocBean(type=MediaDataStore.class)
public class MediaDataFileStore extends AbstractMediaDataStore {
	private static final Log log = Logs.getLog(MediaDataFileStore.class);

	protected static final String DEFAULT_LOCATION = "${web.dir}/WEB-INF/_media";
	
	@IocInject
	protected AppSettings settings;
	
	@IocInject(value=I.MEDIA_LOCATION, required=false)
	protected String location;

	protected String getLocation() {
		String loc = settings.getProperty(S.MEDIA_LOCATION);
		if (Strings.isEmpty(loc)) {
			if (Strings.isEmpty(location)) {
				location = Texts.translate(DEFAULT_LOCATION, settings);
			}
			loc = location;
		}
		return loc;
	}

	protected File getDir(Long id, String slug) {
		String loc = getLocation();
		String sub = Strings.leftPad(String.valueOf(id % 10000), 4, '0') + '/' + slug;
		return new File(loc, sub);
	}
	
	protected String getName(Media m, int sz) {
		String ext = FileNames.getExtension(m.getName());
		String name = Strings.isEmpty(ext) ? String.valueOf(sz) : sz + "." + ext;
		return name;
	}
	
	@Override
	public MediaData find(Dao dao, Media m, int sz) {
		File dir = getDir(m.getId(), m.getSlug());
		
		File file = new File(dir, getName(m, sz));
		
		byte[] data = null;
		
		if (file.exists()) {
			try {
				data = Files.readToBytes(file);
			}
			catch (IOException e) {
				log.error("Failed to read data of media [" + m.getId() + "] (" + sz + ")", e);
				return null;
			}
		}
		else if (sz != V.ORIGINAL) {
			File org = new File(dir, getName(m, V.ORIGINAL));
			if (!org.exists()) {
				return null;
			}

			try {
				// resize
				ImageWrapper iw = Images.i().read(org);
				iw = iw.resize(sz);
				data = iw.getData();

				// save
				Files.write(file, data);
			}
			catch (Exception e) {
				log.error("Failed to save data of media [" + m.getId() + "] (" + sz + ")", e);
				return null;
			}
		}
		else {
			return null;
		}

		MediaData md = new MediaData();
		md.setMid(m.getId());
		md.setMsz(sz);
		md.setSize(data.length);
		md.setData(data);

		return md;
	}

	@Override
	public void save(Dao dao, Media m) {
		try {
			File dir = getDir(m.getId(), m.getSlug());
			Files.makeDirs(dir);
			
			File file = new File(dir, getName(m, V.ORIGINAL));

			FileStores.copy(m.getFile(), file);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to save media data of [" + m.getId() + "]", e);
		}
	}

	@Override
	public void delete(Dao dao, Media... ms) {
		if (Arrays.isEmpty(ms)) {
			return;
		}

		for (Media m : ms) {
			if (m == null) {
				continue;
			}
			
			File dir = getDir(m.getId(), m.getSlug());
			try {
				Files.deleteDir(dir);
				dir.getParentFile().delete();
			}
			catch (Exception e) {
				log.error("Failed to delete media data for [" + dir + "]", e);
			}
		}
	}
}
