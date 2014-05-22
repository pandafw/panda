package panda.ioc.weaver;

import java.lang.reflect.Type;

import panda.castor.Castors;
import panda.ioc.IocEventTrigger;
import panda.ioc.IocMaking;
import panda.ioc.ObjectWeaver;
import panda.ioc.ValueProxy;
import panda.lang.Creator;
import panda.lang.reflect.Types;

/**
 * 默认的对象编织过程
 */
public class DefaultWeaver implements ObjectWeaver {

	/**
	 * 对象创建时的触发器
	 */
	private IocEventTrigger<Object> onCreate;

	/**
	 * 对象构造方法
	 */
	private Creator<?> creator;

	/**
	 * 对象构造方法参数
	 */
	private ValueProxy[] args;

	/**
	 * 对象构造方法参数
	 */
	private Type[] argTypes;

	/**
	 * 字段注入器列表
	 */
	private IocFieldInjector[] fields;

	public void setOnCreate(IocEventTrigger<Object> create) {
		this.onCreate = create;
	}

	public void setCreator(Creator<?> creator) {
		this.creator = creator;
	}

	public void setArgs(ValueProxy[] args) {
		this.args = args;
	}

	public void setArgTypes(Type[] argTypes) {
		this.argTypes = argTypes;
	}

	public void setFields(IocFieldInjector[] fields) {
		this.fields = fields;
	}

	public <T> T fill(IocMaking ing, T obj) {
		// 设置字段的值
		for (IocFieldInjector fi : fields) {
			fi.inject(ing, obj);
		}
		return obj;
	}

	public Object born(IocMaking ing) {
		// 准备构造函数参数
		Object[] args = new Object[this.args.length];
		for (int i = 0; i < args.length; i++) {
			Object o = this.args[i].get(ing);
			if (o != null && !Types.isAssignable(o.getClass(), argTypes[i], false)) {
				o = Castors.i().cast(o, argTypes[i]);
			}
			args[i] = o;
		}

		// 创建实例
		return creator.create(args);
	}

	public Object onCreate(Object obj) {
		if (null != onCreate && null != obj) {
			onCreate.trigger(obj);
		}
		return obj;
	}
}
