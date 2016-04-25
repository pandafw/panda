package panda.net;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import panda.lang.TimeZones;
import panda.lang.time.FastDateFormat;

/**
 * A utility class for parsing and formatting HTTP dates as used in cookies and other headers. This
 * class handles dates as defined by RFC 2616 section 3.3.1 as well as some other common
 * non-standard formats.
 */
public class InetDates {
	/**
	 * Date format pattern used to parse date headers in RFC 1123 format.
	 */
	public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

	/**
	 * Date format used to format date headers in RFC 1123 format.
	 */
	public static final FastDateFormat FDF_RFC1123 = FastDateFormat.getInstance(PATTERN_RFC1123, TimeZones.GMT, Locale.US);
	
	/**
	 * Formats the given date according to the RFC 1123 pattern.
	 * 
	 * @param date The date to format.
	 * @return An RFC 1123 formatted date string.
	 * @see #PATTERN_RFC1123
	 */
	public static String format(Date date) {
		return FDF_RFC1123.format(date);
	}

	/**
	 * Formats the given date according to the RFC 1123 pattern.
	 * 
	 * @param date The date to format.
	 * @return An RFC 1123 formatted date string.
	 * @see #PATTERN_RFC1123
	 */
	public static String format(long date) {
		return FDF_RFC1123.format(date);
	}

	/**
	 * Parses a date value.
	 * 
	 * @param value the date value to parse
	 * @return the parsed date
	 * @throws ParseException if the value could not be parsed using any of the supported date
	 *             formats
	 */
	public static Date parse(String value) throws ParseException {
		return FDF_RFC1123.parse(value);
	}

	/**
	 * Parses a date value.
	 * 
	 * @param value the date value to parse
	 * @return the parsed date
	 */
	public static Date safeParse(String value) {
		try {
			return parse(value);
		}
		catch (ParseException e) {
			return null;
		}
	}
}
