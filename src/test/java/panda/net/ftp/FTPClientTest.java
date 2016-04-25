package panda.net.ftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import panda.net.ftp.FTPClient;
import panda.net.ftp.FTPClientConfig;
import panda.net.ftp.FTPFile;
import panda.net.ftp.FTPFileEntryParser;
import panda.net.ftp.FTPListParseEngine;
import panda.net.ftp.parser.UnixFTPEntryParser;

import junit.framework.TestCase;

public class FTPClientTest extends TestCase {

	private static final String[] TESTS = { "257 /path/without/quotes", "/path/without/quotes",

	"257 \"/path/with/delimiting/quotes/without/commentary\"", "/path/with/delimiting/quotes/without/commentary",

	"257 \"/path/with/quotes\"\" /inside/but/without/commentary\"",
			"/path/with/quotes\" /inside/but/without/commentary",

			"257 \"/path/with/quotes\"\" /inside/string\" and with commentary", "/path/with/quotes\" /inside/string",

			"257 \"/path/with/quotes\"\" /inside/string\" and with commentary that also \"contains quotes\"",
			"/path/with/quotes\" /inside/string",

			"257 \"/path/without/trailing/quote", // invalid syntax, return all after reply code
													// prefix
			"\"/path/without/trailing/quote",

			"257 root is current directory.", // NET-442
			"root is current directory.",

			"257 \"/\"", // NET-502
			"/", };

	public FTPClientTest(String name) {
		super(name);
	}

	public void testParseClient() {
		for (int i = 0; i < TESTS.length; i += 2) {
			assertEquals("Failed to parse", TESTS[i + 1], FTPClient.__parsePathname(TESTS[i]));
		}
	}

	public void testParserCachingWithKey() throws Exception {
		FTPClient client = new FTPClient();
		assertNull(client.getEntryParser());
		client.__createParser(FTPClientConfig.SYST_UNIX);
		final FTPFileEntryParser entryParserSYST = client.getEntryParser();
		assertNotNull(entryParserSYST);
		client.__createParser(FTPClientConfig.SYST_UNIX);
		assertSame(entryParserSYST, client.getEntryParser()); // the previous entry was cached
		client.__createParser(FTPClientConfig.SYST_VMS);
		final FTPFileEntryParser entryParserVMS = client.getEntryParser();
		assertNotSame(entryParserSYST, entryParserVMS); // the previous entry was replaced
		client.__createParser(FTPClientConfig.SYST_VMS);
		assertSame(entryParserVMS, client.getEntryParser()); // the previous entry was cached
		client.__createParser(FTPClientConfig.SYST_UNIX); // revert
		assertNotSame(entryParserVMS, client.getEntryParser()); // the previous entry was replaced
	}

	private static class LocalClient extends FTPClient {
		private String systemType;

		@Override
		public String getSystemType() throws IOException {
			return systemType;
		}

		public void setSystemType(String type) {
			systemType = type;
		}
	}

	public void testParserCachingNullKey() throws Exception {
		LocalClient client = new LocalClient();
		client.setSystemType(FTPClientConfig.SYST_UNIX);
		assertNull(client.getEntryParser());
		client.__createParser(null);
		final FTPFileEntryParser entryParser = client.getEntryParser();
		assertNotNull(entryParser);
		client.__createParser(null);
		assertSame(entryParser, client.getEntryParser()); // parser was cached
		client.setSystemType(FTPClientConfig.SYST_NT);
		client.__createParser(null);
		assertSame(entryParser, client.getEntryParser()); // parser was cached
	}

	public void testUnparseableFiles() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("-rwxr-xr-x   2 root     root         4096 Mar  2 15:13 zxbox".getBytes());
		baos.write(new byte[] { '\r', '\n' });
		baos.write("zrwxr-xr-x   2 root     root         4096 Mar  2 15:13 zxbox".getBytes());
		baos.write(new byte[] { '\r', '\n' });
		FTPFileEntryParser parser = new UnixFTPEntryParser();
		FTPClientConfig config = new FTPClientConfig();
		FTPListParseEngine engine = new FTPListParseEngine(parser, config);
		config.setUnparseableEntries(false);
		engine.readServerList(new ByteArrayInputStream(baos.toByteArray()), null); // use default
																					// encoding
		FTPFile[] files = engine.getFiles();
		assertEquals(1, files.length);
		config.setUnparseableEntries(true);
		engine = new FTPListParseEngine(parser, config);
		engine.readServerList(new ByteArrayInputStream(baos.toByteArray()), null); // use default
																					// encoding
		files = engine.getFiles();
		assertEquals(2, files.length);
	}

}
