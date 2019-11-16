package panda.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;

/**
 * This interface specifies the concept of parsing an FTP server's timestamp.
 */
public interface FTPTimestampParser {

	/**
	 * the default default date format.
	 */
	public static final String DEFAULT_SDF = UnixFTPEntryParser.DEFAULT_DATE_FORMAT;
	/**
	 * the default recent date format.
	 */
	public static final String DEFAULT_RECENT_SDF = UnixFTPEntryParser.DEFAULT_RECENT_DATE_FORMAT;

	/**
	 * Parses the supplied datestamp parameter. This parameter typically would have been pulled from
	 * a longer FTP listing via the regular expression mechanism
	 * 
	 * @param timestampStr - the timestamp portion of the FTP directory listing to be parsed
	 * @return a <code>java.util.Calendar</code> object initialized to the date parsed by the parser
	 * @throws ParseException if none of the parser mechanisms belonging to the implementor can
	 *             parse the input.
	 */
	public Calendar parseTimestamp(String timestampStr) throws ParseException;

}
