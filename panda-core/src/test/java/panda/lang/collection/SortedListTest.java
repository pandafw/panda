package panda.lang.collection;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.comparator.ComparableComparator;

public class SortedListTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSorted() {
		List<String> exp = Arrays.toList("90", "80", "10", "20");
		List<String> act = new SortedList<String>(ComparableComparator.i());
		act.addAll(exp);
		Collections.sort(exp);
		Assert.assertEquals(exp, act);
	}

}
