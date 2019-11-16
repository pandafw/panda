package panda.net.ftp.parser;

import panda.net.ftp.FTPFile;
import panda.net.ftp.FTPFileEntryParser;
import panda.net.ftp.FTPFileEntryParserImpl;

/**
 * This implementation allows to pack some FileEntryParsers together and handle the case where to
 * returned dirstyle isnt clearly defined. The matching parser will be cached. If the cached parser
 * wont match due to the server changed the dirstyle, a new matching parser will be searched.
 */
public class CompositeFileEntryParser extends FTPFileEntryParserImpl {
	private final FTPFileEntryParser[] ftpFileEntryParsers;
	private FTPFileEntryParser cachedFtpFileEntryParser;

	public CompositeFileEntryParser(FTPFileEntryParser[] ftpFileEntryParsers) {
		super();

		this.cachedFtpFileEntryParser = null;
		this.ftpFileEntryParsers = ftpFileEntryParsers;
	}

	// @Override
	public FTPFile parseFTPEntry(String listEntry) {
		if (cachedFtpFileEntryParser != null) {
			FTPFile matched = cachedFtpFileEntryParser.parseFTPEntry(listEntry);
			if (matched != null) {
				return matched;
			}
		}
		else {
			for (FTPFileEntryParser ftpFileEntryParser : ftpFileEntryParsers) {
				FTPFile matched = ftpFileEntryParser.parseFTPEntry(listEntry);
				if (matched != null) {
					cachedFtpFileEntryParser = ftpFileEntryParser;
					return matched;
				}
			}
		}
		return null;
	}
}
