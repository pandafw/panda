package panda.dao.sql.expert;

import panda.dao.query.Query;
import panda.dao.sql.Sql;


public class Mssql2012SqlExpert extends Mssql2005SqlExpert {
	/**
	 * @param sql sql
	 * @param query query
	 */
	@Override
	protected void limit(Sql sql, Query<?> query, String alias) {
		if (query.hasOrders()) {
			// offset needs order
			// @see http://technet.microsoft.com/en-us/library/gg699618.aspx
			sql.append(" OFFSET ").append(query.getStart()).append(" ROWS");
			if (query.getLimit() > 0) {
				sql.append(" FETCH NEXT ").append(query.getLimit()).append(" ROWS ONLY");
			}
		}
		else {
			super.limit(sql, query, alias);
		}
	}
}
