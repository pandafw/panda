package panda.ioc.aop;


public interface MirrorFactory {

	/**
	 * 根据一个类型生成 Mirror 对象
	 */
	<T> Class<T> getMirror(Class<T> type, String name);

}
