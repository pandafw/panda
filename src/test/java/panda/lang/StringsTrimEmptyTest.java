package panda.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.lang.Strings;

/**
 * Unit tests {@link Strings} - Trim/Empty methods
 *
 */
public class StringsTrimEmptyTest  {
    private static final String FOO = "foo";

    //-----------------------------------------------------------------------
    @Test
    public void testIsEmpty() {
        assertTrue(Strings.isEmpty(null));
        assertTrue(Strings.isEmpty(""));
        assertFalse(Strings.isEmpty(" "));
        assertFalse(Strings.isEmpty("foo"));
        assertFalse(Strings.isEmpty("  foo  "));
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse(Strings.isNotEmpty(null));
        assertFalse(Strings.isNotEmpty(""));
        assertTrue(Strings.isNotEmpty(" "));
        assertTrue(Strings.isNotEmpty("foo"));
        assertTrue(Strings.isNotEmpty("  foo  "));
    }

    @Test
    public void testIsBlank() {
        assertTrue(Strings.isBlank(null));
        assertTrue(Strings.isBlank(""));
        assertTrue(Strings.isBlank(StringsTest.WHITESPACE));
        assertFalse(Strings.isBlank("foo"));
        assertFalse(Strings.isBlank("  foo  "));
    }

    @Test
    public void testIsNotBlank() {
        assertFalse(Strings.isNotBlank(null));
        assertFalse(Strings.isNotBlank(""));
        assertFalse(Strings.isNotBlank(StringsTest.WHITESPACE));
        assertTrue(Strings.isNotBlank("foo"));
        assertTrue(Strings.isNotBlank("  foo  "));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testTrim() {
        assertEquals(FOO, Strings.trim(FOO + "  "));
        assertEquals(FOO, Strings.trim(" " + FOO + "  "));
        assertEquals(FOO, Strings.trim(" " + FOO));
        assertEquals(FOO, Strings.trim(FOO + ""));
        assertEquals("", Strings.trim(" \t\r\n\b "));
        assertEquals("", Strings.trim(StringsTest.TRIMMABLE));
        assertEquals(StringsTest.NON_TRIMMABLE, Strings.trim(StringsTest.NON_TRIMMABLE));
        assertEquals("", Strings.trim(""));
        assertEquals(null, Strings.trim(null));
    }

    @Test
    public void testTrimToNull() {
        assertEquals(FOO, Strings.trimToNull(FOO + "  "));
        assertEquals(FOO, Strings.trimToNull(" " + FOO + "  "));
        assertEquals(FOO, Strings.trimToNull(" " + FOO));
        assertEquals(FOO, Strings.trimToNull(FOO + ""));
        assertEquals(null, Strings.trimToNull(" \t\r\n\b "));
        assertEquals(null, Strings.trimToNull(StringsTest.TRIMMABLE));
        assertEquals(StringsTest.NON_TRIMMABLE, Strings.trimToNull(StringsTest.NON_TRIMMABLE));
        assertEquals(null, Strings.trimToNull(""));
        assertEquals(null, Strings.trimToNull(null));
    }

    @Test
    public void testTrimToEmpty() {
        assertEquals(FOO, Strings.trimToEmpty(FOO + "  "));
        assertEquals(FOO, Strings.trimToEmpty(" " + FOO + "  "));
        assertEquals(FOO, Strings.trimToEmpty(" " + FOO));
        assertEquals(FOO, Strings.trimToEmpty(FOO + ""));
        assertEquals("", Strings.trimToEmpty(" \t\r\n\b "));
        assertEquals("", Strings.trimToEmpty(StringsTest.TRIMMABLE));
        assertEquals(StringsTest.NON_TRIMMABLE, Strings.trimToEmpty(StringsTest.NON_TRIMMABLE));
        assertEquals("", Strings.trimToEmpty(""));
        assertEquals("", Strings.trimToEmpty(null));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testStrip_String() {
        assertEquals(null, Strings.strip(null));
        assertEquals("", Strings.strip(""));
        assertEquals("", Strings.strip("        "));
        assertEquals("abc", Strings.strip("  abc  "));
        assertEquals(StringsTest.NON_WHITESPACE, 
            Strings.strip(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE));
    }
    
    @Test
    public void testStripToNull_String() {
        assertEquals(null, Strings.stripToNull(null));
        assertEquals(null, Strings.stripToNull(""));
        assertEquals(null, Strings.stripToNull("        "));
        assertEquals(null, Strings.stripToNull(StringsTest.WHITESPACE));
        assertEquals("ab c", Strings.stripToNull("  ab c  "));
        assertEquals(StringsTest.NON_WHITESPACE, 
            Strings.stripToNull(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE));
    }
    
    @Test
    public void testStripToEmpty_String() {
        assertEquals("", Strings.stripToEmpty(null));
        assertEquals("", Strings.stripToEmpty(""));
        assertEquals("", Strings.stripToEmpty("        "));
        assertEquals("", Strings.stripToEmpty(StringsTest.WHITESPACE));
        assertEquals("ab c", Strings.stripToEmpty("  ab c  "));
        assertEquals(StringsTest.NON_WHITESPACE, 
            Strings.stripToEmpty(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE));
    }
    
    @Test
    public void testStrip_StringString() {
        // null strip
        assertEquals(null, Strings.strip(null, null));
        assertEquals("", Strings.strip("", null));
        assertEquals("", Strings.strip("        ", null));
        assertEquals("abc", Strings.strip("  abc  ", null));
        assertEquals(StringsTest.NON_WHITESPACE, 
            Strings.strip(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, null));

        // "" strip
        assertEquals(null, Strings.strip(null, ""));
        assertEquals("", Strings.strip("", ""));
        assertEquals("        ", Strings.strip("        ", ""));
        assertEquals("  abc  ", Strings.strip("  abc  ", ""));
        assertEquals(StringsTest.WHITESPACE, Strings.strip(StringsTest.WHITESPACE, ""));
        
        // " " strip
        assertEquals(null, Strings.strip(null, " "));
        assertEquals("", Strings.strip("", " "));
        assertEquals("", Strings.strip("        ", " "));
        assertEquals("abc", Strings.strip("  abc  ", " "));
        
        // "ab" strip
        assertEquals(null, Strings.strip(null, "ab"));
        assertEquals("", Strings.strip("", "ab"));
        assertEquals("        ", Strings.strip("        ", "ab"));
        assertEquals("  abc  ", Strings.strip("  abc  ", "ab"));
        assertEquals("c", Strings.strip("abcabab", "ab"));
        assertEquals(StringsTest.WHITESPACE, Strings.strip(StringsTest.WHITESPACE, ""));
    }
    
    @Test
    public void testStripStart_StringString() {
        // null stripStart
        assertEquals(null, Strings.stripStart(null, null));
        assertEquals("", Strings.stripStart("", null));
        assertEquals("", Strings.stripStart("        ", null));
        assertEquals("abc  ", Strings.stripStart("  abc  ", null));
        assertEquals(StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, 
            Strings.stripStart(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, null));

        // "" stripStart
        assertEquals(null, Strings.stripStart(null, ""));
        assertEquals("", Strings.stripStart("", ""));
        assertEquals("        ", Strings.stripStart("        ", ""));
        assertEquals("  abc  ", Strings.stripStart("  abc  ", ""));
        assertEquals(StringsTest.WHITESPACE, Strings.stripStart(StringsTest.WHITESPACE, ""));
        
        // " " stripStart
        assertEquals(null, Strings.stripStart(null, " "));
        assertEquals("", Strings.stripStart("", " "));
        assertEquals("", Strings.stripStart("        ", " "));
        assertEquals("abc  ", Strings.stripStart("  abc  ", " "));
        
        // "ab" stripStart
        assertEquals(null, Strings.stripStart(null, "ab"));
        assertEquals("", Strings.stripStart("", "ab"));
        assertEquals("        ", Strings.stripStart("        ", "ab"));
        assertEquals("  abc  ", Strings.stripStart("  abc  ", "ab"));
        assertEquals("cabab", Strings.stripStart("abcabab", "ab"));
        assertEquals(StringsTest.WHITESPACE, Strings.stripStart(StringsTest.WHITESPACE, ""));
    }
    
    @Test
    public void testStripEnd_StringString() {
        // null stripEnd
        assertEquals(null, Strings.stripEnd(null, null));
        assertEquals("", Strings.stripEnd("", null));
        assertEquals("", Strings.stripEnd("        ", null));
        assertEquals("  abc", Strings.stripEnd("  abc  ", null));
        assertEquals(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE, 
            Strings.stripEnd(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, null));

        // "" stripEnd
        assertEquals(null, Strings.stripEnd(null, ""));
        assertEquals("", Strings.stripEnd("", ""));
        assertEquals("        ", Strings.stripEnd("        ", ""));
        assertEquals("  abc  ", Strings.stripEnd("  abc  ", ""));
        assertEquals(StringsTest.WHITESPACE, Strings.stripEnd(StringsTest.WHITESPACE, ""));
        
        // " " stripEnd
        assertEquals(null, Strings.stripEnd(null, " "));
        assertEquals("", Strings.stripEnd("", " "));
        assertEquals("", Strings.stripEnd("        ", " "));
        assertEquals("  abc", Strings.stripEnd("  abc  ", " "));
        
        // "ab" stripEnd
        assertEquals(null, Strings.stripEnd(null, "ab"));
        assertEquals("", Strings.stripEnd("", "ab"));
        assertEquals("        ", Strings.stripEnd("        ", "ab"));
        assertEquals("  abc  ", Strings.stripEnd("  abc  ", "ab"));
        assertEquals("abc", Strings.stripEnd("abcabab", "ab"));
        assertEquals(StringsTest.WHITESPACE, Strings.stripEnd(StringsTest.WHITESPACE, ""));
    }

    @Test
    public void testStripAll() {
        // test stripAll method, merely an array version of the above strip
        final String[] empty = new String[0];
        final String[] fooSpace = new String[] { "  "+FOO+"  ", "  "+FOO, FOO+"  " };
        final String[] fooDots = new String[] { ".."+FOO+"..", ".."+FOO, FOO+".." };
        final String[] foo = new String[] { FOO, FOO, FOO };

        assertNull(Strings.stripAll((String[]) null));
        // Additional varargs tests
        assertArrayEquals(empty, Strings.stripAll()); // empty array
        assertArrayEquals(new String[]{null}, Strings.stripAll((String) null)); // == new String[]{null}

        assertArrayEquals(empty, Strings.stripAll(empty));
        assertArrayEquals(foo, Strings.stripAll(fooSpace));
        
        assertNull(Strings.stripAll(null, null));
        assertArrayEquals(foo, Strings.stripAll(fooSpace, null));
        assertArrayEquals(foo, Strings.stripAll(fooDots, "."));
    }
}
