package panda.lang.reflect.testbed;

/**
 * @version $Id: Parent.java 1510301 2013-08-04 18:40:48Z mbenson $
 */
class Parent implements Foo {
	public String s = "s";
	protected boolean b = false;
	int i = 0;
	@SuppressWarnings("unused")
	private final double d = 0.0;

	@Override
	public void doIt() {
	}
}
