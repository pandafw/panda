package panda.ioc;

/**
 * 注入值的代理。
 * <p>
 * 对象的注入值可能是对象的构造函数，或者字段属性。通过 ValueProxyMaker （用户可以自定义） 框架可以解释多种 Val。
 * 
 * @see panda.ioc.ValueMaker
 * @see panda.ioc.meta.IocValue
 */
public interface ValueProxy {
	public static final Object UNDEFINED = new Object();
	
	Object get(IocMaking im);

}
