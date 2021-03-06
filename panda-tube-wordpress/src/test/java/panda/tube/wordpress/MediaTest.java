package panda.tube.wordpress;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.codec.binary.Base64;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;
import panda.net.http.HttpStatus;

public class MediaTest extends AbstractWordpressTest {

	@Test
	public void testGetMediaItem() throws Exception {
		MediaItem m = WP.getMediaItem(8);
		
		System.out.println(m);
		
		Assert.assertNotNull(m);
		Assert.assertEquals("ios_7_galaxy-1920x1200.jpg", m.title);
	}

	@Test
	public void testGetMediaLibrary() throws Exception {
		List<MediaItem> ms = WP.getMediaLibrary();
		
//		System.out.println(ms);
		
		Assert.assertNotNull(ms);
		Assert.assertTrue(ms.size() > 0);
	}

	@Test
	public void testUploadFile() throws Exception {
		MediaFile file = new MediaFile();
		file.name = "test.png";
		byte[] bin = Streams.toByteArray(getClass().getResourceAsStream("test.png"));
		file.bits = bin;
		file.type = MimeTypes.getMimeType("test.png");
		file.overwrite = false;

		MediaObject r = WP.uploadFile(file);
	
		Assert.assertNotNull(r);

		HttpResponse hres = HttpClient.get(r.url);
		
		Assert.assertEquals(HttpStatus.SC_OK, hres.getStatusCode());
		
		Assert.assertEquals(Base64.encodeBase64String(bin), Base64.encodeBase64String(hres.getContent()));
	}

	public static void main(String[] args) throws Exception {
		MediaFile file = MediaFile.fromFile(new File(args[0]));

		MediaObject r = WP.uploadFile(file);
	
		System.out.println(r);
	}
}
