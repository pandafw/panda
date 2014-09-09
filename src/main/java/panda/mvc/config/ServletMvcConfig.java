package panda.mvc.config;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletMvcConfig extends AbstractMvcConfig {

	private ServletConfig config;

	public ServletMvcConfig(ServletConfig config) {
		super(config.getServletContext());
		this.config = config;
		setAtMap(new AtMap());
	}

	public ServletContext getServletContext() {
		return config.getServletContext();
	}

	public String getInitParameter(String name) {
		return config.getInitParameter(name);
	}

	public List<String> getInitParameterNames() {
		return enum2list(config.getInitParameterNames());
	}

	public String getAppName() {
		return config.getServletName();
	}

}
