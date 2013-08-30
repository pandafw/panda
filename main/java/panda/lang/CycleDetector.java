package panda.lang;


import java.util.ArrayList;
import java.util.List;


/**
 * @author yf.frank.wang@gmail.com
 */
public class CycleDetector {
	/** cycle detect value stack */
	private List<Object> stack = new ArrayList<Object>();

	/** cycle detect name stack */
	private List<String> names = new ArrayList<String>();


	public void push(String name, Object value) {
		if (value == null) {
			throw new NullPointerException(toPath(name) + "is null");
		}
		names.add(name);
		stack.add(value);
	}
	public void popup() {
		names.remove(names.size() - 1);
		stack.remove(stack.size() - 1);
	}
	public boolean isCycled(Object value) {
		return stack.contains(value);
	}
	
	public String toPath() {
		return '/' + Strings.join(names, "/");
	}
	
	public String toPath(String name) {
		return toPath() + '/' + name;
	}
}
