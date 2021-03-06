package panda.ioc.meta;

import panda.lang.Strings;

/**
 * Ioc Event 
 * <p>
 * <ul>
 * <li>create: invoke on object created
 * <li>fetch:  invoke on object get from Ioc Container
 * <li>depose: invoke on object is removed from Ioc Container
 * </ul>
 * 它们的值：
 * <ul>
 * <li>可以是一个函数名，也可以是一个 IocEventTrigger 的实现类全名
 * <li>如果 是函数，那么这个函数就是对象内的一个非静态 public 的函数，而且不能有参数
 * <li>如果是 IocEventTrigger 的实现类，你的实现类必须有一个 public 的默认构造函数
 * </ul>
 * 
 * @see panda.ioc.IocEventTrigger
 */
public class IocEventSet {

	private String create;

	private String fetch;

	private String depose;

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = Strings.stripToNull(create);
	}

	public String getFetch() {
		return fetch;
	}

	public void setFetch(String fetch) {
		this.fetch = Strings.stripToNull(fetch);
	}

	public String getDepose() {
		return depose;
	}

	public void setDepose(String depose) {
		this.depose = Strings.stripToNull(depose);
	}

	public boolean isNotEmpty() {
		return create != null || fetch != null || depose != null;
	}

	public boolean isEmpty() {
		return create == null && fetch == null && depose == null;
	}
}
