package panda.io;

import org.junit.Assert;
import org.junit.Test;

public class MimeTypesTest {
	/**
	 * test method: GetContentTypeForName
	 */
	@Test
	public void testGetContentTypeForName() {
		Assert.assertEquals(MimeTypes.IMG_GIF, MimeTypes.getMimeType("/a/s1.gif"));
		Assert.assertEquals(MimeTypes.IMG_PNG, MimeTypes.getMimeType("a.png"));
		Assert.assertEquals(MimeTypes.IMG_JPEG, MimeTypes.getMimeType("a.jpg"));
		Assert.assertEquals(MimeTypes.APP_JAVASCRIPT, MimeTypes.getMimeType("a.js"));
		Assert.assertEquals(MimeTypes.TEXT_CSS, MimeTypes.getMimeType("a.css"));
	}
}
