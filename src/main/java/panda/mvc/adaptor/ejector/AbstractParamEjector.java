package panda.mvc.adaptor.ejector;

import java.util.Map;
import java.util.Set;

import panda.mvc.ActionContext;
import panda.mvc.adaptor.ParamEjector;

public abstract class AbstractParamEjector implements ParamEjector {
	protected ActionContext ac;

	public AbstractParamEjector(ActionContext ac) {
		this.ac = ac;
	}

	protected ActionContext getActionContext() {
		return ac;
	}
	
	protected abstract Map<String, Object> getParams();
	
	public Object eject() {
		return getParams();
	}

	public Object eject(String name) {
		return getParams().get(name);
	}

	public Set<String> keys() {
		return getParams().keySet();
	}

}
