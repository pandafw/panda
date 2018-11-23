package panda.mvc.filepool;

import java.io.File;

import javax.servlet.ServletContext;

import panda.io.FileNames;
import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.MvcConstants;
import panda.vfs.FilePool;
import panda.vfs.local.LocalFilePool;

@IocBean(type=FilePool.class, create="initialize")
public class MvcLocalFilePool extends LocalFilePool {
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
	@IocInject(value=MvcConstants.FILEPOOL_LOCAL_PATH, required=false)
	public void setPath(String path) {
		super.setPath(path);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILEPOOL_MAXAGE, required=false)
	public void setMaxAge(int maxAge) {
		super.setMaxAge(maxAge);
	}
	
}
