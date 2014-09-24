package panda.ioc.meta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.lang.Objects;

/**
 * 描述了对象的注入信息
 * 
 * @see panda.ioc.meta.IocEventSet
 * @see panda.ioc.meta.IocValue
 */
public class IocObject {

	/**
	 * 对象类型，如果为 null，则使用 Ioc 接口函数的第一个参数作为本次获取的类型。
	 */
	private Class<?> type;

	/**
	 * 声明对象是否为单例。如果为单例，则在整个上下文环境下，只会有一份实例<br>
	 * 内部对象的 singleton 将会被忽略
	 */
	private boolean singleton;

	/**
	 * 对象监听何种事件
	 */
	private IocEventSet events;

	/**
	 * 对象构造函数的参数列表
	 */
	private List<IocValue> args;

	/**
	 * 对象的字段
	 */
	private Map<String, IocValue> fields;

	/**
	 * 对象基本，容器根据这个字段，来决定将这个对象保存在哪一个上下文范围中<br>
	 * 默认的为 "app"
	 */
	private String scope;

	private String factory;

	public IocObject() {
		args = new ArrayList<IocValue>();
		fields = new LinkedHashMap<String, IocValue>();
		singleton = true;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public IocEventSet getEvents() {
		return events;
	}

	public void setEvents(IocEventSet events) {
		this.events = events;
	}

	public IocValue[] getArgs() {
		return args.toArray(new IocValue[args.size()]);
	}

	public boolean hasArgs() {
		return args.size() > 0;
	}

	public void addArg(IocValue arg) {
		this.args.add(arg);
	}

	public void copyArgs(IocValue[] args) {
		this.args.clear();
		for (IocValue arg : args) {
			addArg(arg);
		}
	}

	public Map<String, IocValue> getFields() {
		return fields;
	}

	public void addField(String name, IocValue value) {
		this.fields.put(name, value);
	}

	public boolean hasField(String name) {
		return fields.containsKey(name);
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getFactory() {
		return factory;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("type", type)
				.append("singletion", singleton)
				.append("scope", scope)
				.append("fields", fields)
				.append("events", events)
				.append("factory", factory)
				.toString();
	}
}
