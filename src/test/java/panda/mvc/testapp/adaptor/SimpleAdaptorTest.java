package panda.mvc.testapp.adaptor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import panda.lang.time.DateTimes;
import panda.mvc.testapp.BaseWebappTest;
import panda.net.http.HttpContentType;

public class SimpleAdaptorTest extends BaseWebappTest {

	@Test
	public void test_date_format() throws ParseException, NumberFormatException, IOException {
		get("/adaptor/edate?d=20120924");
		assertEquals(200, resp.getStatusCode());

		long ms = DateTimes.dateFormat().parse("2012-09-24").getTime();
		long rems = Long.parseLong(resp.getContentText());
		assertEquals(ms, rems);
	}

	@Test
	public void test_json_map_type() {
		resp = post("/adaptor/json/type", "{'abc': 123456}", HttpContentType.TEXT_JSON);
		assertEquals(200, resp.getStatusCode());
	}

	@Test
	public void test_inputstream_as_string() throws IOException {
		resp = post("/adaptor/ins", "I am abc");

		assertEquals(200, resp.getStatusCode());
		assertEquals("I am abc", resp.getContentText());
	}

	@Test
	public void test_reader_as_string() throws IOException {
		resp = post("/adaptor/reader", "I am abc");

		assertEquals(200, resp.getStatusCode());
		assertEquals("I am abc", resp.getContentText());
	}
}
