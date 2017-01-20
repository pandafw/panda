package panda.mvc.processor;

import java.lang.reflect.Method;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;

@IocBean
public class InvokeProcessor extends AbstractProcessor {
	public void process(ActionContext ac) {
		Object action = ac.getAction();
		Method method = ac.getMethod();
		Object[] args = ac.getArgs();
		try {
			Object r = method.invoke(action, args);
			if (r != null) {
				if (r instanceof View) {
					ac.setView((View)r);
				}
				else {
					ac.setResult(r);
				}
			}
			doNext(ac);
		}
		catch (Exception e) {
			String msg = "Failed to invoke " + method;
			throw new RuntimeException(msg, e);
		}
	}
}
