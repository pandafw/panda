package panda.mvc.vfs;

import java.io.File;

import javax.servlet.ServletContext;

import panda.io.FileNames;
import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.MvcConstants;
import panda.vfs.FileStore;
import panda.vfs.local.LocalFileStore;

@IocBean(type=FileStore.class, create="initialize")
public class MvcLocalFileStore extends LocalFileStore {
	@IocInject(required=false)
	private ServletContext servlet;
	
	public void initialize() {
		if (Strings.isEmpty(path)) {
			String sub = null;
			if (servlet != null) {
				sub = FileNames.trimFileName(servlet.getServletContextName());
			}
			if (Strings.isEmpty(sub)) {
				sub = "files";
			}
			path = new File(Files.getTempDirectory(), sub).getPath();
		}
	}
	
	/**
	 * @param path the path to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILESTORE_LOCAL_PATH, required=false)
	public void setPath(String path) {
		super.setPath(path);
	}
}
