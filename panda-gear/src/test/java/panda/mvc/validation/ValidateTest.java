package panda.mvc.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.codec.binary.Base64;
import panda.lang.time.DateTimes;
import panda.mvc.AbstractMvcTestCase;
import panda.mvc.validation.module.ValidateModule;
import panda.mvc.validation.module.ValidateObject;

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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one\":[\"int cast error\"]}}},\"params\":{\"one\":\"a1000\"}}", response.getContentAsString());
	}

	@Test
	public void testOneNumber() throws Throwable {
		request.setRequestURI("/one");
		request.addParameter("one", "1000");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one\":[\"min: -100, max: 100\"]}}},\"params\":{\"one\":\"1000\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitBinaryOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.bin", Base64.encodeBase64String("10000".getBytes()));
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitBinaryErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.bin", Base64.encodeBase64String("1000".getBytes()));
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.bin\":[\"4, 5 ~ 100\"]}}},\"params\":{\"one.bin\":\"MTAwMA==\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.consts\":[\"x, ['a', 'b']\"]}}},\"params\":{\"one.consts\":\"x\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.cardno\":[\"'x' is not a card no.\"]}}},\"params\":{\"one.cardno\":\"x\"}}", response.getContentAsString());
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
		
		long var = DateTimes.isoDatetimeNotFormat().parse("2000-01-01 00:00:00").getTime();
		long min = DateTimes.isoDatetimeNotFormat().parse(ValidateObject.DATE_MIN).getTime();
		long max = DateTimes.isoDatetimeNotFormat().parse(ValidateObject.DATE_MAX).getTime();
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.date\":[\"'" + var + "', " + min + " ~ " + max + "\"]}}},\"params\":{\"one.date\":\"2000-01-01 00:00:00\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.el\":[\"'err', 'top.value == \\\"ok\\\"'\"]}}},\"params\":{\"one.el\":\"err\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.el2\":[\"'ng', 'top.value == top.parent.value.el'\"]}}},\"params\":{\"one.el\":\"ok\",\"one.el2\":\"ng\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitEmailOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.email", "a@ab.com");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitEmailErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.email", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.email\":[\"'err' is not a email.\"]}}},\"params\":{\"one.email\":\"err\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitURLOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.url", "http://a.com/xxx");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}
	
	@Test
	public void testVisitURLErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.url", "http://err!xx");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.url\":[\"'http:\\/\\/err!xx' is not a url.\"]}}},\"params\":{\"one.url\":\"http:\\/\\/err!xx\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.filename\":[\"'e\\/r\\/r' is not a filename.\"]}}},\"params\":{\"one.filename\":\"e\\/r\\/r\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.number\":[\"10000, -100 ~ 100\"]}}},\"params\":{\"one.number\":\"10000\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.prohibited\":[\"a, ['a', 'b']\"]}}},\"params\":{\"one.prohibited\":\"a\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.regex\":[\"'err', 'ok'\"]}}},\"params\":{\"one.regex\":\"err\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoOk() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "090-1234-5678");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}
	
	@Test
	public void testVisitTelnoOk1() throws Throwable {
		request.setRequestURI("/visitOne");
		request.setParameter("one.telno", "09012345678");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoOk2() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "090-1234-5678 (1111)");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoOk3() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "(+81) 090-1234-5678 (1111)");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoOk4() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "81 090-1234-5678 (1111)");
		servlet.service(request, response);
		assertEquals("1", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoErr() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "err");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.telno\":[\"not a telephone number.\"]}}},\"params\":{\"one.telno\":\"err\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoErr2() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "111111 09012345678");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.telno\":[\"not a telephone number.\"]}}},\"params\":{\"one.telno\":\"111111 09012345678\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoErr3() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "+-87 09012345678");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.telno\":[\"not a telephone number.\"]}}},\"params\":{\"one.telno\":\"+-87 09012345678\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitTelnoErr4() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.telno", "0312345678 123");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.telno\":[\"not a telephone number.\"]}}},\"params\":{\"one.telno\":\"0312345678 123\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.string\":[\"'e-r-r' is not A.\"]}}},\"params\":{\"one.string\":\"e-r-r\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.strlen\":[\"4, 5 ~ 100\"]}}},\"params\":{\"one.strlen\":\"1000\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitShortCircuitTrue() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.shortCircuitTrue", "----");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.shortCircuitTrue\":[\"4, 5 ~ 100\"]}}},\"params\":{\"one.shortCircuitTrue\":\"----\"}}", response.getContentAsString());
	}

	@Test
	public void testVisitShortCircuitFalse() throws Throwable {
		request.setRequestURI("/visitOne");
		request.addParameter("one.shortCircuitFalse", "----");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"one.shortCircuitFalse\":[\"4, 5 ~ 100\",\"'----' is not A.\"]}}},\"params\":{\"one.shortCircuitFalse\":\"----\"}}", response.getContentAsString());
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
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"consts\":[\"required\"],\"el2\":[\"required\"]}}},\"params\":{}}", response.getContentAsString());
	}


	@Test
	public void testRequiredAny2Ok() throws Throwable {
		request.setRequestURI("/reqirAny2");
		request.addParameter("consts", "a");
		request.addParameter("el", "ok");
		servlet.service(request, response);
		assertEquals("a ok", response.getContentAsString());
	}

	@Test
	public void testRequiredAny2Err() throws Throwable {
		request.setRequestURI("/reqirAny2");
		servlet.service(request, response);
		assertEquals("{\"success\":false,\"alerts\":{\"params\":{\"errors\":{\"consts\":[\"required\"],\"el\":[\"required\"]}}},\"params\":{}}", response.getContentAsString());
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
