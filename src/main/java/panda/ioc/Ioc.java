package panda.ioc;

import java.util.Set;

/**
 * Ioc 容器接口
 */
public interface Ioc {
	/**
	 * 获取容器的上下文对象
	 * 
	 * @return 当前容器的上下文对象
	 */
	IocContext getContext();

	/**
	 * @return 所有在容器中定义了的对象名称列表。
	 */
	Set<String> getNames();

	/**
	 * @param type 类型
	 * @return 是否存在某一特定对象
	 */
	boolean has(Class<?> type) throws IocException;

	/**
	 * @param name 对象名
	 * @return 是否存在某一特定对象
	 */
	boolean has(String name) throws IocException;

	/**
	 * @param type 类型
	 * @param name 对象名
	 * @return 是否存在某一特定对象
	 */
	boolean has(Class<?> type, String name) throws IocException;

	/**
	 * 从容器中获取一个对象。这个对象的名称会根据传入的类型按如下规则决定
	 * <ul>
	 * <li>如果定义了注解 '@IocInject'，采用其值为注入名
	 * <li>否则采用类型 className 作为注入名
	 * </ul>
	 * 
	 * @param <T>
	 * @param type 类型
	 * @return 对象本身
	 * @throws IocException
	 */
	<T> T get(Class<T> type) throws IocException;

	/**
	 * 从容器中获取一个对象。同时会触发对象的 fetch 事件。如果第一次构建对象 则会先触发对象 create 事件
	 * 
	 * @param <T>
	 * @param type 对象的类型，如果为 null，在对象的注入配置中，比如声明对象的类型 <br>
	 *            如果不为null对象注入配置的类型优先
	 * @param name 对象的名称
	 * @return 对象本身
	 */
	<T> T get(Class<T> type, String name) throws IocException;

	/**
	 * 从容器中获取一个对象。这个对象的名称会根据传入的类型按如下规则决定
	 * <ul>
	 * <li>如果定义了注解 '@IocInject'，采用其值为注入名
	 * <li>否则采用类型 className 作为注入名
	 * </ul>
	 * 
	 * @param <T>
	 * @param type 类型
	 * @return 对象本身
	 * @throws IocException
	 */
	<T> T getIfExists(Class<T> type) throws IocException;

	/**
	 * 从容器中获取一个对象。同时会触发对象的 fetch 事件。如果第一次构建对象 则会先触发对象 create 事件
	 * 
	 * @param <T>
	 * @param type 对象的类型，如果为 null，在对象的注入配置中，比如声明对象的类型 <br>
	 *            如果不为null对象注入配置的类型优先
	 * @param name 对象的名称
	 * @return 对象本身
	 */
	<T> T getIfExists(Class<T> type, String name) throws IocException;

	/**
	 * 将容器恢复成初始创建状态，所有的缓存都将被清空
	 */
	void reset();

	/**
	 * 将容器注销，触发对象的 depose 事件
	 */
	void depose();

}
