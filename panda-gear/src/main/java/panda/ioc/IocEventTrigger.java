package panda.ioc;

/**
 * 容器事件触发器
 * @param <T> the object class type
 */
public interface IocEventTrigger<T> {

	void trigger(T obj);

}
