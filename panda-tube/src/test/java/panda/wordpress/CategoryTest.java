package panda.wordpress;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.wordpress.bean.Category;

public class CategoryTest extends AbstractWordpressTest {

	@Test
	public void testGetCategories() throws Exception {
		List<Category> result = WP.getCategories();

		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(4, result.size());
		
		List<String> ecs = Arrays.toList( "Anime", "Misc", "Program", "Shopping" );
		List<String> acs = new ArrayList<String>();
		for (Category c : result) {
			acs.add(c.categoryName);
		}
		Collections.sort(ecs);
		Collections.sort(acs);
		Assert.assertEquals(ecs, acs);
	}

	@Test
	public void testSuggestCategories() throws Exception {
		List<Category> result = WP.suggestCategories("c");

		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("Misc", result.get(0).categoryName);
	}

	@Test
	public void testNewDeleteCategory() throws Exception {
		Category c = new Category();
		c.categoryName = "Test";
		
		int id = WP.newCategory(c);
		Assert.assertTrue(id > 0);

		boolean r = WP.deleteCategory(id);
		Assert.assertTrue(r);
	}
}
