package panda.mvc.init;

import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.init.errmodule.ErrorCatchMainModule;

public class MvcErrorCatchTest {

	private void _mvc(final Class<?> mainModuleType) throws Throwable {
		try {
			(new AbstractMvcTestCase() {
				protected void initServletConfig() {
					servletConfig.addInitParameter("modules", mainModuleType.getName());
				}
			}).init();
		}
		catch (Exception e) {
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTwoNullAt() throws Throwable {
		_mvc((Class<?>)ErrorCatchMainModule.class);
	}

}
