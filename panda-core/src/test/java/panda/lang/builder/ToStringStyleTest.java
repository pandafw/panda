package panda.lang.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test case for ToStringStyle.
 * 
 * @version $Id: ToStringStyleTest.java 1436770 2013-01-22 07:09:45Z ggregory $
 */
public class ToStringStyleTest {

	private static class ToStringStyleImpl extends ToStringStyle {
		private static final long serialVersionUID = 1L;
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetArrayStart() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setArrayStart(null);
		assertEquals("", style.getArrayStart());
	}

	@Test
	public void testSetArrayEnd() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setArrayEnd(null);
		assertEquals("", style.getArrayEnd());
	}

	@Test
	public void testSetArraySeparator() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setArraySeparator(null);
		assertEquals("", style.getArraySeparator());
	}

	@Test
	public void testSetContentStart() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setContentStart(null);
		assertEquals("", style.getContentStart());
	}

	@Test
	public void testSetContentEnd() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setContentEnd(null);
		assertEquals("", style.getContentEnd());
	}

	@Test
	public void testSetFieldNameValueSeparator() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setFieldNameValueSeparator(null);
		assertEquals("", style.getFieldNameValueSeparator());
	}

	@Test
	public void testSetFieldSeparator() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setFieldSeparator(null);
		assertEquals("", style.getFieldSeparator());
	}

	@Test
	public void testSetNullText() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setNullText(null);
		assertEquals("", style.getNullText());
	}

	@Test
	public void testSetSizeStartText() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setSizeStartText(null);
		assertEquals("", style.getSizeStartText());
	}

	@Test
	public void testSetSizeEndText() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setSizeEndText(null);
		assertEquals("", style.getSizeEndText());
	}

	@Test
	public void testSetSummaryObjectStartText() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setSummaryObjectStartText(null);
		assertEquals("", style.getSummaryObjectStartText());
	}

	@Test
	public void testSetSummaryObjectEndText() {
		final ToStringStyle style = new ToStringStyleImpl();
		style.setSummaryObjectEndText(null);
		assertEquals("", style.getSummaryObjectEndText());
	}

	/**
	 * An object used to test {@link ToStringStyle}.
	 */
	static class Person {
		/**
		 * Test String field.
		 */
		String name;

		/**
		 * Test integer field.
		 */
		int age;

		/**
		 * Test boolean field.
		 */
		boolean smoker;
	}
}
