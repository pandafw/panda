package panda.dao.restriction;

import panda.dao.criteria.Query;


/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractRestriction<E extends Query> {
	
	protected String property;
	protected String column;
	protected String alias;

	protected E example;

	/**
	 * @param example example
	 * @param name name
	 */
	public AbstractRestriction(E example, String name) {
		this(example, name, name, name);
	}

	/**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
	public AbstractRestriction(E example, String property, String column, String alias) {
		super();
		this.example = example;
		this.property = property;
		this.alias = alias;
		this.column = column;
	}

	/**
	 * @return example
	 */
	public E exclude() {
		example.addExclude(property);
		return example;
	}

	/**
	 * @return example
	 */
	public E include() {
		example.removeExclude(property);
		return example;
	}
}
