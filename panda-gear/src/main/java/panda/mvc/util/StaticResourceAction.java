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

	protected static final String BASE = "panda/html/";
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
	public Object execute(@PathArg String path) {
		Object r = findResource(path);
		if (r == null) {
			return Views.scNotFound(context);
		}
		return r;
	}
	
	protected File findFile(String path) {
		String rpath = getServlet().getRealPath(path);
		File file = new File(rpath);
		return (file.exists() ? file : null);
	}
	
	protected Object findResource(String name) {
		String version = getStaticVersion();

		String path = BASE + name;
		
		if (log.isDebugEnabled()) {
			log.debug("Find File: " + path);
		}

		File file = findFile(path);
		if (file != null) {
			return file;
		}
		
		String npath = null;
		if (Strings.isNotEmpty(version) && Strings.startsWith(name, version)) {
			npath = BASE + Strings.substring(name, version.length());
		}

		if (npath != null) {
			if (log.isDebugEnabled()) {
				log.debug("Find File: " + npath);
			}

			file = findFile(npath);
			if (file != null) {
				return file;
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Find Resource: " + path);
		}

		URL url = ClassLoaders.getResourceAsURL(path);
		if (url != null) {
			return url;
		}
		
		if (npath != null) {
			if (log.isDebugEnabled()) {
				log.debug("Find Resource: " + npath);
			}
			
			url = ClassLoaders.getResourceAsURL(npath);
			if (url != null) {
				return url;
			}
		}
		
		return null;
	}
}
