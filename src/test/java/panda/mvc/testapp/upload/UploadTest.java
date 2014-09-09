package panda.mvc.testapp.upload;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;

public class UploadTest extends BaseWebappTest {

	@Test
	public void test_upload() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		File f = File.createTempFile("panda", "data");
		FileWriter fw = new FileWriter(f);
		fw.write("abc");
		fw.flush();
		fw.close();

		params.put("file", f);
		HttpResponse resp = HttpClient.post(getBaseURL() + "/upload/image", params);

		assertEquals("image&3", resp.getContent());
	}

}
