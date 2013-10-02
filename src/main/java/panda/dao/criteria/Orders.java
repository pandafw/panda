package panda.dao.criteria;

import java.util.ArrayList;
import java.util.List;

import panda.lang.Objects;
import panda.lang.Strings;



/**
 * Orders
 * @author yf.frank.wang@gmail.com
 */
public class Orders {
	public static class Entry {
		private String column;
		private Order order;
		
		/**
		 * @param column
		 * @param order
		 */
		public Entry(String column, Order order) {
			super();
			this.column = column;
			this.order = order;
		}
		/**
		 * @return the column
		 */
		public String getColumn() {
			return column;
		}
		/**
		 * @return the order
		 */
		public Order getOrder() {
			return order;
		}
	}

	private List<Entry> entries;

	/**
	 * constructor
	 */
	public Orders() {
		entries = new ArrayList<Entry>();
	}

	/**
	 * @return expressionList
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * isEmpty
	 * @return true/false
	 */
	public boolean isEmpty() {
		return entries.isEmpty();
	}
	
	/**
	 * clear
	 */
	public void clear() {
		entries.clear();
	}


	/**
	 * addOrder
	 * @param column		column
	 * @return this
	 */
	public Orders addOrder(String column) {
		return addOrder(column, true);
	}

	/**
	 * addOrder
	 * @param column		column
	 * @param order		direction
	 * @return this
	 */
	public Orders addOrder(String column, boolean ascend) {
		if (Strings.isNotEmpty(column)) {
			entries.add(new Entry(column, ascend ? Order.ASC : Order.DESC));
		}
		return this;
	}

	/**
	 * addOrderAsc
	 * @param column column
	 * @return this
	 */
	public Orders addOrderAsc(String column) {
		return addOrder(column, true);
	}

	/**
	 * addOrderDesc
	 * @param column column
	 * @return this
	 */
	public Orders addOrderDesc(String column) {
		return addOrder(column, false);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder().append(entries).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Orders rhs = (Orders) obj;
		return Objects.equalsBuilder().append(entries, rhs.entries).isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this).append("entries", entries).toString();
	}
}
