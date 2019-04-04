package panda.lang;

import java.io.File;
import java.io.PrintWriter;

import panda.io.stream.StringBuilderWriter;

/**
 * <p>
 * Helpers for {@code java.lang.System}.
 * </p>
 * <p>
 * If a system property cannot be read due to security restrictions, the corresponding field in this class will be set
 * to {@code null} and a message will be written to {@code System.err}.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 */
public class Systems {

	/**
	 * The prefix String for all Windows OS.
	 */
	private static final String OS_NAME_WINDOWS_PREFIX = "Windows";

	// System property constants
	// -----------------------------------------------------------------------
	// These MUST be declared first. Other constants depend on this.

	/**
	 * The System property key for the user home directory.
	 */
	private static final String USER_HOME_KEY = "user.home";

	/**
	 * The System property key for the user directory.
	 */
	private static final String USER_DIR_KEY = "user.dir";

	/**
	 * The System property key for the Java IO temporary directory.
	 */
	private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";

	/**
	 * The System property key for the Java home directory.
	 */
	private static final String JAVA_HOME_KEY = "java.home";

	/**
	 * <p>
	 * The {@code awt.toolkit} System Property.
	 * </p>
	 * <p>
	 * Holds a class name, on Windows XP this is {@code sun.awt.windows.WToolkit}.
	 * </p>
	 * <p>
	 * <b>On platforms without a GUI, this value is {@code null}.</b>
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 */
	public static final String AWT_TOOLKIT = getProperty("awt.toolkit");

	/**
	 * <p>
	 * The {@code file.encoding} System Property.
	 * </p>
	 * <p>
	 * File encoding, such as {@code Cp1252}.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String FILE_ENCODING = getProperty("file.encoding");

	/**
	 * <p>
	 * The {@code file.separator} System Property. File separator (<code>&quot;/&quot;</code> on
	 * UNIX).
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String FILE_SEPARATOR = getProperty("file.separator");

	/**
	 * <p>
	 * The {@code java.awt.fonts} System Property.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 */
	public static final String JAVA_AWT_FONTS = getProperty("java.awt.fonts");

	/**
	 * <p>
	 * The {@code java.awt.graphicsenv} System Property.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 */
	public static final String JAVA_AWT_GRAPHICSENV = getProperty("java.awt.graphicsenv");

	/**
	 * <p>
	 * The {@code java.awt.headless} System Property. The value of this property is the String
	 * {@code "true"} or {@code "false"}.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @see #isJavaAwtHeadless()
	 * @since Java 1.4
	 */
	public static final String JAVA_AWT_HEADLESS = getProperty("java.awt.headless");

	/**
	 * <p>
	 * The {@code java.awt.printerjob} System Property.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 */
	public static final String JAVA_AWT_PRINTERJOB = getProperty("java.awt.printerjob");

	/**
	 * <p>
	 * The {@code java.class.path} System Property. Java class path.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String JAVA_CLASS_PATH = getProperty("java.class.path");

	/**
	 * <p>
	 * The {@code java.class.version} System Property. Java class format version number.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String JAVA_CLASS_VERSION = getProperty("java.class.version");

	/**
	 * <p>
	 * The {@code java.compiler} System Property. Name of JIT compiler to use. First in JDK version
	 * 1.2. Not used in Sun JDKs after 1.2.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2. Not used in Sun versions after 1.2.
	 */
	public static final String JAVA_COMPILER = getProperty("java.compiler");

	/**
	 * <p>
	 * The {@code java.endorsed.dirs} System Property. Path of endorsed directory or directories.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.4
	 */
	public static final String JAVA_ENDORSED_DIRS = getProperty("java.endorsed.dirs");

	/**
	 * <p>
	 * The {@code java.ext.dirs} System Property. Path of extension directory or directories.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.3
	 */
	public static final String JAVA_EXT_DIRS = getProperty("java.ext.dirs");

	/**
	 * <p>
	 * The {@code java.home} System Property. Java installation directory.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String JAVA_HOME = getProperty(JAVA_HOME_KEY);

	/**
	 * <p>
	 * The {@code java.io.tmpdir} System Property. Default temp file path.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_IO_TMPDIR = getProperty(JAVA_IO_TMPDIR_KEY);

	/**
	 * <p>
	 * The {@code java.library.path} System Property. List of paths to search when loading
	 * libraries.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_LIBRARY_PATH = getProperty("java.library.path");

	/**
	 * <p>
	 * The {@code java.runtime.name} System Property. Java Runtime Environment name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.3
	 */
	public static final String JAVA_RUNTIME_NAME = getProperty("java.runtime.name");

	/**
	 * <p>
	 * The {@code java.runtime.version} System Property. Java Runtime Environment version.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.3
	 */
	public static final String JAVA_RUNTIME_VERSION = getProperty("java.runtime.version");

	/**
	 * <p>
	 * The {@code java.specification.name} System Property. Java Runtime Environment specification
	 * name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_SPECIFICATION_NAME = getProperty("java.specification.name");

	/**
	 * <p>
	 * The {@code java.specification.vendor} System Property. Java Runtime Environment specification
	 * vendor.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_SPECIFICATION_VENDOR = getProperty("java.specification.vendor");

	/**
	 * <p>
	 * The {@code java.specification.version} System Property. Java Runtime Environment
	 * specification version.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 */
	public static final String JAVA_SPECIFICATION_VERSION = getProperty("java.specification.version");

	/**
	 * This is value is inilialized by {@link #JAVA_SPECIFICATION_VERSION}
	 * 1.1 -> 1
	 * 1.x -> x
	 * 1.8 -> 8
	 * 9 -> 9
	 * ..
	 * 12 -> 12
	 */
	public static final int JAVA_MAJOR_VERSION = getJavaMajorVersion();
	
	/**
	 * <p>
	 * The {@code java.util.prefs.PreferencesFactory} System Property. A class name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.4
	 */
	public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getProperty("java.util.prefs.PreferencesFactory");

	/**
	 * <p>
	 * The {@code java.vendor} System Property. Java vendor-specific string.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String JAVA_VENDOR = getProperty("java.vendor");

	/**
	 * <p>
	 * The {@code java.vendor.url} System Property. Java vendor URL.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String JAVA_VENDOR_URL = getProperty("java.vendor.url");

	/**
	 * <p>
	 * The {@code java.version} System Property. Java version number.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String JAVA_VERSION = getProperty("java.version");

	/**
	 * <p>
	 * The {@code java.vm.info} System Property. Java Virtual Machine implementation info.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_INFO = getProperty("java.vm.info");

	/**
	 * <p>
	 * The {@code java.vm.name} System Property. Java Virtual Machine implementation name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_NAME = getProperty("java.vm.name");

	/**
	 * <p>
	 * The {@code java.vm.specification.name} System Property. Java Virtual Machine specification
	 * name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_SPECIFICATION_NAME = getProperty("java.vm.specification.name");

	/**
	 * <p>
	 * The {@code java.vm.specification.vendor} System Property. Java Virtual Machine specification
	 * vendor.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_SPECIFICATION_VENDOR = getProperty("java.vm.specification.vendor");

	/**
	 * <p>
	 * The {@code java.vm.specification.version} System Property. Java Virtual Machine specification
	 * version.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_SPECIFICATION_VERSION = getProperty("java.vm.specification.version");

	/**
	 * <p>
	 * The {@code java.vm.vendor} System Property. Java Virtual Machine implementation vendor.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_VENDOR = getProperty("java.vm.vendor");

	/**
	 * <p>
	 * The {@code java.vm.version} System Property. Java Virtual Machine implementation version.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String JAVA_VM_VERSION = getProperty("java.vm.version");

	/**
	 * <p>
	 * The {@code line.separator} System Property. Line separator (<code>&quot;\n&quot;</code> on
	 * UNIX).
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String LINE_SEPARATOR = getSystemLineSeparator();

	private static String getSystemLineSeparator() {
		String ls = getProperty("line.separator");
		if (ls == null) {
			// avoid security issues
			StringBuilderWriter buf = new StringBuilderWriter(4);
			PrintWriter out = new PrintWriter(buf);
			out.println();
			ls = buf.toString();
			out.close();
		}
		return ls;
	}


	/**
	 * <p>
	 * The {@code os.arch} System Property. Operating system architecture.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String OS_ARCH = getProperty("os.arch");

	/**
	 * is 32bit OS
	 */
	public static final boolean IS_OS_32BIT = Strings.endsWith(OS_ARCH, "86");

	/**
	 * is 64bit OS
	 */
	public static final boolean IS_OS_64BIT = Strings.endsWith(OS_ARCH, "64");

	/**
	 * <p>
	 * The {@code os.name} System Property. Operating system name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String OS_NAME = getProperty("os.name");

	/**
	 * <p>
	 * The {@code os.version} System Property. Operating system version.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String OS_VERSION = getProperty("os.version");

	/**
	 * <p>
	 * The {@code user.country} or {@code user.region} System Property. User's country code, such as
	 * {@code GB}. First in Java version 1.2 as {@code user.region}. Renamed to {@code user.country}
	 * in 1.4
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String USER_COUNTRY = getProperty("user.country") == null ? getProperty("user.region") : getProperty("user.country");

	/**
	 * <p>
	 * The {@code user.dir} System Property. User's current working directory.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String USER_DIR = getProperty(USER_DIR_KEY);

	/**
	 * <p>
	 * The {@code user.home} System Property. User's home directory.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String USER_HOME = getProperty(USER_HOME_KEY);

	/**
	 * <p>
	 * The {@code user.language} System Property. User's language code, such as {@code "en"}.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.2
	 */
	public static final String USER_LANGUAGE = getProperty("user.language");

	/**
	 * <p>
	 * The {@code user.name} System Property. User's account name.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 * @since Java 1.1
	 */
	public static final String USER_NAME = getProperty("user.name");

	/**
	 * <p>
	 * The {@code user.timezone} System Property. For example: {@code "America/Los_Angeles"}.
	 * </p>
	 * <p>
	 * Defaults to {@code null} if the runtime does not have security access to read this property
	 * or the property does not exist.
	 * </p>
	 * <p>
	 * This value is initialized when the class is loaded. If
	 * {@link System#setProperty(String,String)} or
	 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the
	 * value will be out of sync with that System property.
	 * </p>
	 * 
	 */
	public static final String USER_TIMEZONE = getProperty("user.timezone");

	// Operating system checks
	// -----------------------------------------------------------------------
	// These MUST be declared after those above as they depend on the
	// values being set up
	// OS names from http://www.vamphq.com/os.html

	/**
	 * <p>
	 * Is {@code true} if this is Android.
	 * </p>
	 * <p>
	 * The field will return {@code false} if Class: android.Manifest can not be loaded.
	 * </p>
	 */
	public static final boolean IS_OS_ANDROID = (Classes.findClass("android.Manifest") != null);

	/**
	 * <p>
	 * Is {@code true} if this is AppEngine.
	 * </p>
	 * <p>
	 * The field will return {@code false} if Class: com.google.appengine.api.log.LogService can not be loaded.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_APPENGINE = Strings.isNotEmpty(getProperty("com.google.appengine.application.id"));

	/**
	 * <p>
	 * Is {@code true} if this is AIX.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_AIX = getOSMatchesName("AIX");

	/**
	 * <p>
	 * Is {@code true} if this is HP-UX.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_HP_UX = getOSMatchesName("HP-UX");

	/**
	 * <p>
	 * Is {@code true} if this is IBM OS/400.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 *
	 * @since 3.3
	 */
	public static final boolean IS_OS_400 = getOSMatchesName("OS/400");

	/**
	 * <p>
	 * Is {@code true} if this is Irix.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_IRIX = getOSMatchesName("Irix");

	/**
	 * <p>
	 * Is {@code true} if this is Linux.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_LINUX = getOSMatchesName("Linux")
			|| getOSMatchesName("LINUX");

	/**
	 * <p>
	 * Is {@code true} if this is Mac.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_MAC = getOSMatchesName("Mac");

	/**
	 * <p>
	 * Is {@code true} if this is Mac.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_MAC_OSX = getOSMatchesName("Mac OS X");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Cheetah.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_CHEETAH = getOSMatches("Mac OS X", "10.0");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Puma.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_PUMA = getOSMatches("Mac OS X", "10.1");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Jaguar.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_JAGUAR = getOSMatches("Mac OS X", "10.2");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Panther.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 *
	 * @since 3.4
	 */
	public static final boolean IS_OS_MAC_OSX_PANTHER = getOSMatches("Mac OS X", "10.3");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Tiger.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_TIGER = getOSMatches("Mac OS X", "10.4");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Leopard.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_LEOPARD = getOSMatches("Mac OS X", "10.5");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Snow Leopard.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_SNOW_LEOPARD = getOSMatches("Mac OS X", "10.6");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Lion.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_LION = getOSMatches("Mac OS X", "10.7");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Mountain Lion.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_MOUNTAIN_LION = getOSMatches("Mac OS X", "10.8");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Mavericks.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_MAVERICKS = getOSMatches("Mac OS X", "10.9");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X Yosemite.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_YOSEMITE = getOSMatches("Mac OS X", "10.10");

	/**
	 * <p>
	 * Is {@code true} if this is Mac OS X El Capitan.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_MAC_OSX_EL_CAPITAN = getOSMatches("Mac OS X", "10.11");

	/**
	 * <p>
	 * Is {@code true} if this is FreeBSD.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_FREE_BSD = getOSMatchesName("FreeBSD");

	/**
	 * <p>
	 * Is {@code true} if this is OpenBSD.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_OPEN_BSD = getOSMatchesName("OpenBSD");

	/**
	 * <p>
	 * Is {@code true} if this is NetBSD.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_NET_BSD = getOSMatchesName("NetBSD");

	/**
	 * <p>
	 * Is {@code true} if this is OS/2.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_OS2 = getOSMatchesName("OS/2");

	/**
	 * <p>
	 * Is {@code true} if this is Solaris.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_SOLARIS = getOSMatchesName("Solaris");

	/**
	 * <p>
	 * Is {@code true} if this is SunOS.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_SUN_OS = getOSMatchesName("SunOS");

	/**
	 * <p>
	 * Is {@code true} if this is a UNIX like system, as in any of AIX, HP-UX, Irix, Linux, MacOSX,
	 * Solaris or SUN OS.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX
			|| IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS || IS_OS_FREE_BSD || IS_OS_OPEN_BSD
			|| IS_OS_NET_BSD;

	/**
	 * <p>
	 * Is {@code true} if this is Windows.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS = getOSMatchesName(OS_NAME_WINDOWS_PREFIX);

	/**
	 * <p>
	 * Is {@code true} if this is Windows 2000.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_2000 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 2000");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 2003.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_2003 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 2003");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 2008.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_2008 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2008");

	/**
	 * <p>
	 * Is {@code true} if this is Windows Server 2012.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_WINDOWS_2012 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2012");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 95.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_95 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 95");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 98.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_98 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 98");

	/**
	 * <p>
	 * Is {@code true} if this is Windows ME.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_ME = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " Me");

	/**
	 * <p>
	 * Is {@code true} if this is Windows NT.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_NT = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " NT");
	// Windows 2000 returns 'Windows 2000' but may suffer from same Java1.2 problem

	/**
	 * <p>
	 * Is {@code true} if this is Windows XP.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_XP = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " XP");

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Is {@code true} if this is Windows Vista.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_VISTA = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " Vista");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 7.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 * 
	 */
	public static final boolean IS_OS_WINDOWS_7 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 7");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 8.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_WINDOWS_8 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 8");

	/**
	 * <p>
	 * Is {@code true} if this is Windows 10.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	public static final boolean IS_OS_WINDOWS_10 = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " 10");

	/**
	 * <p>
	 * Is {@code true} if this is z/OS.
	 * </p>
	 * <p>
	 * The field will return {@code false} if {@code OS_NAME} is {@code null}.
	 * </p>
	 */
	// Values on a z/OS system I tested (Gary Gregory - 2016-03-12)
	// os.arch = s390x
	// os.encoding = ISO8859_1
	// os.name = z/OS
	// os.version = 02.02.00
	public static final boolean IS_OS_ZOS = getOSMatchesName("z/OS");

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the Java home directory as a {@code File}.
	 * </p>
	 * 
	 * @return a directory
	 * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
	 *             method doesn't allow access to the specified system property.
	 * @see System#getProperty(String)
	 */
	public static File getJavaHome() {
		return new File(System.getProperty(JAVA_HOME_KEY));
	}

	/**
	 * <p>
	 * Gets the Java IO temporary directory as a {@code File}.
	 * </p>
	 * 
	 * @return a directory
	 * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
	 *             method doesn't allow access to the specified system property.
	 * @see System#getProperty(String)
	 */
	public static File getJavaIoTmpDir() {
		return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
	}

	/**
	 * <p>
	 * Decides if the Java version matches.
	 * </p>
	 * 
	 * @param versionPrefix the prefix for the java version
	 * @return true if matches, or false if not or can't determine
	 */
	private static int getJavaMajorVersion() {
		if (JAVA_SPECIFICATION_VERSION == null || JAVA_SPECIFICATION_VERSION.length() == 0) {
			return 0;
		}
		try {
			if (JAVA_SPECIFICATION_VERSION.length() >= 3 && JAVA_SPECIFICATION_VERSION.startsWith("1.")) {
				return Integer.parseInt(JAVA_SPECIFICATION_VERSION.substring(2, 3));
			}
			return Integer.parseInt(JAVA_SPECIFICATION_VERSION);
		}
		catch (Exception e) {
			System.err.println("Failed to getJavaMajorVersion for " + JAVA_SPECIFICATION_VERSION);
			return 0;
		}
	}

	/**
	 * Decides if the operating system matches.
	 * 
	 * @param osNamePrefix the prefix for the os name
	 * @param osVersionPrefix the prefix for the version
	 * @return true if matches, or false if not or can't determine
	 */
	private static boolean getOSMatches(final String osNamePrefix, final String osVersionPrefix) {
		return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
	}

	/**
	 * Decides if the operating system matches.
	 * 
	 * @param osNamePrefix the prefix for the os name
	 * @return true if matches, or false if not or can't determine
	 */
	private static boolean getOSMatchesName(final String osNamePrefix) {
		return isOSNameMatch(OS_NAME, osNamePrefix);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a System property, defaulting to {@code null} if the property cannot be read.
	 * </p>
	 * <p>
	 * If a {@code SecurityException} is caught, the return value is {@code null} and a message is
	 * written to {@code System.err}.
	 * </p>
	 * 
	 * @param name the system property name
	 * @return the system property value or {@code null} if a security problem occurs
	 */
	public static String getProperty(final String name) {
		try {
			return System.getProperty(name);
		}
		catch (final SecurityException ex) {
			// we are not allowed to look at this property
			System.err.println("Caught a SecurityException reading the system property '" + name + "'.");
			return null;
		}
	}

	/**
	 * Gets the system property indicated by the specified key.
	 * If a {@code SecurityException} is caught, the default value is returned and a message is
	 * written to {@code System.err}.
	 * </p>
	 *
	 * @param name the system property name
	 * @param defv   a default value.
	 * 
	 * @return the system property value or the default if a security problem occurs
	 */
	public static String getProperty(final String name, final String defv) {
		try {
			return System.getProperty(name, defv);
		}
		catch (final SecurityException ex) {
			// we are not allowed to look at this property
			System.err.println("Caught a SecurityException reading the system property '" + name + "'.");
			return defv;
		}
	}

	/**
	 * <p>
	 * Gets a environment variable, defaulting to {@code null} if the variable cannot be read.
	 * </p>
	 * <p>
	 * If a {@code SecurityException} is caught, the return value is {@code null} and a message is
	 * written to {@code System.err}.
	 * </p>
	 * 
	 * @param name the environment variable name
	 * @return the environment variable value or {@code null} if a security problem occurs
	 */
	public static String getenv(final String name) {
		try {
			return System.getenv(name);
		}
		catch (final SecurityException ex) {
			// we are not allowed to look at this variable
			System.err.println("Caught a SecurityException reading the environment variable '" + name + "'.");
			return null;
		}
	}


	/**
	 * <p>
	 * Gets a environment variable, defaulting to {@code null} if the variable cannot be read.
	 * </p>
	 * <p>
	 * If a {@code SecurityException} is caught, the default value is returned and a message is
	 * written to {@code System.err}.
	 * </p>
	 * 
	 * @param name the environment variable name
     * @param defv a default value.
	 * @return the environment variable value or the default value if a security problem occurs
	 */
	public static String getenv(final String name, final String defv) {
		try {
			String v = System.getenv(name);
			return v == null ? defv : v;
		}
		catch (final SecurityException ex) {
			// we are not allowed to look at this variable
			System.err.println("Caught a SecurityException reading the environment variable '" + name + "'.");
			return defv;
		}
	}

	/**
	 * <p>
	 * Gets the user directory as a {@code File}.
	 * </p>
	 * 
	 * @return a directory
	 * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
	 *             method doesn't allow access to the specified system property.
	 * @see System#getProperty(String)
	 */
	public static File getUserDir() {
		return new File(System.getProperty(USER_DIR_KEY));
	}

	/**
	 * <p>
	 * Gets the user home directory as a {@code File}.
	 * </p>
	 * 
	 * @return a directory
	 * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
	 *             method doesn't allow access to the specified system property.
	 * @see System#getProperty(String)
	 */
	public static File getUserHome() {
		return new File(System.getProperty(USER_HOME_KEY));
	}

	/**
	 * Returns whether the {@link #JAVA_AWT_HEADLESS} value is {@code true}.
	 * 
	 * @return {@code true} if {@code JAVA_AWT_HEADLESS} is {@code "true"}, {@code false} otherwise.
	 * @see #JAVA_AWT_HEADLESS
	 * @since Java 1.4
	 */
	public static boolean isJavaAwtHeadless() {
		return Boolean.TRUE.toString().equals(JAVA_AWT_HEADLESS);
	}

	/**
	 * Decides if the operating system matches.
	 * <p>
	 * This method is package private instead of private to support unit test invocation.
	 * </p>
	 * 
	 * @param osName the actual OS name
	 * @param osVersion the actual OS version
	 * @param osNamePrefix the prefix for the expected OS name
	 * @param osVersionPrefix the prefix for the expected OS version
	 * @return true if matches, or false if not or can't determine
	 */
	static boolean isOSMatch(final String osName, final String osVersion, final String osNamePrefix,
			String osVersionPrefix) {
		if (osName == null || osVersion == null) {
			return false;
		}
		return isOSNameMatch(osName, osNamePrefix) && isOSVersionMatch(osVersion, osVersionPrefix);
	}

	/**
	 * Decides if the operating system matches.
	 * <p>
	 * This method is package private instead of private to support unit test invocation.
	 * </p>
	 * 
	 * @param osName the actual OS name
	 * @param osNamePrefix the prefix for the expected OS name
	 * @return true if matches, or false if not or can't determine
	 */
	static boolean isOSNameMatch(final String osName, final String osNamePrefix) {
		if (osName == null) {
			return false;
		}
		return osName.startsWith(osNamePrefix);
	}

	/**
	 * Decides if the operating system version matches.
	 * <p>
	 * This method is package private instead of private to support unit test invocation.
	 * </p>
	 *
	 * @param osVersion the actual OS version
	 * @param osVersionPrefix the prefix for the expected OS version
	 * @return true if matches, or false if not or can't determine
	 */
	static boolean isOSVersionMatch(final String osVersion, final String osVersionPrefix) {
		if (Strings.isEmpty(osVersion)) {
			return false;
		}
		
		// Compare parts of the version string instead of using String.startsWith(String) because
		// otherwise
		// osVersionPrefix 10.1 would also match osVersion 10.10
		final String[] versionPrefixParts = osVersionPrefix.split("\\.");
		final String[] versionParts = osVersion.split("\\.");
		for (int i = 0; i < Math.min(versionPrefixParts.length, versionParts.length); i++) {
			if (!versionPrefixParts[i].equals(versionParts[i])) {
				return false;
			}
		}
		return true;
	}
}
