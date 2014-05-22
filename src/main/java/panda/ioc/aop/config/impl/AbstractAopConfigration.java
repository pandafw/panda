package panda.ioc.aop.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import panda.aop.MethodInterceptor;
import panda.aop.matcher.MethodMatcherFactory;
import panda.ioc.Ioc;
import panda.ioc.aop.config.AopConfigration;
import panda.ioc.aop.config.InterceptorPair;
import panda.lang.Classes;

public abstract class AbstractAopConfigration implements AopConfigration {

	private List<AopConfigrationItem> aopItemList;

	public List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz) {
		List<InterceptorPair> ipList = new ArrayList<InterceptorPair>();
		for (AopConfigrationItem aopItem : aopItemList) {
			if (aopItem.matchClassName(clazz.getName()))
				ipList.add(new InterceptorPair(getMethodInterceptor(ioc, aopItem.getInterceptor(),
					aopItem.isSingleton()), MethodMatcherFactory.matcher(aopItem.getMethodName())));
		}
		return ipList;
	}

	public void setAopItemList(List<AopConfigrationItem> aopItemList) {
		this.aopItemList = aopItemList;
	}

	protected MethodInterceptor getMethodInterceptor(Ioc ioc, String interceptorName, boolean singleton) {
		if (interceptorName.startsWith("ioc:")) {
			return ioc.get(MethodInterceptor.class, interceptorName.substring(4));
		}
		
		if (singleton == false) {
			return (MethodInterceptor)Classes.born(interceptorName);
		}

		MethodInterceptor methodInterceptor = cachedMethodInterceptor.get(interceptorName);
		if (methodInterceptor == null) {
			methodInterceptor = (MethodInterceptor)Classes.born(interceptorName);
			cachedMethodInterceptor.put(interceptorName, methodInterceptor);
		}
		return methodInterceptor;
	}

	private HashMap<String, MethodInterceptor> cachedMethodInterceptor = new HashMap<String, MethodInterceptor>();

}
