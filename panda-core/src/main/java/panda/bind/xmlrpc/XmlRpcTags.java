package panda.bind.xmlrpc;

public class XmlRpcTags {
	public final static String T_METHOD_CALL = "methodCall";
	
	public final static String T_METHOD_NAME = "methodName";
	
	public final static String T_METHOD_RESPONSE = "methodResponse";
	
	public final static String T_PARAMS = "params";
	
	public final static String T_PARAM = "param";
	
	public final static String T_FAULT = "fault";
	
	/** The tag name of value elements */
	public final static String T_VALUE = "value";

	/** The tag name of string elements */
	public final static String T_STRING = "string";

	/** The tag name of i4 elements */
	public final static String T_I4 = "i4";

	/** The tag name of i8, Apache elements */
	public final static String T_I8 = "i8";

	/** The tag name of String elements */
	public final static String T_INT = "int";

	/** The tag name of boolean elements */
	public final static String T_BOOLEAN = "boolean";

	/** The tag name of double elements */
	public final static String T_DOUBLE = "double";

	/** The tag name of double elements */
	public final static String T_DATE = "dateTime.iso8601";

	/** The tag name of double elements */
	public final static String T_BASE64 = "base64";

	/** The tag name of struct elements */
	public final static String T_STRUCT = "struct";

	/** The tag name of member elements */
	public final static String T_MEMBER = "member";

	/** The tag name of name elements */
	public final static String T_NAME = "name";

	/** The tag name of array elements */
	public final static String T_ARRAY = "array";

	/** The tag name of data elements */
	public final static String T_DATA = "data";

	/** The tag name of nil elements */
	public final static String T_NIL = "nil";

	//-----------------------------------------------------------
	public final static int I_METHOD_CALL = hash(T_METHOD_CALL);
	
	public final static int I_METHOD_NAME = hash(T_METHOD_NAME);
	
	public final static int I_METHOD_RESPONSE = hash(T_METHOD_RESPONSE);
	
	public final static int I_PARAMS = hash(T_PARAMS);
	
	public final static int I_PARAM = hash(T_PARAM);
	
	public final static int I_FAULT = hash(T_FAULT);
	
	
	/** The hash value of value elements */
	public final static int I_VALUE = hash(T_VALUE);

	/** The hash value of string elements */
	public final static int I_STRING = hash(T_STRING);

	/** The hash value of i4 elements */
	public final static int I_I4 = hash(T_I4);

	/** The hash value of i8, Apache elements */
	public final static int I_I8 = hash(T_I8);

	/** The hash value of int elements */
	public final static int I_INT = hash(T_INT);

	/** The hash value of boolean elements */
	public final static int I_BOOLEAN = hash(T_BOOLEAN);

	/** The hash value of double elements */
	public final static int I_DOUBLE = hash(T_DOUBLE);

	/** The hash value of double elements */
	public final static int I_DATE = hash(T_DATE);

	/** The hash value of double elements */
	public final static int I_BASE64 = hash(T_BASE64);

	/** The hash value of struct elements */
	public final static int I_STRUCT = hash(T_STRUCT);

	/** The hash value of member elements */
	public final static int I_MEMBER = hash(T_MEMBER);

	/** The hash value of name elements */
	public final static int I_NAME = hash(T_NAME);

	/** The hash value of array elements */
	public final static int I_ARRAY = hash(T_ARRAY);

	/** The hash value of data elements */
	public final static int I_DATA = hash(T_DATA);

	/** The hash value of nil elements */
	public final static int I_NIL = hash(T_NIL);

	/**
	 * Internal hashcode algorithm. This algorithm is used instead of the build-in hashCode() method
	 * of java.lang.String to ensure that that the same hash value is calculated in all JVMs. The
	 * code in this class switches execution based on has values rather than using the
	 * String.equals() call for each element. Hash values for the XML-RPC elements have been
	 * pre-calculated and are represented by the hash constants at the top of this file.
	 * 
	 * @param s The string to calculate a hash for.
	 * 
	 * @return the hash code
	 */
	public static int hash(String s) {
		int hash = 0;
		int length = s.length();

		for (int i = 0; i < length; ++i) {
			hash = 31 * hash + s.charAt(i);
		}

		return hash;
	}
	
	public static String name(int hash) {
		if (hash == I_METHOD_CALL) {
			return T_METHOD_CALL;
		}

		if (hash == I_METHOD_NAME) {
			return T_METHOD_NAME;
		}

		if (hash == I_METHOD_RESPONSE) {
			return T_METHOD_RESPONSE;
		}

		if (hash == I_PARAMS) {
			return T_PARAMS;
		}

		if (hash == I_PARAM) {
			return T_PARAM;
		}

		if (hash == I_FAULT) {
			return T_FAULT;
		}

		if (hash == I_VALUE) {
			return T_VALUE;
		}

		if (hash == I_STRING) {
			return T_STRING;
		}

		if (hash == I_I4) {
			return T_I4;
		}

		if (hash == I_I8) {
			return T_I8;
		}

		if (hash == I_INT) {
			return T_INT;
		}

		if (hash == I_BOOLEAN) {
			return T_BOOLEAN;
		}

		if (hash == I_DOUBLE) {
			return T_DOUBLE;
		}

		if (hash == I_DATE) {
			return T_DATE;
		}

		if (hash == I_BASE64) {
			return T_BASE64;
		}

		if (hash == I_STRUCT) {
			return T_STRUCT;
		}

		if (hash == I_MEMBER) {
			return T_MEMBER;
		}

		if (hash == I_NAME) {
			return T_NAME;
		}

		if (hash == I_ARRAY) {
			return T_ARRAY;
		}

		if (hash == I_DATA) {
			return T_DATA;
		}

		if (hash == I_NIL) {
			return T_NIL;
		}

		return "";
	}
}
