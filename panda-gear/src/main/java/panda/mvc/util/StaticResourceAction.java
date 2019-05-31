package panda.mvc.util;

import java.io.File;
import java.net.URL;

import panda.Panda;
import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.SetConstants;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;


@At("${!!static_path|||'/static'}")
public class StaticResourceAction extends ActionSupport {
	private static final Log log = Logs.getLog(StaticResourceAction.class);

	protected static final String RESBASE = "/panda/html/";
	protected static final String VERSION = Panda.VERSION + '/';
	
	protected String getStaticVersion() {
		String version = getSettings().getProperty(SetConstants.MVC_STATIC_VERSION);

		version = Strings.stripToNull(version);
		if (Strings.isEmpty(version)) {
			return VERSION;
		}
		
		if (!Strings.endsWithChar(version, '/')) {
			version += '/';
		}
		return version;
	}

	@At("(.*)$")
	@To(Views.RES)
	public Object execute(@PathArg String name) {
		Object r = findResource(context.getPath(), name);
		if (r == null) {
			return Views.scNotFound(context);
		}
		return r;
	}
	
	protected File findFile(String path) {
		if (log.isDebugEnabled()) {
			log.debug("Find File: " + path);
		}

		String rpath = getServlet().getRealPath(path);
		File file = new File(rpath);
		return (file.exists() ? file : null);
	}

	protected URL findClass(String path) {
		if (log.isDebugEnabled()) {
			log.debug("Find Resource: " + path);
		}

		return ClassLoaders.getResourceAsURL(path);
	}

	protected Object findResource(String path, String name) {
		String version = getStaticVersion();

		File file = findFile(path);
		if (file != null) {
			return file;
		}
		
		String sname = null;
		if (Strings.isNotEmpty(version) && Strings.startsWith(name, version)) {
			sname = name.substring(version.length());
		}

		if (sname != null) {
			path = path.substring(0, path.length() - name.length()) + sname;
			file = findFile(path);
			if (file != null) {
				return file;
			}
		}

		path = RESBASE + name;
		URL url = findClass(path);
		if (url != null) {
			return url;
		}
		
		if (sname != null) {
			path = RESBASE + sname;
			return findClass(path);
		}
		
		return null;
	}
}
