package panda.wing.test;

import panda.mvc.test.MvcTestCase;
import panda.wing.lucene.LuceneProvider;

public abstract class ActionTestCase extends MvcTestCase {
	public synchronized static void init(Class<? extends LuceneProvider> clazz) {
		AppletTestCase.init(clazz);
	}
	
	public synchronized static void init(Class<? extends LuceneProvider> clazz, String contextRoot) {
		AppletTestCase.init(clazz, contextRoot);
	}
}
