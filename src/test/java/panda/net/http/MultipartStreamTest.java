package panda.net.http;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import panda.net.http.MultipartStream;

/**
 * Unit tests {@link panda.net.http.MultipartStream}.
 */
public class MultipartStreamTest {

	static private final String BOUNDARY_TEXT = "myboundary";

	@Test
	public void testThreeParamConstructor() throws Exception {
		final String strData = "foobar";
		final byte[] contents = strData.getBytes();
		InputStream input = new ByteArrayInputStream(contents);
		byte[] boundary = BOUNDARY_TEXT.getBytes();
		int iBufSize = boundary.length + MultipartStream.BOUNDARY_PREFIX.length + 1;
		MultipartStream ms = new MultipartStream(input, boundary, iBufSize, new MultipartStream.ProgressNotifier(null,
			contents.length));
		assertNotNull(ms);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSmallBuffer() throws Exception {
		final String strData = "foobar";
		final byte[] contents = strData.getBytes();
		InputStream input = new ByteArrayInputStream(contents);
		byte[] boundary = BOUNDARY_TEXT.getBytes();
		int iBufSize = 1;
		new MultipartStream(input, boundary, iBufSize, new MultipartStream.ProgressNotifier(null, contents.length));
	}

	@Test
	public void testTwoParamConstructor() throws Exception {
		final String strData = "foobar";
		final byte[] contents = strData.getBytes();
		InputStream input = new ByteArrayInputStream(contents);
		byte[] boundary = BOUNDARY_TEXT.getBytes();
		MultipartStream ms = new MultipartStream(input, boundary, new MultipartStream.ProgressNotifier(null,
			contents.length));
		assertNotNull(ms);
	}

}
