package panda.mvc.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.itextpdf.xmp.impl.Base64;

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

	@Test
	public void testVisitBinaryOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.bin", Base64.encode("10000"));
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitBinaryErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.bin", Base64.encode("1000"));
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.bin\":[\"4, 5 ~ 100\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitConstsOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.consts", "a");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitConstsErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.consts", "x");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.consts\":[\"x, ['a', 'b']\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitCardNoOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.cardno", "4485119590460568");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitCardNoErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.cardno", "x");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.cardno\":[\"'x' is not a card no.\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitDateOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.date", "2010-01-01 00:00:00");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitDateErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.date", "2000-01-01 00:00:00");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.date\":[\"'946652400000', 1262271600000 ~ 1292123532000\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitElOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.el", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitElErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.el", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.el\":[\"'err', 'top.value == \\\"ok\\\"'\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitEmailOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.email", "a@a.ca");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitEmailErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.email", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.email\":[\"'err' is not a email.\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitFilenameOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.filename", "aa.txt");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitFilenameErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.filename", "e/r/r");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.filename\":[\"'e\\/r\\/r' is not a filename.\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitNumberOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.number", "10");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitNumberErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.number", "10000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.number\":[\"10000, -100 ~ 100\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitProhibitedOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.prohibited", "x");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitProhibitedErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.prohibited", "a");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.prohibited\":[\"a, ['a', 'b']\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitRegexOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.regex", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitRegexErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.regex", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.regex\":[\"'err', 'ok'\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitStringOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.string", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitStringErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.string", "e-r-r");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.string\":[\"'e-r-r' is not A.\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitStrlenOk() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.strlen", "10000");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitStrlenErr() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.strlen", "1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.strlen\":[\"4, 5 ~ 100\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitShortCircuitTrue() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.shortCircuitTrue", "----");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.shortCircuitTrue\":[\"4, 5 ~ 100\"]}}}", response.getContentAsString());
	}

	@Test
	public void testVisitShortCircuitFalse() throws Throwable {
		request.setPathInfo("/visitOne");
		request.addParameter("one.shortCircuitFalse", "----");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"param\":{\"errors\":{\"one.shortCircuitFalse\":[\"4, 5 ~ 100\",\"'----' is not A.\"]}}}", response.getContentAsString());
	}
}
