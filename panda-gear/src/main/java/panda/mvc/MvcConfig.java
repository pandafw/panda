package panda.mvc;

import javax.servlet.ServletContext;

import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class MvcConfig {
	private static final Log log = Logs.getLog(MvcConfig.class);

	private ServletContext servlet;
	private Class<?> mainModule;

	public MvcConfig(ServletContext servlet, String mainModule) {
		this.servlet = servlet;

		String name = Strings.trim(mainModule);
		if (Strings.isBlank(name)) {
			throw new IllegalArgumentException(
				"You need declare 'modules' parameter in your context configuration file or web.xml!");
		}

		try {
			this.mainModule = Classes.getClass(mainModule);
		}
		catch (ClassNotFoundException e) {
			throw Exceptions.wrapThrow(e);
		}

		log.debugf("MainModule: <%s>", this.mainModule.getName());
	}

	public ServletContext getServletContext() {
		return servlet;
	}

	public Class<?> getMainModule() {
		return mainModule;
	}
}
