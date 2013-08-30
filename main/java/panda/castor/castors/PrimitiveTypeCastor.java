package panda.castor.castors;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.lang.Numbers;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <S> source type
 * @param <T> target type
 */
public abstract class PrimitiveTypeCastor<S, T> extends AbstractCastor<S, T> {
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
		protected Boolean convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).intValue() != 0;
			}
			
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
		protected Byte convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).byteValue();
			}
			
			return Numbers.toByte(value.toString(), defaultValue());
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
		protected Character convertValue(Object value, CastContext context) {
			if (value instanceof Character) {
				return (Character)value;
			}
			
			if (value instanceof Number) {
				return (char)((Number)value).intValue();
			}

			String s = value.toString();
			if (s.length() > 0) {
				return s.charAt(0);
			}
			
			return defaultValue();
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
		protected Double convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).doubleValue();
			}
			
			return Numbers.toDouble(value.toString(), defaultValue());
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
		protected Float convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).floatValue();
			}
			
			return Numbers.toFloat(value.toString(), defaultValue());
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
		protected Integer convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).intValue();
			}
			
			return Numbers.toInt(value.toString(), defaultValue());
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
		protected Long convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).longValue();
			}
			else if (value instanceof Date) {
				return ((Date)value).getTime();
			}
			else if (value instanceof Calendar) {
				return ((Calendar)value).getTimeInMillis();
			}
			
			return Numbers.toLong(value.toString(), defaultValue());
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
		protected Short convertValue(Object value, CastContext context) {
			if (value instanceof Number) {
				return ((Number)value).shortValue();
			}
			
			return Numbers.toShort(value.toString(), defaultValue());
		}
	}


}
