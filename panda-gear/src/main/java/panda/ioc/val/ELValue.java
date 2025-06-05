package panda.ioc.val;

import panda.el.EL;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.bean.IocProxy;

/**
 * 支持用户通过自定的 EL Expression 来生成值
 * <p>
 * 通过静态 Java 函数
 * 
 * <pre>
 * ${'com.you.app.ClassName'@funcName()}
 * </pre>
 * 
 * 通过容器内对象提供的方法或属性
 * 
 * <pre>
 * ${objName.attrName}
 * 或者
 * ${objName.funcName()}
 * 或者可以支持更多的级别
 * ${objName.attrName.attrName}
 * ${objName.funcName().attrName.attrName}
 * </pre>
 * 
 * 无论那种方式，如果要为函数的设定参数，可以：
 * 
 * <pre>
 * ${objName.funcName(anotherName, 'abc', true, false)}
 * </pre>
 * 
 * 参数只支持
 * <ul>
 * <li>xxx 表示容器中的一个对象的名称
 * <li>'ddd' 字符串，只支持单/双引号
 * <li>true | false 布尔类型
 * <li>数字
 * <li>常量： $ioc 容器自身
 * <li>常量： $ctx 容器上下文对象
 * <li>常量不区分大小写
 * </ul>
 * 容器，会尽量为你转换参数类型，比如你
 * 
 * <pre>
 * ${'com.you.app.MyUtils'@getTime('2009-08-07 12:23:34')}
 * </pre>
 * 
 * 但是你的 getTime 函数的参数是一个 java.sql.Timestamp，那么容器会自动为你转型。<br>
 * 任何对象，只要有一个接受字符串作为参数的构造函数，都可以被成功的从字符串构建
 */
public class ELValue implements ValueProxy {
	private EL el;

	public ELValue(String expr) {
		el = new EL(expr);
	}

	@Override
	public Object get(IocMaking im) {
		return el.calculate(new IocProxy(im.getIoc()));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + el;
	}
}
