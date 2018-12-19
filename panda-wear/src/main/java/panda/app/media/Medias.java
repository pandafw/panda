package panda.app.media;

import java.io.IOException;

import panda.app.entity.Media;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class Medias {
	private static final Log log = Logs.getLog(Medias.class);
	
	public static final int MAX_FILENAME_LENGTH = 200;

	public static final int DEFAULT_PAGE_LIMIT = 50;

	public static final int DEFAULT_ICON_SIZE = 32;

	public static final int DEFAULT_THUMB_SIZE = 128;

	public static String getMediaIcon(Media m) {
		return "<img class=\"p-mwh32px\" src=\"icon?id=" + m.getId() + "\">";
	}
	
	public static String getMediaLink(Media m) {
		if (m == null || m.getSize() <= 0) {
			return "";
		}
		return "<a href=\"view?id=" + m.getId() + "\">" + getMediaIcon(m) + "</a>";
	}

	public static void setFileMeta(Media m) {
		if (m.getFile() == null || !m.getFile().isExists()) {
			return;
		}
		
		m.setSize(m.getFile().getSize());
		if (Strings.isEmpty(m.getName())) {
			m.setName(Strings.right(m.getFile().getName(), Medias.MAX_FILENAME_LENGTH));
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
