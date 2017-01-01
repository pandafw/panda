package panda.cast.castor;

/**
 *
 */
public class PrimitiveWrapCastor {
	public static class BooleanCastor extends PrimitiveTypeCastor.BooleanCastor {
		public BooleanCastor() {
			super(Boolean.class);
		}

		@Override
		protected Boolean defaultValue() {
			return null;
		}
	}

	public static class ByteCastor extends PrimitiveTypeCastor.ByteCastor {
		public ByteCastor() {
			super(Byte.class);
		}

		@Override
		protected Byte defaultValue() {
			return null;
		}
	}


	public static class CharacterCastor extends PrimitiveTypeCastor.CharacterCastor {
		public CharacterCastor() {
			super(Character.class);
		}

		@Override
		protected Character defaultValue() {
			return null;
		}
	}

	public static class DoubleCastor extends PrimitiveTypeCastor.DoubleCastor {
		public DoubleCastor() {
			super(Double.class);
		}

		@Override
		protected Double defaultValue() {
			return null;
		}
	}

	public static class FloatCastor extends PrimitiveTypeCastor.FloatCastor {
		public FloatCastor() {
			super(Float.class);
		}

		@Override
		protected Float defaultValue() {
			return null;
		}
	}

	public static class IntegerCastor extends PrimitiveTypeCastor.IntegerCastor {
		public IntegerCastor() {
			super(Integer.class);
		}

		@Override
		protected Integer defaultValue() {
			return null;
		}
	}

	public static class LongCastor extends PrimitiveTypeCastor.LongCastor {
		public LongCastor() {
			super(Long.class);
		}

		@Override
		protected Long defaultValue() {
			return null;
		}
	}

	public static class ShortCastor extends PrimitiveTypeCastor.ShortCastor {
		public ShortCastor() {
			super(Short.class);
		}

		@Override
		protected Short defaultValue() {
			return null;
		}
	}


}
