package panda.lang.codec.binary;

import java.nio.charset.Charset;

import panda.lang.Charsets;
import panda.lang.Strings;
import panda.lang.codec.BinaryDecoder;
import panda.lang.codec.BinaryEncoder;
import panda.lang.codec.DecoderException;
import panda.lang.codec.EncoderException;

/**
 * Converts hexadecimal Strings. This class is thread-safe.
 */
public class Hex implements BinaryEncoder, BinaryDecoder {
	/**
	 * Default charset name is {@link Charsets#CS_UTF_8}
	 */
	public static final Charset DEFAULT_CHARSET = Charsets.CS_UTF_8;

	/**
	 * Default charset name is {@link Charsets#UTF_8}
	 */
	public static final String DEFAULT_CHARSET_NAME = Charsets.UTF_8;

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	/**
	 * Converts an array of bytes representing hexadecimal values into an array of bytes of those
	 * same values. The returned array will be half the length of the passed array, as it takes two
	 * characters to represent any given byte. An exception is thrown if the passed char array has
	 * an odd number of elements.
	 * 
	 * @param data An array of bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied char array.
	 * @throws DecoderException Thrown if an odd number or illegal of bytes is supplied
	 */
	public static byte[] decodeHex(final String data) throws DecoderException {
		return decodeHex(data.toCharArray());
	}
	
	/**
	 * Converts an array of bytes representing hexadecimal values into an array of bytes of those
	 * same values. The returned array will be half the length of the passed array, as it takes two
	 * characters to represent any given byte. An exception is thrown if the passed char array has
	 * an odd number of elements.
	 * 
	 * @param data An array of bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied char array.
	 * @throws DecoderException Thrown if an odd number or illegal of bytes is supplied
	 */
	public static byte[] decodeHex(final char[] data) throws DecoderException {
		final int len = data.length;

		if ((len & 0x01) != 0) {
			throw new DecoderException("Odd number of characters.");
		}

		final byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte)(f & 0xFF);
		}

		return out;
	}

	/**
	 * Converts an array of bytes representing hexadecimal values into an array of bytes of
	 * those same values. The returned array will be half the length of the passed array, as it
	 * takes two characters to represent any given byte. An exception is thrown if the passed char
	 * array has an odd number of elements.
	 * 
	 * @param data An array of bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied char array.
	 * @throws DecoderException Thrown if an odd number or illegal of bytes is supplied
	 */
	public static String decodeHexString(final String data) throws DecoderException {
		return Strings.newStringUtf8(decodeHex(data));
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of
	 * each byte in order. The returned array will be double the length of the passed array, as it
	 * takes two characters to represent any given byte.
	 * 
	 * @param data a byte[] to convert to Hex characters
	 * @return A char[] containing hexadecimal characters
	 */
	public static char[] encodeHex(final byte[] data) {
		return encodeHex(data, true);
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of
	 * each byte in order. The returned array will be double the length of the passed array, as it
	 * takes two characters to represent any given byte.
	 * 
	 * @param data a byte[] to convert to Hex characters
	 * @param toLowerCase <code>true</code> converts to lowercase, <code>false</code> to uppercase
	 * @return A char[] containing hexadecimal characters
	 */
	public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of
	 * each byte in order. The returned array will be double the length of the passed array, as it
	 * takes two characters to represent any given byte.
	 * 
	 * @param data a byte[] to convert to Hex characters
	 * @param toDigits the output alphabet
	 * @return A char[] containing hexadecimal characters
	 */
	protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * Converts an array of bytes into a String representing the hexadecimal values of each byte in
	 * order. The returned String will be double the length of the passed array, as it takes two
	 * characters to represent any given byte.
	 * 
	 * @param data a byte[] to convert to Hex characters
	 * @param toLowerCase <code>true</code> converts to lowercase, <code>false</code> to uppercase
	 * @return A String containing hexadecimal characters
	 */
	public static String encodeHexString(final byte[] data, final boolean toLowerCase) {
		return new String(encodeHex(data, toLowerCase));
	}

	/**
	 * Converts an array of bytes into a String representing the hexadecimal values of each byte in
	 * order. The returned String will be double the length of the passed array, as it takes two
	 * characters to represent any given byte.
	 * 
	 * @param data a byte[] to convert to Hex characters
	 * @return A String containing hexadecimal characters
	 */
	public static String encodeHexString(final byte[] data) {
		return new String(encodeHex(data));
	}


	/**
	 * Converts a String into a String representing the hexadecimal values of each byte in
	 * order. The returned String will be double the length of the passed array, as it takes two
	 * characters to represent any given byte.
	 * 
	 * @param data a string to convert to Hex characters
	 * @return A String containing hexadecimal characters
	 */
	public static String encodeHexString(final String data) {
		return encodeHexString(Strings.getBytesUtf8(data));
	}

	/**
	 * Converts a String into a String representing the hexadecimal values of each byte in
	 * order. The returned String will be double the length of the passed array, as it takes two
	 * characters to represent any given byte.
	 * 
	 * @param data a string to convert to Hex characters
	 * @param toLowerCase {@code true} converts to lowercase, {@code false} to uppercase
	 * @return A String containing hexadecimal characters
	 */
	public static String encodeHexString(final String data, final boolean toLowerCase) {
		return encodeHexString(Strings.getBytesUtf8(data), toLowerCase);
	}
	
	/**
	 * Converts a String into a String representing the hexadecimal values of each char in
	 * order. The returned String will be double the length of the passed array, as it takes two
	 * characters to represent any given byte.
	 * 
	 * @param data a string to convert to Hex characters
	 * @param prefix prefix string to every code point
	 * @return A String containing hexadecimal characters
	 */
	public static String encodeHexChars(final String data, final String prefix) {
		return encodeHexChars(data, prefix, true);
	}
	
	/**
	 * Converts a String into a String representing the hexadecimal values of each byte in
	 * order. The returned String will be double the length of the passed array, as it takes two
	 * characters to represent any given byte.
	 * 
	 * @param data a string to convert to Hex characters
	 * @param prefix prefix string to every code point
	 * @param toLowerCase {@code true} converts to lowercase, {@code false} to uppercase
	 * @return A String containing hexadecimal characters
	 */
	public static String encodeHexChars(final String data, final String prefix, final boolean toLowerCase) {
		if (Strings.isEmpty(prefix)) {
			return encodeHexString(data, toLowerCase);
		}
		
		char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length(); i++) {
			sb.append(prefix);
			
			char c = data.charAt(i);
			sb.append((char)(toDigits[(0xF000 & c) >>> 12]));
			sb.append((char)(toDigits[(0xF00 & c) >>> 8]));
			sb.append((char)(toDigits[(0xF0 & c) >>> 4]));
			sb.append((char)(toDigits[0x0F & c]));
		}
		return sb.toString();
	}

	/**
	 * Converts a hexadecimal character to an integer.
	 * 
	 * @param ch A character to convert to an integer digit
	 * @param index The index of the character in the source
	 * @return An integer
	 * @throws DecoderException Thrown if ch is an illegal hex character
	 */
	protected static int toDigit(final char ch, final int index) throws DecoderException {
		final int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	private final Charset charset;

	/**
	 * Creates a new codec with the default charset name {@link #DEFAULT_CHARSET}
	 */
	public Hex() {
		// use default encoding
		this.charset = DEFAULT_CHARSET;
	}

	/**
	 * Creates a new codec with the given Charset.
	 * 
	 * @param charset the charset.
	 */
	public Hex(final Charset charset) {
		this.charset = charset;
	}

	/**
	 * Creates a new codec with the given charset name.
	 * 
	 * @param charsetName the charset name.
	 * @throws java.nio.charset.UnsupportedCharsetException If the named charset is unavailable
	 */
	public Hex(final String charsetName) {
		this(Charset.forName(charsetName));
	}

	/**
	 * Converts an array of character bytes representing hexadecimal values into an array of bytes
	 * of those same values. The returned array will be half the length of the passed array, as it
	 * takes two characters to represent any given byte. An exception is thrown if the passed char
	 * array has an odd number of elements.
	 * 
	 * @param array An array of character bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied byte array
	 *         (representing characters).
	 * @throws DecoderException Thrown if an odd number of characters is supplied to this function
	 * @see #decodeHex(char[])
	 */
	@Override
	public byte[] decode(final byte[] array) throws DecoderException {
		return decodeHex(new String(array, getCharset()).toCharArray());
	}

	/**
	 * Converts a String or an array of character bytes representing hexadecimal values into an
	 * array of bytes of those same values. The returned array will be half the length of the passed
	 * String or array, as it takes two characters to represent any given byte. An exception is
	 * thrown if the passed char array has an odd number of elements.
	 * 
	 * @param object A String or, an array of character bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied byte array
	 *         (representing characters).
	 * @throws DecoderException Thrown if an odd number of characters is supplied to this function
	 *             or the object is not a String or char[]
	 * @see #decodeHex(char[])
	 */
	@Override
	public Object decode(final Object object) throws DecoderException {
		try {
			final char[] charArray = object instanceof String ? ((String)object).toCharArray() : (char[])object;
			return decodeHex(charArray);
		}
		catch (final ClassCastException e) {
			throw new DecoderException(e.getMessage(), e);
		}
	}

	/**
	 * Converts an array of bytes into an array of bytes for the characters representing the
	 * hexadecimal values of each byte in order. The returned array will be double the length of the
	 * passed array, as it takes two characters to represent any given byte.
	 * <p>
	 * The conversion from hexadecimal characters to the returned bytes is performed with the
	 * charset named by {@link #getCharset()}.
	 * </p>
	 * 
	 * @param array a byte[] to convert to Hex characters
	 * @return A byte[] containing the bytes of the hexadecimal characters
	 * @see #encodeHex(byte[])
	 */
	@Override
	public byte[] encode(final byte[] array) {
		return encodeHexString(array).getBytes(this.getCharset());
	}

	/**
	 * Converts a String or an array of bytes into an array of characters representing the
	 * hexadecimal values of each byte in order. The returned array will be double the length of the
	 * passed String or array, as it takes two characters to represent any given byte.
	 * <p>
	 * The conversion from hexadecimal characters to bytes to be encoded to performed with the
	 * charset named by {@link #getCharset()}.
	 * </p>
	 * 
	 * @param object a String, or byte[] to convert to Hex characters
	 * @return A char[] containing hexadecimal characters
	 * @throws EncoderException Thrown if the given object is not a String or byte[]
	 * @see #encodeHex(byte[])
	 */
	@Override
	public Object encode(final Object object) throws EncoderException {
		try {
			final byte[] byteArray = object instanceof String ? ((String)object).getBytes(this.getCharset()) : (byte[])object;
			return encodeHex(byteArray);
		}
		catch (final ClassCastException e) {
			throw new EncoderException(e.getMessage(), e);
		}
	}

	/**
	 * Gets the charset.
	 * 
	 * @return the charset.
	 */
	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * Gets the charset name.
	 * 
	 * @return the charset name.
	 */
	public String getCharsetName() {
		return this.charset.name();
	}

	/**
	 * Returns a string representation of the object, which includes the charset name.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return super.toString() + "[charsetName=" + this.charset + "]";
	}
}
