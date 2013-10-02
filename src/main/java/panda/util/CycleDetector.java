package panda.util;

import java.util.Stack;

/**
 * CycleDetector
 * @author yf.frank.wang@gmail.com
 */
public class CycleDetector {
	private static ThreadLocal<Stack<Object>> context = new ThreadLocal<Stack<Object>>();
	
	private static Stack<Object> getStack() {
		Stack<Object> stack = context.get();
		if (stack == null) {
			stack = new Stack<Object>();
			context.set(stack);
		}
		return stack;
	}
	
	/**
	 * push a object to the stack
	 * @param object object
	 */
	public static void push(Object object) {
		getStack().push(object);
	}
	
	/**
	 * pop the top object out of the stack
	 * @return popup object
	 */
	public static Object pop() {
		return getStack().pop();
	}

	/**
	 * @param object object
	 * @return true if the object is in the stack
	 */
	public static boolean contains(Object object) {
		return getStack().contains(object);
	}
}
