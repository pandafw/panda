package panda.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class CollectionsTest {
	@Test
	public void testRemoveNullCol() {
		List<String> act = new ArrayList<String>();
		
		act.add(null);
		act.add("t1");
		act.add(null);
		act.add("t2");
		
		Collections.removeNull(act);

		List<String> exp = Arrays.toList("t1", "t2");
		
		Assert.assertEquals(exp, act);
	}

	@Test
	public void testRemoveNullMap() {
		Map<String, String> act = new HashMap<String, String>();
		
		act.put("1", null);
		act.put("2", "t1");
		act.put("3", null);
		act.put("4", "t2");
		
		Collections.removeNull(act);

		Map<String, String> exp = Arrays.toMap("2", "t1", "4", "t2");
		
		Assert.assertEquals(exp, act);
	}

}
