package panda.mvc.config;

import java.util.List;

import javax.servlet.ServletConfig;

public class ServletMvcConfig extends AbstractMvcConfig {

	private ServletConfig config;

	public ServletMvcConfig(ServletConfig config) {
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
		return config.getServletName();
	}

}
