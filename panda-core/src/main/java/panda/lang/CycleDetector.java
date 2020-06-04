package panda.lang;


import java.util.LinkedList;


/**
 */
public class CycleDetector {
	/** cycle detect value stack */
	protected LinkedList<Object> stack = new LinkedList<Object>();

	/** cycle detect name stack */
	protected LinkedList<String> names = new LinkedList<String>();

	public void push(String name, Object value) {
		names.add(name);
		stack.add(value);
	}

	public void popup() {
		names.pollLast();
		stack.pollLast();
	}

	public boolean isCycled(Object value) {
		return value != null && stack.contains(value);
	}
	
	public String toPath() {
		return '/' + Strings.join(names, '/');
	}
	
	public String toPath(String name) {
		return toPath() + '/' + name;
	}
}
