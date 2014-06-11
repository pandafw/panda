package panda.dao.query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import panda.lang.Asserts;
import panda.lang.Objects;


/**
 * @author yf.frank.wang@gmail.com
 */
public class Filter {
	public static class OperatorFilter extends Filter {
		protected Operator operator;
		
		/**
		 * @param operator
		 */
		protected OperatorFilter(Operator operator) {
			this.operator = operator;
		}
	
		/**
		 * @return the operator
		 */
		public Operator getOperator() {
			return operator;
		}
	
		/**
		 * @param operator the operator to set
		 */
		protected void setOperator(Operator operator) {
			this.operator = operator;
		}
	
		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return operator.toString();
		}
	}
	
	public static class SimpleFilter extends OperatorFilter {
		protected String field;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 */
		public SimpleFilter(String field, Operator operator) {
			super(operator);
			setField(field);
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
		protected void setField(String field) {
			Asserts.notEmpty(field, "The field is empty.");
			this.field = field;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return field + ' ' + operator;
		}
	}

	public static class ValueFilter extends OperatorFilter {
		protected String field;
		protected Object value;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 * @param value value
		 */
		public ValueFilter(String field, Operator operator, Object value) {
			super(operator);
			setField(field);
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
		protected void setField(String field) {
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
		 * @return the value
		 */
		public Object getValue(int index) {
			return Array.get(value, index);
		}

		/**
		 * @param value the value to set
		 */
		protected void setValue(Object value) {
			Asserts.notNull(value, "The value is null.");
			this.value = value;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return field + ' ' + operator + ' ' + Objects.toString(value);
		}
	}

	public static class ReferFilter extends OperatorFilter {
		protected String field;
		protected String refer;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 * @param refer refer
		 */
		public ReferFilter(String field, Operator operator, String refer) {
			super(operator);
			setField(field);
			setRefer(refer);
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
		protected void setField(String field) {
			Asserts.notEmpty(field, "The field is empty.");
			this.field = field;
		}

		/**
		 * @return the refer
		 */
		public String getRefer() {
			return refer;
		}

		/**
		 * @param refer the refer to set
		 */
		protected void setRefer(String refer) {
			Asserts.notEmpty(refer, "The refer is empty.");
			this.refer = refer;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return field + ' ' + operator + ' ' + refer;
		}
	}

	public static class ComboFilter extends Filter implements Cloneable {
		private Logical logical;
		private List<Filter> filters = new ArrayList<Filter>();

		/**
		 * @param logical
		 */
		public ComboFilter(Logical logical) {
			this.logical = logical;
		}
		
		/**
		 * @return the logical
		 */
		public Logical getLogical() {
			return logical;
		}

		/**
		 * @param logical the logical to set
		 */
		public void setLogical(Logical logical) {
			this.logical = logical;
		}

		/**
		 * @return the filters
		 */
		public List<Filter> getFilters() {
			return filters;
		}

		/**
		 * @param filters the filters to set
		 */
		public void setFilters(List<Filter> filters) {
			this.filters = filters;
		}

		/**
		 * @return the first filter
		 */
		public Filter first() {
			return filters.get(0);
		}

		/**
		 * @return the last filter
		 */
		public Filter last() {
			return filters.get(filters.size() - 1);
		}

		public void add(Filter filter) {
			filters.add(filter);
		}

		public void clear() {
			this.filters.clear();
		}
		
		public boolean isEmpty() {
			return filters.isEmpty();
		}
		
		public ComboFilter clone() {
			ComboFilter copy = new ComboFilter(this.logical);
			copy.filters.addAll(this.filters);
			return copy;
		}
	}
}
