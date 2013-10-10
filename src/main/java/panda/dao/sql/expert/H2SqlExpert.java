package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.JdbcTypes;
import panda.lang.Strings;

public class H2SqlExpert extends SqlExpert {
	@Override
	public boolean isSupportDropIfExists() {
		return true;
	}

	@Override
	public String dropTable(String tableName) {
		return "DROP TABLE IF EXISTS " + escapeTable(tableName);
	}

	@Override
	public List<String> create(Entity<?> entity) {
		List<String> sqls = new ArrayList<String>();
		
		StringBuilder sb = new StringBuilder("CREATE TABLE " + entity.getTableName() + "(");
		for (EntityField ef : entity.getFields()) {
			sb.append('\n').append(ef.getColumn());
			sb.append(' ').append(evalFieldType(ef));
			
			// unsupported
//			if (ef.isUnsigned()) {
//				sb.append(" UNSIGNED");
//			}

			if (ef.isAutoIncrement()) {
				sb.append(" AUTO_INCREMENT(").append(ef.getStartWith()).append(")");
			}
			else if (ef.isNotNull()) {
				sb.append(" NOT NULL");
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


	/**
	 * @see http://www.h2database.com/html/datatypes.html
	 */
	@Override
	protected String evalFieldType(EntityField ef) {
		if (Strings.isNotEmpty(ef.getDbType())) {
			return super.evalFieldType(ef);
		}
		
		int jdbcType = JdbcTypes.getType(ef.getJdbcType());
		switch (jdbcType) {
		case Types.BIT:
			return JdbcTypes.BOOLEAN;
		case Types.LONGVARCHAR:
			return JdbcTypes.VARCHAR;
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			return JdbcTypes.BINARY;
		default:
			return super.evalFieldType(ef);
		}
	}

	/**
	 * @param sql sql
	 * @param query query
	 * @see http://www.h2database.com/html/grammar.html
	 */
	@Override
	protected void limit(StringBuilder sql, Query query) {
		if (query.getStart() > 0 || query.getLimit() > 0) {
			sql.append(" LIMIT ").append(query.getLimit() > 0 ? query.getLimit() : Integer.MAX_VALUE);
			
			if (query.getStart() > 0) {
				sql.append(" OFFSET ").append(query.getStart());
			}
		}
	}
}

