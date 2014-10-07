package panda.mvc.validation.aware;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;

/**
 * Provides a default implementation of SessionValidationAware. 
 */
@IocBean(type=SessionValidationAware.class, scope=Scope.SESSION)
public class SessionValidationAwareSupport extends ValidationAwareSupport implements SessionValidationAware {
}

