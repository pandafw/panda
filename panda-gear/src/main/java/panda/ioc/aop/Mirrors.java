package panda.ioc.aop;

import panda.ioc.Ioc;

public class Mirrors {

	/**
	 * Get a mirror class for AOP
	 * 
	 * @param ioc the ioc
	 * @param type the class type
	 * @param name the mirror name
	 * @return the mirror class
	 */
	public <T> Class<T> getMirror(Ioc ioc, Class<T> type, String name) {
		return type;
	}

}
