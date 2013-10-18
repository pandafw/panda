package panda.lang;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import panda.io.stream.ByteArrayOutputStream;
import panda.lang.codec.DecoderException;
import panda.lang.codec.binary.Base64;
import panda.lang.codec.binary.Hex;


/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class Encodes {
	//---------------------------------------------------------------------
	public static byte[] zipEncode(String input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		zos.write(Strings.getBytesUtf8(input));
		zos.close();
		return baos.toByteArray();
	}

	//---------------------------------------------------------------------
	public static byte[] hexDecode(final byte[] data) throws DecoderException {
		return Hex.decodeHex(data);
	}

	public static byte[] hexDecode(final String data) throws DecoderException {
		return Hex.decodeHex(data);
	}

	public static String hexDecodeHexString(final String data) throws DecoderException {
		return Hex.decodeHexString(data);
	}

	public static byte[] hexEncode(final byte[] data) {
		return Hex.encodeHex(data);
	}

	public static byte[] hexEncode(final byte[] data, final boolean toLowerCase) {
		return Hex.encodeHex(data, toLowerCase);
	}

	public static byte[] hexEncode(final String data) {
		return Hex.encodeHex(data);
	}

	public static byte[] hexEncode(final String data, final boolean toLowerCase) {
		return Hex.encodeHex(data, toLowerCase);
	}

	public static String hexEncodeString(final byte[] data) {
		return Hex.encodeHexString(data);
	}

	public static String hexEncodeString(final byte[] data, final boolean toLowerCase) {
		return Hex.encodeHexString(data, toLowerCase);
	}

	public static String hexEncodeString(final String data) {
		return Hex.encodeHexString(data);
	}

	public static String hexEncodeString(final String data, final boolean toLowerCase) {
		return Hex.encodeHexString(data, toLowerCase);
	}

	//---------------------------------------------------------------------
	public static boolean isBase64(final byte octet) {
		return Base64.isBase64(octet);
	}

	public static boolean isBase64(final String base64) {
		return Base64.isBase64(base64);
	}

	public static boolean isBase64(final byte[] arrayOctet) {
		return Base64.isBase64(arrayOctet);
	}

	public static byte[] b64Encode(final byte[] binaryData) {
		return Base64.encodeBase64(binaryData);
	}

	public static String b64EncodeString(final byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}

	public static byte[] b64EncodeURLSafe(final byte[] binaryData) {
		return Base64.encodeBase64URLSafe(binaryData);
	}

	public static String b64EncodeURLSafeString(final byte[] binaryData) {
		return Base64.encodeBase64URLSafeString(binaryData);
	}

	public static byte[] b64EncodeChunked(final byte[] binaryData) {
		return Base64.encodeBase64Chunked(binaryData);
	}

	public static byte[] b64Encode(final byte[] binaryData, final boolean isChunked) {
		return Base64.encodeBase64(binaryData, isChunked);
	}

	public static byte[] b64Encode(final byte[] binaryData, final boolean isChunked, final boolean urlSafe) {
		return Base64.encodeBase64(binaryData, isChunked, urlSafe);
	}

	public static byte[] b64Encode(final byte[] binaryData, final boolean isChunked, final boolean urlSafe,
			final int maxResultSize) {
		return Base64.encodeBase64(binaryData, isChunked, urlSafe, maxResultSize);
	}

	public static byte[] b64Decode(final String base64String) {
		return Base64.decodeBase64(base64String);
	}

	public static byte[] b64Decode(final byte[] base64Data) {
		return Base64.decodeBase64(base64Data);
	}
}
