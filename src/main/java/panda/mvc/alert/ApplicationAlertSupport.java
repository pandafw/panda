package panda.mvc.alert;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;


@IocBean(type=ApplicationAlert.class, scope=Scope.APP)
public class ApplicationAlertSupport extends AlertSupport implements ApplicationAlert {
}
