package panda.el;

import java.util.List;

/**
 * 方法执行接口.<br>
 * 供 MethodOpt 在执行方法的时候使用.<br>
 * 所有要进行方法执行的操作都需要实现这个接口,包括对象自身的方法,以及各自定义函数.<br>
 */
public interface RunMethod {

	/**
	 * 根据传入的参数执行方法
	 * 
	 * @param ec context
	 * @param ps 参数, 即EL表达式中, 函数括号内的内容.
	 * @return the method return value
	 */
	Object invoke(ELContext ec, List<?> ps);

	/**
	 * 取得方法自身的符号
	 * @return the operator string
	 */
	String operator();
}
