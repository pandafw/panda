package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.JdbcTypes;
import panda.lang.Strings;

public class MssqlSqlExpert extends SqlExpert {

	@Override
	public List<String> create(Entity<?> entity) {
		List<String> sqls = new ArrayList<String>();

		StringBuilder sb = new StringBuilder("CREATE TABLE " + entity.getTableName() + "(");
		for (EntityField ef : entity.getFields()) {
			sb.append('\n').append(escapeColumn(ef.getColumn()));
			sb.append(' ').append(evalFieldType(ef));
			if (ef.isUnsigned()) {
				sb.append(" UNSIGNED");
			}
			if (ef.isNotNull()) {
				sb.append(" NOT NULL");
			}
			if (ef.isAutoIncrement()) {
				sb.append(" IDENTITY");
			}
			if (ef.hasDefaultValue()) {
				sb.append(" DEFAULT '").append(ef.getDefaultValue()).append('\'');
			}
			sb.append(',');
		}
		
		// append primary keys
		addPrimaryKeys(sb, entity);
		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		addIndexes(sqls, entity);
		addComments(sqls, entity);
		return sqls;
	}

	@Override
	protected String evalFieldType(EntityField ef) {
		if (Strings.isNotEmpty(ef.getDbType())) {
			return super.evalFieldType(ef);
		}
		
		int jdbcType = JdbcTypes.getType(ef.getJdbcType());
		switch (jdbcType) {
		case Types.BOOLEAN:
			return JdbcTypes.BIT;
		case Types.DOUBLE:
			return JdbcTypes.FLOAT;
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return "DATETIME";
		case Types.LONGVARBINARY:
		case Types.BLOB:
			return "IMAGE";
		case Types.CLOB:
		case Types.LONGVARCHAR:
			return "NTEXT";
		default:
			break;
		}
		return super.evalFieldType(ef);
	}

	/**
	 * @param sql sql
	 * @param query query
	 */
	@Override
	protected void setLimitAndOffset(StringBuilder sql, Query query) {
		if (query.getLimit() > 0) {
			int start = query.getStart() > 0 ? query.getStart() : 0;

			// very rough, but works
			String beg = 
					"SELECT * FROM ("
					+ "SELECT ROW_NUMBER() OVER(ORDER BY __tc__) __rn__, * FROM ("
					+ "SELECT TOP " + (start + query.getLimit()) + " 0 __tc__, * FROM (";
			String end = ") t1 ) t2 ) t3 WHERE __rn__ > " + start;
			sql.insert(0, beg);
			sql.append(end);
		}
	}
}
