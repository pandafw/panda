package panda.lang.time;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Unit tests for the print methods of FastDateFormat
 */
public class FastDateFormat_PrinterTest extends FastDatePrinterTest {

	@Override
	protected DatePrinter getInstance(final String format, final TimeZone timeZone, final Locale locale) {
		return FastDateFormat.getInstance(format, timeZone, locale);
	}
}
