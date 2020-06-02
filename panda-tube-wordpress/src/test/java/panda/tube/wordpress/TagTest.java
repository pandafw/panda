package panda.tube.wordpress;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Collections;

public class TagTest extends AbstractWordpressTest {

	@Test
	public void testGetTags() throws Exception {
		List<Tag> result = WP.getTags();

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		List<String> ets = Arrays.toList( "top" );
		List<String> ats = new ArrayList<String>();
		for (Tag t : result) {
			ats.add(t.name);
		}
		
		Collections.sort(ets);
		Collections.sort(ats);
		
		Assert.assertEquals(ets, ats);
	}
}
