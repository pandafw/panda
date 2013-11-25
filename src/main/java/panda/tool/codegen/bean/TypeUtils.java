package panda.tool.codegen.bean;

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

import panda.exts.fileupload.UploadFile;
import panda.exts.fileupload.UploadImage;
import panda.lang.Classes;
import panda.lang.Strings;

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
				simpleType = Classes.getSimpleClassName(simpleType);
			}
		}

		if (simpleType == null) {
			simpleType = Classes.getSimpleClassName(type);
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

	private static Map<String, String> ALIAS_MAP = new HashMap<String, String>();

	static {
		ALIAS_MAP.put("bool", Boolean.class.getName());
		ALIAS_MAP.put("boolean", Boolean.class.getName());
		ALIAS_MAP.put("Boolean", Boolean.class.getName());
		ALIAS_MAP.put("byte", Byte.class.getName());
		ALIAS_MAP.put("Byte", Byte.class.getName());
		ALIAS_MAP.put("char", Character.class.getName());
		ALIAS_MAP.put("character", Character.class.getName());
		ALIAS_MAP.put("Character", Character.class.getName());
		ALIAS_MAP.put("double", Double.class.getName());
		ALIAS_MAP.put("Double", Double.class.getName());
		ALIAS_MAP.put("float", Float.class.getName());
		ALIAS_MAP.put("Float", Float.class.getName());
		ALIAS_MAP.put("int", Integer.class.getName());
		ALIAS_MAP.put("integer", Integer.class.getName());
		ALIAS_MAP.put("Integer", Integer.class.getName());
		ALIAS_MAP.put("long", Long.class.getName());
		ALIAS_MAP.put("Long", Long.class.getName());
		ALIAS_MAP.put("short", Long.class.getName());
		ALIAS_MAP.put("Short", Long.class.getName());

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
		ALIAS_MAP.put("uploadfile", UploadFile.class.getName());
		ALIAS_MAP.put("UploadFile", UploadFile.class.getName());
		ALIAS_MAP.put("uploadimage", UploadImage.class.getName());
		ALIAS_MAP.put("UploadImage", UploadImage.class.getName());
	}

	private static String classByAlias(String type) {
		return ALIAS_MAP.get(type);
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
