package panda.mvc.adaptor.ejector;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.adaptor.ParamEjector;

/**
 * Form parameter ejector (Default)
 */
@IocBean(singleton=false)
public class FormParamEjector implements ParamEjector {
	@IocInject
	private HttpServletRequest req;

	public FormParamEjector() {
	}

	public Object eject() {
		return req.getParameterMap();
	}
	
	public Object eject(String name) {
		return req.getParameterValues(name);
	}

	public Set<String> keys() {
		return req.getParameterMap().keySet();
	}

}
