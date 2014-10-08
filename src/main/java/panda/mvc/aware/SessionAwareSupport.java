package panda.mvc.aware;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;

/**
 * Provides a default implementation of SessionValidationAware. 
 */
@IocBean(type=SessionAware.class, scope=Scope.SESSION)
public class SessionAwareSupport extends AwareSupport implements SessionAware {
}

