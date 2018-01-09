package panda.mvc.adaptor.multipart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.net.http.MultipartStream.MalformedStreamException;

/**
 * Unit test for items with varying sizes.
 */
public class StreamingTest extends FileUploadTestCase {

	/**
	 * Tests a file upload with varying file sizes.
	 * @throws IOException IOException
	 */
	@Test
	public void testFileUpload() throws IOException {
		byte[] request = newRequest();
		List<TestFileItem> fileItems = parseUpload(request);
		Iterator<TestFileItem> fileIter = fileItems.iterator();
		int add = 16;
		int num = 0;
		for (int i = 0; i < 16384; i += add) {
			if (++add == 32) {
				add = 16;
			}
			TestFileItem item = fileIter.next();
			Assert.assertEquals("field" + (num++), item.getFieldName());
			byte[] bytes = item.getBody();
			Assert.assertEquals(i, bytes.length);
			for (int j = 0; j < i; j++) {
				Assert.assertEquals((byte)j, bytes[j]);
			}
		}
		Assert.assertTrue(!fileIter.hasNext());
	}

	/**
	 * Tests, whether an invalid request throws a proper exception.
	 * @throws IOException IOException
	 */
	@Test
	public void testFileUploadException() throws IOException {
		byte[] request = newRequest();
		byte[] invalidRequest = new byte[request.length - 11];
		System.arraycopy(request, 0, invalidRequest, 0, request.length - 11);
		try {
			parseUpload(invalidRequest);
			Assert.fail("Expected EndOfStreamException");
		}
		catch (MalformedStreamException e) {
			// expected
		}
	}

	/**
	 * Tests, whether an IOException is properly delegated.
	 * @throws IOException IOException
	 */
	@Test
	public void testIOException() throws IOException {
		byte[] request = newRequest();
		InputStream stream = new FilterInputStream(new ByteArrayInputStream(request)) {
			private int num;

			@Override
			public int read() throws IOException {
				if (++num > 123) {
					throw new IOException("123");
				}
				return super.read();
			}

			@Override
			public int read(byte[] pB, int pOff, int pLen) throws IOException {
				for (int i = 0; i < pLen; i++) {
					int res = read();
					if (res == -1) {
						return i == 0 ? -1 : i;
					}
					pB[pOff + i] = (byte)res;
				}
				return pLen;
			}
		};
		
		try {
			parseUpload(stream, request.length);
			Assert.fail("Expected IOException");
		}
		catch (IOException e) {
			Assert.assertEquals("123", e.getMessage());
		}
	}

	/**
	 * Test for FILEUPLOAD-135
	 * @throws IOException IOException
	 */
	@Test
	public void testFILEUPLOAD135() throws IOException {
		byte[] request = newShortRequest();
		final ByteArrayInputStream bais = new ByteArrayInputStream(request);
		List<TestFileItem> fileItems = parseUpload(new InputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public int read(byte b[], int off, int len) throws IOException {
				return bais.read(b, off, Math.min(len, 3));
			}

		}, request.length);
		Iterator<TestFileItem> fileIter = fileItems.iterator();
		Assert.assertTrue(fileIter.hasNext());
		TestFileItem item = fileIter.next();
		Assert.assertEquals("field", item.getFieldName());
		byte[] bytes = item.getBody();
		Assert.assertEquals(3, bytes.length);
		Assert.assertEquals((byte)'1', bytes[0]);
		Assert.assertEquals((byte)'2', bytes[1]);
		Assert.assertEquals((byte)'3', bytes[2]);
		Assert.assertTrue(!fileIter.hasNext());
	}

	private String getHeader(String pField) {
		return "-----1234\r\n" + "Content-Disposition: form-data; name=\"" + pField + "\"\r\n" + "\r\n";

	}

	private String getFooter() {
		return "-----1234--\r\n";
	}

	private byte[] newShortRequest() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final OutputStreamWriter osw = new OutputStreamWriter(baos, "US-ASCII");
		osw.write(getHeader("field"));
		osw.write("123");
		osw.write("\r\n");
		osw.write(getFooter());
		osw.close();
		return baos.toByteArray();
	}

	private byte[] newRequest() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final OutputStreamWriter osw = new OutputStreamWriter(baos, "US-ASCII");
		int add = 16;
		int num = 0;
		for (int i = 0; i < 16384; i += add) {
			if (++add == 32) {
				add = 16;
			}
			osw.write(getHeader("field" + (num++)));
			osw.flush();
			for (int j = 0; j < i; j++) {
				baos.write((byte)j);
			}
			osw.write("\r\n");
		}
		osw.write(getFooter());
		osw.close();
		return baos.toByteArray();
	}

}
