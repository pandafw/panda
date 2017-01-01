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
import panda.mvc.ActionContext;
import panda.mvc.MvcConfig;

public abstract class AbstractMvcConfig implements MvcConfig {

	private static final Log log = Logs.getLog(AbstractMvcConfig.class);

	private Ioc ioc;
	private ServletContext servlet;
	private Class<?> mainModule;
	private Class<? extends ActionContext> contextClass;

	public AbstractMvcConfig() {
	}
	
	protected void init(ServletContext servlet) {
		this.servlet = servlet;

		String name = Strings.trim(getInitParameter("modules"));
		if (Strings.isBlank(name)) {
			throw new IllegalArgumentException(
				"You need declare 'modules' parameter in your context configuration file or web.xml! Only found -> "
						+ getInitParameterNames());
		}

		try {
			mainModule = Classes.getClass(name);
		}
		catch (ClassNotFoundException e) {
			throw Exceptions.wrapThrow(e);
		}

		log.debugf("MainModule: <%s>", mainModule.getName());
	}

	public Ioc getIoc() {
		return ioc;
	}

	public void setIoc(Ioc ioc) {
		this.ioc = ioc;
	}

	public ServletContext getServletContext() {
		return servlet;
	}

	public Class<?> getMainModule() {
		return mainModule;
	}

	/**
	 * @return the contextClass
	 */
	public Class<? extends ActionContext> getContextClass() {
		return contextClass;
	}

	/**
	 * @param contextClass the contextClass to set
	 */
	public void setContextClass(Class<? extends ActionContext> contextClass) {
		this.contextClass = contextClass;
	}

	protected List<String> enum2list(Enumeration<?> enums) {
		LinkedList<String> re = new LinkedList<String>();
		while (enums.hasMoreElements()) {
			re.add(enums.nextElement().toString());
		}
		return re;
	}

}
