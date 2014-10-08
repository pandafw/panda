package panda.mvc.aware;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;



/**
 * Provides a default implementation of ActionValidationAware.
 */
@IocBean(type=ActionAware.class, scope=Scope.REQUEST)
public class ActionAwareSupport extends AwareSupport implements ActionAware {
}
