package panda.mvc.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import panda.ioc.annotation.IocBean;
import panda.lang.reflect.Methods;
import panda.mvc.ActionContext;
import panda.mvc.ValidateException;
import panda.mvc.View;
import panda.mvc.view.Views;

@IocBean
public class InvokeProcessor extends AbstractProcessor {
	public void process(ActionContext ac) {
		Object action = ac.getAction();
		Method method = ac.getMethod();
		Object[] args = ac.getArgs();
		Object r = null;
		
		try {
			r = method.invoke(action, args);
		}
		catch (InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof ValidateException) {
				r = Views.createView(ac, ac.getConfig().getErrorView());
				if (r == null) {
					throw (ValidateException)ex;
				}
			}
			else if (ex instanceof RuntimeException) {
				throw (RuntimeException)ex;
			}
			else {
				throw new RuntimeException(ex);
			}
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to invoke " + Methods.toSimpleString(method) + ": " + e.getMessage(), e);
		}

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
}
