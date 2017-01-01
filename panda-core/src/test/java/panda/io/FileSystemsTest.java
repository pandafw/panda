package panda.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is used to test FileSystems.
 */
public class FileSystemsTest extends FileBasedTestCase {

	public FileSystemsTest(final String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
	}

	@Override
	protected void tearDown() throws Exception {
	}

	// -----------------------------------------------------------------------
	public void testGetFreeSpaceOS_String_NullPath() throws Exception {
		final FileSystems fsu = new FileSystems();
		try {
			fsu.freeSpaceOS(null, 1, false, -1);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			fsu.freeSpaceOS(null, 1, true, -1);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	public void testGetFreeSpaceOS_String_InitError() throws Exception {
		final FileSystems fsu = new FileSystems();
		try {
			fsu.freeSpaceOS("", -1, false, -1);
			fail();
		}
		catch (final IllegalStateException ex) {
		}
		try {
			fsu.freeSpaceOS("", -1, true, -1);
			fail();
		}
		catch (final IllegalStateException ex) {
		}
	}

	public void testGetFreeSpaceOS_String_Other() throws Exception {
		final FileSystems fsu = new FileSystems();
		try {
			fsu.freeSpaceOS("", 0, false, -1);
			fail();
		}
		catch (final IllegalStateException ex) {
		}
		try {
			fsu.freeSpaceOS("", 0, true, -1);
			fail();
		}
		catch (final IllegalStateException ex) {
		}
	}

	public void testGetFreeSpaceOS_String_Windows() throws Exception {
		final FileSystems fsu = new FileSystems() {
			@Override
			protected long freeSpaceWindows(final String path, final long timeout) throws IOException {
				return 12345L;
			}
		};
		assertEquals(12345L, fsu.freeSpaceOS("", 1, false, -1));
		assertEquals(12345L / 1024, fsu.freeSpaceOS("", 1, true, -1));
	}

	public void testGetFreeSpaceOS_String_Unix() throws Exception {
		final FileSystems fsu = new FileSystems() {
			@Override
			protected long freeSpaceUnix(final String path, final boolean kb, final boolean posix, final long timeout)
					throws IOException {
				return kb ? 12345L : 54321;
			}
		};
		assertEquals(54321L, fsu.freeSpaceOS("", 2, false, -1));
		assertEquals(12345L, fsu.freeSpaceOS("", 2, true, -1));
	}

	// -----------------------------------------------------------------------
	public void testGetFreeSpaceWindows_String_ParseCommaFormatBytes() throws Exception {
		// this is the format of response when calling dir /c
		// we have now switched to dir /-c, so we should never get this
		final String lines = " Volume in drive C is HDD\n" + " Volume Serial Number is XXXX-YYYY\n" + "\n"
				+ " Directory of C:\\Documents and Settings\\Xxxx\n" + "\n" + "19/08/2005  22:43    <DIR>          .\n"
				+ "19/08/2005  22:43    <DIR>          ..\n" + "11/08/2005  01:07                81 build.properties\n"
				+ "17/08/2005  21:44    <DIR>          Desktop\n" + "               7 File(s)        180,260 bytes\n"
				+ "              10 Dir(s)  41,411,551,232 bytes free";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(41411551232L, fsu.freeSpaceWindows("", -1));
	}

	// -----------------------------------------------------------------------
	public void testGetFreeSpaceWindows_String_EmptyPath() throws Exception {
		final String lines = " Volume in drive C is HDD\n" + " Volume Serial Number is XXXX-YYYY\n" + "\n"
				+ " Directory of C:\\Documents and Settings\\Xxxx\n" + "\n" + "19/08/2005  22:43    <DIR>          .\n"
				+ "19/08/2005  22:43    <DIR>          ..\n" + "11/08/2005  01:07                81 build.properties\n"
				+ "17/08/2005  21:44    <DIR>          Desktop\n" + "               7 File(s)         180260 bytes\n"
				+ "              10 Dir(s)     41411551232 bytes free";
		final FileSystems fsu = new MockFileSystems(0, lines, "dir /a /-c ");
		assertEquals(41411551232L, fsu.freeSpaceWindows("", -1));
	}

	public void testGetFreeSpaceWindows_String_NormalResponse() throws Exception {
		final String lines = " Volume in drive C is HDD\n" + " Volume Serial Number is XXXX-YYYY\n" + "\n"
				+ " Directory of C:\\Documents and Settings\\Xxxx\n" + "\n" + "19/08/2005  22:43    <DIR>          .\n"
				+ "19/08/2005  22:43    <DIR>          ..\n" + "11/08/2005  01:07                81 build.properties\n"
				+ "17/08/2005  21:44    <DIR>          Desktop\n" + "               7 File(s)         180260 bytes\n"
				+ "              10 Dir(s)     41411551232 bytes free";
		final FileSystems fsu = new MockFileSystems(0, lines, "dir /a /-c \"C:\"");
		assertEquals(41411551232L, fsu.freeSpaceWindows("C:", -1));
	}

	public void testGetFreeSpaceWindows_String_StripDrive() throws Exception {
		final String lines = " Volume in drive C is HDD\n" + " Volume Serial Number is XXXX-YYYY\n" + "\n"
				+ " Directory of C:\\Documents and Settings\\Xxxx\n" + "\n" + "19/08/2005  22:43    <DIR>          .\n"
				+ "19/08/2005  22:43    <DIR>          ..\n" + "11/08/2005  01:07                81 build.properties\n"
				+ "17/08/2005  21:44    <DIR>          Desktop\n" + "               7 File(s)         180260 bytes\n"
				+ "              10 Dir(s)     41411551232 bytes free";
		final FileSystems fsu = new MockFileSystems(0, lines, "dir /a /-c \"C:\\somedir\"");
		assertEquals(41411551232L, fsu.freeSpaceWindows("C:\\somedir", -1));
	}

	public void testGetFreeSpaceWindows_String_quoted() throws Exception {
		final String lines = " Volume in drive C is HDD\n" + " Volume Serial Number is XXXX-YYYY\n" + "\n"
				+ " Directory of C:\\Documents and Settings\\Xxxx\n" + "\n" + "19/08/2005  22:43    <DIR>          .\n"
				+ "19/08/2005  22:43    <DIR>          ..\n" + "11/08/2005  01:07                81 build.properties\n"
				+ "17/08/2005  21:44    <DIR>          Desktop\n" + "               7 File(s)         180260 bytes\n"
				+ "              10 Dir(s)     41411551232 bytes free";
		final FileSystems fsu = new MockFileSystems(0, lines, "dir /a /-c \"C:\\somedir\"");
		assertEquals(41411551232L, fsu.freeSpaceWindows("\"C:\\somedir\"", -1));
	}

	public void testGetFreeSpaceWindows_String_EmptyResponse() throws Exception {
		final String lines = "";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceWindows("C:", -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceWindows_String_EmptyMultiLineResponse() throws Exception {
		final String lines = "\n\n";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceWindows("C:", -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceWindows_String_InvalidTextResponse() throws Exception {
		final String lines = "BlueScreenOfDeath";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceWindows("C:", -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceWindows_String_NoSuchDirectoryResponse() throws Exception {
		final String lines = " Volume in drive C is HDD\n" + " Volume Serial Number is XXXX-YYYY\n" + "\n"
				+ " Directory of C:\\Documents and Settings\\empty" + "\n";
		final FileSystems fsu = new MockFileSystems(1, lines);
		try {
			fsu.freeSpaceWindows("C:", -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testGetFreeSpaceUnix_String_EmptyPath() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "xxx:/home/users/s     14428928  12956424   1472504  90% /home/users/s";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceUnix("", false, false, -1);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			fsu.freeSpaceUnix("", true, false, -1);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			fsu.freeSpaceUnix("", true, true, -1);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			fsu.freeSpaceUnix("", false, true, -1);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

	}

	public void testGetFreeSpaceUnix_String_NormalResponseLinux() throws Exception {
		// from Sourceforge 'GNU bash, version 2.05b.0(1)-release (i386-redhat-linux-gnu)'
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "/dev/xxx                497944    308528    189416  62% /";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(189416L, fsu.freeSpaceUnix("/", false, false, -1));
	}

	public void testGetFreeSpaceUnix_String_NormalResponseFreeBSD() throws Exception {
		// from Apache 'FreeBSD 6.1-RELEASE (SMP-turbo)'
		final String lines = "Filesystem  1K-blocks      Used    Avail Capacity  Mounted on\n"
				+ "/dev/xxxxxx    128990    102902    15770    87%    /";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(15770L, fsu.freeSpaceUnix("/", false, false, -1));
	}

	// -----------------------------------------------------------------------
	public void testGetFreeSpaceUnix_String_NormalResponseKbLinux() throws Exception {
		// from Sourceforge 'GNU bash, version 2.05b.0(1)-release (i386-redhat-linux-gnu)'
		// df, df -k and df -kP are all identical
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "/dev/xxx                497944    308528    189416  62% /";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(189416L, fsu.freeSpaceUnix("/", true, false, -1));
	}

	public void testGetFreeSpaceUnix_String_NormalResponseKbFreeBSD() throws Exception {
		// from Apache 'FreeBSD 6.1-RELEASE (SMP-turbo)'
		// df and df -k are identical, but df -kP uses 512 blocks (not relevant as not used)
		final String lines = "Filesystem  1K-blocks      Used    Avail Capacity  Mounted on\n"
				+ "/dev/xxxxxx    128990    102902    15770    87%    /";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(15770L, fsu.freeSpaceUnix("/", true, false, -1));
	}

	public void testGetFreeSpaceUnix_String_NormalResponseKbSolaris() throws Exception {
		// from IO-91 - ' SunOS et 5.10 Generic_118822-25 sun4u sparc SUNW,Ultra-4'
		// non-kb response does not contain free space - see IO-91
		final String lines = "Filesystem            kbytes    used   avail capacity  Mounted on\n"
				+ "/dev/dsk/x0x0x0x0    1350955  815754  481163    63%";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(481163L, fsu.freeSpaceUnix("/dev/dsk/x0x0x0x0", true, false, -1));
	}

	public void testGetFreeSpaceUnix_String_LongResponse() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "xxx-yyyyyyy-zzz:/home/users/s\n"
				+ "                      14428928  12956424   1472504  90% /home/users/s";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(1472504L, fsu.freeSpaceUnix("/home/users/s", false, false, -1));
	}

	public void testGetFreeSpaceUnix_String_LongResponseKb() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "xxx-yyyyyyy-zzz:/home/users/s\n"
				+ "                      14428928  12956424   1472504  90% /home/users/s";
		final FileSystems fsu = new MockFileSystems(0, lines);
		assertEquals(1472504L, fsu.freeSpaceUnix("/home/users/s", true, false, -1));
	}

	public void testGetFreeSpaceUnix_String_EmptyResponse() throws Exception {
		final String lines = "";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceUnix("/home/users/s", false, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", false, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceUnix_String_InvalidResponse1() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "                      14428928  12956424       100";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceUnix("/home/users/s", false, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", false, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceUnix_String_InvalidResponse2() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "xxx:/home/users/s     14428928  12956424   nnnnnnn  90% /home/users/s";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceUnix("/home/users/s", false, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", false, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceUnix_String_InvalidResponse3() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "xxx:/home/users/s     14428928  12956424        -1  90% /home/users/s";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceUnix("/home/users/s", false, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", false, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	public void testGetFreeSpaceUnix_String_InvalidResponse4() throws Exception {
		final String lines = "Filesystem           1K-blocks      Used Available Use% Mounted on\n"
				+ "xxx-yyyyyyy-zzz:/home/users/s";
		final FileSystems fsu = new MockFileSystems(0, lines);
		try {
			fsu.freeSpaceUnix("/home/users/s", false, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, false, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", false, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			fsu.freeSpaceUnix("/home/users/s", true, true, -1);
			fail();
		}
		catch (final IOException ex) {
		}
	}

	// -----------------------------------------------------------------------
	static class MockFileSystems extends FileSystems {
		private final int exitCode;
		private final byte[] bytes;
		private final String cmd;

		public MockFileSystems(final int exitCode, final String lines) {
			this(exitCode, lines, null);
		}

		public MockFileSystems(final int exitCode, final String lines, final String cmd) {
			this.exitCode = exitCode;
			this.bytes = lines.getBytes();
			this.cmd = cmd;
		}

		@Override
		Process openProcess(final String[] params) {
			if (cmd != null) {
				assertEquals(cmd, params[params.length - 1]);
			}
			return new Process() {
				@Override
				public InputStream getErrorStream() {
					return null;
				}

				@Override
				public InputStream getInputStream() {
					return new ByteArrayInputStream(bytes);
				}

				@Override
				public OutputStream getOutputStream() {
					return null;
				}

				@Override
				public int waitFor() throws InterruptedException {
					return exitCode;
				}

				@Override
				public int exitValue() {
					return exitCode;
				}

				@Override
				public void destroy() {
				}
			};
		}
	}

}
