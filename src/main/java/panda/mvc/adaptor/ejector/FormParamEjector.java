package panda.mvc.adaptor.ejector;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import panda.mvc.ActionContext;
import panda.mvc.adaptor.ParamEjector;

/**
 * Form parameter ejector (Default)
 */
public class FormParamEjector implements ParamEjector {
	private HttpServletRequest req;

	public FormParamEjector(ActionContext ac) {
		this.req = ac.getRequest();
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
