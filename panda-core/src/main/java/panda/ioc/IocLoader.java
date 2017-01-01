package panda.ioc;

import java.util.Set;

import panda.ioc.meta.IocObject;

public interface IocLoader {

	/**
	 * @return 配置信息里所有对象的名称
	 */
	Set<String> getNames();

	/**
	 * 每次这个函数被调用，则要构造一个新的 IocObject
	 * 
	 * @param name
	 * @return IocObject
	 * @throws IocLoadException
	 */
	IocObject load(String name) throws IocLoadException;

	/**
	 * @param name
	 * @return 配置信息里是否存在一个对象
	 */
	boolean has(String name);

}
