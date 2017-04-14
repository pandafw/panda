package panda.ioc.aop.config;

import java.util.List;

import panda.ioc.Ioc;

public interface AopConfigration {

	/**
	 * The IocBean Name
	 */
	String IOCNAME = "$aop";

	/**
	 * Get intercept list for class methods
	 * 
	 * @param ioc the ioc
	 * @param clazz the class to intercept
	 * @return InterceptorPair List
	 */
	List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz);

}
