package panda.lang.codec.net;

import panda.lang.codec.DecoderException;

/**
 * Utility methods for this package.
 * <p>
 * This class is immutable and thread-safe.
 * </p>
 */
class Utils {

	/**
	 * Radix used in encoding and decoding.
	 */
	private static final int RADIX = 16;

	/**
	 * Returns the numeric value of the character <code>b</code> in radix 16.
	 *
	 * @param b The byte to be converted.
	 * @return The numeric value represented by the character in radix 16.
	 * @throws DecoderException Thrown when the byte is not valid per
	 *             {@link Character#digit(char,int)}
	 */
	static int safeDigit16(final byte b) throws DecoderException {
		return Character.digit((char)b, RADIX);
	}

	/**
	 * Returns the numeric value of the character <code>b</code> in radix 16.
	 *
	 * @param b The byte to be converted.
	 * @return The numeric value represented by the character in radix 16.
	 * @throws DecoderException Thrown when the byte is not valid per
	 *             {@link Character#digit(char,int)}
	 */
	static int digit16(final byte b) throws DecoderException {
		final int i = Character.digit((char)b, RADIX);
		if (i == -1) {
			throw new DecoderException("Invalid URL encoding: not a valid digit (radix " + RADIX + "): " + b);
		}
		return i;
	}

	/**
	 * Returns the upper case hex digit of the lower 4 bits of the int.
	 *
	 * @param b the input int
	 * @return the upper case hex digit of the lower 4 bits of the int.
	 */
	static char hexDigit(final int b) {
		return Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
	}

}
