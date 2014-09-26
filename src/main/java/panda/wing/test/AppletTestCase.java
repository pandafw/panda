package panda.wing.test;

import panda.wing.ServletApplet;

/**
 * AppletTestCase
 */
public abstract class AppletTestCase {
	public synchronized static void init(Class<? extends ServletApplet> clazz) {
	}
	
	public synchronized static void init(Class<? extends ServletApplet> clazz, String contextRoot) {
	}
}
