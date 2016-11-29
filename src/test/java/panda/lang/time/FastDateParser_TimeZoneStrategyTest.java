package panda.lang.time;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class FastDateParser_TimeZoneStrategyTest {

    @Test
    public void testTimeZoneStrategyPattern() {
        for(final Locale locale : Locale.getAvailableLocales()) {
            final FastDateParser parser = new FastDateParser("z", TimeZone.getDefault(), locale);
            final String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
            for(final String[] zone :  zones) {
                for(int t = 1; t<zone.length; ++t) {
                    final String tzDisplay = zone[t];
                    if (tzDisplay == null) {
                        break;
                    }
                    try {
                        parser.parse(tzDisplay);
                    }
                    catch(final Exception ex) {
                        Assert.fail("'" + tzDisplay + "'"
                                + " Locale: '" + locale.getDisplayName() + "'"
                                + " TimeZone: " + zone[0]
                                + " offset: " + t
                                + " defaultLocale: " + Locale.getDefault()
                                + " defaultTimeZone: " + TimeZone.getDefault().getDisplayName()
                                );
                    }
                }
            }
        }
    }

    @Test
    public void testLang1219() throws ParseException {
        final FastDateParser parser = new FastDateParser("dd.MM.yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.GERMAN);

        final Date summer = parser.parse("26.10.2014 02:00:00 MESZ");
        final Date standard = parser.parse("26.10.2014 02:00:00 MEZ");
        Assert.assertNotEquals(summer.getTime(), standard.getTime());
    }
}
