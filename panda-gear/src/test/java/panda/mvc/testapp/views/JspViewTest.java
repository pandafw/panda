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
		System.out.println("2: " + resp.getContentText());
		assertEquals("2", resp.getContentText());
		get("/views/jsp3");
		System.out.println("3: " + resp.getContentText());
		assertEquals("3", resp.getContentText());
		get("/views/jsp4");
		System.out.println("4: " + resp.getContentText());
		assertEquals("4", resp.getContentText());
	}
}
