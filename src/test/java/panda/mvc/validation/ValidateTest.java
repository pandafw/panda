package panda.mvc.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.itextpdf.xmp.impl.Base64;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.validation.module.ValidateModule;

public class ValidateTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", ValidateModule.class.getName());
	}

	@Test
	public void testOneCastNot() throws Throwable {
		request.setRequestURI("/one");
		request.addParameter("one", "a1000");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testOneCastError() throws Throwable {
		request.setRequestURI("/oneCast");
		request.addParameter("one", "a1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one\":[\"int cast error\"]}}},\"params\":{\"one\":0}}", response.getContentAsString());
	}

	@Test
	public void testOneNumber() throws Throwable {
		request.setRequestURI("/one");
		request.addParameter("one", "1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one\":[\"min: -100, max: 100\"]}}},\"params\":{\"one\":1000}}", response.getContentAsString());
	}

	@Test
	public void testVisitBinaryOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.bin", Base64.encode("10000"));
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitBinaryErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.bin", Base64.encode("1000"));
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.bin\":[\"4, 5 ~ 100\"]}}},\"params\":{\"one\":{\"bin\":\"MTAwMA==\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitConstsOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.consts", "a");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitConstsErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.consts", "x");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.consts\":[\"x, ['a', 'b']\"]}}},\"params\":{\"one\":{\"consts\":\"x\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitCardNoOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.cardno", "4485119590460568");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitCardNoErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.cardno", "x");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.cardno\":[\"'x' is not a card no.\"]}}},\"params\":{\"one\":{\"cardno\":\"x\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitDateOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.date", "2010-01-01 00:00:00");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitDateErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.date", "2000-01-01 00:00:00");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.date\":[\"'946652400000', 1262271600000 ~ 1292123532000\"]}}},\"params\":{\"one\":{\"date\":946652400000,\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitElOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.el", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitElErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.el", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.el\":[\"'err', 'top.value == \\\"ok\\\"'\"]}}},\"params\":{\"one\":{\"el\":\"err\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitEl2Ok() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.el", "ok");
		request.addParameter("one.el2", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitEl2Err() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.el", "ok");
		request.addParameter("one.el2", "ng");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.el2\":[\"'ng', 'top.value == top.parent.value.el'\"]}}},\"params\":{\"one\":{\"el\":\"ok\",\"el2\":\"ng\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitEmailOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.email", "a@a.ca");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitEmailErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.email", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.email\":[\"'err' is not a email.\"]}}},\"params\":{\"one\":{\"email\":\"err\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitFilenameOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.filename", "aa.txt");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitFilenameErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.filename", "e/r/r");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.filename\":[\"'e\\/r\\/r' is not a filename.\"]}}},\"params\":{\"one\":{\"filename\":\"e\\/r\\/r\",\"number\":0}}}", response.getContentAsString());
	}

	@Test
	public void testVisitNumberOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.number", "10");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitNumberErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.number", "10000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.number\":[\"10000, -100 ~ 100\"]}}},\"params\":{\"one\":{\"number\":10000}}}", response.getContentAsString());
	}

	@Test
	public void testVisitProhibitedOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.prohibited", "x");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitProhibitedErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.prohibited", "a");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.prohibited\":[\"a, ['a', 'b']\"]}}},\"params\":{\"one\":{\"number\":0,\"prohibited\":\"a\"}}}", response.getContentAsString());
	}

	@Test
	public void testVisitRegexOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.regex", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitRegexErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.regex", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.regex\":[\"'err', 'ok'\"]}}},\"params\":{\"one\":{\"number\":0,\"regex\":\"err\"}}}", response.getContentAsString());
	}

	@Test
	public void testVisitStringOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.string", "ok");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitStringErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.string", "e-r-r");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.string\":[\"'e-r-r' is not A.\"]}}},\"params\":{\"one\":{\"number\":0,\"string\":\"e-r-r\"}}}", response.getContentAsString());
	}

	@Test
	public void testVisitStrlenOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.strlen", "10000");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitStrlenErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.strlen", "1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.strlen\":[\"4, 5 ~ 100\"]}}},\"params\":{\"one\":{\"number\":0,\"strlen\":\"1000\"}}}", response.getContentAsString());
	}

	@Test
	public void testVisitShortCircuitTrue() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.shortCircuitTrue", "----");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.shortCircuitTrue\":[\"4, 5 ~ 100\"]}}},\"params\":{\"one\":{\"number\":0,\"shortCircuitTrue\":\"----\"}}}", response.getContentAsString());
	}

	@Test
	public void testVisitShortCircuitFalse() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.shortCircuitFalse", "----");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.shortCircuitFalse\":[\"4, 5 ~ 100\",\"'----' is not A.\"]}}},\"params\":{\"one\":{\"number\":0,\"shortCircuitFalse\":\"----\"}}}", response.getContentAsString());
	}


	@Test
	public void testRequiredAnyOk() throws Throwable {
		request.setRequestURI("/reqirAny");
		request.addParameter("consts", "a");
		request.addParameter("el", "ok");
		servlet.service(request, response);
		assertEquals("a ok", response.getContentAsString());
	}

	@Test
	public void testRequiredAnyErr() throws Throwable {
		request.setRequestURI("/reqirAny");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"consts\":[\"required\"],\"el\":[\"required\"]}}},\"params\":{\"number\":0}}", response.getContentAsString());
	}

	@Test
	public void testRequiredOneOk() throws Throwable {
		request.setRequestURI("/reqirOne");
		request.addParameter("one.consts", "a");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testRequiredOneErr() throws Throwable {
		request.setRequestURI("/reqirOne");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one\":[\"required\"]}}},\"params\":{}}", response.getContentAsString());
	}
}
