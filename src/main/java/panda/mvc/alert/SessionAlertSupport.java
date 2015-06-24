package panda.mvc.alert;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;

@IocBean(type=SessionAlert.class, scope=Scope.SESSION)
public class SessionAlertSupport extends AlertSupport implements SessionAlert {
}

