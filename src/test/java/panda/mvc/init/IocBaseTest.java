package panda.mvc.init;

import org.junit.Assert;
import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.init.module.MainModule;

public class IocBaseTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", MainModule.class.getName());
	}

	@Test
	public void testAppValue() throws Throwable {
		request.setRequestURI("/ioc/getAppValue");
		servlet.service(request, response);
		String resp = response.getContentAsString();
		Assert.assertEquals("null", resp);

		newreq();
		request.setRequestURI("/ioc/setAppValue");
		request.addParameter("v", "test");
		servlet.service(request, response);
		resp = response.getContentAsString();
		Assert.assertEquals("test", resp);

		newreq();
		request.setRequestURI("/ioc/getAppValue");
		servlet.service(request, response);
		resp = response.getContentAsString();
		Assert.assertEquals("null", resp);
	}
}
