package panda.ioc.aop.config.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import panda.aop.MethodInterceptor;
import panda.aop.MethodMatcher;
import panda.aop.matcher.SimpleMethodMatcher;
import panda.ioc.Ioc;
import panda.ioc.aop.Aop;
import panda.ioc.aop.config.AopConfigration;
import panda.ioc.aop.config.InterceptorPair;
import panda.lang.reflect.Methods;

/**
 * 通过扫描@Aop标注过的Method判断需要拦截哪些方法
 */
public class AnnotationAopConfigration implements AopConfigration {

	public List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz) {
		List<Method> aops = this.getAopMethod(clazz);
		List<InterceptorPair> ipList = new ArrayList<InterceptorPair>();
		if (aops.size() < 1) {
			return ipList;
		}
		
		for (Method m : aops) {
			MethodMatcher mm = new SimpleMethodMatcher(m);
			for (String nm : m.getAnnotation(Aop.class).value())
				ipList.add(new InterceptorPair(ioc.get(MethodInterceptor.class, nm), mm));
		}
		return ipList;
	}

	private List<Method> getAopMethod(Class<?> cls) {
		List<Method> aops = new LinkedList<Method>();
		for (Method m : Methods.getDeclaredMethods(cls)) {
			if (null != m.getAnnotation(Aop.class)) {
				int modify = m.getModifiers();
				if (Modifier.isAbstract(modify) 
						|| Modifier.isFinal(modify)
						|| Modifier.isPrivate(modify)
						|| Modifier.isStatic(modify)) {
					continue;
				}
				aops.add(m);
			}
		}
		return aops;
	}
}
