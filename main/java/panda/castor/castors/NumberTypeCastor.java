package panda.castor.castors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.lang.Numbers;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class NumberTypeCastor {
	public static class NumberCastor extends AbstractCastor<Object, Number> {
		public NumberCastor() {
			super(Object.class, Number.class);
		}

		@Override
		protected Number convertValue(Object value, CastContext context) {
			if (value instanceof Date) {
				return ((Date)value).getTime();
			}
			else if (value instanceof Calendar) {
				return ((Calendar)value).getTimeInMillis();
			}
			return Numbers.createNumber(value.toString());
		}
	}

	public static class BigDecimalCastor extends AbstractCastor<Object, BigDecimal> {
		public BigDecimalCastor() {
			super(Object.class, BigDecimal.class);
		}

		@Override
		protected BigDecimal convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				Number num = (Number)value;
				return BigDecimal.valueOf(num.longValue());
			}
			
			return Numbers.toBigDecimal(value.toString());
		}
	}

	public static class BigIntegerCastor extends AbstractCastor<Object, BigInteger> {
		public BigIntegerCastor() {
			super(Object.class, BigInteger.class);
		}
		
		@Override
		protected BigInteger convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				Number num = (Number)value;
				return BigInteger.valueOf(num.longValue());
			}
			
			return Numbers.toBigInteger(value.toString());
		}
	}
}

