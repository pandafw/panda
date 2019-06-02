package panda.gems.media;

import java.io.IOException;

import panda.gems.media.entity.Media;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.FileNames;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;

public class Medias {
	private static final Log log = Logs.getLog(Medias.class);
	
	//------------------------------------------------
	// The ioc media settings
	//
	public static final String IOC_MEDIA_LOCATION = "panda.media.location";
	public static final String IOC_MEDIA_GCS_BUCKET = "panda.media.gcs.bucket";
	public static final String IOC_MEDIA_GCS_PREFIX = "panda.media.gcs.prefix";

	//------------------------------------------------
	// The media settings
	//
	public static final String SET_MEDIA_LOCATION = "media.location";
	public static final String SET_MEDIA_ICON_SIZE = "media.icon.size";
	public static final String SET_MEDIA_THUMB_SIZE = "media.thumb.size";
	public static final String SET_MEDIA_INDEX_LIMIT = "media.index.limit";
	public static final String SET_MEDIA_CACHE_MAXAGE = "media.cache.maxage";

	//------------------------------------------------
	public static final int ORIGINAL = 0;

	public static final int MAX_FILENAME_LENGTH = 200;

	public static final int DEFAULT_ICON_SIZE = 32;

	public static final int DEFAULT_THUMB_SIZE = 128;

	public static final int DEFAULT_INDEX_LIMIT = 50;

	public static final int DEFAULT_CACHE_MAXAGE = 30 * DateTimes.SEC_DAY;

	public static void setFileMeta(Media m) {
		if (m.getFile() == null || !m.getFile().isExists()) {
			return;
		}
		
		m.setSize(m.getFile().getSize());
		if (Strings.isEmpty(m.getName())) {
			m.setName(Strings.right(FileNames.getName(m.getFile().getName()), Medias.MAX_FILENAME_LENGTH));
		}

		try {
			ImageWrapper iw = Images.i().read(m.getFile().data());
			m.setWidth(iw.getWidth());
			m.setHeight(iw.getHeight());
		}
		catch (IOException e) {
			log.warn("Failed to read media meta: " + e.getMessage());
		}
	}

}
