package panda.mvc.alert;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;

@IocBean(type=ActionAlert.class, scope=Scope.REQUEST)
public class ActionAlertSupport extends AlertSupport implements ActionAlert {
}
