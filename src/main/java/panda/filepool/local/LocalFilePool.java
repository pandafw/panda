package panda.filepool.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import panda.filepool.FileItem;
import panda.filepool.FilePool;
import panda.filepool.NullFileItem;
import panda.io.FileNames;
import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.mvc.adaptor.multipart.FileItemStream;

@IocBean(type=FilePool.class)
public class LocalFilePool implements FilePool {
	protected final static FastDateFormat fdf = FastDateFormat.getInstance("yyyy/MM/dd/HH/mm/ss/SSS");

	@IocInject(value="ref:panda.filepool.local.path", required=false)
	protected String path;

	public LocalFilePool() {
		path = Files.getTempDirectoryPath();
	}
	
	protected LocalFileItem randFile(String name, boolean temporary) {
		while (true) {
			long id = DateTimes.getDate().getTime();
			if (temporary && (id & 1L) != 0) {
				id++;
			}
	
			File dir = new File(path, fdf.format(id));
			if (dir.exists()) {
				Objects.safeSleep(1);
				continue;
			}

			File file = new File(dir, name);
			return new LocalFileItem(id, file);
		}
	}
	
	public FileItem saveFile(FileItemStream fis, boolean temporary) throws IOException {
		String name = FileNames.getName(fis.getName());
		return saveFile(name, fis.openStream(), temporary);
	}
	
	public FileItem saveFile(String name, final InputStream body, boolean temporary) throws IOException {
		LocalFileItem fi = randFile(name ,temporary);
		fi.getFile().getParentFile().mkdirs();
		Files.write(fi.getFile(), body);
		return fi;
	}

	public FileItem findFile(Long id) {
		File dir = new File(path, fdf.format(id));
		if (dir.exists()) {
			File[] fs = dir.listFiles();
			if (Arrays.isNotEmpty(fs)) {
				return new LocalFileItem(id, fs[0]);
			}
		}

		return new NullFileItem(id);
	}
}
