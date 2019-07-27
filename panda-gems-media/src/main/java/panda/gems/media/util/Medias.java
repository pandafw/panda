package panda.gems.media.util;

import java.io.IOException;

import panda.gems.media.V;
import panda.gems.media.entity.Media;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.FileNames;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class Medias {
	private static final Log log = Logs.getLog(Medias.class);
	
	public static void setFileMeta(Media m) {
		if (m.getFile() == null || !m.getFile().isExists()) {
			return;
		}
		
		m.setSize(m.getFile().getSize());
		if (Strings.isEmpty(m.getName())) {
			m.setName(Strings.right(FileNames.getName(m.getFile().getName()), V.FILENAME_MAX_LENGTH));
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
