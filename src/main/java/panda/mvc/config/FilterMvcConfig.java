package panda.mvc.config;

import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

public class FilterMvcConfig extends AbstractMvcConfig {

	private FilterConfig config;

	public FilterMvcConfig(FilterConfig config) {
		super(config.getServletContext());
		this.config = config;
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
		return config.getFilterName();
	}

}
