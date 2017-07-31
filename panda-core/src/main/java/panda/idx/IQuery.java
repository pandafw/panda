package panda.idx;

import java.util.Date;

import panda.lang.Asserts;
import panda.lang.time.DateTimes;

public abstract class IQuery {
	/**
	 * Specifies the type of the terms to be sorted, or special types such as CUSTOM
	 */
	public static enum SortType {
		/**
		 * Sort by document score (relevance). Sort values are Float and higher values are at the
		 * front.
		 */
		SCORE,

		/**
		 * Sort by document number (index order). Sort values are Integer and lower values are at
		 * the front.
		 */
		DOC,

		/**
		 * Sort using term values as Strings. Sort values are String and lower values are at the
		 * front.
		 */
		STRING,

		/**
		 * Sort using term values as date.
		 */
		DATE,

		/**
		 * Sort using term values as encoded Integers. Sort values are Integer and lower values are
		 * at the front.
		 */
		INT,

		/**
		 * Sort using term values as encoded Floats. Sort values are Float and lower values are at
		 * the front.
		 */
		FLOAT,

		/**
		 * Sort using term values as encoded Longs. Sort values are Long and lower values are at the
		 * front.
		 */
		LONG,

		/**
		 * Sort using term values as encoded Doubles. Sort values are Double and lower values are at
		 * the front.
		 */
		DOUBLE
	}

	protected StringBuilder query;
	protected long start;
	protected long limit;

	public IQuery() {
		this.query = new StringBuilder();
	}

	/**
	 * clear
	 * @return this
	 */
	public IQuery clear() {
		query.setLength(0);
		start = 0L;
		limit = 0L;
		
		return this;
	}
	
	//---------------------------------------------------------------
	// query
	//
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query.toString();
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query.setLength(0);
		this.query.append(query);
	}

	protected void addSpace() {
		if (query.length() > 0) {
			query.append(' ');
		}
	}

	public IQuery and() {
		addSpace();
		query.append("AND");
		return this;
	}

	public IQuery or() {
		addSpace();
		query.append("OR");
		return this;
	}
	
	public IQuery not() {
		addSpace();
		query.append("NOT");
		return this;
	}
	
	public IQuery begin() {
		addSpace();
		query.append("(");
		return this;
	}
	
	public IQuery end() {
		addSpace();
		query.append(")");
		return this;
	}
	
	public IQuery field(String field) {
		addSpace();
		query.append(field);
		return this;
	}
	
	public IQuery value(String value) {
		addSpace();
		query.append(value);
		return this;
	}
	
	public IQuery value(Number value) {
		addSpace();
		query.append(value);
		return this;
	}

	public IQuery value(Date value) {
		addSpace();
		query.append(DateTimes.isoDatetimeNohFormat().format(value));
		return this;
	}
	
	public IQuery equal() {
		addSpace();
		query.append(':');
		return this;
	}
	
	public IQuery lessThan() {
		addSpace();
		query.append('<');
		return this;
	}
	
	public IQuery lessEqual() {
		addSpace();
		query.append("<=");
		return this;
	}
	
	public IQuery greatThan() {
		addSpace();
		query.append('>');
		return this;
	}
	
	public IQuery greatEqual() {
		addSpace();
		query.append(">=");
		return this;
	}
	
	//---------------------------------------------------------------
	// start & limit
	//
	/**
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(long start) {
		Asserts.isTrue(start >= 0, "The start must >= 0");
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public long getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(long limit) {
		Asserts.isTrue(limit >= 0, "The limit must >= 0");
		this.limit = limit;
	}

	/**
	 * @param start the start to set
	 * @return this
	 */
	public IQuery start(long start) {
		setStart(start);
		return this;
	}

	/**
	 * @param limit the limit to set
	 * @return this
	 */
	public IQuery limit(long limit) {
		setLimit(limit);
		return this;
	}

	/**
	 * is this query needs paginate
	 * @return true if start or limit > 0
	 */
	public boolean needsPaginate() {
		return start > 0 || limit > 0;
	}

	//---------------------------------------------------------------
	// sort
	//
	/**
	 * add a sort
	 * @param field field
	 * @param type sort type
	 * @param desc descend
	 * @return this
	 */
	public abstract IQuery sort(String field, SortType type, boolean desc);

	@Override
	public String toString() {
		return "IQuery [query=" + query + ", start=" + start + ", limit=" + limit + "]";
	}
}
