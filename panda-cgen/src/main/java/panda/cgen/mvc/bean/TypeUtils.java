package panda.cgen.mvc.bean;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.lang.Classes;
import panda.lang.Strings;
import panda.vfs.FileItem;

/**
 * Utilities of Java Type
 */
public class TypeUtils {
	/**
	 * check java type
	 * @param type type
	 * @param name name
	 */
	private static void checkJavaType(String type, String name) {
		if (Strings.isEmpty(type)) {
			throw new IllegalArgumentException("Empty java type for property [" + name + "]");
		}
	}

	/**
	 * @param type type
	 * @return generic java type
	 */
	public static String getGenericJavaType(String type) {
		if (type.endsWith(">")) {
			int lt = type.indexOf('<');
			if (lt > 0) {
				return type.substring(0, lt);
			}
		}
		return type;
	}
	
	/**
	 * @param type type
	 * @param name name
	 * @return native java type
	 */
	public static String getNativeJavaType(String type, String name) {
		type = getFullJavaType(type, name);

		try {
			@SuppressWarnings("rawtypes")
			Class cls = Classes.getClass(type);
			return cls.getName();
		}
		catch (ClassNotFoundException e) {

		}

		if (type.endsWith("[]")) {
			type = type.substring(0, type.length() - 2);
			type = "[L" + getGenericJavaType(type) + ";";
		}
		else {
			type = getGenericJavaType(type);
		}

		return type;
	}

	/**
	 * getFullJavaType
	 * @param value value
	 * @param name name
	 * @return full java type
	 */
	public static String getFullJavaType(String value, String name) {
		checkJavaType(value, name);
		String type = getFullJavaType(value);
		checkJavaType(type, name);
		return type;
	}
	
	/**
	 * getFullJavaType
	 * @param value value
	 * @return full java type
	 */
	private static String getFullJavaType(String value) {
		boolean array = value.endsWith(Classes.ARRAY_SUFFIX);

		String type = value;
		if (array) {
			type = type.substring(0, type.length() - Classes.ARRAY_SUFFIX.length());
		}

		List<String> ctl = null;
		if (type.endsWith(">")) {
			int lt = type.indexOf('<');
			if (lt > 0) {
				String[] cts = Strings.split(type.substring(lt + 1, type.length() - 1), ", ");
				ctl = new ArrayList<String>(cts.length);
				for (String ct : cts) {
					ctl.add(getFullJavaType(ct));
				}
				type = type.substring(0, lt);
			}
		}

		String fullType = null;
		if (type.indexOf('.') == -1) {
			fullType = classByAlias(type);

			if (fullType == null) {
				fullType = classByName(type);
			}
			// if (fullType == null) {
			// fullType = classByName("java.lang." + type);
			// }
			// if (fullType == null) {
			// fullType = classByName("java.math." + type);
			// }
			// if (fullType == null) {
			// fullType = classByName("java.io." + type);
			// }
			// if (fullType == null) {
			// fullType = classByName("java.util." + type);
			// }
			// if (fullType == null) {
			// fullType = classByName(UploadFile.class.getClassLoader(),
			// ClassUtils.getPackageName(UploadFile.class) + "." + type);
			// }
		}

		if (fullType == null) {
			fullType = type;
		}

		if (ctl != null) {
			fullType += '<' + Strings.join(ctl, ", ") + '>';
		}

		if (array) {
			fullType += Classes.ARRAY_SUFFIX;
		}

		if (fullType.equals("java.lang.Byte[]")) {
			fullType = "byte[]";
		}
		return fullType;
	}

	/**
	 * @param value value
	 * @param name name
	 * @return simple generic java type
	 */
	public static String getSimpleGenericJavaType(String value, String name) {
		String type = getSimpleJavaType(value, name);
		type = getGenericJavaType(type);
		return type;
	}

	/**
	 * getSimpleJavaType
	 * @param value value
	 * @param name name
	 * @return simple java type
	 */
	public static String getSimpleJavaType(String value, String name) {
		String type = getSimpleJavaType(value);
		checkJavaType(type, name);
		return type;
	}

	public static String getElementType(String value, String name) {
		String type = getSimpleJavaType(value, name);
		if (Strings.endsWith(type, "[]")) {
			return Strings.removeEnd(type, "[]");
		}
		if (Strings.endsWithChar(type, '>')) {
			return Strings.substringBefore(type, '<');
		}
		return type;
	}

	/**
	 * getSimpleJavaWrapType
	 * @param value value
	 * @param name name
	 * @return simple java type
	 */
	public static String getSimpleJavaWrapType(String value, String name) {
		String type = getSimpleJavaType(value, name);
		return toSimpleWrap(type);
	}
	
	/**
	 * getSimpleJavaType
	 * @param value value
	 * @return simple java type
	 */
	private static String getSimpleJavaType(String value) {
		boolean array = value.endsWith(Classes.ARRAY_SUFFIX);

		String type = value;
		if (array) {
			type = type.substring(0, type.length() - Classes.ARRAY_SUFFIX.length());
		}

		List<String> ctl = null;
		if (type.endsWith(">")) {
			int lt = type.indexOf('<');
			if (lt > 0) {
				String[] cts = Strings.split(type.substring(lt + 1, type.length() - 1), ", ");
				ctl = new ArrayList<String>(cts.length);
				for (String ct : cts) {
					ctl.add(getSimpleJavaType(ct));
				}
				type = type.substring(0, lt);
			}
		}

		String simpleType = null;
		if (type.indexOf('.') == -1) {
			simpleType = classByAlias(type);

			if (simpleType == null) {
				simpleType = classByName(type);
			}

			// if (simpleType == null) {
			// simpleType = classByName("java.lang." + type);
			// }
			// if (simpleType == null) {
			// simpleType = classByName("java.math." + type);
			// }
			// if (simpleType == null) {
			// simpleType = classByName("java.io." + type);
			// }
			// if (simpleType == null) {
			// simpleType = classByName("java.util." + type);
			// }
			// if (simpleType == null) {
			// simpleType = classByName(ClassUtils.getPackageName(UploadFile.class) + "."
			// + type);
			// }

			if (simpleType != null) {
				simpleType = Classes.getShortClassName(simpleType);
			}
		}

		if (simpleType == null) {
			simpleType = Classes.getShortClassName(type);
		}

		if (ctl != null) {
			simpleType += '<' + Strings.join(ctl, ", ") + '>';
		}

		if (array) {
			simpleType += Classes.ARRAY_SUFFIX;
			if (simpleType.equals("Byte[]")) {
				simpleType = "byte[]";
			}
		}

		return simpleType;
	}

	private final static Map<String, String> ALIAS_MAP = new HashMap<String, String>();
	static {
		ALIAS_MAP.put("bool", boolean.class.getName());
		ALIAS_MAP.put("boolean", boolean.class.getName());
		ALIAS_MAP.put("Boolean", Boolean.class.getName());
		ALIAS_MAP.put("byte", byte.class.getName());
		ALIAS_MAP.put("Byte", Byte.class.getName());
		ALIAS_MAP.put("char", char.class.getName());
		ALIAS_MAP.put("character", char.class.getName());
		ALIAS_MAP.put("Character", Character.class.getName());
		ALIAS_MAP.put("double", double.class.getName());
		ALIAS_MAP.put("Double", Double.class.getName());
		ALIAS_MAP.put("float", float.class.getName());
		ALIAS_MAP.put("Float", Float.class.getName());
		ALIAS_MAP.put("int", int.class.getName());
		ALIAS_MAP.put("integer", int.class.getName());
		ALIAS_MAP.put("Integer", Integer.class.getName());
		ALIAS_MAP.put("long", long.class.getName());
		ALIAS_MAP.put("Long", Long.class.getName());
		ALIAS_MAP.put("short", short.class.getName());
		ALIAS_MAP.put("Short", Short.class.getName());

		ALIAS_MAP.put("string", String.class.getName());
		ALIAS_MAP.put("String", String.class.getName());

		ALIAS_MAP.put("number", Number.class.getName());
		ALIAS_MAP.put("Number", Number.class.getName());
		ALIAS_MAP.put("decimal", BigDecimal.class.getName());
		ALIAS_MAP.put("Decimal", BigDecimal.class.getName());
		ALIAS_MAP.put("bigdecimal", BigDecimal.class.getName());
		ALIAS_MAP.put("BigDecimal", BigDecimal.class.getName());
		ALIAS_MAP.put("biginteger", BigInteger.class.getName());
		ALIAS_MAP.put("BigInteger", BigInteger.class.getName());

		ALIAS_MAP.put("date", Date.class.getName());
		ALIAS_MAP.put("Date", Date.class.getName());
		ALIAS_MAP.put("calendar", Calendar.class.getName());
		ALIAS_MAP.put("Calendar", Calendar.class.getName());

		ALIAS_MAP.put("collection", Collection.class.getName());
		ALIAS_MAP.put("Collection", Collection.class.getName());
		ALIAS_MAP.put("list", List.class.getName());
		ALIAS_MAP.put("List", List.class.getName());
		ALIAS_MAP.put("Set", Set.class.getName());
		ALIAS_MAP.put("set", Set.class.getName());
		ALIAS_MAP.put("map", Map.class.getName());
		ALIAS_MAP.put("Map", Map.class.getName());

		ALIAS_MAP.put("file", File.class.getName());
		ALIAS_MAP.put("File", File.class.getName());

		ALIAS_MAP.put("fileitem", FileItem.class.getName());
		ALIAS_MAP.put("FileItem", FileItem.class.getName());
	}
	private static String classByAlias(String type) {
		return ALIAS_MAP.get(type);
	}

	private final static Map<String, String> WRAP_MAP = new HashMap<String, String>();
	static {
		WRAP_MAP.put("boolean", "Boolean");
		WRAP_MAP.put("byte", "Byte");
		WRAP_MAP.put("char", "Character");
		WRAP_MAP.put("double", "Double");
		WRAP_MAP.put("float", "Float");
		WRAP_MAP.put("int", "Integer");
		WRAP_MAP.put("long", "Long");
		WRAP_MAP.put("short", "Short");
	}
	private static String toSimpleWrap(String type) {
		return WRAP_MAP.containsKey(type) ? WRAP_MAP.get(type) : type;
	}

	// private static String classByName(ClassLoader classLoader, String type) {
	// try {
	// Class cls = ClassUtils.getClass(classLoader, type);
	// if (cls.isPrimitive()) {
	// return ClassUtils.primitive2Wrapper(cls).getName();
	// }
	// else {
	// return cls.getName();
	// }
	// }
	// catch (ClassNotFoundException e) {
	// }
	//
	// return null;
	// }
	//
	private static String classByName(String type) {
		try {
			Class cls = Classes.getClass(type);
			if (cls.isPrimitive()) {
				return Classes.primitive2Wrapper(cls).getName();
			}
			else {
				return cls.getName();
			}
		}
		catch (ClassNotFoundException e) {
		}

		return null;
	}

}
