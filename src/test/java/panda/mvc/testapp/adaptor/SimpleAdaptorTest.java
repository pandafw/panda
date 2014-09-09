package panda.mvc.testapp.adaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import panda.lang.time.DateTimes;
import panda.mvc.testapp.BaseWebappTest;

public class SimpleAdaptorTest extends BaseWebappTest {

	@Test
	public void test_issue_543() throws ParseException, NumberFormatException, IOException {
		get("/adaptor/github/issue/543?d=20120924");
		assertEquals(200, resp.getStatusCode());

		long ms = DateTimes.dateFormat().parse("2012-09-24").getTime();
		long rems = Long.parseLong(resp.getContentText());
		assertEquals(ms, rems);
	}

	@Test
	public void test_err_param() {
		get("/adaptor/err/param?id=ABC");
		assertEquals(200, resp.getStatusCode());

		get("/adaptor/err/param/ABC");
		assertEquals(200, resp.getStatusCode());
	}

	@Test
	public void test_json_map_type() {
		resp = post("/adaptor/json/type", "{'abc': 123456}");
		if (resp.getStatusCode() != 200) {
			fail();
		}
	}

	@Test
	public void test_inputstream_as_string() throws IOException {
		resp = post("/adaptor/ins", "I am abc");
		if (resp.getStatusCode() != 200) {
			fail();
		}
		assertEquals("I am abc", resp.getContentText());
	}

	@Test
	public void test_reader_as_string() throws IOException {
		resp = post("/adaptor/reader", "I am abc");
		if (resp.getStatusCode() != 200) {
			fail();
		}
		assertEquals("I am abc", resp.getContentText());
	}
}
