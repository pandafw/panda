package panda.dao.criteria;

import panda.lang.Asserts;
import panda.lang.Objects;


/**
 * @author yf.frank.wang@gmail.com
 */
public class Expression {
	protected Operator operator;
	
	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		if (operator != Operator.OPEN_PAREN && operator != Operator.CLOSE_PAREN) {
			throw new IllegalArgumentException("operator '" + operator + "' must be " + Operator.OPEN_PAREN + " | " + Operator.CLOSE_PAREN);
		}
		this.operator = operator;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("operator", operator)
				.toString();
	}
	
	public static class Paren extends Expression {
		/**
		 * Constructor
		 * @param operator	operator
		 */
		public Paren(Operator operator) {
			setOperator(operator);
		}
		
		/**
		 * @param operator the operator to set
		 */
		public void setOperator(Operator operator) {
			if (operator != Operator.OPEN_PAREN && operator != Operator.CLOSE_PAREN) {
				throw new IllegalArgumentException("operator '" + operator + "' must be " + Operator.OPEN_PAREN + " | " + Operator.CLOSE_PAREN);
			}
			this.operator = operator;
		}
	}

	public static class AndOr extends Expression {
		/**
		 * Constructor
		 * @param operator	operator
		 */
		public AndOr(Operator operator) {
			setOperator(operator);
		}
		
		/**
		 * @param operator the operator to set
		 */
		public void setOperator(Operator operator) {
			if (operator != Operator.AND && operator != Operator.OR) {
				throw new IllegalArgumentException("operator '" + operator + "' must be " + Operator.AND + " | " + Operator.OR);
			}
			this.operator = operator;
		}

	}

	public static class Simple extends Expression {
		protected String field;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 */
		public Simple(String field, Operator operator) {
			setField(field);
			setOperator(operator);
		}

		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @param field the field to set
		 */
		public void setField(String field) {
			Asserts.notEmpty(field, "The field is empty.");
			this.field = field;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return Objects.toStringBuilder(this)
					.append("field", field)
					.append("operator", operator)
					.toString();
		}
	}

	public static class ValueCompare extends Expression {
		protected String field;
		protected Object value;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 * @param value value
		 */
		public ValueCompare(String field, Operator operator, Object value) {
			setField(field);
			setOperator(operator);
			setValue(value);
		}

		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @param field the field to set
		 */
		public void setField(String field) {
			Asserts.notEmpty(field, "The field is empty.");
			this.field = field;
		}

		/**
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(Object value) {
			Asserts.notNull(value, "The value is null.");
			this.value = value;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return Objects.toStringBuilder(this)
					.append("field", field)
					.append("operator", operator)
					.append("value", value)
					.toString();
		}
	}

	public static class FieldCompare extends Expression {
		protected String field;
		protected String value;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 * @param value value
		 */
		public FieldCompare(String field, Operator operator, String value) {
			setField(field);
			setOperator(operator);
			setValue(value);
		}

		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @param field the field to set
		 */
		public void setField(String field) {
			Asserts.notEmpty(field, "The field is empty.");
			this.field = field;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			Asserts.notEmpty(value, "The value is empty.");
			this.value = value;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return Objects.toStringBuilder(this)
					.append("field", field)
					.append("operator", operator)
					.append("value", value)
					.toString();
		}
	}

}
