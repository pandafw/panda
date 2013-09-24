package panda.castor.castors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import panda.castor.CastContext;
import panda.castor.Castor;
import panda.lang.Numbers;

/**
 * @author yf.frank.wang@gmail.com
 */
public class NumberTypeCastor {
	public static class NumberCastor extends Castor<Object, Number> {
		public NumberCastor() {
			super(Object.class, Number.class);
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
				return Numbers.createNumber(value.toString());
			}
			throw castError(value, context);
		}
	}

	public static class BigDecimalCastor extends Castor<Object, BigDecimal> {
		public BigDecimalCastor() {
			super(Object.class, BigDecimal.class);
		}

		@Override
		protected BigDecimal castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				Number num = (Number)value;
				return BigDecimal.valueOf(num.longValue());
			}
			if (value instanceof CharSequence) {
				return Numbers.createBigDecimal(value.toString());
			}
			throw castError(value, context);
		}
	}

	public static class BigIntegerCastor extends Castor<Object, BigInteger> {
		public BigIntegerCastor() {
			super(Object.class, BigInteger.class);
		}
		
		@Override
		protected BigInteger castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				Number num = (Number)value;
				return BigInteger.valueOf(num.longValue());
			}
			if (value instanceof CharSequence) {
				return Numbers.createBigInteger(value.toString());
			}
			throw castError(value, context);
		}
	}
}

