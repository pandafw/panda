package panda.lang;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import panda.io.ByteArrayOutputStream;



/**
 * Utility class for Compress.
 * @author yf.frank.wang@gmail.com
 */
public abstract class StringCompress {
	public static byte[] zipEncode(String input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		zos.write(Strings.getBytesUtf8(input));
		zos.close();
		return baos.toByteArray();
	}
}
