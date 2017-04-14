package panda.ioc;

import java.util.Set;

import panda.ioc.meta.IocObject;

public interface IocLoader {

	/**
	 * @return the bean names
	 */
	Set<String> getNames();

	/**
	 * Get a IocObject of the bean (A new IocObject should be created every time)
	 * 
	 * @param name the bean name
	 * @return IocObject
	 * @throws IocLoadException if an error occurs
	 */
	IocObject load(String name) throws IocLoadException;

	/**
	 * @param name the bean name
	 * @return true if the loader has the specified bean description
	 */
	boolean has(String name);

}
