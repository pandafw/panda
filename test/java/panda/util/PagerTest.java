package panda.util;

import panda.util.Pager;
import junit.framework.TestCase;

/**
 */
public class PagerTest extends TestCase {

	/**
	 */
	public void test01() throws Exception {
		Pager p = new Pager();
		
		assertNotNull(p.getStart());
		assertNull(p.getLimit());
	}
}
