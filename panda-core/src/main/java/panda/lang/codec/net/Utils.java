package panda.lang.codec.net;

import panda.lang.codec.DecoderException;

/**
 * Utility methods for this package.
 * <p>
 * This class is immutable and thread-safe.
 * </p>
 * 
 */
class Utils {

	/**
	 * Returns the numeric value of the character <code>b</code> in radix 16.
	 * 
	 * @param b The byte to be converted.
	 * @return The numeric value represented by the character in radix 16.
	 * @throws DecoderException Thrown when the byte is not valid per
	 *             {@link Character#digit(char,int)}
	 */
	static int digit16(final byte b) throws DecoderException {
		final int i = Character.digit((char)b, URLCodec.RADIX);
		if (i == -1) {
			throw new DecoderException("Invalid URL encoding: not a valid digit (radix " + URLCodec.RADIX + "): " + b);
		}
		return i;
	}

}
