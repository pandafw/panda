package panda.ex.gcloud.vision;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import panda.ex.gcloud.vision.images.EntityAnnotation;
import panda.io.Streams;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class ImagesAnnotateTest {
	private static final Log log = Logs.getLog(ImagesAnnotateTest.class);
	
	@Before
	public void setUp() {
		if (Strings.isEmpty(getApiKey())) {
			log.info("SKIP: " + this.getClass().getName());
			Assume.assumeTrue(false);
		}
	}

	private String getApiKey() {
		return System.getenv("GOOGLE_CLOUD_APIKEY");
	}

	private ImagesAnnotate getImagesAnnotate() {
		String ak = getApiKey();
		ImagesAnnotate ia = new ImagesAnnotate(ak);
		return ia;
	}
	
	@Test
	public void testImageUri() throws IOException {
		ImagesAnnotate ia = getImagesAnnotate();
		EntityAnnotation ea = ia.detectLabel("https://cloud.google.com/vision/docs/images/ferris-wheel.jpg");
		Assert.assertEquals(ea.getDescription(), "ferris wheel");
	}
	
	@Test
	public void testImageFile() throws IOException {
		ImagesAnnotate ia = getImagesAnnotate();
		byte[] bs = Streams.toByteArray(getClass().getResourceAsStream("cat.jpg"));
		EntityAnnotation ea = ia.detectLabel(bs);
		Assert.assertEquals(ea.getDescription(), "cat");
	}
}
