package panda.lang;

public interface Injector {

	/**
	 * 通过反射，向对象某一字段设置一个值
	 * 
	 * @param obj 被设值的对象
	 * @param value 值
	 */
	void inject(Object obj, Object value);
}
