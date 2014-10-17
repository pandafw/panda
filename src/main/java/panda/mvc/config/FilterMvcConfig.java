package panda.mvc.config;

import java.util.List;

import javax.servlet.FilterConfig;

public class FilterMvcConfig extends AbstractMvcConfig {

	private FilterConfig config;

	public FilterMvcConfig(FilterConfig config) {
		this.config = config;
		init(config.getServletContext());
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
