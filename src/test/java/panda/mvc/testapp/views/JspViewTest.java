package panda.mvc.testapp.views;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;

/**
 * For ViewTestModule
 */
public class JspViewTest extends BaseWebappTest {

	@Test
	public void test_simple() throws IOException {
		get("/views/jsp2");
		assertEquals("null", resp.getContentText());
		get("/views/jsp3");
		assertEquals("null", resp.getContentText());
		get("/views/jsp4");
		assertEquals("null", resp.getContentText());
	}
}
