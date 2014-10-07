package panda.mvc.validation.aware;

import panda.ioc.annotation.IocBean;


/**
 * Provides a default implementation of ApplicationValidationAware. 
 */
@IocBean(type=ApplicationValidationAware.class)
public class ApplicationValidationAwareSupport extends ValidationAwareSupport implements ApplicationValidationAware {
}
