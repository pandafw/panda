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
	protected static class OperatorFilter extends Filter {
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

			Asserts.notEmpty(field, "The field is empty.");
			this.field = field;
		}

		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return field + ' ' + operator;
		}
	}

	public static class ValueFilter extends SimpleFilter {
		protected Object value;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 * @param value value
		 */
		public ValueFilter(String field, Operator operator, Object value) {
			super(field, operator);

			Asserts.notNull(value, "The value is null.");
			this.value = value;
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
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return field + ' ' + operator + ' ' + Objects.toString(value);
		}
	}

	public static class ReferFilter extends SimpleFilter {
		protected String refer;
		
		/**
		 * Constructor
		 * @param field field
		 * @param operator	operator
		 * @param refer refer
		 */
		public ReferFilter(String field, Operator operator, String refer) {
			super(field, operator);

			Asserts.notEmpty(refer, "The refer is empty.");
			this.refer = refer;
		}

		/**
		 * @return the refer
		 */
		public String getRefer() {
			return refer;
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

		public boolean hasFilter(String name) {
			for (Filter f : filters) {
				if (f instanceof SimpleFilter) {
					if (((SimpleFilter)f).getField().equals(name)) {
						return true;
					}
				}
				else if (f instanceof ComboFilter) {
					if (((ComboFilter)f).hasFilter(name)) {
						return true;
					}
				}
			}
			return false;
		}

		public ComboFilter clone() {
			ComboFilter copy = new ComboFilter(this.logical);
			copy.filters.addAll(this.filters);
			return copy;
		}

		/**
		 * @return a string representation of the object.
		 */
		@Override
		public String toString() {
			return Objects.toStringBuilder()
					.append("logical", logical)
					.append("filters", filters)
					.toString();
		}
	}
}
