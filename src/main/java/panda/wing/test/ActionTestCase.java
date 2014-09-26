package panda.wing.test;

import panda.mvc.test.MvcTestCase;
import panda.wing.ServletApplet;

public abstract class ActionTestCase extends MvcTestCase {
	public synchronized static void init(Class<? extends ServletApplet> clazz) {
		AppletTestCase.init(clazz);
	}
	
	public synchronized static void init(Class<? extends ServletApplet> clazz, String contextRoot) {
		AppletTestCase.init(clazz, contextRoot);
	}
}
