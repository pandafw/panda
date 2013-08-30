package panda.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.lang.Strings;

/**
 * Unit tests {@link Strings} - Substring methods
 *
 */
public class StringsIsTest  {

	// -----------------------------------------------------------------------

	@Test
	public void testIsAlpha() {
		assertFalse(Strings.isAlpha(null));
		assertFalse(Strings.isAlpha(""));
		assertFalse(Strings.isAlpha(" "));
		assertTrue(Strings.isAlpha("a"));
		assertTrue(Strings.isAlpha("A"));
		assertTrue(Strings.isAlpha("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
		assertFalse(Strings.isAlpha("ham kso"));
		assertFalse(Strings.isAlpha("1"));
		assertFalse(Strings.isAlpha("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
		assertFalse(Strings.isAlpha("_"));
		assertFalse(Strings.isAlpha("hkHKHik*khbkuh"));
	}

	@Test
	public void testIsAlphanumeric() {
		assertFalse(Strings.isAlphanumeric(null));
		assertFalse(Strings.isAlphanumeric(""));
		assertFalse(Strings.isAlphanumeric(" "));
		assertTrue(Strings.isAlphanumeric("a"));
		assertTrue(Strings.isAlphanumeric("A"));
		assertTrue(Strings.isAlphanumeric("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
		assertFalse(Strings.isAlphanumeric("ham kso"));
		assertTrue(Strings.isAlphanumeric("1"));
		assertTrue(Strings.isAlphanumeric("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
		assertFalse(Strings.isAlphanumeric("_"));
		assertFalse(Strings.isAlphanumeric("hkHKHik*khbkuh"));
	}

	@Test
	public void testIsWhitespace() {
		assertFalse(Strings.isWhitespace(null));
		assertTrue(Strings.isWhitespace(""));
		assertTrue(Strings.isWhitespace(" "));
		assertTrue(Strings.isWhitespace("\t \n \t"));
		assertFalse(Strings.isWhitespace("\t aa\n \t"));
		assertTrue(Strings.isWhitespace(" "));
		assertFalse(Strings.isWhitespace(" a "));
		assertFalse(Strings.isWhitespace("a  "));
		assertFalse(Strings.isWhitespace("  a"));
		assertFalse(Strings.isWhitespace("aba"));
		assertTrue(Strings.isWhitespace(StringsTest.WHITESPACE));
		assertFalse(Strings.isWhitespace(StringsTest.NON_WHITESPACE));
	}

	@Test
	public void testIsAlphaspace() {
		assertFalse(Strings.isAlphaSpace(null));
		assertTrue(Strings.isAlphaSpace(""));
		assertTrue(Strings.isAlphaSpace(" "));
		assertTrue(Strings.isAlphaSpace("a"));
		assertTrue(Strings.isAlphaSpace("A"));
		assertTrue(Strings.isAlphaSpace("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
		assertTrue(Strings.isAlphaSpace("ham kso"));
		assertFalse(Strings.isAlphaSpace("1"));
		assertFalse(Strings.isAlphaSpace("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
		assertFalse(Strings.isAlphaSpace("_"));
		assertFalse(Strings.isAlphaSpace("hkHKHik*khbkuh"));
	}

	@Test
	public void testIsAlphanumericSpace() {
		assertFalse(Strings.isAlphanumericSpace(null));
		assertTrue(Strings.isAlphanumericSpace(""));
		assertTrue(Strings.isAlphanumericSpace(" "));
		assertTrue(Strings.isAlphanumericSpace("a"));
		assertTrue(Strings.isAlphanumericSpace("A"));
		assertTrue(Strings.isAlphanumericSpace("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
		assertTrue(Strings.isAlphanumericSpace("ham kso"));
		assertTrue(Strings.isAlphanumericSpace("1"));
		assertTrue(Strings.isAlphanumericSpace("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
		assertFalse(Strings.isAlphanumericSpace("_"));
		assertFalse(Strings.isAlphanumericSpace("hkHKHik*khbkuh"));
	}

	@Test
	public void testIsAsciiPrintable_String() {
		assertFalse(Strings.isAsciiPrintable(null));
		assertTrue(Strings.isAsciiPrintable(""));
		assertTrue(Strings.isAsciiPrintable(" "));
		assertTrue(Strings.isAsciiPrintable("a"));
		assertTrue(Strings.isAsciiPrintable("A"));
		assertTrue(Strings.isAsciiPrintable("1"));
		assertTrue(Strings.isAsciiPrintable("Ceki"));
		assertTrue(Strings.isAsciiPrintable("!ab2c~"));
		assertTrue(Strings.isAsciiPrintable("1000"));
		assertTrue(Strings.isAsciiPrintable("10 00"));
		assertFalse(Strings.isAsciiPrintable("10\t00"));
		assertTrue(Strings.isAsciiPrintable("10.00"));
		assertTrue(Strings.isAsciiPrintable("10,00"));
		assertTrue(Strings.isAsciiPrintable("!ab-c~"));
		assertTrue(Strings.isAsciiPrintable("hkHK=Hik6i?UGH_KJgU7.tUJgKJ*GI87GI,kug"));
		assertTrue(Strings.isAsciiPrintable("\u0020"));
		assertTrue(Strings.isAsciiPrintable("\u0021"));
		assertTrue(Strings.isAsciiPrintable("\u007e"));
		assertFalse(Strings.isAsciiPrintable("\u007f"));
		assertTrue(Strings.isAsciiPrintable("G?lc?"));
		assertTrue(Strings.isAsciiPrintable("=?iso-8859-1?Q?G=FClc=FC?="));
		assertFalse(Strings.isAsciiPrintable("G\u00fclc\u00fc"));
	}

	@Test
	public void testIsNumeric() {
		assertFalse(Strings.isNumeric(null));
		assertFalse(Strings.isNumeric(""));
		assertFalse(Strings.isNumeric(" "));
		assertFalse(Strings.isNumeric("a"));
		assertFalse(Strings.isNumeric("A"));
		assertFalse(Strings.isNumeric("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
		assertFalse(Strings.isNumeric("ham kso"));
		assertTrue(Strings.isNumeric("1"));
		assertTrue(Strings.isNumeric("1000"));
		assertFalse(Strings.isNumeric("2.3"));
		assertFalse(Strings.isNumeric("10 00"));
		assertFalse(Strings.isNumeric("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
		assertFalse(Strings.isNumeric("_"));
		assertFalse(Strings.isNumeric("hkHKHik*khbkuh"));
		assertFalse(Strings.isNumeric("+123"));
		assertFalse(Strings.isNumeric("-123"));
	}

	@Test
	public void testIsNumericSpace() {
		assertFalse(Strings.isNumericSpace(null));
		assertTrue(Strings.isNumericSpace(""));
		assertTrue(Strings.isNumericSpace(" "));
		assertFalse(Strings.isNumericSpace("a"));
		assertFalse(Strings.isNumericSpace("A"));
		assertFalse(Strings.isNumericSpace("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
		assertFalse(Strings.isNumericSpace("ham kso"));
		assertTrue(Strings.isNumericSpace("1"));
		assertTrue(Strings.isNumericSpace("1000"));
		assertFalse(Strings.isNumericSpace("2.3"));
		assertTrue(Strings.isNumericSpace("10 00"));
		assertFalse(Strings.isNumericSpace("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
		assertFalse(Strings.isNumericSpace("_"));
		assertFalse(Strings.isNumericSpace("hkHKHik*khbkuh"));
	}

}
