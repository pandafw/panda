package panda.castor.castors;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import panda.castor.CastContext;
import panda.castor.Castor;
import panda.lang.Numbers;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <S> source type
 * @param <T> target type
 */
public abstract class PrimitiveTypeCastor<S, T> extends Castor<S, T> {
	public PrimitiveTypeCastor(Type type) {
		super(Object.class, type);
	}
	
	public static class BooleanCastor extends PrimitiveTypeCastor<Object, Boolean> {
		protected BooleanCastor(Type type) {
			super(type);
		}
		public BooleanCastor() {
			super(Boolean.TYPE);
		}
		
		@Override
		protected Boolean defaultValue() {
			return false;
		}

		@Override
		protected Boolean castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).intValue() != 0;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return false;
				}
				
				switch (s.charAt(0)) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case 'Y':
				case 'y':
				case 'T':
				case 't':
				case 'O':
				case 'o':
					return true;
				default:
					return false;
				}
			}
			throw castError(value, context);
		}
	}

	public static class ByteCastor extends PrimitiveTypeCastor<Object, Byte> {
		protected ByteCastor(Type type) {
			super(type);
		}
		public ByteCastor() {
			super(Byte.TYPE);
		}
		
		@Override
		protected Byte defaultValue() {
			return 0;
		}

		@Override
		protected Byte castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).byteValue();
			}
			if (value instanceof CharSequence) {
				return Numbers.toByte(value.toString(), defaultValue());
			}
			throw castError(value, context);
		}
	}


	public static class CharacterCastor extends PrimitiveTypeCastor<Object, Character> {
		protected CharacterCastor(Type type) {
			super(type);
		}
		public CharacterCastor() {
			super(Character.TYPE);
		}
		
		@Override
		protected Character defaultValue() {
			return 0;
		}

		@Override
		protected Character castValue(Object value, CastContext context) {
			if (value instanceof Character) {
				return (Character)value;
			}
			if (value instanceof Number) {
				return (char)((Number)value).intValue();
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() > 0) {
					return s.charAt(0);
				}
			}
			throw castError(value, context);
		}
	}

	public static class DoubleCastor extends PrimitiveTypeCastor<Object, Double> {
		protected DoubleCastor(Type type) {
			super(type);
		}
		
		public DoubleCastor() {
			super(Double.TYPE);
		}
		
		@Override
		protected Double defaultValue() {
			return (double)0;
		}

		@Override
		protected Double castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).doubleValue();
			}
			if (value instanceof CharSequence) {
				return Numbers.toDouble(value.toString(), defaultValue());
			}
			throw castError(value, context);
		}
	}

	public static class FloatCastor extends PrimitiveTypeCastor<Object, Float> {
		protected FloatCastor(Type type) {
			super(type);
		}

		public FloatCastor() {
			super(Float.TYPE);
		}
		
		@Override
		protected Float defaultValue() {
			return (float)0;
		}

		@Override
		protected Float castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).floatValue();
			}
			if (value instanceof CharSequence) {
				return Numbers.toFloat(value.toString(), defaultValue());
			}
			throw castError(value, context);
		}
	}

	public static class IntegerCastor extends PrimitiveTypeCastor<Object, Integer> {
		protected IntegerCastor(Type type) {
			super(type);
		}

		public IntegerCastor() {
			super(Integer.TYPE);
		}
		
		@Override
		protected Integer defaultValue() {
			return 0;
		}

		@Override
		protected Integer castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).intValue();
			}
			if (value instanceof CharSequence) {
				return Numbers.toInt(value.toString(), defaultValue());
			}
			throw castError(value, context);
		}
	}

	public static class LongCastor extends PrimitiveTypeCastor<Object, Long> {
		protected LongCastor(Type type) {
			super(type);
		}
		public LongCastor() {
			super(Long.TYPE);
		}
		
		@Override
		protected Long defaultValue() {
			return 0L;
		}

		@Override
		protected Long castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).longValue();
			}
			if (value instanceof java.sql.Date) {
				return ((java.sql.Date)value).getTime();
			}
			if (value instanceof java.sql.Time) {
				return ((java.sql.Time)value).getTime();
			}
			if (value instanceof java.sql.Timestamp) {
				return ((java.sql.Timestamp)value).getTime();
			}
			if (value instanceof Date) {
				return ((Date)value).getTime();
			}
			if (value instanceof Calendar) {
				return ((Calendar)value).getTimeInMillis();
			}
			if (value instanceof CharSequence) {
				return Numbers.toLong(value.toString(), defaultValue());
			}
			throw castError(value, context);
		}
	}

	public static class ShortCastor extends PrimitiveTypeCastor<Object, Short> {
		protected ShortCastor(Type type) {
			super(type);
		}
		public ShortCastor() {
			super(Short.TYPE);
		}
		
		@Override
		protected Short defaultValue() {
			return 0;
		}

		@Override
		protected Short castValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).shortValue();
			}
			if (value instanceof CharSequence) {
				return Numbers.toShort(value.toString(), defaultValue());
			}
			throw castError(value, context);
		}
	}
}
