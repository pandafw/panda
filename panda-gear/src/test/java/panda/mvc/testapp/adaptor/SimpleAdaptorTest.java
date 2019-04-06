package panda.mvc.testapp.adaptor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import panda.io.MimeTypes;
import panda.lang.time.DateTimes;
import panda.mvc.testapp.BaseWebappTest;

public class SimpleAdaptorTest extends BaseWebappTest {

	@Test
	public void test_date_format() throws ParseException, NumberFormatException, IOException {
		get("/adaptor/edate?d=20120924");
		assertEquals(200, resp.getStatusCode());

		long ms = DateTimes.isoDateFormat().parse("2012-09-24").getTime();
		long rems = Long.parseLong(resp.getContentText());
		assertEquals(ms, rems);
	}

	@Test
	public void test_json_map_type() {
		resp = post("/adaptor/json/type", "{'abc': 123456}", MimeTypes.TEXT_JSON);
		assertEquals(200, resp.getStatusCode());
	}
}
