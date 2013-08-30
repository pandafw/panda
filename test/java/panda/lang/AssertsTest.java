package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Asserts;

/**
 * Unit tests {@link Asserts}.
 *
 */
public class AssertsTest  {

	// -----------------------------------------------------------------------
	@Test
	public void testIsTrue1() {
		Asserts.isTrue(true);
		try {
			Asserts.isTrue(false);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated expression is false", ex.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsTrue2() {
		Asserts.isTrue(true, "MSG");
		try {
			Asserts.isTrue(false, "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsTrue3() {
		Asserts.isTrue(true, "MSG", 6);
		try {
			Asserts.isTrue(false, "MSG", 6);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsTrue4() {
		Asserts.isTrue(true, "MSG", 7);
		try {
			Asserts.isTrue(false, "MSG", 7);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsTrue5() {
		Asserts.isTrue(true, "MSG", 7.4d);
		try {
			Asserts.isTrue(false, "MSG", 7.4d);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNotNull1() {
		Asserts.notNull(new Object());
		try {
			Asserts.notNull(null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated object is null", ex.getMessage());
		}

		final String str = "Hi";
		final String testStr = Asserts.notNull(str);
		assertSame(str, testStr);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotNull2() {
		Asserts.notNull(new Object(), "MSG");
		try {
			Asserts.notNull(null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("MSG", ex.getMessage());
		}

		final String str = "Hi";
		final String testStr = Asserts.notNull(str, "Message");
		assertSame(str, testStr);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyArray1() {
		Asserts.notEmpty(new Object[] { null });
		try {
			Asserts.notEmpty((Object[])null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated array is empty", ex.getMessage());
		}
		try {
			Asserts.notEmpty(new Object[0]);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated array is empty", ex.getMessage());
		}

		final String[] array = new String[] { "hi" };
		final String[] test = Asserts.notEmpty(array);
		assertSame(array, test);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyArray2() {
		Asserts.notEmpty(new Object[] { null }, "MSG");
		try {
			Asserts.notEmpty((Object[])null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("MSG", ex.getMessage());
		}
		try {
			Asserts.notEmpty(new Object[0], "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}

		final String[] array = new String[] { "hi" };
		final String[] test = Asserts.notEmpty(array, "Message");
		assertSame(array, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyCollection1() {
		final Collection<Integer> coll = new ArrayList<Integer>();
		try {
			Asserts.notEmpty((Collection<?>)null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated collection is empty", ex.getMessage());
		}
		try {
			Asserts.notEmpty(coll);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated collection is empty", ex.getMessage());
		}
		coll.add(Integer.valueOf(8));
		Asserts.notEmpty(coll);

		final Collection<Integer> test = Asserts.notEmpty(coll);
		assertSame(coll, test);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyCollection2() {
		final Collection<Integer> coll = new ArrayList<Integer>();
		try {
			Asserts.notEmpty((Collection<?>)null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("MSG", ex.getMessage());
		}
		try {
			Asserts.notEmpty(coll, "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}
		coll.add(Integer.valueOf(8));
		Asserts.notEmpty(coll, "MSG");

		final Collection<Integer> test = Asserts.notEmpty(coll, "Message");
		assertSame(coll, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyMap1() {
		final Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			Asserts.notEmpty((Map<?, ?>)null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated map is empty", ex.getMessage());
		}
		try {
			Asserts.notEmpty(map);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated map is empty", ex.getMessage());
		}
		map.put("ll", Integer.valueOf(8));
		Asserts.notEmpty(map);

		final Map<String, Integer> test = Asserts.notEmpty(map);
		assertSame(map, test);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyMap2() {
		final Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			Asserts.notEmpty((Map<?, ?>)null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("MSG", ex.getMessage());
		}
		try {
			Asserts.notEmpty(map, "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}
		map.put("ll", Integer.valueOf(8));
		Asserts.notEmpty(map, "MSG");

		final Map<String, Integer> test = Asserts.notEmpty(map, "Message");
		assertSame(map, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyString1() {
		Asserts.notEmpty("hjl");
		try {
			Asserts.notEmpty((String)null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated character sequence is empty", ex.getMessage());
		}
		try {
			Asserts.notEmpty("");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated character sequence is empty", ex.getMessage());
		}

		final String str = "Hi";
		final String testStr = Asserts.notEmpty(str);
		assertSame(str, testStr);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotEmptyString2() {
		Asserts.notEmpty("a", "MSG");
		try {
			Asserts.notEmpty((String)null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("MSG", ex.getMessage());
		}
		try {
			Asserts.notEmpty("", "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}

		final String str = "Hi";
		final String testStr = Asserts.notEmpty(str, "Message");
		assertSame(str, testStr);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankNullStringShouldThrow() {
		// given
		final String string = null;

		try {
			// when
			Asserts.notBlank(string);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException e) {
			// then
			assertEquals("The validated character sequence is blank", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgNullStringShouldThrow() {
		// given
		final String string = null;

		try {
			// when
			Asserts.notBlank(string, "Message");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException e) {
			// then
			assertEquals("Message", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankEmptyStringShouldThrow() {
		// given
		final String string = "";

		try {
			// when
			Asserts.notBlank(string);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// then
			assertEquals("The validated character sequence is blank", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankBlankStringWithWhitespacesShouldThrow() {
		// given
		final String string = "   ";

		try {
			// when
			Asserts.notBlank(string);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// then
			assertEquals("The validated character sequence is blank", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankBlankStringWithNewlinesShouldThrow() {
		// given
		final String string = " \n \t \r \n ";

		try {
			// when
			Asserts.notBlank(string);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// then
			assertEquals("The validated character sequence is blank", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgBlankStringShouldThrow() {
		// given
		final String string = " \n \t \r \n ";

		try {
			// when
			Asserts.notBlank(string, "Message");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// then
			assertEquals("Message", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgBlankStringWithWhitespacesShouldThrow() {
		// given
		final String string = "   ";

		try {
			// when
			Asserts.notBlank(string, "Message");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// then
			assertEquals("Message", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgEmptyStringShouldThrow() {
		// given
		final String string = "";

		try {
			// when
			Asserts.notBlank(string, "Message");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// then
			assertEquals("Message", e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankNotBlankStringShouldNotThrow() {
		// given
		final String string = "abc";

		// when
		Asserts.notBlank(string);

		// then should not throw
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankNotBlankStringWithWhitespacesShouldNotThrow() {
		// given
		final String string = "  abc   ";

		// when
		Asserts.notBlank(string);

		// then should not throw
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankNotBlankStringWithNewlinesShouldNotThrow() {
		// given
		final String string = " \n \t abc \r \n ";

		// when
		Asserts.notBlank(string);

		// then should not throw
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgNotBlankStringShouldNotThrow() {
		// given
		final String string = "abc";

		// when
		Asserts.notBlank(string, "Message");

		// then should not throw
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgNotBlankStringWithWhitespacesShouldNotThrow() {
		// given
		final String string = "  abc   ";

		// when
		Asserts.notBlank(string, "Message");

		// then should not throw
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankMsgNotBlankStringWithNewlinesShouldNotThrow() {
		// given
		final String string = " \n \t abc \r \n ";

		// when
		Asserts.notBlank(string, "Message");

		// then should not throw
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNotBlankReturnValues1() {
		final String str = "Hi";
		final String test = Asserts.notBlank(str);
		assertSame(str, test);
	}

	@Test
	public void testNotBlankReturnValues2() {
		final String str = "Hi";
		final String test = Asserts.notBlank(str, "Message");
		assertSame(str, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNoNullElementsArray1() {
		String[] array = new String[] { "a", "b" };
		Asserts.noNullElements(array);
		try {
			Asserts.noNullElements((Object[])null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated object is null", ex.getMessage());
		}
		array[1] = null;
		try {
			Asserts.noNullElements(array);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated array contains null element at index: 1", ex.getMessage());
		}

		array = new String[] { "a", "b" };
		final String[] test = Asserts.noNullElements(array);
		assertSame(array, test);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNoNullElementsArray2() {
		String[] array = new String[] { "a", "b" };
		Asserts.noNullElements(array, "MSG");
		try {
			Asserts.noNullElements((Object[])null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated object is null", ex.getMessage());
		}
		array[1] = null;
		try {
			Asserts.noNullElements(array, "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}

		array = new String[] { "a", "b" };
		final String[] test = Asserts.noNullElements(array, "Message");
		assertSame(array, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testNoNullElementsCollection1() {
		final List<String> coll = new ArrayList<String>();
		coll.add("a");
		coll.add("b");
		Asserts.noNullElements(coll);
		try {
			Asserts.noNullElements((Collection<?>)null);
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated object is null", ex.getMessage());
		}
		coll.set(1, null);
		try {
			Asserts.noNullElements(coll);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("The validated collection contains null element at index: 1", ex.getMessage());
		}

		coll.set(1, "b");
		final List<String> test = Asserts.noNullElements(coll);
		assertSame(coll, test);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testNoNullElementsCollection2() {
		final List<String> coll = new ArrayList<String>();
		coll.add("a");
		coll.add("b");
		Asserts.noNullElements(coll, "MSG");
		try {
			Asserts.noNullElements((Collection<?>)null, "MSG");
			fail("Expecting NullPointerException");
		}
		catch (final NullPointerException ex) {
			assertEquals("The validated object is null", ex.getMessage());
		}
		coll.set(1, null);
		try {
			Asserts.noNullElements(coll, "MSG");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			assertEquals("MSG", ex.getMessage());
		}

		coll.set(1, "b");
		final List<String> test = Asserts.noNullElements(coll, "Message");
		assertSame(coll, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testValidIndex_withMessage_array() {
		final Object[] array = new Object[2];
		Asserts.validIndex(array, 0, "Broken: ");
		Asserts.validIndex(array, 1, "Broken: ");
		try {
			Asserts.validIndex(array, -1, "Broken: ");
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("Broken: ", ex.getMessage());
		}
		try {
			Asserts.validIndex(array, 2, "Broken: ");
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("Broken: ", ex.getMessage());
		}

		final String[] strArray = new String[] { "Hi" };
		final String[] test = Asserts.noNullElements(strArray, "Message");
		assertSame(strArray, test);
	}

	@Test
	public void testValidIndex_array() {
		final Object[] array = new Object[2];
		Asserts.validIndex(array, 0);
		Asserts.validIndex(array, 1);
		try {
			Asserts.validIndex(array, -1);
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("The validated array index is invalid: -1", ex.getMessage());
		}
		try {
			Asserts.validIndex(array, 2);
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("The validated array index is invalid: 2", ex.getMessage());
		}

		final String[] strArray = new String[] { "Hi" };
		final String[] test = Asserts.noNullElements(strArray);
		assertSame(strArray, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testValidIndex_withMessage_collection() {
		final Collection<String> coll = new ArrayList<String>();
		coll.add(null);
		coll.add(null);
		Asserts.validIndex(coll, 0, "Broken: ");
		Asserts.validIndex(coll, 1, "Broken: ");
		try {
			Asserts.validIndex(coll, -1, "Broken: ");
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("Broken: ", ex.getMessage());
		}
		try {
			Asserts.validIndex(coll, 2, "Broken: ");
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("Broken: ", ex.getMessage());
		}

		final List<String> strColl = Arrays.asList(new String[] { "Hi" });
		final List<String> test = Asserts.validIndex(strColl, 0, "Message");
		assertSame(strColl, test);
	}

	@Test
	public void testValidIndex_collection() {
		final Collection<String> coll = new ArrayList<String>();
		coll.add(null);
		coll.add(null);
		Asserts.validIndex(coll, 0);
		Asserts.validIndex(coll, 1);
		try {
			Asserts.validIndex(coll, -1);
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("The validated collection index is invalid: -1", ex.getMessage());
		}
		try {
			Asserts.validIndex(coll, 2);
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("The validated collection index is invalid: 2", ex.getMessage());
		}

		final List<String> strColl = Arrays.asList(new String[] { "Hi" });
		final List<String> test = Asserts.validIndex(strColl, 0);
		assertSame(strColl, test);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	@Test
	public void testValidIndex_withMessage_charSequence() {
		final CharSequence str = "Hi";
		Asserts.validIndex(str, 0, "Broken: ");
		Asserts.validIndex(str, 1, "Broken: ");
		try {
			Asserts.validIndex(str, -1, "Broken: ");
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("Broken: ", ex.getMessage());
		}
		try {
			Asserts.validIndex(str, 2, "Broken: ");
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("Broken: ", ex.getMessage());
		}

		final String input = "Hi";
		final String test = Asserts.validIndex(input, 0, "Message");
		assertSame(input, test);
	}

	@Test
	public void testValidIndex_charSequence() {
		final CharSequence str = "Hi";
		Asserts.validIndex(str, 0);
		Asserts.validIndex(str, 1);
		try {
			Asserts.validIndex(str, -1);
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("The validated character sequence index is invalid: -1", ex.getMessage());
		}
		try {
			Asserts.validIndex(str, 2);
			fail("Expecting IndexOutOfBoundsException");
		}
		catch (final IndexOutOfBoundsException ex) {
			assertEquals("The validated character sequence index is invalid: 2", ex.getMessage());
		}

		final String input = "Hi";
		final String test = Asserts.validIndex(input, 0);
		assertSame(input, test);
	}

	@Test
	public void testMatchesPattern() {
		final CharSequence str = "hi";
		Asserts.matchesPattern(str, "[a-z]*");
		try {
			Asserts.matchesPattern(str, "[0-9]*");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("The string hi does not match the pattern [0-9]*", e.getMessage());
		}
	}

	@Test
	public void testMatchesPattern_withMessage() {
		final CharSequence str = "hi";
		Asserts.matchesPattern(str, "[a-z]*", "Does not match");
		try {
			Asserts.matchesPattern(str, "[0-9]*", "Does not match");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Does not match", e.getMessage());
		}
	}

	@Test
	public void testInclusiveBetween() {
		Asserts.inclusiveBetween("a", "c", "b");
		Asserts.inclusiveBetween(0, 2, 1);
		Asserts.inclusiveBetween(0, 2, 2);
		try {
			Asserts.inclusiveBetween(0, 5, 6);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("The value 6 is not in the specified inclusive range of 0 to 5", e.getMessage());
		}
	}

	@Test
	public void testInclusiveBetween_withMessage() {
		Asserts.inclusiveBetween("a", "c", "b", "Error");
		Asserts.inclusiveBetween(0, 2, 1, "Error");
		Asserts.inclusiveBetween(0, 2, 2, "Error");
		try {
			Asserts.inclusiveBetween(0, 5, 6, "Error");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Error", e.getMessage());
		}
	}

	@Test
	public void testExclusiveBetween() {
		Asserts.exclusiveBetween("a", "c", "b");
		Asserts.exclusiveBetween(0, 2, 1);
		try {
			Asserts.exclusiveBetween(0, 5, 6);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("The value 6 is not in the specified exclusive range of 0 to 5", e.getMessage());
		}
		try {
			Asserts.exclusiveBetween(0, 5, 5);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("The value 5 is not in the specified exclusive range of 0 to 5", e.getMessage());
		}
	}

	@Test
	public void testExclusiveBetween_withMessage() {
		Asserts.exclusiveBetween("a", "c", "b", "Error");
		Asserts.exclusiveBetween(0, 2, 1, "Error");
		try {
			Asserts.exclusiveBetween(0, 5, 6, "Error");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			Asserts.exclusiveBetween(0, 5, 5, "Error");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Error", e.getMessage());
		}
	}

	@Test
	public void testIsInstanceOf() {
		Asserts.isInstanceOf(String.class, "hi");
		Asserts.isInstanceOf(Integer.class, 1);
	}

	@Test
	public void testIsInstanceOfExceptionMessage() {
		try {
			Asserts.isInstanceOf(List.class, "hi");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Expected type: java.util.List, actual: java.lang.String", e.getMessage());
		}
	}

	@Test
	public void testIsInstanceOf_withMessage() {
		Asserts.isInstanceOf(String.class, "hi", "Error");
		Asserts.isInstanceOf(Integer.class, 1, "Error");
		try {
			Asserts.isInstanceOf(List.class, "hi", "Error");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Error", e.getMessage());
		}
	}

	@Test
	public void testIsAssignable() {
		Asserts.isAssignableFrom(CharSequence.class, String.class);
		Asserts.isAssignableFrom(AbstractList.class, ArrayList.class);
	}

	@Test
	public void testIsAssignableExceptionMessage() {
		try {
			Asserts.isAssignableFrom(List.class, String.class);
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Cannot assign a java.lang.String to a java.util.List", e.getMessage());
		}
	}

	@Test
	public void testIsAssignable_withMessage() {
		Asserts.isAssignableFrom(CharSequence.class, String.class, "Error");
		Asserts.isAssignableFrom(AbstractList.class, ArrayList.class, "Error");
		try {
			Asserts.isAssignableFrom(List.class, String.class, "Error");
			fail("Expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			assertEquals("Error", e.getMessage());
		}
	}

}
