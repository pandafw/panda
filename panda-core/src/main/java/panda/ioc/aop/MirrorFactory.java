package panda.ioc.aop;


public class MirrorFactory {

	/**
	 * Get a mirror class for AOP
	 * 
	 * @param type the class type
	 * @param name the mirror name
	 * @return the mirror class
	 */
	public <T> Class<T> getMirror(Class<T> type, String name) {
		return type;
	}

}
