package panda.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import junit.framework.TestCase;

/**
 */
public class CollectionsTest extends TestCase {

	/**
	 */
	public void testRemoveNullCol() {
		List<String> tl = new ArrayList<String>();
		
		tl.add(null);
		tl.add("t1");
		tl.add(null);
		tl.add("t2");
		
		System.out.println(tl);
		
		Collections.removeNull(tl);

		System.out.println(tl);
	}

	/**
	 */
	public void testRemoveNullMap() {
		Map<String, String> m = new HashMap<String, String>();
		
		m.put("1", null);
		m.put("2", "t1");
		m.put("3", null);
		m.put("4", "t2");
		
		System.out.println(m);
		
		Collections.removeNull(m);

		System.out.println(m);
	}

}
