package panda.net.http;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import panda.lang.TimeZones;
import panda.lang.time.DateTimes;
import panda.net.InetDates;

/**
 * A utility class for parsing and formatting HTTP dates as used in cookies and other headers. This
 * class handles dates as defined by RFC 2616 section 3.3.1 as well as some other common
 * non-standard formats.
 */
public class HttpDates extends InetDates {
	/**
	 * Date format pattern used to parse HTTP date headers in RFC 1036 format.
	 */
	public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";

	/**
	 * Date format pattern used to parse HTTP date headers in ANSI C <code>asctime()</code> format.
	 */
	public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";

	private static final String[] DEFAULT_PATTERNS = new String[] { PATTERN_RFC1036, PATTERN_RFC1123, PATTERN_ASCTIME };

	/**
	 * Parses a date value. The formats used for parsing the date value are retrieved from the
	 * default http params.
	 * 
	 * @param dateValue the date value to parse
	 * @return the parsed date
	 * @throws ParseException if the value could not be parsed using any of the supported date
	 *             formats
	 */
	public static Date parse(String dateValue) throws ParseException {
		return parse(dateValue, null);
	}

	/**
	 * Parses a date value. The formats used for parsing the date value are retrieved from the
	 * default http params.
	 * 
	 * @param dateValue the date value to parse
	 * @return the parsed date
	 */
	public static Date safeParse(String dateValue) {
		return safeParse(dateValue, null);
	}

	/**
	 * Parses the date value using the given date formats.
	 * 
	 * @param dateValue the date value to parse
	 * @param dateFormats the date formats to use
	 * @return the parsed date
	 * @throws ParseException if none of the dataFormats could parse the dateValue
	 */
	public static Date parse(String dateValue, String[] dateFormats) throws ParseException {
		return parse(dateValue, dateFormats, false);
	}

	/**
	 * Parses the date value using the given date formats.
	 * 
	 * @param dateValue the date value to parse
	 * @param dateFormats the date formats to use
	 * @return the parsed date
	 */
	public static Date safeParse(String dateValue, String[] dateFormats) {
		try {
			return parse(dateValue, dateFormats, true);
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Parses the date value using the given date formats.
	 * 
	 * @param dateValue the date value to parse
	 * @param dateFormats the date formats to use
	 * @return the parsed date
	 * @throws ParseException if none of the dataFormats could parse the dateValue
	 */
	private static Date parse(String dateValue, String[] dateFormats, boolean safe) throws ParseException {
		if (dateValue == null) {
			return null;
		}
		if (dateFormats == null) {
			dateFormats = DEFAULT_PATTERNS;
		}

		// trim single quotes around date if present
		if (dateValue.length() > 1 && dateValue.startsWith("'") && dateValue.endsWith("'")) {
			dateValue = dateValue.substring(1, dateValue.length() - 1);
		}

		for (String pattern : dateFormats) {
			try {
				return DateTimes.parse(dateValue, pattern, TimeZones.GMT, Locale.US);
			}
			catch (ParseException pe) {
				// ignore this exception, we will try the next format
			}
		}

		if (safe) {
			return null;
		}
		
		// we were unable to parse the date
		throw new ParseException("Unable to parse the date " + dateValue, -1);
	}
}
