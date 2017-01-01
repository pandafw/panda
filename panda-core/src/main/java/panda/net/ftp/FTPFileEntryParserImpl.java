package panda.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * This abstract class implements both the older FTPFileListParser and newer FTPFileEntryParser
 * interfaces with default functionality. All the classes in the parser subpackage inherit from
 * this.
 */
public abstract class FTPFileEntryParserImpl implements FTPFileEntryParser {
	/**
	 * The constructor for a FTPFileEntryParserImpl object.
	 */
	public FTPFileEntryParserImpl() {
	}

	/**
	 * Reads the next entry using the supplied BufferedReader object up to whatever delimits one
	 * entry from the next. This default implementation simply calls BufferedReader.readLine().
	 * 
	 * @param reader The BufferedReader object from which entries are to be read.
	 * @return A string representing the next ftp entry or null if none found.
	 * @exception java.io.IOException thrown on any IO Error reading from the reader.
	 */
	// @Override
	public String readNextEntry(BufferedReader reader) throws IOException {
		return reader.readLine();
	}

	/**
	 * This method is a hook for those implementors (such as VMSVersioningFTPEntryParser, and
	 * possibly others) which need to perform some action upon the FTPFileList after it has been
	 * created from the server stream, but before any clients see the list. This default
	 * implementation does nothing.
	 * 
	 * @param original Original list after it has been created from the server stream
	 * @return <code>original</code> unmodified.
	 */
	// @Override
	public List<String> preParse(List<String> original) {
		return original;
	}
}

/*
 * Emacs configuration Local variables: ** mode: java ** c-basic-offset: 4 ** indent-tabs-mode: nil
 * ** End: **
 */
