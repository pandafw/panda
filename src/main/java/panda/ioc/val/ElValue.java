package panda.ioc.val;

import panda.el.El;
import panda.ioc.IocMaking;
import panda.ioc.IocProxy;
import panda.ioc.ValueProxy;

/**
 * 支持用户通过自定的 EL Expression 来生成值
 * <p>
 * 通过静态 Java 函数
 * 
 * <pre>
 * {el : "'com.you.app.ClassName'@funcName()"}
 * </pre>
 * 
 * 通过容器内对象提供的方法或属性
 * 
 * <pre>
 * {el : "objName.attrName"}
 * 或者
 * {el : "objName.funcName()"}
 * 或者可以支持更多的级别
 * {el : "objName.attrName.attrName"}
 * {el : "objName.funcName().attrName.attrName"}
 * </pre>
 * 
 * 无论那种方式，如果要为函数的设定参数，可以：
 * 
 * <pre>
 * {el : "objName.funcName(anotherName, 'abc', true, false)"}
 * </pre>
 * 
 * 参数只支持
 * <ul>
 * <li>xxx 表示容器中的一个对象的名称，相当于 {ref: "xxx"}
 * <li>'ddd' 字符串，只支持单/双引号
 * <li>true | false 布尔类型
 * <li>数字
 * <li>常量： $ioc 容器自身
 * <li>常量： $ibn 对象名称
 * <li>常量： $ictx 容器上下文对象
 * <li>常量不区分大小写
 * </ul>
 * 容器，会尽量为你转换参数类型，比如你
 * 
 * <pre>
 * {el : "'com.you.app.MyUtils'@getTime('2009-08-07 12:23:34')"}
 * </pre>
 * 
 * 但是你的 getTime 函数的参数是一个 java.sql.Timestamp，那么容器会自动为你转型。<br>
 * 任何对象，只要有一个接受字符串作为参数的构造函数，都可以被成功的从字符串构建
 */
public class ElValue implements ValueProxy {
	private El el;

	public ElValue(String expr) {
		el = new El(expr);
	}

	public Object get(IocMaking ing) {
		return el.eval(new IocProxy(ing.getIoc()));
	}
}
