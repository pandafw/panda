package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.lang.Chars;

/**
 * Unit tests {@link panda.lang.Chars}.
 */
public class CharsTest {

	private static final Character CHARACTER_A = new Character('A');
	private static final Character CHARACTER_B = new Character('B');
	private static final char CHAR_COPY = '\u00a9';

	@Test
	public void testToCharacterObject_String() {
		assertEquals(null, Chars.toCharacterObject(null));
		assertEquals(null, Chars.toCharacterObject(""));
		assertEquals(new Character('a'), Chars.toCharacterObject("a"));
		assertEquals(new Character('a'), Chars.toCharacterObject("abc"));
		assertSame(Chars.toCharacterObject("a"), Chars.toCharacterObject("a"));
	}

	@Test
	public void testToChar_Character() {
		assertEquals('A', Chars.toChar(CHARACTER_A));
		assertEquals('B', Chars.toChar(CHARACTER_B));
		try {
			Chars.toChar((Character)null);
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToChar_Character_char() {
		assertEquals('A', Chars.toChar(CHARACTER_A, 'X'));
		assertEquals('B', Chars.toChar(CHARACTER_B, 'X'));
		assertEquals('X', Chars.toChar((Character)null, 'X'));
	}

	@Test
	public void testToChar_String() {
		assertEquals('A', Chars.toChar("A"));
		assertEquals('B', Chars.toChar("BA"));
		try {
			Chars.toChar((String)null);
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			Chars.toChar("");
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToChar_String_char() {
		assertEquals('A', Chars.toChar("A", 'X'));
		assertEquals('B', Chars.toChar("BA", 'X'));
		assertEquals('X', Chars.toChar("", 'X'));
		assertEquals('X', Chars.toChar((String)null, 'X'));
	}

	@Test
	public void testToIntValue_char() {
		assertEquals(0, Chars.toIntValue('0'));
		assertEquals(1, Chars.toIntValue('1'));
		assertEquals(2, Chars.toIntValue('2'));
		assertEquals(3, Chars.toIntValue('3'));
		assertEquals(4, Chars.toIntValue('4'));
		assertEquals(5, Chars.toIntValue('5'));
		assertEquals(6, Chars.toIntValue('6'));
		assertEquals(7, Chars.toIntValue('7'));
		assertEquals(8, Chars.toIntValue('8'));
		assertEquals(9, Chars.toIntValue('9'));
		try {
			Chars.toIntValue('a');
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToIntValue_char_int() {
		assertEquals(0, Chars.toIntValue('0', -1));
		assertEquals(3, Chars.toIntValue('3', -1));
		assertEquals(-1, Chars.toIntValue('a', -1));
	}

	@Test
	public void testToIntValue_Character() {
		assertEquals(0, Chars.toIntValue(new Character('0')));
		assertEquals(3, Chars.toIntValue(new Character('3')));
		try {
			Chars.toIntValue(null);
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			Chars.toIntValue(CHARACTER_A);
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	@Test
	public void testToIntValue_Character_int() {
		assertEquals(0, Chars.toIntValue(new Character('0'), -1));
		assertEquals(3, Chars.toIntValue(new Character('3'), -1));
		assertEquals(-1, Chars.toIntValue(new Character('A'), -1));
		assertEquals(-1, Chars.toIntValue(null, -1));
	}

	@Test
	public void testToString_char() {
		assertEquals("a", Chars.toString('a'));
		assertSame(Chars.toString('a'), Chars.toString('a'));

		for (int i = 0; i < 128; i++) {
			final String str = Chars.toString((char)i);
			final String str2 = Chars.toString((char)i);
			assertSame(str, str2);
			assertEquals(1, str.length());
			assertEquals(i, str.charAt(0));
		}
		for (int i = 128; i < 196; i++) {
			final String str = Chars.toString((char)i);
			final String str2 = Chars.toString((char)i);
			assertEquals(str, str2);
			assertTrue(str != str2);
			assertEquals(1, str.length());
			assertEquals(i, str.charAt(0));
			assertEquals(1, str2.length());
			assertEquals(i, str2.charAt(0));
		}
	}

	@Test
	public void testToString_Character() {
		assertEquals(null, Chars.toString(null));
		assertEquals("A", Chars.toString(CHARACTER_A));
		assertSame(Chars.toString(CHARACTER_A), Chars.toString(CHARACTER_A));
	}

	@Test
	public void testToUnicodeEscaped_char() {
		assertEquals("\\u0041", Chars.unicodeEscaped('A'));

		for (int i = 0; i < 196; i++) {
			final String str = Chars.unicodeEscaped((char)i);
			assertEquals(6, str.length());
			final int val = Integer.parseInt(str.substring(2), 16);
			assertEquals(i, val);
		}
		assertEquals("\\u0999", Chars.unicodeEscaped((char)0x999));
		assertEquals("\\u1001", Chars.unicodeEscaped((char)0x1001));
	}

	@Test
	public void testToUnicodeEscaped_Character() {
		assertEquals(null, Chars.unicodeEscaped(null));
		assertEquals("\\u0041", Chars.unicodeEscaped(CHARACTER_A));
	}

	@Test
	public void testIsAscii_char() {
		assertTrue(Chars.isAscii('a'));
		assertTrue(Chars.isAscii('A'));
		assertTrue(Chars.isAscii('3'));
		assertTrue(Chars.isAscii('-'));
		assertTrue(Chars.isAscii('\n'));
		assertFalse(Chars.isAscii(CHAR_COPY));

		for (int i = 0; i < 128; i++) {
			if (i < 128) {
				assertTrue(Chars.isAscii((char)i));
			}
			else {
				assertFalse(Chars.isAscii((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiPrintable_char() {
		assertTrue(Chars.isAsciiPrintable('a'));
		assertTrue(Chars.isAsciiPrintable('A'));
		assertTrue(Chars.isAsciiPrintable('3'));
		assertTrue(Chars.isAsciiPrintable('-'));
		assertFalse(Chars.isAsciiPrintable('\n'));
		assertFalse(Chars.isAscii(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if (i >= 32 && i <= 126) {
				assertTrue(Chars.isAsciiPrintable((char)i));
			}
			else {
				assertFalse(Chars.isAsciiPrintable((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiControl_char() {
		assertFalse(Chars.isAsciiControl('a'));
		assertFalse(Chars.isAsciiControl('A'));
		assertFalse(Chars.isAsciiControl('3'));
		assertFalse(Chars.isAsciiControl('-'));
		assertTrue(Chars.isAsciiControl('\n'));
		assertFalse(Chars.isAsciiControl(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if (i < 32 || i == 127) {
				assertTrue(Chars.isAsciiControl((char)i));
			}
			else {
				assertFalse(Chars.isAsciiControl((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiAlpha_char() {
		assertTrue(Chars.isAsciiAlpha('a'));
		assertTrue(Chars.isAsciiAlpha('A'));
		assertFalse(Chars.isAsciiAlpha('3'));
		assertFalse(Chars.isAsciiAlpha('-'));
		assertFalse(Chars.isAsciiAlpha('\n'));
		assertFalse(Chars.isAsciiAlpha(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if ((i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z')) {
				assertTrue(Chars.isAsciiAlpha((char)i));
			}
			else {
				assertFalse(Chars.isAsciiAlpha((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiAlphaUpper_char() {
		assertFalse(Chars.isAsciiAlphaUpper('a'));
		assertTrue(Chars.isAsciiAlphaUpper('A'));
		assertFalse(Chars.isAsciiAlphaUpper('3'));
		assertFalse(Chars.isAsciiAlphaUpper('-'));
		assertFalse(Chars.isAsciiAlphaUpper('\n'));
		assertFalse(Chars.isAsciiAlphaUpper(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if (i >= 'A' && i <= 'Z') {
				assertTrue(Chars.isAsciiAlphaUpper((char)i));
			}
			else {
				assertFalse(Chars.isAsciiAlphaUpper((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiAlphaLower_char() {
		assertTrue(Chars.isAsciiAlphaLower('a'));
		assertFalse(Chars.isAsciiAlphaLower('A'));
		assertFalse(Chars.isAsciiAlphaLower('3'));
		assertFalse(Chars.isAsciiAlphaLower('-'));
		assertFalse(Chars.isAsciiAlphaLower('\n'));
		assertFalse(Chars.isAsciiAlphaLower(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if (i >= 'a' && i <= 'z') {
				assertTrue(Chars.isAsciiAlphaLower((char)i));
			}
			else {
				assertFalse(Chars.isAsciiAlphaLower((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiNumeric_char() {
		assertFalse(Chars.isAsciiNumeric('a'));
		assertFalse(Chars.isAsciiNumeric('A'));
		assertTrue(Chars.isAsciiNumeric('3'));
		assertFalse(Chars.isAsciiNumeric('-'));
		assertFalse(Chars.isAsciiNumeric('\n'));
		assertFalse(Chars.isAsciiNumeric(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if (i >= '0' && i <= '9') {
				assertTrue(Chars.isAsciiNumeric((char)i));
			}
			else {
				assertFalse(Chars.isAsciiNumeric((char)i));
			}
		}
	}

	@Test
	public void testIsAsciiAlphanumeric_char() {
		assertTrue(Chars.isAsciiAlphanumeric('a'));
		assertTrue(Chars.isAsciiAlphanumeric('A'));
		assertTrue(Chars.isAsciiAlphanumeric('3'));
		assertFalse(Chars.isAsciiAlphanumeric('-'));
		assertFalse(Chars.isAsciiAlphanumeric('\n'));
		assertFalse(Chars.isAsciiAlphanumeric(CHAR_COPY));

		for (int i = 0; i < 196; i++) {
			if ((i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z') || (i >= '0' && i <= '9')) {
				assertTrue(Chars.isAsciiAlphanumeric((char)i));
			}
			else {
				assertFalse(Chars.isAsciiAlphanumeric((char)i));
			}
		}
	}

}
