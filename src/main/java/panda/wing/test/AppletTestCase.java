package panda.wing.test;

import panda.wing.lucene.LuceneProvider;

/**
 * AppletTestCase
 */
public abstract class AppletTestCase {
	public synchronized static void init(Class<? extends LuceneProvider> clazz) {
	}
	
	public synchronized static void init(Class<? extends LuceneProvider> clazz, String contextRoot) {
	}
}
