package panda.ioc;

/**
 * 每次获取对象时会触发 fetch 事件，销毁时触发 depose 事件。
 * <p>
 * 这个对象需要小心被创建和使用。为了防止循环注入的问题，通常，ObjectMaker 需要快速<br>
 * 创建一个 ObjectProxy， 存入上下文。 然后慢慢的设置它的 weaver 和 fetch。
 * <p>
 * 在出现异常的时候，一定要将该对象从上下文中移除掉。
 */
public interface ObjectProxy {
	<T> T get(Class<T> type, IocMaking imak);

	void depose();
}
