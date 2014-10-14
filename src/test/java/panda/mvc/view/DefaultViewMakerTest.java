package panda.mvc.view;

import static org.junit.Assert.*;

import org.junit.Test;

import panda.mvc.impl.DefaultViewMaker;

public class DefaultViewMakerTest {

	@Test
	public void testMake() {
		DefaultViewMaker maker = new DefaultViewMaker();
		assertNotNull(maker.make(null, "raw", null));
		assertNotNull(maker.make(null, "raw", "js"));
		assertNotNull(maker.make(null, "raw", "xml"));

		assertNotNull(maker.make(null, "jsp", "auth.login"));
		assertNotNull(maker.make(null, "jsp", "auth.login.jsp"));
		assertNotNull(maker.make(null, "json", "{}"));
		assertNotNull(maker.make(null, "json", "{compact:false,ignoreNull:false}"));
		assertNotNull(maker.make(null, "json", null));
		assertNotNull(maker.make(null, "redirect", "/auth/login"));
		assertNotNull(maker.make(null, ">>", "/auth/login"));
		assertNotNull(maker.make(null, "forward", "/auth/login"));
		assertNotNull(maker.make(null, "->", "/auth/login"));

		assertNotNull(maker.make(null, "http", "404"));
		assertNotNull(maker.make(null, "http", "503"));
	}

}
