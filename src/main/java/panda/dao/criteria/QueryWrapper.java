package panda.dao.criteria;



/**
 * @author yf.frank.wang@gmail.com
 */
public class QueryWrapper {
	protected Query query;

	/**
	 * Constructor
	 */
	public QueryWrapper() {
		query = new Query();
	}
	
	/**
	 * @param query the query to set
	 */
	public QueryWrapper(Query query) {
		this.query = query;
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}
	
}

