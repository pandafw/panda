package panda.lang.time;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Unit tests for the parse methods of FastDateFormat
 */
public class FastDateFormat_ParserTest extends FastDateParserTest {

	@Override
	protected DateParser getInstance(final String format, final TimeZone timeZone, final Locale locale) {
		return FastDateFormat.getInstance(format, timeZone, locale);
	}
}
