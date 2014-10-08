package panda.mvc.aware;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;


/**
 * Provides a default implementation of ApplicationValidationAware. 
 */
@IocBean(type=ApplicationAware.class, scope=Scope.APP)
public class ApplicationAwareSupport extends AwareSupport implements ApplicationAware {
}
