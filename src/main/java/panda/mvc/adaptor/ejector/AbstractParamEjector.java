package panda.mvc.adaptor.ejector;

import java.util.Map;
import java.util.Set;

import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;
import panda.mvc.adaptor.ParamEjector;

public abstract class AbstractParamEjector implements ParamEjector {
	@IocInject
	protected ActionContext ac;

	public AbstractParamEjector() {
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
