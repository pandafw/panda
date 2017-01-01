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
					aopItem.isSingleton()), MethodMatcherFactory.matcher(aopItem.getMethod())));
		}
		return ipList;
	}

	public void setAopItemList(List<AopConfigrationItem> aopItemList) {
		this.aopItemList = aopItemList;
	}

	protected MethodInterceptor getMethodInterceptor(Ioc ioc, String interceptor, boolean singleton) {
		if (interceptor.startsWith("#")) {
			return ioc.get(MethodInterceptor.class, interceptor.substring(1));
		}
		
		if (singleton == false) {
			return (MethodInterceptor)Classes.born(interceptor);
		}

		MethodInterceptor mi = cachedMethodInterceptor.get(interceptor);
		if (mi == null) {
			mi = (MethodInterceptor)Classes.born(interceptor);
			cachedMethodInterceptor.put(interceptor, mi);
		}
		return mi;
	}

	private HashMap<String, MethodInterceptor> cachedMethodInterceptor = new HashMap<String, MethodInterceptor>();

}
