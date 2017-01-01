package panda.lang.reflect;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.reflect.testbed.Ambig;
import panda.lang.reflect.testbed.Foo;
import panda.lang.reflect.testbed.Horse;
import panda.lang.reflect.testbed.PrivatelyShadowedChild;
import panda.lang.reflect.testbed.PublicChild;
import panda.lang.reflect.testbed.PubliclyShadowedChild;
import panda.lang.reflect.testbed.StaticContainer;
import panda.lang.reflect.testbed.StaticContainerChild;

/**
 * Unit tests Fields
 * 
 */
public class FieldsTest {

	static final Integer I0 = Integer.valueOf(0);
	static final Integer I1 = Integer.valueOf(1);
	static final Double D0 = Double.valueOf(0.0);
	static final Double D1 = Double.valueOf(1.0);

	private PublicChild publicChild;
	private PubliclyShadowedChild publiclyShadowedChild;
	private PrivatelyShadowedChild privatelyShadowedChild;
	private final Class<? super PublicChild> parentClass = PublicChild.class.getSuperclass();

	@Before
	public void setUp() {
		StaticContainer.reset();
		publicChild = new PublicChild();
		publiclyShadowedChild = new PubliclyShadowedChild();
		privatelyShadowedChild = new PrivatelyShadowedChild();
	}

	@Test
	public void testGetDeclaredFields() {
		Collection<Field> fs = Fields.getDeclaredFields(Horse.BlackHorse.class);
		assertEquals(1, fs.size());
		assertEquals(Horse.BlackHorse.class, fs.iterator().next().getDeclaringClass());
	}
	
	@Test
	public void testGetField() {
		assertEquals(Foo.class, Fields.getField(PublicChild.class, "VALUE").getDeclaringClass());
		assertEquals(parentClass, Fields.getField(PublicChild.class, "s").getDeclaringClass());
		assertNull(Fields.getField(PublicChild.class, "b"));
		assertNull(Fields.getField(PublicChild.class, "i"));
		assertNull(Fields.getField(PublicChild.class, "d"));
		assertEquals(Foo.class, Fields.getField(PubliclyShadowedChild.class, "VALUE").getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "s").getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "b").getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "i").getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "d").getDeclaringClass());
		assertEquals(Foo.class, Fields.getField(PrivatelyShadowedChild.class, "VALUE").getDeclaringClass());
		assertEquals(parentClass, Fields.getField(PrivatelyShadowedChild.class, "s").getDeclaringClass());
		assertNull(Fields.getField(PrivatelyShadowedChild.class, "b"));
		assertNull(Fields.getField(PrivatelyShadowedChild.class, "i"));
		assertNull(Fields.getField(PrivatelyShadowedChild.class, "d"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldIllegalArgumentException1() {
		Fields.getField(null, "none");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldIllegalArgumentException2() {
		Fields.getField(PublicChild.class, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldIllegalArgumentException3() {
		Fields.getField(PublicChild.class, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldIllegalArgumentException4() {
		Fields.getField(PublicChild.class, " ");
	}

	@Test
	public void testGetFieldForceAccess() {
		assertEquals(PublicChild.class, Fields.getField(PublicChild.class, "VALUE", true).getDeclaringClass());
		assertEquals(parentClass, Fields.getField(PublicChild.class, "s", true).getDeclaringClass());
		assertEquals(parentClass, Fields.getField(PublicChild.class, "b", true).getDeclaringClass());
		assertEquals(parentClass, Fields.getField(PublicChild.class, "i", true).getDeclaringClass());
		assertEquals(parentClass, Fields.getField(PublicChild.class, "d", true).getDeclaringClass());
		assertEquals(Foo.class, Fields.getField(PubliclyShadowedChild.class, "VALUE", true).getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "s", true)
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "b", true)
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "i", true)
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getField(PubliclyShadowedChild.class, "d", true)
			.getDeclaringClass());
		assertEquals(Foo.class, Fields.getField(PrivatelyShadowedChild.class, "VALUE", true).getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getField(PrivatelyShadowedChild.class, "s", true)
			.getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getField(PrivatelyShadowedChild.class, "b", true)
			.getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getField(PrivatelyShadowedChild.class, "i", true)
			.getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getField(PrivatelyShadowedChild.class, "d", true)
			.getDeclaringClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldForceAccessIllegalArgumentException1() {
		Fields.getField(null, "none", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldForceAccessIllegalArgumentException2() {
		Fields.getField(PublicChild.class, null, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldForceAccessIllegalArgumentException3() {
		Fields.getField(PublicChild.class, "", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldForceAccessIllegalArgumentException4() {
		Fields.getField(PublicChild.class, " ", true);
	}

	@Test
	public void testGetAllFields() {
		assertArrayEquals(new Field[0], Fields.getAllFields(Object.class));
		final Field[] fieldsNumber = Number.class.getDeclaredFields();
		assertArrayEquals(fieldsNumber, Fields.getAllFields(Number.class));
		final Field[] fieldsInteger = Integer.class.getDeclaredFields();
		assertArrayEquals(Arrays.addAll(fieldsInteger, fieldsNumber), Fields.getAllFields(Integer.class));
		assertEquals(5, Fields.getAllFields(PublicChild.class).length);
	}

	@Test
	public void testGetAllFieldsList() {
		assertEquals(0, Fields.getAllFieldsList(Object.class).size());
		final List<Field> fieldsNumber = Arrays.asList(Number.class.getDeclaredFields());
		assertEquals(fieldsNumber, Fields.getAllFieldsList(Number.class));
		final List<Field> fieldsInteger = Arrays.asList(Integer.class.getDeclaredFields());
		final List<Field> allFieldsInteger = new ArrayList<Field>(fieldsInteger);
		allFieldsInteger.addAll(fieldsNumber);
		assertEquals(allFieldsInteger, Fields.getAllFieldsList(Integer.class));
		assertEquals(5, Fields.getAllFieldsList(PublicChild.class).size());
	}

	@Test
	public void testGetDeclaredField() {
		assertNull(Fields.getDeclaredField(PublicChild.class, "VALUE"));
		assertNull(Fields.getDeclaredField(PublicChild.class, "s"));
		assertNull(Fields.getDeclaredField(PublicChild.class, "b"));
		assertNull(Fields.getDeclaredField(PublicChild.class, "i"));
		assertNull(Fields.getDeclaredField(PublicChild.class, "d"));
		assertNull(Fields.getDeclaredField(PubliclyShadowedChild.class, "VALUE"));
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "s")
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "b")
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "i")
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "d")
			.getDeclaringClass());
		assertNull(Fields.getDeclaredField(PrivatelyShadowedChild.class, "VALUE"));
		assertNull(Fields.getDeclaredField(PrivatelyShadowedChild.class, "s"));
		assertNull(Fields.getDeclaredField(PrivatelyShadowedChild.class, "b"));
		assertNull(Fields.getDeclaredField(PrivatelyShadowedChild.class, "i"));
		assertNull(Fields.getDeclaredField(PrivatelyShadowedChild.class, "d"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldAccessIllegalArgumentException1() {
		Fields.getDeclaredField(null, "none");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldAccessIllegalArgumentException2() {
		Fields.getDeclaredField(PublicChild.class, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldAccessIllegalArgumentException3() {
		Fields.getDeclaredField(PublicChild.class, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldAccessIllegalArgumentException4() {
		Fields.getDeclaredField(PublicChild.class, " ");
	}

	@Test
	public void testGetDeclaredFieldForceAccess() {
		assertEquals(PublicChild.class, Fields.getDeclaredField(PublicChild.class, "VALUE", true).getDeclaringClass());
		assertNull(Fields.getDeclaredField(PublicChild.class, "s", true));
		assertNull(Fields.getDeclaredField(PublicChild.class, "b", true));
		assertNull(Fields.getDeclaredField(PublicChild.class, "i", true));
		assertNull(Fields.getDeclaredField(PublicChild.class, "d", true));
		assertNull(Fields.getDeclaredField(PubliclyShadowedChild.class, "VALUE", true));
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "s", true)
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "b", true)
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "i", true)
			.getDeclaringClass());
		assertEquals(PubliclyShadowedChild.class, Fields.getDeclaredField(PubliclyShadowedChild.class, "d", true)
			.getDeclaringClass());
		assertNull(Fields.getDeclaredField(PrivatelyShadowedChild.class, "VALUE", true));
		assertEquals(PrivatelyShadowedChild.class, Fields.getDeclaredField(PrivatelyShadowedChild.class, "s", true)
			.getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getDeclaredField(PrivatelyShadowedChild.class, "b", true)
			.getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getDeclaredField(PrivatelyShadowedChild.class, "i", true)
			.getDeclaringClass());
		assertEquals(PrivatelyShadowedChild.class, Fields.getDeclaredField(PrivatelyShadowedChild.class, "d", true)
			.getDeclaringClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldForceAccessIllegalArgumentException1() {
		Fields.getDeclaredField(null, "none", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldForceAccessIllegalArgumentException2() {
		Fields.getDeclaredField(PublicChild.class, null, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldForceAccessIllegalArgumentException3() {
		Fields.getDeclaredField(PublicChild.class, "", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDeclaredFieldForceAccessIllegalArgumentException4() {
		Fields.getDeclaredField(PublicChild.class, " ", true);
	}

	@Test
	public void testReadStaticField() throws Exception {
		assertEquals(Foo.VALUE, Fields.readStaticField(Fields.getField(Foo.class, "VALUE")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadStaticFieldIllegalArgumentException1() throws Exception {
		Fields.readStaticField(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadStaticFieldIllegalArgumentException2() throws Exception {
		assertEquals(Foo.VALUE, Fields.readStaticField(Fields.getField(Foo.class, "VALUE")));
		final Field nonStaticField = Fields.getField(PublicChild.class, "s");
		assumeNotNull(nonStaticField);
		Fields.readStaticField(nonStaticField);
	}

	@Test
	public void testReadStaticFieldForceAccess() throws Exception {
		assertEquals(Foo.VALUE, Fields.readStaticField(Fields.getField(Foo.class, "VALUE")));
		assertEquals(Foo.VALUE, Fields.readStaticField(Fields.getField(PublicChild.class, "VALUE")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadStaticFieldForceAccessIllegalArgumentException1() throws Exception {
		Fields.readStaticField(null, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadStaticFieldForceAccessIllegalArgumentException2() throws Exception {
		final Field nonStaticField = Fields.getField(PublicChild.class, "s", true);
		assumeNotNull(nonStaticField);
		Fields.readStaticField(nonStaticField);
	}

	@Test
	public void testReadNamedStaticField() throws Exception {
		assertEquals(Foo.VALUE, Fields.readStaticField(Foo.class, "VALUE"));
		assertEquals(Foo.VALUE, Fields.readStaticField(PubliclyShadowedChild.class, "VALUE"));
		assertEquals(Foo.VALUE, Fields.readStaticField(PrivatelyShadowedChild.class, "VALUE"));
		assertEquals(Foo.VALUE, Fields.readStaticField(PublicChild.class, "VALUE"));

		try {
			Fields.readStaticField(null, "none");
			fail("null class should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, null);
			fail("null field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, "");
			fail("empty field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, " ");
			fail("blank field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, "does_not_exist");
			fail("a field that doesn't exist should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(PublicChild.class, "s");
			fail("non-static field should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testReadNamedStaticFieldForceAccess() throws Exception {
		assertEquals(Foo.VALUE, Fields.readStaticField(Foo.class, "VALUE", true));
		assertEquals(Foo.VALUE, Fields.readStaticField(PubliclyShadowedChild.class, "VALUE", true));
		assertEquals(Foo.VALUE, Fields.readStaticField(PrivatelyShadowedChild.class, "VALUE", true));
		assertEquals("child", Fields.readStaticField(PublicChild.class, "VALUE", true));

		try {
			Fields.readStaticField(null, "none", true);
			fail("null class should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, null, true);
			fail("null field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, "", true);
			fail("empty field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, " ", true);
			fail("blank field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(Foo.class, "does_not_exist", true);
			fail("a field that doesn't exist should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readStaticField(PublicChild.class, "s", false);
			fail("non-static field should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testReadDeclaredNamedStaticField() throws Exception {
		assertEquals(Foo.VALUE, Fields.readDeclaredStaticField(Foo.class, "VALUE"));
		try {
			Fields.readDeclaredStaticField(PublicChild.class, "VALUE");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readDeclaredStaticField(PubliclyShadowedChild.class, "VALUE");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readDeclaredStaticField(PrivatelyShadowedChild.class, "VALUE");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testReadDeclaredNamedStaticFieldForceAccess() throws Exception {
		assertEquals(Foo.VALUE, Fields.readDeclaredStaticField(Foo.class, "VALUE", true));
		assertEquals("child", Fields.readDeclaredStaticField(PublicChild.class, "VALUE", true));
		try {
			Fields.readDeclaredStaticField(PubliclyShadowedChild.class, "VALUE", true);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readDeclaredStaticField(PrivatelyShadowedChild.class, "VALUE", true);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testReadField() throws Exception {
		final Field parentS = Fields.getDeclaredField(parentClass, "s");
		assertEquals("s", Fields.readField(parentS, publicChild));
		assertEquals("s", Fields.readField(parentS, publiclyShadowedChild));
		assertEquals("s", Fields.readField(parentS, privatelyShadowedChild));
		final Field parentB = Fields.getDeclaredField(parentClass, "b", true);
		assertEquals(Boolean.FALSE, Fields.readField(parentB, publicChild));
		assertEquals(Boolean.FALSE, Fields.readField(parentB, publiclyShadowedChild));
		assertEquals(Boolean.FALSE, Fields.readField(parentB, privatelyShadowedChild));
		final Field parentI = Fields.getDeclaredField(parentClass, "i", true);
		assertEquals(I0, Fields.readField(parentI, publicChild));
		assertEquals(I0, Fields.readField(parentI, publiclyShadowedChild));
		assertEquals(I0, Fields.readField(parentI, privatelyShadowedChild));
		final Field parentD = Fields.getDeclaredField(parentClass, "d", true);
		assertEquals(D0, Fields.readField(parentD, publicChild));
		assertEquals(D0, Fields.readField(parentD, publiclyShadowedChild));
		assertEquals(D0, Fields.readField(parentD, privatelyShadowedChild));

		try {
			Fields.readField(null, publicChild);
			fail("a null field should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testReadFieldForceAccess() throws Exception {
		final Field parentS = Fields.getDeclaredField(parentClass, "s");
		parentS.setAccessible(false);
		assertEquals("s", Fields.readField(parentS, publicChild, true));
		assertEquals("s", Fields.readField(parentS, publiclyShadowedChild, true));
		assertEquals("s", Fields.readField(parentS, privatelyShadowedChild, true));
		final Field parentB = Fields.getDeclaredField(parentClass, "b", true);
		parentB.setAccessible(false);
		assertEquals(Boolean.FALSE, Fields.readField(parentB, publicChild, true));
		assertEquals(Boolean.FALSE, Fields.readField(parentB, publiclyShadowedChild, true));
		assertEquals(Boolean.FALSE, Fields.readField(parentB, privatelyShadowedChild, true));
		final Field parentI = Fields.getDeclaredField(parentClass, "i", true);
		parentI.setAccessible(false);
		assertEquals(I0, Fields.readField(parentI, publicChild, true));
		assertEquals(I0, Fields.readField(parentI, publiclyShadowedChild, true));
		assertEquals(I0, Fields.readField(parentI, privatelyShadowedChild, true));
		final Field parentD = Fields.getDeclaredField(parentClass, "d", true);
		parentD.setAccessible(false);
		assertEquals(D0, Fields.readField(parentD, publicChild, true));
		assertEquals(D0, Fields.readField(parentD, publiclyShadowedChild, true));
		assertEquals(D0, Fields.readField(parentD, privatelyShadowedChild, true));

		try {
			Fields.readField(null, publicChild, true);
			fail("a null field should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testReadNamedField() throws Exception {
		assertEquals("s", Fields.readField(publicChild, "s"));
		assertEquals("ss", Fields.readField(publiclyShadowedChild, "s"));
		assertEquals("s", Fields.readField(privatelyShadowedChild, "s"));

		try {
			Fields.readField(publicChild, null);
			fail("a null field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField(publicChild, "");
			fail("an empty field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField(publicChild, " ");
			fail("a blank field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField((Object)null, "none");
			fail("a null target should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField(publicChild, "b");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(Boolean.TRUE, Fields.readField(publiclyShadowedChild, "b"));
		try {
			Fields.readField(privatelyShadowedChild, "b");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readField(publicChild, "i");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(I1, Fields.readField(publiclyShadowedChild, "i"));
		try {
			Fields.readField(privatelyShadowedChild, "i");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readField(publicChild, "d");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(D1, Fields.readField(publiclyShadowedChild, "d"));
		try {
			Fields.readField(privatelyShadowedChild, "d");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testReadNamedFieldForceAccess() throws Exception {
		assertEquals("s", Fields.readField(publicChild, "s", true));
		assertEquals("ss", Fields.readField(publiclyShadowedChild, "s", true));
		assertEquals("ss", Fields.readField(privatelyShadowedChild, "s", true));
		assertEquals(Boolean.FALSE, Fields.readField(publicChild, "b", true));
		assertEquals(Boolean.TRUE, Fields.readField(publiclyShadowedChild, "b", true));
		assertEquals(Boolean.TRUE, Fields.readField(privatelyShadowedChild, "b", true));
		assertEquals(I0, Fields.readField(publicChild, "i", true));
		assertEquals(I1, Fields.readField(publiclyShadowedChild, "i", true));
		assertEquals(I1, Fields.readField(privatelyShadowedChild, "i", true));
		assertEquals(D0, Fields.readField(publicChild, "d", true));
		assertEquals(D1, Fields.readField(publiclyShadowedChild, "d", true));
		assertEquals(D1, Fields.readField(privatelyShadowedChild, "d", true));

		try {
			Fields.readField(publicChild, null, true);
			fail("a null field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField(publicChild, "", true);
			fail("an empty field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField(publicChild, " ", true);
			fail("a blank field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readField((Object)null, "none", true);
			fail("a null target should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testReadDeclaredNamedField() throws Exception {
		try {
			Fields.readDeclaredField(publicChild, null);
			fail("a null field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(publicChild, "");
			fail("an empty field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(publicChild, " ");
			fail("a blank field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(null, "none");
			fail("a null target should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(publicChild, "s");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals("ss", Fields.readDeclaredField(publiclyShadowedChild, "s"));
		try {
			Fields.readDeclaredField(privatelyShadowedChild, "s");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readDeclaredField(publicChild, "b");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(Boolean.TRUE, Fields.readDeclaredField(publiclyShadowedChild, "b"));
		try {
			Fields.readDeclaredField(privatelyShadowedChild, "b");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readDeclaredField(publicChild, "i");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(I1, Fields.readDeclaredField(publiclyShadowedChild, "i"));
		try {
			Fields.readDeclaredField(privatelyShadowedChild, "i");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.readDeclaredField(publicChild, "d");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(D1, Fields.readDeclaredField(publiclyShadowedChild, "d"));
		try {
			Fields.readDeclaredField(privatelyShadowedChild, "d");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testReadDeclaredNamedFieldForceAccess() throws Exception {
		try {
			Fields.readDeclaredField(publicChild, null, true);
			fail("a null field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(publicChild, "", true);
			fail("an empty field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(publicChild, " ", true);
			fail("a blank field name should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(null, "none", true);
			fail("a null target should cause an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// expected
		}

		try {
			Fields.readDeclaredField(publicChild, "s", true);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals("ss", Fields.readDeclaredField(publiclyShadowedChild, "s", true));
		assertEquals("ss", Fields.readDeclaredField(privatelyShadowedChild, "s", true));
		try {
			Fields.readDeclaredField(publicChild, "b", true);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(Boolean.TRUE, Fields.readDeclaredField(publiclyShadowedChild, "b", true));
		assertEquals(Boolean.TRUE, Fields.readDeclaredField(privatelyShadowedChild, "b", true));
		try {
			Fields.readDeclaredField(publicChild, "i", true);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(I1, Fields.readDeclaredField(publiclyShadowedChild, "i", true));
		assertEquals(I1, Fields.readDeclaredField(privatelyShadowedChild, "i", true));
		try {
			Fields.readDeclaredField(publicChild, "d", true);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		assertEquals(D1, Fields.readDeclaredField(publiclyShadowedChild, "d", true));
		assertEquals(D1, Fields.readDeclaredField(privatelyShadowedChild, "d", true));
	}

	@Test
	public void testWriteStaticField() throws Exception {
		Field field = StaticContainer.class.getDeclaredField("mutablePublic");
		Fields.writeStaticField(field, "new");
		assertEquals("new", StaticContainer.mutablePublic);
		field = StaticContainer.class.getDeclaredField("mutableProtected");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("mutablePackage");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("mutablePrivate");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PUBLIC");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PROTECTED");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PACKAGE");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PRIVATE");
		try {
			Fields.writeStaticField(field, "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
	}

	@Test
	public void testWriteStaticFieldForceAccess() throws Exception {
		Field field = StaticContainer.class.getDeclaredField("mutablePublic");
		Fields.writeStaticField(field, "new", true);
		assertEquals("new", StaticContainer.mutablePublic);
		field = StaticContainer.class.getDeclaredField("mutableProtected");
		Fields.writeStaticField(field, "new", true);
		assertEquals("new", StaticContainer.getMutableProtected());
		field = StaticContainer.class.getDeclaredField("mutablePackage");
		Fields.writeStaticField(field, "new", true);
		assertEquals("new", StaticContainer.getMutablePackage());
		field = StaticContainer.class.getDeclaredField("mutablePrivate");
		Fields.writeStaticField(field, "new", true);
		assertEquals("new", StaticContainer.getMutablePrivate());
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PUBLIC");
		try {
			Fields.writeStaticField(field, "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PROTECTED");
		try {
			Fields.writeStaticField(field, "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PACKAGE");
		try {
			Fields.writeStaticField(field, "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = StaticContainer.class.getDeclaredField("IMMUTABLE_PRIVATE");
		try {
			Fields.writeStaticField(field, "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
	}

	@Test
	public void testWriteNamedStaticField() throws Exception {
		Fields.writeStaticField(StaticContainerChild.class, "mutablePublic", "new");
		assertEquals("new", StaticContainer.mutablePublic);
		try {
			Fields.writeStaticField(StaticContainerChild.class, "mutableProtected", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "mutablePackage", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "mutablePrivate", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PUBLIC", "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PROTECTED", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PACKAGE", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PRIVATE", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testWriteNamedStaticFieldForceAccess() throws Exception {
		Fields.writeStaticField(StaticContainerChild.class, "mutablePublic", "new", true);
		assertEquals("new", StaticContainer.mutablePublic);
		Fields.writeStaticField(StaticContainerChild.class, "mutableProtected", "new", true);
		assertEquals("new", StaticContainer.getMutableProtected());
		Fields.writeStaticField(StaticContainerChild.class, "mutablePackage", "new", true);
		assertEquals("new", StaticContainer.getMutablePackage());
		Fields.writeStaticField(StaticContainerChild.class, "mutablePrivate", "new", true);
		assertEquals("new", StaticContainer.getMutablePrivate());
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PUBLIC", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PROTECTED", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PACKAGE", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeStaticField(StaticContainerChild.class, "IMMUTABLE_PRIVATE", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
	}

	@Test
	public void testWriteDeclaredNamedStaticField() throws Exception {
		Fields.writeStaticField(StaticContainer.class, "mutablePublic", "new");
		assertEquals("new", StaticContainer.mutablePublic);
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "mutableProtected", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "mutablePackage", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "mutablePrivate", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PUBLIC", "new");
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PROTECTED", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PACKAGE", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PRIVATE", "new");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testWriteDeclaredNamedStaticFieldForceAccess() throws Exception {
		Fields.writeDeclaredStaticField(StaticContainer.class, "mutablePublic", "new", true);
		assertEquals("new", StaticContainer.mutablePublic);
		Fields.writeDeclaredStaticField(StaticContainer.class, "mutableProtected", "new", true);
		assertEquals("new", StaticContainer.getMutableProtected());
		Fields.writeDeclaredStaticField(StaticContainer.class, "mutablePackage", "new", true);
		assertEquals("new", StaticContainer.getMutablePackage());
		Fields.writeDeclaredStaticField(StaticContainer.class, "mutablePrivate", "new", true);
		assertEquals("new", StaticContainer.getMutablePrivate());
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PUBLIC", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PROTECTED", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PACKAGE", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		try {
			Fields.writeDeclaredStaticField(StaticContainer.class, "IMMUTABLE_PRIVATE", "new", true);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
	}

	@Test
	public void testWriteField() throws Exception {
		Field field = parentClass.getDeclaredField("s");
		Fields.writeField(field, publicChild, "S");
		assertEquals("S", field.get(publicChild));
		field = parentClass.getDeclaredField("b");
		try {
			Fields.writeField(field, publicChild, Boolean.TRUE);
			fail("Expected IllegalAccessException");
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = parentClass.getDeclaredField("i");
		try {
			Fields.writeField(field, publicChild, Integer.valueOf(Integer.MAX_VALUE));
		}
		catch (final IllegalAccessException e) {
			// pass
		}
		field = parentClass.getDeclaredField("d");
		try {
			Fields.writeField(field, publicChild, Double.valueOf(Double.MAX_VALUE));
		}
		catch (final IllegalAccessException e) {
			// pass
		}
	}

	@Test
	public void testWriteFieldForceAccess() throws Exception {
		Field field = parentClass.getDeclaredField("s");
		Fields.writeField(field, publicChild, "S", true);
		assertEquals("S", field.get(publicChild));
		field = parentClass.getDeclaredField("b");
		Fields.writeField(field, publicChild, Boolean.TRUE, true);
		assertEquals(Boolean.TRUE, field.get(publicChild));
		field = parentClass.getDeclaredField("i");
		Fields.writeField(field, publicChild, Integer.valueOf(Integer.MAX_VALUE), true);
		assertEquals(Integer.valueOf(Integer.MAX_VALUE), field.get(publicChild));
		field = parentClass.getDeclaredField("d");
		Fields.writeField(field, publicChild, Double.valueOf(Double.MAX_VALUE), true);
		assertEquals(Double.valueOf(Double.MAX_VALUE), field.get(publicChild));
	}

	@Test
	public void testWriteNamedField() throws Exception {
		Fields.writeField(publicChild, "s", "S");
		assertEquals("S", Fields.readField(publicChild, "s"));
		try {
			Fields.writeField(publicChild, "b", Boolean.TRUE);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeField(publicChild, "i", Integer.valueOf(1));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeField(publicChild, "d", Double.valueOf(1.0));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}

		Fields.writeField(publiclyShadowedChild, "s", "S");
		assertEquals("S", Fields.readField(publiclyShadowedChild, "s"));
		Fields.writeField(publiclyShadowedChild, "b", Boolean.FALSE);
		assertEquals(Boolean.FALSE, Fields.readField(publiclyShadowedChild, "b"));
		Fields.writeField(publiclyShadowedChild, "i", Integer.valueOf(0));
		assertEquals(Integer.valueOf(0), Fields.readField(publiclyShadowedChild, "i"));
		Fields.writeField(publiclyShadowedChild, "d", Double.valueOf(0.0));
		assertEquals(Double.valueOf(0.0), Fields.readField(publiclyShadowedChild, "d"));

		Fields.writeField(privatelyShadowedChild, "s", "S");
		assertEquals("S", Fields.readField(privatelyShadowedChild, "s"));
		try {
			Fields.writeField(privatelyShadowedChild, "b", Boolean.TRUE);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeField(privatelyShadowedChild, "i", Integer.valueOf(1));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeField(privatelyShadowedChild, "d", Double.valueOf(1.0));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testWriteNamedFieldForceAccess() throws Exception {
		Fields.writeField(publicChild, "s", "S", true);
		assertEquals("S", Fields.readField(publicChild, "s", true));
		Fields.writeField(publicChild, "b", Boolean.TRUE, true);
		assertEquals(Boolean.TRUE, Fields.readField(publicChild, "b", true));
		Fields.writeField(publicChild, "i", Integer.valueOf(1), true);
		assertEquals(Integer.valueOf(1), Fields.readField(publicChild, "i", true));
		Fields.writeField(publicChild, "d", Double.valueOf(1.0), true);
		assertEquals(Double.valueOf(1.0), Fields.readField(publicChild, "d", true));

		Fields.writeField(publiclyShadowedChild, "s", "S", true);
		assertEquals("S", Fields.readField(publiclyShadowedChild, "s", true));
		Fields.writeField(publiclyShadowedChild, "b", Boolean.FALSE, true);
		assertEquals(Boolean.FALSE, Fields.readField(publiclyShadowedChild, "b", true));
		Fields.writeField(publiclyShadowedChild, "i", Integer.valueOf(0), true);
		assertEquals(Integer.valueOf(0), Fields.readField(publiclyShadowedChild, "i", true));
		Fields.writeField(publiclyShadowedChild, "d", Double.valueOf(0.0), true);
		assertEquals(Double.valueOf(0.0), Fields.readField(publiclyShadowedChild, "d", true));

		Fields.writeField(privatelyShadowedChild, "s", "S", true);
		assertEquals("S", Fields.readField(privatelyShadowedChild, "s", true));
		Fields.writeField(privatelyShadowedChild, "b", Boolean.FALSE, true);
		assertEquals(Boolean.FALSE, Fields.readField(privatelyShadowedChild, "b", true));
		Fields.writeField(privatelyShadowedChild, "i", Integer.valueOf(0), true);
		assertEquals(Integer.valueOf(0), Fields.readField(privatelyShadowedChild, "i", true));
		Fields.writeField(privatelyShadowedChild, "d", Double.valueOf(0.0), true);
		assertEquals(Double.valueOf(0.0), Fields.readField(privatelyShadowedChild, "d", true));
	}

	@Test
	public void testWriteDeclaredNamedField() throws Exception {
		try {
			Fields.writeDeclaredField(publicChild, "s", "S");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(publicChild, "b", Boolean.TRUE);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(publicChild, "i", Integer.valueOf(1));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(publicChild, "d", Double.valueOf(1.0));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}

		Fields.writeDeclaredField(publiclyShadowedChild, "s", "S");
		assertEquals("S", Fields.readDeclaredField(publiclyShadowedChild, "s"));
		Fields.writeDeclaredField(publiclyShadowedChild, "b", Boolean.FALSE);
		assertEquals(Boolean.FALSE, Fields.readDeclaredField(publiclyShadowedChild, "b"));
		Fields.writeDeclaredField(publiclyShadowedChild, "i", Integer.valueOf(0));
		assertEquals(Integer.valueOf(0), Fields.readDeclaredField(publiclyShadowedChild, "i"));
		Fields.writeDeclaredField(publiclyShadowedChild, "d", Double.valueOf(0.0));
		assertEquals(Double.valueOf(0.0), Fields.readDeclaredField(publiclyShadowedChild, "d"));

		try {
			Fields.writeDeclaredField(privatelyShadowedChild, "s", "S");
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(privatelyShadowedChild, "b", Boolean.TRUE);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(privatelyShadowedChild, "i", Integer.valueOf(1));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(privatelyShadowedChild, "d", Double.valueOf(1.0));
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
	}

	@Test
	public void testWriteDeclaredNamedFieldForceAccess() throws Exception {
		try {
			Fields.writeDeclaredField(publicChild, "s", "S", true);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(publicChild, "b", Boolean.TRUE, true);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(publicChild, "i", Integer.valueOf(1), true);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}
		try {
			Fields.writeDeclaredField(publicChild, "d", Double.valueOf(1.0), true);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e) {
			// pass
		}

		Fields.writeDeclaredField(publiclyShadowedChild, "s", "S", true);
		assertEquals("S", Fields.readDeclaredField(publiclyShadowedChild, "s", true));
		Fields.writeDeclaredField(publiclyShadowedChild, "b", Boolean.FALSE, true);
		assertEquals(Boolean.FALSE, Fields.readDeclaredField(publiclyShadowedChild, "b", true));
		Fields.writeDeclaredField(publiclyShadowedChild, "i", Integer.valueOf(0), true);
		assertEquals(Integer.valueOf(0), Fields.readDeclaredField(publiclyShadowedChild, "i", true));
		Fields.writeDeclaredField(publiclyShadowedChild, "d", Double.valueOf(0.0), true);
		assertEquals(Double.valueOf(0.0), Fields.readDeclaredField(publiclyShadowedChild, "d", true));

		Fields.writeDeclaredField(privatelyShadowedChild, "s", "S", true);
		assertEquals("S", Fields.readDeclaredField(privatelyShadowedChild, "s", true));
		Fields.writeDeclaredField(privatelyShadowedChild, "b", Boolean.FALSE, true);
		assertEquals(Boolean.FALSE, Fields.readDeclaredField(privatelyShadowedChild, "b", true));
		Fields.writeDeclaredField(privatelyShadowedChild, "i", Integer.valueOf(0), true);
		assertEquals(Integer.valueOf(0), Fields.readDeclaredField(privatelyShadowedChild, "i", true));
		Fields.writeDeclaredField(privatelyShadowedChild, "d", Double.valueOf(0.0), true);
		assertEquals(Double.valueOf(0.0), Fields.readDeclaredField(privatelyShadowedChild, "d", true));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAmbig() {
		Fields.getField(Ambig.class, "VALUE");
	}

	@Test
	public void testRemoveFinalModifier() throws Exception {
		Field field = StaticContainer.class.getDeclaredField("IMMUTABLE_PRIVATE_2");
		assertFalse(field.isAccessible());
		assertTrue(Modifier.isFinal(field.getModifiers()));
		Fields.removeFinalModifier(field);
		// The field is no longer final
		assertFalse(Modifier.isFinal(field.getModifiers()));
		assertFalse(field.isAccessible());
	}

	@Test
	public void testRemoveFinalModifierWithAccess() throws Exception {
		Field field = StaticContainer.class.getDeclaredField("IMMUTABLE_PRIVATE_2");
		assertFalse(field.isAccessible());
		assertTrue(Modifier.isFinal(field.getModifiers()));
		Fields.removeFinalModifier(field, true);
		// The field is no longer final
		assertFalse(Modifier.isFinal(field.getModifiers()));
		assertFalse(field.isAccessible());
	}

	@Test
	public void testRemoveFinalModifierWithoutAccess() throws Exception {
		Field field = StaticContainer.class.getDeclaredField("IMMUTABLE_PRIVATE_2");
		assertFalse(field.isAccessible());
		assertTrue(Modifier.isFinal(field.getModifiers()));
		Fields.removeFinalModifier(field, false);
		// The field is STILL final because we did not force access
		assertTrue(Modifier.isFinal(field.getModifiers()));
		assertFalse(field.isAccessible());
	}

	@Test
	public void testRemoveFinalModifierAccessNotNeeded() throws Exception {
		Field field = StaticContainer.class.getDeclaredField("IMMUTABLE_PACKAGE");
		assertFalse(field.isAccessible());
		assertTrue(Modifier.isFinal(field.getModifiers()));
		Fields.removeFinalModifier(field, false);
		// The field is no longer final AND we did not need to force access
		assertTrue(Modifier.isFinal(field.getModifiers()));
		assertFalse(field.isAccessible());
	}

}
