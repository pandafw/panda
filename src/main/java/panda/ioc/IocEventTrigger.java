package panda.ioc;

/**
 * 容器事件触发器
 * 
 */
public interface IocEventTrigger<T> {

	void trigger(T obj);

}
