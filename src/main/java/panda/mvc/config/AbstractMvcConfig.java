package panda.mvc.config;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import panda.ioc.Ioc;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConfig;

public abstract class AbstractMvcConfig implements MvcConfig {

	private static final Log log = Logs.getLog(AbstractMvcConfig.class);

	private Ioc ioc;

	public AbstractMvcConfig(ServletContext context) {
	}

	public String getAppRoot() {
		String webinf = getServletContext().getRealPath("/WEB-INF/");
		if (webinf == null) {
			log.info("/WEB-INF/ not Found?!");
			return "";
		}

		String root = getServletContext().getRealPath("/").replace('\\', '/');
		if (root.endsWith("/")) {
			return root.substring(0, root.length() - 1);
		}
		
		if (root.endsWith("/.")) {
			return root.substring(0, root.length() - 2);
		}
		
		return root;
	}

	public Ioc getIoc() {
		return ioc;
	}

	public void setIoc(Ioc ioc) {
		this.ioc = ioc;
	}

	protected Object getAttribute(String name) {
		return this.getServletContext().getAttribute(name);
	}

	protected void setAttribute(String name, Object obj) {
		this.getServletContext().setAttribute(name, obj);
	}

	public Class<?> getMainModule() {
		try {
			String name = Strings.trim(getInitParameter("modules"));
			Class<?> mainModule = null;
			if (Strings.isBlank(name)) {
				throw new IllegalArgumentException(
					"You need declare 'modules' parameter in your context configuration file or web.xml ! Only found -> "
							+ getInitParameterNames());
			}
	
			mainModule = Classes.getClass(name);
			log.debugf("MainModule: <%s>", mainModule.getName());
			return mainModule;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	protected List<String> enum2list(Enumeration<?> enums) {
		LinkedList<String> re = new LinkedList<String>();
		while (enums.hasMoreElements()) {
			re.add(enums.nextElement().toString());
		}
		return re;
	}

}
