package panda.cast.castor;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import panda.cast.CastContext;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class DateTypeCastor {
	public final static Map<String, FastDateFormat> DATE_FORMATS = new LinkedHashMap<String, FastDateFormat>();
	public final static Map<String, FastDateFormat> JP_DATE_FORMATS = new LinkedHashMap<String, FastDateFormat>();

	public final static Map<Locale, Map<String, FastDateFormat>> LOCALE_DATE_FORMATS = new HashMap<Locale, Map<String, FastDateFormat>>();
	static {
		DATE_FORMATS.put("timestamp", DateTimes.timestampFormat());
		DATE_FORMATS.put("datetime", DateTimes.datetimeFormat());
		DATE_FORMATS.put("isodate", DateTimes.isoDatetimeFormat());
		DATE_FORMATS.put("isodate2", DateTimes.isoAltDatetimeFormat());
		DATE_FORMATS.put("date", DateTimes.dateFormat());
		DATE_FORMATS.put("time", DateTimes.timeFormat());

		JP_DATE_FORMATS.put("timestamp", FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss.SSS"));
		JP_DATE_FORMATS.put("datetime", FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss"));
		JP_DATE_FORMATS.put("date", FastDateFormat.getInstance("yyyy/MM/dd"));
		
		LOCALE_DATE_FORMATS.put(Locale.JAPAN, JP_DATE_FORMATS);
		LOCALE_DATE_FORMATS.put(Locale.JAPANESE, JP_DATE_FORMATS);
	}

	public static class DateCastor extends AnySingleCastor<Date> {
		private FastDateFormat[] formats;
		
		public DateCastor() {
			this(null);
		}
		
		public DateCastor(FastDateFormat[] formats) {
			super(Date.class);
			this.formats = formats;
		}
		
		/**
		 * getDateFormat
		 *
		 * @param format format
		 * @param locale locale
		 * @return DateFormat object
		 */
		public FastDateFormat getDateFormat(String format, Locale locale) {
			if (format == null) {
				format = "timestamp";
			}

			FastDateFormat fdf = null;
			if (locale != null) {
				Map<String, FastDateFormat> formats = LOCALE_DATE_FORMATS.get(locale);
				if (formats != null) {
					fdf = formats.get(format);
					if (fdf != null) {
						return fdf;
					}
				}
			}
			
			fdf = DATE_FORMATS.get(format);
			if (fdf != null) {
				return fdf;
			}

			try {
				return FastDateFormat.getInstance(format, locale);
			}
			catch (Exception e) {
				//skip
			}

			return DateTimes.timestampFormat();
		}
		
		@Override
		protected Date castValue(Object value, CastContext cc) {
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
			if (value instanceof CharSequence) {
				String sv = value.toString();
				if (sv.length() < 1) {
					return defaultValue();
				}
	
				if (Strings.isNumeric(sv)) {
					long nv = Numbers.toLong(sv, 0L);
					return new Date(nv);
				}
				
				ParseException ex = null;
				if (formats != null) {
					for (FastDateFormat df : formats) {
						try {
							return df.parse(sv);
						}
						catch (ParseException e) {
							ex = e;
						}
					}
					return castError(value, cc, ex);
				}
				
				if (Strings.isNotEmpty(cc.getFormat())) {
					try {
						return getDateFormat(cc.getFormat(), cc.getLocale()).parse(sv);
					}
					catch (ParseException e) {
						ex = e;
					}
					return castError(value, cc, ex);
				}

				if (cc.getLocale() != null) {
					Map<String, FastDateFormat> fs = LOCALE_DATE_FORMATS.get(cc.getLocale());
					if (fs != null) {
						for (FastDateFormat df : fs.values()) {
							try {
								return df.parse(sv);
							}
							catch (ParseException e) {
								ex = e;
							}
						}
					}
				}
				
				for (FastDateFormat df : DATE_FORMATS.values()) {
					try {
						return df.parse(sv);
					}
					catch (ParseException e) {
						ex = e;
					}
				}

				return castError(value, cc, ex);
			}
			return castError(value, cc);
		}
	
		@Override
		public Date castValueTo(Object value, Date target, CastContext context) {
			Date v = castValue(value, context);
			target.setTime(v.getTime());
			return target;
		}
	}

	//------------------------------------------------------------------------------------
	public static class CalendarCastor extends AnySingleCastor<Calendar> {
		private DateCastor dateCastor;
		
		public CalendarCastor(DateCastor dateCastor) {
			super(Calendar.class);
			this.dateCastor = dateCastor;
		}
		
		@Override
		protected Calendar castValue(Object value, CastContext context) {
			Date d = dateCastor.cast(value, context);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c;
		}

		@Override
		protected Calendar castValueTo(Object value, Calendar target, CastContext context) {
			Date d = dateCastor.cast(value, context);
			target.setTime(d);
			return target;
		}
	}

	//------------------------------------------------------------------------------------
	public static class GregorianCalendarCastor extends AnySingleCastor<GregorianCalendar> {
		private DateCastor dateCastor;
		
		public GregorianCalendarCastor(DateCastor dateCastor) {
			super(GregorianCalendar.class);
			this.dateCastor = dateCastor;
		}
		
		@Override
		protected GregorianCalendar castValue(Object value, CastContext context) {
			Date d = dateCastor.cast(value, context);
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(d);
			return c;
		}

		@Override
		protected GregorianCalendar castValueTo(Object value, GregorianCalendar target, CastContext context) {
			Date d = dateCastor.cast(value, context);
			target.setTime(d);
			return target;
		}
	}
	
	//------------------------------------------------------------------------------------
	public static class SqlDateCastor extends AnySingleCastor<java.sql.Date> {
		private DateCastor dateCastor;
		
		public SqlDateCastor(DateCastor dateCastor) {
			super(java.sql.Date.class);
			this.dateCastor = dateCastor;
		}
		
		@Override
		protected java.sql.Date castValue(Object value, CastContext context) {
			Date d = dateCastor.cast(value, context);
			java.sql.Date sd = new java.sql.Date(d.getTime());
			return sd;
		}

		@Override
		protected java.sql.Date castValueTo(Object value, java.sql.Date target, CastContext context) {
			Date d = dateCastor.cast(value, context);
			target.setTime(d.getTime());
			return target;
		}
	}
	
	//------------------------------------------------------------------------------------
	public static class SqlTimeCastor extends AnySingleCastor<java.sql.Time> {
		private DateCastor dateCastor;
		
		public SqlTimeCastor(DateCastor dateCastor) {
			super(java.sql.Time.class);
			this.dateCastor = dateCastor;
		}
		
		@Override
		protected java.sql.Time castValue(Object value, CastContext context) {
			Date d = dateCastor.cast(value, context);
			java.sql.Time st = new java.sql.Time(d.getTime());
			return st;
		}

		@Override
		protected java.sql.Time castValueTo(Object value, java.sql.Time target, CastContext context) {
			Date d = dateCastor.cast(value, context);
			target.setTime(d.getTime());
			return target;
		}
	}
	
	//------------------------------------------------------------------------------------
	public static class SqlTimestampCastor extends AnySingleCastor<java.sql.Timestamp> {
		private DateCastor dateCastor;
		
		public SqlTimestampCastor(DateCastor dateCastor) {
			super(java.sql.Timestamp.class);
			this.dateCastor = dateCastor;
		}
		
		@Override
		protected java.sql.Timestamp castValue(Object value, CastContext context) {
			Date d = dateCastor.cast(value, context);
			java.sql.Timestamp st = new java.sql.Timestamp(d.getTime());
			return st;
		}

		@Override
		protected java.sql.Timestamp castValueTo(Object value, java.sql.Timestamp target, CastContext context) {
			Date d = dateCastor.cast(value, context);
			target.setTime(d.getTime());
			return target;
		}
	}
}
