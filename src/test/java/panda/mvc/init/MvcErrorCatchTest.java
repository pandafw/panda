package panda.mvc.init;

import org.junit.Assert;
import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.init.errmodule.ErrorCatchMainModule;

public class MvcErrorCatchTest {

	private void _mvc(final Class<?> mainModuleType) {
			(new AbstractMvcTestCase() {
				protected void initServletConfig() {
					servletConfig.addInitParameter("modules", mainModuleType.getName());
				}
			}).init();
	}

	@Test
	public void testTwoNullAt() {
		try {
			_mvc((Class<?>)ErrorCatchMainModule.class);
			Assert.fail("Except IllegalArgumentException");
		}
		catch (Throwable e) {
			if (!(e instanceof IllegalArgumentException)) {
				e.printStackTrace();
				Assert.fail("Except IllegalArgumentException");
			}
		}
	}

}
