package panda.lang.reflect.testbed;

@SuppressWarnings({ "unused" })
// deliberate re-use of variable names
public class PrivatelyShadowedChild extends Parent {
	private final String s = "ss";
	private final boolean b = true;
	private final int i = 1;
	private final double d = 1.0;
}
