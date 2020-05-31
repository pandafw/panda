package panda.lang.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 */
public final class TypeTokenTest extends TestCase {

	List<Integer> listOfInteger = null;
	List<Number> listOfNumber = null;
	List<String> listOfString = null;
	List<?> listOfUnknown = null;
	List<Set<String>> listOfSetOfString = null;
	List<Set<?>> listOfSetOfUnknown = null;

	private void checkCollectionElementType(Type expected, String field) throws Exception {
		Type t = getClass().getDeclaredField(field).getGenericType();
		TypeToken tt = TypeToken.get(t);
		Type actual = Types.getCollectionElementType(tt);
		assertEquals(expected, actual);
	}

	public void testGetCollectionElementType() throws Exception {
		checkCollectionElementType(String.class, "listOfString");
	}

	public void testOurTypeFunctionality() throws Exception {
		ParameterizedType ourType = Types.paramTypeOfOwner(null, List.class, String.class);

		Type parameterizedType = new TypeToken<List<String>>() {}.getType();
		assertNull(ourType.getOwnerType());
		assertEquals(String.class, ourType.getActualTypeArguments()[0]);
		assertEquals(List.class, ourType.getRawType());
		assertEquals(parameterizedType, ourType);
		assertEquals(parameterizedType.hashCode(), ourType.hashCode());
	}

	public void testNotEquals() throws Exception {
		ParameterizedType ourType = Types.paramTypeOfOwner(null, List.class, String.class);

		Type differentParameterizedType = new TypeToken<List<Integer>>() {}.getType();
		assertFalse(differentParameterizedType.equals(ourType));
		assertFalse(ourType.equals(differentParameterizedType));
	}
}
