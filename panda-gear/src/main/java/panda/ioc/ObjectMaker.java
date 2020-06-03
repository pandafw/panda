package panda.ioc;

import panda.ioc.meta.IocObject;

/**
 * 这个接口封装了对象注入逻辑。生成对象代理。
 * 
 * @see panda.ioc.ObjectProxy
 */
public interface ObjectMaker {

	/**
	 * create a singleton ObjectProxy and save to scope IocContext
	 * 
	 * @param im the IocMaking
	 * @param iobj the IocObject
	 * @return the ObjectProxy
	 */
	ObjectProxy makeSingleton(IocMaking im, IocObject iobj);

	/**
	 * create a dynamic ObjectProxy and save to thread local IocContext
	 * 
	 * @param im the IocMaking
	 * @param iobj the IocObject
	 * @return the ObjectProxy
	 */
	ObjectProxy makeDynamic(IocMaking im, IocObject iobj);
}
