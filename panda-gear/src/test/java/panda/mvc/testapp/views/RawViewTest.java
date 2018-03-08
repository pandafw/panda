package panda.mvc.testapp.views;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;
import panda.net.http.HttpHeader;

public class RawViewTest extends BaseWebappTest {

	@Test
	public void test_raw() throws IOException {
		get("/views/raw");
		assertEquals("ABC", resp.getContentText());

		get("/views/raw2");
		assertEquals(3, resp.getContentText().length());

		get("/views/raw3");
		assertEquals(3, resp.getContentText().length());

		get("/views/raw4");
		assertEquals("", resp.getContentText());

		get("/views/raw5");
		assertEquals("attachment; filename=\"5.json\"", resp.getHeader().getString(HttpHeader.CONTENT_DISPOSITION));
	}
}
