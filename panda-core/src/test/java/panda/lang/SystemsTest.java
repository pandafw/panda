package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests {@link Systems}.
 * 
 * Only limited testing can be performed.
 * 
 */
public class SystemsTest {

	/**
	 * Assums no security manager exists.
	 */
	@Test
	public void testGetJavaHome() {
		final File dir = Systems.getJavaHome();
		Assert.assertNotNull(dir);
		Assert.assertTrue(dir.exists());
	}

	/**
	 * Assums no security manager exists.
	 */
	@Test
	public void testGetJavaIoTmpDir() {
		final File dir = Systems.getJavaIoTmpDir();
		Assert.assertNotNull(dir);
		Assert.assertTrue(dir.exists());
	}

	/**
	 * Assums no security manager exists.
	 */
	@Test
	public void testGetUserDir() {
		final File dir = Systems.getUserDir();
		Assert.assertNotNull(dir);
		Assert.assertTrue(dir.exists());
	}

	/**
	 * Assums no security manager exists.
	 */
	@Test
	public void testGetUserHome() {
		final File dir = Systems.getUserHome();
		Assert.assertNotNull(dir);
		Assert.assertTrue(dir.exists());
	}

	@Test
	public void testIS_JAVA() {
		final String javaVersion = System.getProperty("java.version");
		if (javaVersion == null) {
			assertEquals(0, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.1")) {
			assertEquals(1, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.2")) {
			assertEquals(2, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.3")) {
			assertEquals(3, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.4")) {
			assertEquals(4, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.5")) {
			assertEquals(5, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.6")) {
			assertEquals(6, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.7")) {
			assertEquals(7, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("1.8")) {
			assertEquals(8, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("9")) {
			assertEquals(9, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("10")) {
			assertEquals(10, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("11")) {
			assertEquals(11, Systems.JAVA_MAJOR_VERSION);
		}
		else if (javaVersion.startsWith("12")) {
			assertEquals(12, Systems.JAVA_MAJOR_VERSION);
		}
		else {
			System.out.println("Can't test IS_JAVA value: " + javaVersion);
		}
	}

	@Test
	public void testIS_OS() {
		final String osName = System.getProperty("os.name");
		if (osName == null) {
			assertFalse(Systems.IS_OS_WINDOWS);
			assertFalse(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_SOLARIS);
			assertFalse(Systems.IS_OS_LINUX);
			assertFalse(Systems.IS_OS_MAC_OSX);
		}
		else if (osName.startsWith("Windows")) {
			assertFalse(Systems.IS_OS_UNIX);
			assertTrue(Systems.IS_OS_WINDOWS);
		}
		else if (osName.startsWith("Solaris")) {
			assertTrue(Systems.IS_OS_SOLARIS);
			assertTrue(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_WINDOWS);
		}
		else if (osName.toLowerCase(Locale.ENGLISH).startsWith("linux")) {
			assertTrue(Systems.IS_OS_LINUX);
			assertTrue(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_WINDOWS);
		}
		else if (osName.startsWith("Mac OS X")) {
			assertTrue(Systems.IS_OS_MAC_OSX);
			assertTrue(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_WINDOWS);
		}
		else if (osName.startsWith("OS/2")) {
			assertTrue(Systems.IS_OS_OS2);
			assertFalse(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_WINDOWS);
		}
		else if (osName.startsWith("SunOS")) {
			assertTrue(Systems.IS_OS_SUN_OS);
			assertTrue(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_WINDOWS);
		}
		else if (osName.startsWith("FreeBSD")) {
			assertTrue(Systems.IS_OS_FREE_BSD);
			assertTrue(Systems.IS_OS_UNIX);
			assertFalse(Systems.IS_OS_WINDOWS);
		}
		else {
			System.out.println("Can't test IS_OS value: " + osName);
		}
	}

	@Test
	public void testOSMatchesName() {
		String osName = null;
		assertFalse(Systems.isOSNameMatch(osName, "Windows"));
		osName = "";
		assertFalse(Systems.isOSNameMatch(osName, "Windows"));
		osName = "Windows 95";
		assertTrue(Systems.isOSNameMatch(osName, "Windows"));
		osName = "Windows NT";
		assertTrue(Systems.isOSNameMatch(osName, "Windows"));
		osName = "OS/2";
		assertFalse(Systems.isOSNameMatch(osName, "Windows"));
	}

	@Test
	public void testOSMatchesNameAndVersion() {
		String osName = null;
		String osVersion = null;
		assertFalse(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
		osName = "";
		osVersion = "";
		assertFalse(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
		osName = "Windows 95";
		osVersion = "4.0";
		assertFalse(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
		osName = "Windows 95";
		osVersion = "4.1";
		assertTrue(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
		osName = "Windows 98";
		osVersion = "4.1";
		assertTrue(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
		osName = "Windows NT";
		osVersion = "4.0";
		assertFalse(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
		osName = "OS/2";
		osVersion = "4.0";
		assertFalse(Systems.isOSMatch(osName, osVersion, "Windows 9", "4.1"));
	}

	@Test
	public void testJavaAwtHeadless() {
		final boolean atLeastJava14 = true;
		final String expectedStringValue = System.getProperty("java.awt.headless");
		final String expectedStringValueWithDefault = System.getProperty("java.awt.headless", "false");
		assertNotNull(expectedStringValueWithDefault);
		if (atLeastJava14) {
			final boolean expectedValue = Boolean.valueOf(expectedStringValue).booleanValue();
			if (expectedStringValue != null) {
				assertEquals(expectedStringValue, Systems.JAVA_AWT_HEADLESS);
			}
			assertEquals(expectedValue, Systems.isJavaAwtHeadless());
		}
		else {
			assertNull(expectedStringValue);
			assertNull(Systems.JAVA_AWT_HEADLESS);
			assertEquals(expectedStringValueWithDefault, "" + Systems.isJavaAwtHeadless());
		}
		assertEquals(expectedStringValueWithDefault, "" + Systems.isJavaAwtHeadless());
	}
}
