package panda.castor.castors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.castor.CastException;
import panda.lang.collection.MultiKey;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class DateTypeCastor {
	public static class DateCastor extends AbstractCastor<Object, Date> {
		/**
		 * DateFormat cache
		 */
		private Map<MultiKey, DateFormat> formatCache = new ConcurrentHashMap<MultiKey, DateFormat>();
	
		public static String DATE_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
		public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
		public static String DATE_FORMAT_DATE = "yyyy-MM-dd";
		public static String DATE_FORMAT_TIME = "HH:mm:ss";
		public static String DATE_FORMAT = DATE_FORMAT_TIMESTAMP;
	
		public static String[] DATE_FORMATS = {
			DATE_FORMAT_TIMESTAMP,
			DATE_FORMAT_DATETIME,
			DATE_FORMAT_DATE,
			DATE_FORMAT_TIME,
		};
	
		public DateCastor() {
			super(Object.class, Date.class);
		}
		
		@Override
		protected Date convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				Number num = (Number)value;
				return new Date(num.longValue());
			}
			if (value instanceof Calendar) {
				return ((Calendar)value).getTime();
			}
			if (value instanceof java.sql.Date) {
				return new Date(((java.sql.Date)value).getTime());
			}
			if (value instanceof java.sql.Time) {
				return new Date(((java.sql.Time)value).getTime());
			}
			if (value instanceof java.sql.Timestamp) {
				return new Date(((java.sql.Timestamp)value).getTime());
			}
			
			String sv = value.toString();

			ParseException ex = null;
			for (String f : DATE_FORMATS) {
				try {
					DateFormat df = getDateFormat(f, Locale.getDefault(), null);
					if (df != null) {
						return df.parse(sv);
					}
				}
				catch (ParseException e) {
					ex = e;
				}
			}
			throw new CastException("Failed to convert date: " + value.getClass() + " - " + sv, ex);
		}
	
		/**
		 * get the cached DateFormat
		 * @param pattern pattern string
		 * @param locale locale
		 * @param timezone time zone
		 * @return cached DateFormat
		 */
		private DateFormat getCachedDateFormat(String pattern, Locale locale, TimeZone timezone) {
			MultiKey key = new MultiKey(pattern, locale, timezone);
			return formatCache.get(key);
		}
		
		/**
		 * set the DateFormat to cache
		 * @param dateForamt DateFormat object
		 * @param pattern pattern string
		 * @param locale locale
		 * @param timezone time zone
		 */
		private void setCachedDateFormat(DateFormat dateForamt, String pattern, Locale locale, TimeZone timezone) {
			MultiKey key = new MultiKey(pattern, locale, timezone);
			formatCache.put(key, dateForamt);
		}

		/**
		 * @return default date format
		 */
		public DateFormat getDefaultDateFormat() {
			return getDateFormat(DATE_FORMAT, Locale.getDefault(), null);
		}
	
		/**
		 * getDateFormat
		 *
		 * @param pattern pattern
		 * @param locale locale
		 * @param timezone time zone
		 * @return DateFormat object
		 */
		public DateFormat getDateFormat(String pattern, Locale locale, TimeZone timezone) {
			DateFormat df = getCachedDateFormat(pattern, locale, timezone);
			if (df == null) {
				try {
					df = new SimpleDateFormat(pattern, locale);
					if (timezone != null) {
						df.setTimeZone(timezone);
					}
				}
				catch (Exception e) {
					throw new IllegalArgumentException("The DateFormat pattern [" + pattern + "] is invalid.", e);
				}
				setCachedDateFormat(df, pattern, locale, timezone);
			}
			return df;
		}
	}

	public static class CalendarCastor extends AbstractCastor<Object, Calendar> {
		private DateCastor dateCastor;
		
		public CalendarCastor(DateCastor dateCastor) {
			super(Object.class, Calendar.class);
			this.dateCastor = dateCastor;
		}
		
		@Override
		protected Calendar convertValue(Object value, CastContext context) {
			Date d = dateCastor.cast(value, context);
			if (d == null) {
				return null;
			}

			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c;
		}
	}
}
