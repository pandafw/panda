package panda.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Strings;

/**
 * Utilities for working with {@link Field}s by reflection. Adapted and refactored from the dormant
 * [reflect] Commons sandbox component.
 * <p>
 * The ability is provided to break the scoping restrictions coded by the programmer. This can allow
 * fields to be changed that shouldn't be. This facility should be used with care.
 */
public abstract class Fields {
	/**
	 * @param field field
	 * @return generic type or class
	 */
	public static Type getFieldType(Field field) {
		Type type = field.getGenericType();
		if (type == null) {
			type = field.getType();
		}
		return type;
	}
	
	/**
	 * Gets an accessible {@link Field} by name respecting scope. Superclasses/interfaces will be
	 * considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @return the Field object
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty
	 */
	public static Field getField(final Class<?> cls, final String fieldName) {
		final Field field = getField(cls, fieldName, false);
		Members.setAccessibleWorkaround(field);
		return field;
	}

	/**
	 * Gets an accessible {@link Field} by name, breaking scope if requested.
	 * Superclasses/interfaces will be considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @return the Field object
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty or is matched at multiple places in the inheritance hierarchy
	 */
	public static Field getField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
		Asserts.isTrue(cls != null, "The class must not be null");
		Asserts.isTrue(Strings.isNotBlank(fieldName), "The field name must not be blank/empty");
		// Sun Java 1.3 has a bugged implementation of getField hence we write the
		// code ourselves

		// getField() will return the Field object with the declaring class
		// set correctly to the class that declares the field. Thus requesting the
		// field on a subclass will return the field from the superclass.
		//
		// priority order for lookup:
		// searchclass private/protected/package/public
		// superclass protected/package/public
		// private/different package blocks access to further superclasses
		// implementedinterface public

		// check up the superclass hierarchy
		for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
			try {
				final Field field = acls.getDeclaredField(fieldName);
				// getDeclaredField checks for non-public scopes as well
				// and it returns accurate results
				if (!Modifier.isPublic(field.getModifiers())) {
					if (forceAccess) {
						field.setAccessible(true);
					}
					else {
						continue;
					}
				}
				return field;
			}
			catch (final NoSuchFieldException ex) { // NOPMD
				// ignore
			}
		}
		// check the public interface case. This must be manually searched for
		// incase there is a public supersuperclass field hidden by a private/package
		// superclass field.
		Field match = null;
		for (final Class<?> class1 : Classes.getAllInterfaces(cls)) {
			try {
				final Field test = class1.getField(fieldName);
				Asserts.isTrue(match == null, "Reference to field %s is ambiguous relative to %s"
						+ "; a matching field exists on two or more implemented interfaces.", fieldName, cls);
				match = test;
			}
			catch (final NoSuchFieldException ex) { // NOPMD
				// ignore
			}
		}
		return match;
	}

	/**
	 * Gets an accessible {@link Field} by name respecting scope. Only the specified class will be
	 * considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @return the Field object
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty
	 */
	public static Field getDeclaredField(final Class<?> cls, final String fieldName) {
		return getDeclaredField(cls, fieldName, false);
	}

	/**
	 * Gets an accessible {@link Field} by name, breaking scope if requested. Only the specified
	 * class will be considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @return the Field object
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty
	 */
	public static Field getDeclaredField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
		Asserts.isTrue(cls != null, "The class must not be null");
		Asserts.isTrue(Strings.isNotBlank(fieldName), "The field name must not be blank/empty");
		try {
			// only consider the specified class by using getDeclaredField()
			final Field field = cls.getDeclaredField(fieldName);
			if (!Members.isAccessible(field)) {
				if (forceAccess) {
					field.setAccessible(true);
				}
				else {
					return null;
				}
			}
			return field;
		}
		catch (final NoSuchFieldException e) { // NOPMD
			// ignore
		}
		return null;
	}

	/**
	 * get all annotation fields of the class and it's super class
	 * Discard duplicate field of parents.
	 * 
	 * @param cls the class
	 * @param ann the annotation
	 * @return fields
	 */
	public static <A extends Annotation> List<Field> getAnnotationFields(Class<?> cls, Class<A> ann) {
		List<Field> fields = new ArrayList<Field>();
		for (Field f : getDeclaredFields(cls)) {
			if (f.isAnnotationPresent(ann)) {
				fields.add(f);
			}
		}
		return fields;
	}

	/**
	 * Return all fields of the class and it's super class (exclude the Object class). <br>
	 * Discard duplicate field of parents.
	 * 
	 * @param cls the class
	 * @return field list
	 */
	public static Collection<Field> getDeclaredFields(Class<?> cls) {
		return _getFields(cls, true, false, true, true);
	}

	/**
	 * Return all static fields of the class and it's super class (exclude the Object class). <br>
	 * Discard duplicate field of parents.
	 * 
	 * @param cls the class
	 * @param noFinal include final fields
	 * @return field list
	 */
	public static Collection<Field> getStaticFields(Class<?> cls, boolean noFinal) {
		return _getFields(cls, false, true, noFinal, true);
	}

	private static Collection<Field> _getFields(Class<?> cc, boolean noStatic, boolean noMember, boolean noFinal, boolean noInner) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		while (null != cc && cc != Object.class) {
			Field[] fs = cc.getDeclaredFields();
			for (Field f : fs) {
				int m = f.getModifiers();
				if (noStatic && Modifier.isStatic(m)) {
					continue;
				}
				if (noFinal && Modifier.isFinal(m)) {
					continue;
				}
				if (noInner && f.isSynthetic()) {
					continue;
				}
				if (noMember && !Modifier.isStatic(m)) {
					continue;
				}

				if (map.containsKey(f.getName())) {
					continue;
				}

				map.put(f.getName(), f);
			}
			cc = cc.getSuperclass();
		}
		return map.values();
	}

	/**
	 * Gets all fields of the given class and its parents (if any).
	 * 
	 * @param cls the {@link Class} to query
	 * @return an array of Fields (possibly empty).
	 * @throws IllegalArgumentException if the class is {@code null}
	 */
	public static Field[] getAllFields(Class<?> cls) {
		final List<Field> allFieldsList = getAllFieldsList(cls);
		return allFieldsList.toArray(new Field[allFieldsList.size()]);
	}

	/**
	 * Gets all fields of the given class and its parents (if any).
	 * 
	 * @param cls the {@link Class} to query
	 * @return an array of Fields (possibly empty).
	 * @throws IllegalArgumentException if the class is {@code null}
	 */
	public static List<Field> getAllFieldsList(Class<?> cls) {
		Asserts.isTrue(cls != null, "The class must not be null");
		final List<Field> allFields = new ArrayList<Field>();
		Class<?> currentClass = cls;
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			for (Field field : declaredFields) {
				allFields.add(field);
			}
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}

	/**
	 * Reads an accessible {@code static} {@link Field}.
	 * 
	 * @param field to read
	 * @return the field value
	 * @throws IllegalArgumentException if the field is {@code null}, or not {@code static}
	 * @throws IllegalAccessException if the field is not accessible
	 */
	public static Object readStaticField(final Field field) throws IllegalAccessException {
		return readStaticField(field, false);
	}

	/**
	 * Reads a static {@link Field}.
	 * 
	 * @param field to read
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 * @return the field value
	 * @throws IllegalArgumentException if the field is {@code null} or not {@code static}
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static Object readStaticField(final Field field, final boolean forceAccess) throws IllegalAccessException {
		Asserts.isTrue(field != null, "The field must not be null");
		Asserts.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
		return readField(field, (Object)null, forceAccess);
	}

	/**
	 * Reads the named {@code public static} {@link Field}. Superclasses will be considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @return the value of the field
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty, is not {@code static}, or could not be found
	 * @throws IllegalAccessException if the field is not accessible
	 */
	public static Object readStaticField(final Class<?> cls, final String fieldName) throws IllegalAccessException {
		return readStaticField(cls, fieldName, false);
	}

	/**
	 * Reads the named {@code static} {@link Field}. Superclasses will be considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @return the Field object
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty, is not {@code static}, or could not be found
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static Object readStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess)
			throws IllegalAccessException {
		final Field field = getField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate field '%s' on %s", fieldName, cls);
		// already forced access above, don't repeat it here:
		return readStaticField(field, false);
	}

	/**
	 * Gets the value of a {@code static} {@link Field} by name. The field must be {@code public}.
	 * Only the specified class will be considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @return the value of the field
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty, is not {@code static}, or could not be found
	 * @throws IllegalAccessException if the field is not accessible
	 */
	public static Object readDeclaredStaticField(final Class<?> cls, final String fieldName)
			throws IllegalAccessException {
		return readDeclaredStaticField(cls, fieldName, false);
	}

	/**
	 * Gets the value of a {@code static} {@link Field} by name. Only the specified class will be
	 * considered.
	 * 
	 * @param cls the {@link Class} to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @return the Field object
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty, is not {@code static}, or could not be found
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static Object readDeclaredStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess)
			throws IllegalAccessException {
		final Field field = getDeclaredField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
		// already forced access above, don't repeat it here:
		return readStaticField(field, false);
	}

	/**
	 * Reads an accessible {@link Field}.
	 * 
	 * @param field the field to use
	 * @param target the object to call on, may be {@code null} for {@code static} fields
	 * @return the field value
	 * @throws IllegalArgumentException if the field is {@code null}
	 * @throws IllegalAccessException if the field is not accessible
	 */
	public static Object readField(final Field field, final Object target) throws IllegalAccessException {
		return readField(field, target, false);
	}

	/**
	 * Reads a {@link Field}.
	 * 
	 * @param field the field to use
	 * @param target the object to call on, may be {@code null} for {@code static} fields
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 * @return the field value
	 * @throws IllegalArgumentException if the field is {@code null}
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static Object readField(final Field field, final Object target, final boolean forceAccess)
			throws IllegalAccessException {
		Asserts.isTrue(field != null, "The field must not be null");
		if (forceAccess && !field.isAccessible()) {
			field.setAccessible(true);
		}
		else {
			Members.setAccessibleWorkaround(field);
		}
		return field.get(target);
	}

	/**
	 * Reads the named {@code public} {@link Field}. Superclasses will be considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @return the value of the field
	 * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or
	 *             empty or could not be found
	 * @throws IllegalAccessException if the named field is not {@code public}
	 */
	public static Object readField(final Object target, final String fieldName) throws IllegalAccessException {
		return readField(target, fieldName, false);
	}

	/**
	 * Reads the named {@link Field}. Superclasses will be considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @return the field value
	 * @throws IllegalArgumentException if {@code target} is {@code null}, or the field name is
	 *             blank or empty or could not be found
	 * @throws IllegalAccessException if the named field is not made accessible
	 */
	public static Object readField(final Object target, final String fieldName, final boolean forceAccess)
			throws IllegalAccessException {
		Asserts.isTrue(target != null, "target object must not be null");
		final Class<?> cls = target.getClass();
		final Field field = getField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
		// already forced access above, don't repeat it here:
		return readField(field, target, false);
	}

	/**
	 * Reads the named {@code public} {@link Field}. Only the class of the specified object will be
	 * considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @return the value of the field
	 * @throws IllegalArgumentException if {@code target} is {@code null}, or the field name is
	 *             blank or empty or could not be found
	 * @throws IllegalAccessException if the named field is not {@code public}
	 */
	public static Object readDeclaredField(final Object target, final String fieldName) throws IllegalAccessException {
		return readDeclaredField(target, fieldName, false);
	}

	/**
	 * <p<>Gets a {@link Field} value by name. Only the class of the specified object will be
	 * considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match public fields.
	 * @return the Field object
	 * @throws IllegalArgumentException if {@code target} is {@code null}, or the field name is
	 *             blank or empty or could not be found
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static Object readDeclaredField(final Object target, final String fieldName, final boolean forceAccess)
			throws IllegalAccessException {
		Asserts.isTrue(target != null, "target object must not be null");
		final Class<?> cls = target.getClass();
		final Field field = getDeclaredField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate declared field %s.%s", cls, fieldName);
		// already forced access above, don't repeat it here:
		return readField(field, target, false);
	}

	/**
	 * Writes a {@code public static} {@link Field}.
	 * 
	 * @param field to write
	 * @param value to set
	 * @throws IllegalArgumentException if the field is {@code null} or not {@code static}, or
	 *             {@code value} is not assignable
	 * @throws IllegalAccessException if the field is not {@code public} or is {@code final}
	 */
	public static void writeStaticField(final Field field, final Object value) throws IllegalAccessException {
		writeStaticField(field, value, false);
	}

	/**
	 * Writes a static {@link Field}.
	 * 
	 * @param field to write
	 * @param value to set
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @throws IllegalArgumentException if the field is {@code null} or not {@code static}, or
	 *             {@code value} is not assignable
	 * @throws IllegalAccessException if the field is not made accessible or is {@code final}
	 */
	public static void writeStaticField(final Field field, final Object value, final boolean forceAccess)
			throws IllegalAccessException {
		Asserts.isTrue(field != null, "The field must not be null");
		Asserts.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", field
			.getDeclaringClass().getName(), field.getName());
		writeField(field, (Object)null, value, forceAccess);
	}

	/**
	 * Writes a named {@code public static} {@link Field}. Superclasses will be considered.
	 * 
	 * @param cls {@link Class} on which the field is to be found
	 * @param fieldName to write
	 * @param value to set
	 * @throws IllegalArgumentException if {@code cls} is {@code null}, the field name is blank or
	 *             empty, the field cannot be located or is not {@code static}, or {@code value} is
	 *             not assignable
	 * @throws IllegalAccessException if the field is not {@code public} or is {@code final}
	 */
	public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value)
			throws IllegalAccessException {
		writeStaticField(cls, fieldName, value, false);
	}

	/**
	 * Writes a named {@code static} {@link Field}. Superclasses will be considered.
	 * 
	 * @param cls {@link Class} on which the field is to be found
	 * @param fieldName to write
	 * @param value to set
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @throws IllegalArgumentException if {@code cls} is {@code null}, the field name is blank or
	 *             empty, the field cannot be located or is not {@code static}, or {@code value} is
	 *             not assignable
	 * @throws IllegalAccessException if the field is not made accessible or is {@code final}
	 */
	public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value,
			final boolean forceAccess) throws IllegalAccessException {
		final Field field = getField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
		// already forced access above, don't repeat it here:
		writeStaticField(field, value, false);
	}

	/**
	 * Writes a named {@code public static} {@link Field}. Only the specified class will be
	 * considered.
	 * 
	 * @param cls {@link Class} on which the field is to be found
	 * @param fieldName to write
	 * @param value to set
	 * @throws IllegalArgumentException if {@code cls} is {@code null}, the field name is blank or
	 *             empty, the field cannot be located or is not {@code static}, or {@code value} is
	 *             not assignable
	 * @throws IllegalAccessException if the field is not {@code public} or is {@code final}
	 */
	public static void writeDeclaredStaticField(final Class<?> cls, final String fieldName, final Object value)
			throws IllegalAccessException {
		writeDeclaredStaticField(cls, fieldName, value, false);
	}

	/**
	 * Writes a named {@code static} {@link Field}. Only the specified class will be considered.
	 * 
	 * @param cls {@link Class} on which the field is to be found
	 * @param fieldName to write
	 * @param value to set
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@code AccessibleObject#setAccessible(boolean)} method. {@code false} will only
	 *            match {@code public} fields.
	 * @throws IllegalArgumentException if {@code cls} is {@code null}, the field name is blank or
	 *             empty, the field cannot be located or is not {@code static}, or {@code value} is
	 *             not assignable
	 * @throws IllegalAccessException if the field is not made accessible or is {@code final}
	 */
	public static void writeDeclaredStaticField(final Class<?> cls, final String fieldName, final Object value,
			final boolean forceAccess) throws IllegalAccessException {
		final Field field = getDeclaredField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
		// already forced access above, don't repeat it here:
		writeField(field, (Object)null, value, false);
	}

	/**
	 * Writes an accessible {@link Field}.
	 * 
	 * @param field to write
	 * @param target the object to call on, may be {@code null} for {@code static} fields
	 * @param value to set
	 * @throws IllegalAccessException if the field or target is {@code null}, the field is not
	 *             accessible or is {@code final}, or {@code value} is not assignable
	 */
	public static void writeField(final Field field, final Object target, final Object value)
			throws IllegalAccessException {
		writeField(field, target, value, false);
	}

	/**
	 * Writes a {@link Field}.
	 * 
	 * @param field to write
	 * @param target the object to call on, may be {@code null} for {@code static} fields
	 * @param value to set
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @throws IllegalArgumentException if the field is {@code null} or {@code value} is not
	 *             assignable
	 * @throws IllegalAccessException if the field is not made accessible or is {@code final}
	 */
	public static void writeField(final Field field, final Object target, final Object value, final boolean forceAccess)
			throws IllegalAccessException {
		Asserts.isTrue(field != null, "The field must not be null");
		if (forceAccess && !field.isAccessible()) {
			field.setAccessible(true);
		}
		else {
			Members.setAccessibleWorkaround(field);
		}
		field.set(target, value);
	}

	/**
	 * Removes the final modifier from a {@link Field}.
	 * 
	 * @param field to remove the final modifier
	 * @throws IllegalArgumentException if the field is {@code null}
	 */
	public static void removeFinalModifier(Field field) {
		removeFinalModifier(field, true);
	}

	/**
	 * Removes the final modifier from a {@link Field}.
	 * 
	 * @param field to remove the final modifier
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @throws IllegalArgumentException if the field is {@code null}
	 */
	public static void removeFinalModifier(Field field, boolean forceAccess) {
		Asserts.isTrue(field != null, "The field must not be null");

		try {
			if (Modifier.isFinal(field.getModifiers())) {
				// Do all JREs implement Field with a private ivar called "modifiers"?
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				final boolean doForceAccess = forceAccess && !modifiersField.isAccessible();
				if (doForceAccess) {
					modifiersField.setAccessible(true);
				}
				try {
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				}
				finally {
					if (doForceAccess) {
						modifiersField.setAccessible(false);
					}
				}
			}
		}
		catch (NoSuchFieldException ignored) {
			// The field class contains always a modifiers field
		}
		catch (IllegalAccessException ignored) {
			// The modifiers field is made accessible
		}
	}

	/**
	 * Writes a {@code public} {@link Field}. Superclasses will be considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param value to set
	 * @throws IllegalArgumentException if {@code target} is {@code null}, {@code fieldName} is
	 *             blank or empty or could not be found, or {@code value} is not assignable
	 * @throws IllegalAccessException if the field is not accessible
	 */
	public static void writeField(final Object target, final String fieldName, final Object value)
			throws IllegalAccessException {
		writeField(target, fieldName, value, false);
	}

	/**
	 * Writes a {@link Field}. Superclasses will be considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param value to set
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @throws IllegalArgumentException if {@code target} is {@code null}, {@code fieldName} is
	 *             blank or empty or could not be found, or {@code value} is not assignable
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static void writeField(final Object target, final String fieldName, final Object value,
			final boolean forceAccess) throws IllegalAccessException {
		Asserts.isTrue(target != null, "target object must not be null");
		final Class<?> cls = target.getClass();
		final Field field = getField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
		// already forced access above, don't repeat it here:
		writeField(field, target, value, false);
	}

	/**
	 * Writes a {@code public} {@link Field}. Only the specified class will be considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param value to set
	 * @throws IllegalArgumentException if {@code target} is {@code null}, {@code fieldName} is
	 *             blank or empty or could not be found, or {@code value} is not assignable
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static void writeDeclaredField(final Object target, final String fieldName, final Object value)
			throws IllegalAccessException {
		writeDeclaredField(target, fieldName, value, false);
	}

	/**
	 * Writes a {@code public} {@link Field}. Only the specified class will be considered.
	 * 
	 * @param target the object to reflect, must not be {@code null}
	 * @param fieldName the field name to obtain
	 * @param value to set
	 * @param forceAccess whether to break scope restrictions using the
	 *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
	 *            {@code false} will only match {@code public} fields.
	 * @throws IllegalArgumentException if {@code target} is {@code null}, {@code fieldName} is
	 *             blank or empty or could not be found, or {@code value} is not assignable
	 * @throws IllegalAccessException if the field is not made accessible
	 */
	public static void writeDeclaredField(final Object target, final String fieldName, final Object value,
			final boolean forceAccess) throws IllegalAccessException {
		Asserts.isTrue(target != null, "target object must not be null");
		final Class<?> cls = target.getClass();
		final Field field = getDeclaredField(cls, fieldName, forceAccess);
		Asserts.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
		// already forced access above, don't repeat it here:
		writeField(field, target, value, false);
	}

	/**
	 * transient field
	 * 
	 * @param field field
	 * @return true - field is transient
	 */
	public static boolean isTransientField(Field field) {
		return (field.getModifiers() & Modifier.TRANSIENT) == Modifier.TRANSIENT;
	}
}
