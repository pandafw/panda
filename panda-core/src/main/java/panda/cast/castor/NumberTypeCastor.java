package panda.cast.castor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import panda.cast.CastContext;
import panda.lang.Numbers;
import panda.vfs.FileItem;

/**
 */
public class NumberTypeCastor {
	public static class NumberCastor extends AnySingleCastor<Number> {
		public NumberCastor() {
			super(Number.class);
		}

		@Override
		protected Number castValue(Object value, CastContext context) {
			if (value instanceof Date) {
				return ((Date)value).getTime();
			}
			if (value instanceof Calendar) {
				return ((Calendar)value).getTimeInMillis();
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createNumber(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof FileItem) {
				return ((FileItem)value).getId();
			}
			return castError(value, context);
		}
	}

	public static class BigDecimalCastor extends AnySingleCastor<BigDecimal> {
		public BigDecimalCastor() {
			super(BigDecimal.class);
		}

		@Override
		protected BigDecimal castValue(Object value, CastContext context) {
			if (value instanceof Date) {
				return BigDecimal.valueOf(((Date)value).getTime());
			}
			if (value instanceof Number) {
				Number num = (Number)value;
				return BigDecimal.valueOf(num.longValue());
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createBigDecimal(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof FileItem) {
				FileItem fi = (FileItem)value;
				return fi.getId() == null ? defaultValue() : BigDecimal.valueOf(fi.getId());
			}
			return castError(value, context);
		}
	}

	public static class BigIntegerCastor extends AnySingleCastor<BigInteger> {
		public BigIntegerCastor() {
			super(BigInteger.class);
		}
		
		@Override
		protected BigInteger castValue(Object value, CastContext context) {
			if (value instanceof Date) {
				return BigInteger.valueOf(((Date)value).getTime());
			}
			if (value instanceof Number) {
				Number num = (Number)value;
				return BigInteger.valueOf(num.longValue());
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createBigInteger(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof FileItem) {
				FileItem fi = (FileItem)value;
				return fi.getId() == null ? defaultValue() : BigInteger.valueOf(fi.getId());
			}
			return castError(value, context);
		}
	}
}

