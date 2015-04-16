package panda.lang.reflect.testbed;

public class StaticContainer {
	public static final Object IMMUTABLE_PUBLIC = "public";
	protected static final Object IMMUTABLE_PROTECTED = "protected";
	static final Object IMMUTABLE_PACKAGE = "";
	@SuppressWarnings("unused")
	private static final Object IMMUTABLE_PRIVATE = "private";

	/**
	 * This final modifier of this field is meant to be removed by a test. Using this field may
	 * produce unpredictable results.
	 */
	@SuppressWarnings("unused")
	private static final Object IMMUTABLE_PRIVATE_2 = "private";

	public static Object mutablePublic;
	protected static Object mutableProtected;
	static Object mutablePackage;
	private static Object mutablePrivate;

	public static void reset() {
		mutablePublic = null;
		mutableProtected = null;
		mutablePackage = null;
		mutablePrivate = null;
	}

	public static Object getMutableProtected() {
		return mutableProtected;
	}

	public static Object getMutablePackage() {
		return mutablePackage;
	}

	public static Object getMutablePrivate() {
		return mutablePrivate;
	}
}
