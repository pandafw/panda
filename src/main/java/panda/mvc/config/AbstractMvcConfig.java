package panda.mvc.config;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import panda.ioc.Ioc;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Loading;
import panda.mvc.MvcConfig;
import panda.mvc.MvcConfigException;
import panda.mvc.annotation.LoadingBy;
import panda.mvc.impl.MvcLoading;

public abstract class AbstractMvcConfig implements MvcConfig {

	private static final Log log = Logs.getLog(AbstractMvcConfig.class);

	private static final String ATTR_IOC = "$ioc";
	private static final String ATTR_LOADCTX = "$loadctx";

	public AbstractMvcConfig(ServletContext context) {
	}

	public Loading createLoading() {
		// 确保用户声明了 MainModule
		Class<?> mainModule = getMainModule();

		// 获取 Loading
		LoadingBy by = mainModule.getAnnotation(LoadingBy.class);
		if (null == by) {
			if (log.isDebugEnabled()) {
				log.debug("Loading by " + MvcLoading.class);
			}
			return new MvcLoading();
		}
		if (log.isDebugEnabled()) {
			log.debug("Loading by " + by.value());
		}
		return Classes.born(by.value());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getLoadingContext() {
		return (Map)getAttribute(ATTR_LOADCTX);
	}

	public void setLoadingContext(Map<String, Object> context) {
		setAttribute(ATTR_LOADCTX, context);
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
		return (Ioc)getAttribute(ATTR_IOC);
	}

	public void setIoc(Ioc ioc) {
		setAttribute(ATTR_IOC, ioc);
	}

	protected Object getAttribute(String name) {
		return this.getServletContext().getAttribute(name);
	}

	protected void setAttribute(String name, Object obj) {
		this.getServletContext().setAttribute(name, obj);
	}

	public Class<?> getMainModule() {
		String name = Strings.trim(getInitParameter("modules"));
		try {
			Class<?> mainModule = null;
			if (Strings.isBlank(name)) {
				throw new MvcConfigException(
					"You need declare 'modules' parameter in your context configuration file or web.xml ! Only found -> "
							+ getInitParameterNames());
			}
			mainModule = Classes.getClass(name);
			log.debugf("MainModule: <%s>", mainModule.getName());
			return mainModule;
		}
		catch (MvcConfigException e) {
			throw e;
		}
		catch (Exception e) {
			throw new MvcConfigException(e);
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
