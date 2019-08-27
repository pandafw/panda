package panda.idx;

import java.util.Date;

public interface IQuery {
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

	/**
	 * clear
	 * @return this
	 */
	IQuery clear();
	
	//---------------------------------------------------------------
	// query
	//
	public IQuery and();

	public IQuery or();

	public IQuery field(String field);

	public IQuery match(String text);
	
	public IQuery eq(String... values);
	
	public IQuery eq(Number... values);
	
	public IQuery eq(Date... values);
	
	public IQuery ne(String... values);
	
	public IQuery ne(Number... values);
	
	public IQuery ne(Date... values);
	
	public IQuery lt(Number value);
	
	public IQuery lt(Date value);
	
	public IQuery le(Number value);
	
	public IQuery le(Date value);
	
	public IQuery gt(Number value);
	
	public IQuery gt(Date value);
	
	public IQuery ge(Number value);
	
	public IQuery ge(Date value);
	
	//---------------------------------------------------------------
	// start & limit
	//
	/**
	 * @param start the start to set
	 * @return this
	 */
	public IQuery start(long start);

	/**
	 * @param limit the limit to set
	 * @return this
	 */
	public IQuery limit(long limit);

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
	public IQuery sort(SortType type, boolean desc);
}
