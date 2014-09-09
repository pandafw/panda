package panda.mvc.impl.processor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.impl.ComboContext;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.IocRequestListener;
import panda.mvc.IocSessionListener;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.RequestIocContext;
import panda.mvc.ioc.SessionIocContext;

public class ActionProcessor extends AbstractProcessor {

	private static final Log log = Logs.getLog(ActionProcessor.class);
	private static Map<Class<?>, Object> moduleMap = new HashMap<Class<?>, Object>();

	private String moduleName;
	private Class<?> moduleType;
	private Method method;
	private Object moduleObj;


	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		method = ai.getMethod();
		moduleType = ai.getActionType();
		
		// 不使用 Ioc 容器管理模块
		if (Strings.isBlank(ai.getActionName())) {
			// 同一个类的入口方法,共用同一个实例
			synchronized (moduleMap) {
				moduleObj = moduleMap.get(moduleType);
				if (moduleObj == null) {
					if (log.isInfoEnabled()) {
						log.info("Create Module obj without Ioc --> " + moduleType);
					}
					moduleObj = Classes.born(moduleType);
					moduleMap.put(moduleType, moduleObj);
				}
			}
		}
		// 使用 Ioc 容器管理模块
		else {
			moduleName = ai.getActionName();
		}
	}

	public void process(ActionContext ac) throws Throwable {
		if (null != moduleObj) {
			ac.setAction(moduleObj);
		}
		else {
			Ioc ioc = ac.getIoc();
			if (null == ioc) {
				throw Exceptions.makeThrow(
					"Moudle with @IocBean('%s') but you not declare a Ioc for this app",
					moduleName);
			}

			Object obj;
			IocContext ictx = null;
			
			if (IocRequestListener.isRequestScopeEnable && IocSessionListener.isSessionScopeEnable) {
				ictx = new ComboContext(RequestIocContext.get(ac.getRequest()), SessionIocContext.get(ac.getRequest().getSession()));
			}
			else if (IocRequestListener.isRequestScopeEnable) {
				ictx = RequestIocContext.get(ac.getRequest());
			}
			else if (IocSessionListener.isSessionScopeEnable) {
				ictx = SessionIocContext.get(ac.getRequest().getSession());
			}
			
			obj = ioc.get(moduleType, moduleName, ictx);
			ac.setAction(obj);
		}
		
		ac.setMethod(method);
		doNext(ac);
	}

}
