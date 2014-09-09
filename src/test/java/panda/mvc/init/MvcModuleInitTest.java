package panda.mvc.init;

import static org.junit.Assert.*;

import org.junit.Test;

import panda.mvc.AbstractMvcTest;
import panda.mvc.init.conf.MainModuleA;

public class MvcModuleInitTest extends AbstractMvcTest {

	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", MainModuleA.class.getName());
	}

	@Test
	public void test_array_long_param() throws Exception {
		request.setPathInfo("/param/a");
		request.setParameter("ids", "1,2,3");
		servlet.service(request, response);
		String re = response.getContentAsString();
		assertEquals("[\"[1, 2, 3]\"]", re);
	}

	@Test
	public void test_array_long_param2() throws Exception {
		request.setPathInfo("/param/b");
		request.setParameter("nm", "xyz");
		request.setParameter("ids", "1,2,3");
		servlet.service(request, response);
		String re = response.getContentAsString();
		assertEquals("[1, 2, 3]", re);
	}

}
