package panda.ioc.aop;


public interface MirrorFactory {

	/**
	 * 根据一个类型生成 Mirror 对象
	 * 
	 * @param type the class type
	 * @param name the mirror name
	 * @return the mirror class
	 */
	<T> Class<T> getMirror(Class<T> type, String name);

}
