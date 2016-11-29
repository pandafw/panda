package panda.lang.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * DateParser is the "missing" interface for the parsing methods of {@link java.text.DateFormat}.
 * </p>
 */
public interface DateParser {

	/**
	 * Equivalent to DateFormat.parse(String). See {@link java.text.DateFormat#parse(String)} for
	 * more information.
	 * 
	 * @param source A <code>String</code> whose beginning should be parsed.
	 * @return A <code>Date</code> parsed from the string
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 */
	Date parse(CharSequence source) throws ParseException;

	/**
	 * Equivalent to DateFormat.parse(String, ParsePosition). See
	 * {@link java.text.DateFormat#parse(String, ParsePosition)} for more information.
	 * 
	 * @param source A <code>String</code>, part of which should be parsed.
	 * @param pos A <code>ParsePosition</code> object with index and error index information as
	 *            described above.
	 * @return A <code>Date</code> parsed from the string. In case of error, returns null.
	 * @throws NullPointerException if text or pos is null.
	 */
	Date parse(CharSequence source, ParsePosition pos);

    /**
     * Parses a formatted date string according to the format.  Updates the Calendar with parsed fields.
     * Upon success, the ParsePosition index is updated to indicate how much of the source text was consumed.
     * Not all source text needs to be consumed.  Upon parse failure, ParsePosition error index is updated to
     * the offset of the source text which does not match the supplied format.
     *
     * @param source The text to parse.
     * @param pos On input, the position in the source to start parsing, on output, updated position.
     * @param calendar The calendar into which to set parsed fields.
     * @return true, if source has been parsed (pos parsePosition is updated); otherwise false (and pos errorIndex is updated)
     * @throws IllegalArgumentException when Calendar has been set to be not lenient, and a parsed field is
     * out of range.
     */
    boolean parse(CharSequence source, ParsePosition pos, Calendar calendar);

	// Accessors
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Get the pattern used by this parser.
	 * </p>
	 * 
	 * @return the pattern, {@link java.text.SimpleDateFormat} compatible
	 */
	String getPattern();

	/**
	 * <p>
	 * Get the time zone used by this parser.
	 * </p>
	 * <p>
	 * The default {@link TimeZone} used to create a {@link Date} when the {@link TimeZone} is not
	 * specified by the format pattern.
	 * </p>
	 * 
	 * @return the time zone
	 */
	TimeZone getTimeZone();

	/**
	 * <p>
	 * Get the locale used by this parser.
	 * </p>
	 * 
	 * @return the locale
	 */
	Locale getLocale();

	/**
	 * Parses text from a string to produce a Date.
	 * 
	 * @param source A <code>String</code> whose beginning should be parsed.
	 * @return a <code>java.util.Date</code> object
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 * @see java.text.DateFormat#parseObject(String)
	 */
	Object parseObject(CharSequence source) throws ParseException;

	/**
	 * Parse a date/time string according to the given parse position.
	 * 
	 * @param source A <code>String</code> whose beginning should be parsed.
	 * @param pos the parse position
	 * @return a <code>java.util.Date</code> object
	 * @see java.text.DateFormat#parseObject(String, ParsePosition)
	 */
	Object parseObject(CharSequence source, ParsePosition pos);
}
