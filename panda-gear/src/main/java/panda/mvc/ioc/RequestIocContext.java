package panda.mvc.ioc;

import panda.ioc.Scope;
import panda.ioc.impl.ScopeIocContext;

public class RequestIocContext extends ScopeIocContext {
	public RequestIocContext() {
		super(Scope.REQUEST);
	}
}
