package panda.lang;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import panda.lang.Types;
import junit.framework.TestCase;

/**
 */
public final class TypesTest extends TestCase {

	List<Integer> listOfInteger = null;
	List<Number> listOfNumber = null;
	List<String> listOfString = null;
	List<?> listOfUnknown = null;
	List<Set<String>> listOfSetOfString = null;
	List<Set<?>> listOfSetOfUnknown = null;

	public void testEquals() throws Exception {
		Type e = getClass().getDeclaredField("listOfString").getGenericType();
		Type a = Types.paramTypeOf(List.class, String.class);

		assertTrue(e.equals(a));
		assertTrue(a.equals(e));
	}
	
	public void testGetDeclaredFieldType() throws Exception {
		assertEquals(Types.paramTypeOf(List.class, Integer.class), 
			Types.getDeclaredFieldType(TypesTest.class, "listOfInteger"));
		assertEquals(Types.paramTypeOf(List.class, Number.class), 
			Types.getDeclaredFieldType(TypesTest.class, "listOfNumber"));
		assertEquals(Types.paramTypeOf(List.class, String.class), 
			Types.getDeclaredFieldType(TypesTest.class, "listOfString"));
		
	}
}
