package panda.tube.wordpress;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.tube.wordpress.Option;

public class OptionTest extends AbstractWordpressTest {

	@Test
	public void testGetOptionsAll() throws Exception {
		Map<String, Option> result = WP.getOptions();

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 1);
		
		Assert.assertEquals("WordPress", result.get("software_name").value);
	}

	@Test
	public void testGetOptions1() throws Exception {
		Map<String, Option> result = WP.getOptions("software_name");

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		Assert.assertEquals("WordPress", result.get("software_name").value);
	}

	// TODO
//	@Test
//	public void testSetOptions1() throws Exception {
//		Map<String, Option> params = new HashMap<String, Option>();
//		Option o = new Option();
//		o.desc = "test_option";
//		o.readonly = false;
//		o.value = Randoms.randUUID();
//		params.put(o.desc, o);
//		
//		Map<String, Option> result = WP.setOptions(params);
//
////		System.out.print(result);
//		Assert.assertNotNull(result);
//		Assert.assertEquals(1, result.size());
//		
//		Assert.assertEquals(o, result.get("o.desc"));
//	}
}
