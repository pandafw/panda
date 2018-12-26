package panda.cast.castor;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import panda.cast.CastContext;
import panda.lang.Numbers;


/**
 * 
 *
 * @param <T> target type
 */
public abstract class PrimitiveTypeCastor<T> extends AnySingleCastor<T> {
	public PrimitiveTypeCastor(Type type) {
		super(type);
	}
	
	public static class BooleanCastor extends PrimitiveTypeCastor<Boolean> {
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
			if (value instanceof Boolean) {
				return (Boolean)value;
			}
			if (value instanceof Number) {
				return ((Number)value).intValue() != 0;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
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
					return true;
				case '0':
				case 'f':
				case 'F':
				case 'n':
				case 'N':
					return false;
				case 'O':
				case 'o':
					if (s.length() > 1) {
						char c2 = s.charAt(1);
						if (c2 == 'n' || c2 == 'N') {
							return true;
						}
						if (c2 == 'f' || c2 == 'F') {
							return false;
						}
					}
					return defaultValue();
				default:
					return defaultValue();
				}
			}
			return castError(value, context);
		}
	}

	public static class ByteCastor extends PrimitiveTypeCastor<Byte> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? (byte)1 : (byte)0;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createByte(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof Number) {
				return ((Number)value).byteValue();
			}
			return castError(value, context);
		}
	}


	public static class CharacterCastor extends PrimitiveTypeCastor<Character> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? '1' : '0';
			}
			if (value instanceof Character) {
				return (Character)value;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				return s.charAt(0);
			}
			if (value instanceof Number) {
				return (char)((Number)value).intValue();
			}
			return castError(value, context);
		}
	}

	public static class DoubleCastor extends PrimitiveTypeCastor<Double> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? 1D : 0D;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createDouble(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof Number) {
				return ((Number)value).doubleValue();
			}
			return castError(value, context);
		}
	}

	public static class FloatCastor extends PrimitiveTypeCastor<Float> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? 1F : 0F;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createFloat(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof Number) {
				return ((Number)value).floatValue();
			}
			return castError(value, context);
		}
	}

	public static class IntegerCastor extends PrimitiveTypeCastor<Integer> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? 1 : 0;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createInteger(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof Number) {
				return ((Number)value).intValue();
			}
			return castError(value, context);
		}
	}

	public static class LongCastor extends PrimitiveTypeCastor<Long> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? 1L : 0L;
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
					return Numbers.createLong(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof Date) {
				return ((Date)value).getTime();
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
			if (value instanceof Number) {
				return ((Number)value).longValue();
			}
			return castError(value, context);
		}
	}

	public static class ShortCastor extends PrimitiveTypeCastor<Short> {
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
			if (value instanceof Boolean) {
				return ((Boolean)value).booleanValue() ? (short)1 : (short)0;
			}
			if (value instanceof CharSequence) {
				String s = value.toString();
				if (s.length() < 1) {
					return defaultValue();
				}
				try {
					return Numbers.createShort(s);
				}
				catch (NumberFormatException e) {
					return castError(value, context, e);
				}
			}
			if (value instanceof Number) {
				return ((Number)value).shortValue();
			}
			return castError(value, context);
		}
	}
}
