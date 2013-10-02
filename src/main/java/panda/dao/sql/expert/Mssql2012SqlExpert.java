package panda.dao.sql.expert;

import panda.dao.criteria.Query;

public class Mssql2012SqlExpert extends Mssql2005SqlExpert {
	/**
	 * @param sql sql
	 * @param query query
	 */
	@Override
	protected void setLimitAndOffset(StringBuilder sql, Query query) {
		if (query.getStart() > 0) {
			sql.append(" OFFSET ").append(query.getStart()).append(" ROWS");
		}
		if (query.getLimit() > 0) {
			sql.append(" FETCH NEXT ").append(query.getLimit()).append(" ROWS ONLY");
		}
	}
}
