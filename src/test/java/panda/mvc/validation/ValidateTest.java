package panda.mvc.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;

public class ValidateTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", ValidateModule.class.getName());
	}

	@Test
	public void testOneCastNot() throws Throwable {
		request.setPathInfo("/one");
		request.addParameter("one", "a1000");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testOneCastError() throws Throwable {
		request.setPathInfo("/oneCast");
		request.addParameter("one", "a1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one\":[\"int cast error\"]}}}", response.getContentAsString());
	}

	@Test
	public void testOneNumber() throws Throwable {
		request.setPathInfo("/one");
		request.addParameter("one", "1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one\":[\"min: -100, max: 100\"]}}}", response.getContentAsString());
	}

}
