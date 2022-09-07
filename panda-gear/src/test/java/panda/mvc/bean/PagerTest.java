package panda.mvc.bean;

import org.junit.Assert;
import org.junit.Test;

public class PagerTest {

	@Test
	public void test_calcPage() {
		Pager p = new Pager();

		p.setLimit(10);
		
		p.setStart(0);
		Assert.assertEquals(1L, p.calcPage());

		p.setStart(9);
		Assert.assertEquals(1L, p.calcPage());

		p.setStart(10);
		Assert.assertEquals(2L, p.calcPage());
	}
}
