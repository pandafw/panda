package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.DB;
import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.Sql;
import panda.lang.Strings;

public class PostgreSqlExpert extends SqlExpert {
	@Override
	public DB getDatabaseType() {
		return DB.POSTGRE;
	}

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

			if (ef.isAutoIncrement()) {
				if (JdbcTypes.BIGINT.equals(ef.getJdbcType())) {
					sb.append(" BIGSERIAL");
				}
				else {
					sb.append(" SERIAL");
				}
			}
			else {
				sb.append(' ').append(evalFieldType(ef));
				if (ef.isUnsigned()) {
					sb.append(" UNSIGNED");
				}
				if (ef.isNotNull()) {
					sb.append(" NOT NULL");
				}
				if (ef.hasDefaultValue()) {
					sb.append(" DEFAULT '").append(ef.getDefaultValue()).append('\'');
				}
			}
			sb.append(',');
		}

		// append primary keys
		addPrimaryKeysConstraint(sb, entity);
		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		// alter sequence start value
		EntityField id = entity.getIdentity();
		if (id != null && id.isAutoIncrement() && id.getStartWith() > 1) {
			String sql = "ALTER SEQUENCE " + entity.getTableName() + '_' + id.getColumn() + "_SEQ RESTART WITH " + id.getStartWith();
			sqls.add(sql);
		}

		// add comments & constraints
		addComments(sqls, entity);
		addIndexes(sqls, entity);
		addForeignKeys(sqls, entity);
		return sqls;
	}

	/**
	 * @see <a href="http://www.postgresql.org/docs/8.4/static/datatype.html">http://www.postgresql.org/docs/8.4/static/datatype.html</a>
	 */
	@Override
	protected String evalFieldType(EntityField ef) {
		if (Strings.isNotEmpty(ef.getDbType())) {
			return super.evalFieldType(ef);
		}
		
		int jdbcType = JdbcTypes.getType(ef.getJdbcType());
		switch (jdbcType) {
		case Types.BLOB:
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			return "BYTEA";
		case Types.CLOB:
		case Types.LONGVARCHAR:
			return "TEXT";
		case Types.DOUBLE:
			return "DOUBLE PRECISION";
		case Types.FLOAT:
			return "REAL";
		case Types.TINYINT:
			return JdbcTypes.SMALLINT;
		default:
			return super.evalFieldType(ef);
		}
	}

	/**
	 * @param sql sql
	 * @param query query
	 * @see <a href="http://www.postgresql.org/docs/8.0/static/queries-limit.html">http://www.postgresql.org/docs/8.0/static/queries-limit.html</a>
	 */
	@Override
	protected void limit(Sql sql, Query query) {
		if (query.getLimit() > 0) {
			sql.append(" LIMIT ").append(query.getLimit());
		}
		
		if (query.getStart() > 0) {
			sql.append(" OFFSET ").append(query.getStart());
		}
	}
}
