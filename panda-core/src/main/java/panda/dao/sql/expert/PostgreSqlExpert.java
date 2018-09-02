package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.DB;
import panda.dao.DaoTypes;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.Query;
import panda.dao.sql.Sql;
import panda.io.Streams;
import panda.lang.Strings;

public class PostgreSqlExpert extends SqlExpert {
	@Override
	public DB getDatabaseType() {
		return DB.POSTGRE;
	}

	@Override
	public String escape(String s) {
		return '"' + s + '"'; 
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

		StringBuilder sb = new StringBuilder("CREATE TABLE " + escapeTable(client.getTableName(entity)) + " (");
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly()) {
				continue;
			}

			sb.append(Streams.EOL);
			sb.append(escapeColumn(ef.getColumn()));

			if (ef.isAutoIncrement()) {
				if (DaoTypes.BIGINT.equals(ef.getJdbcType())) {
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
		EntityField eid = entity.getIdentity();
		if (eid != null && eid.isAutoIncrement() && eid.getIdStartWith() > 1) {
			String sql = "ALTER SEQUENCE " + client.getTableName(entity) + '_' + eid.getColumn() + "_SEQ RESTART WITH " + eid.getIdStartWith();
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
		if (Strings.isNotEmpty(ef.getNativeType())) {
			return super.evalFieldType(ef);
		}
		
		int jdbcType = DaoTypes.getType(ef.getJdbcType());
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
			return DaoTypes.SMALLINT;
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
	protected void limit(Sql sql, Query<?> query, String alias) {
		if (query.getLimit() > 0) {
			sql.append(" LIMIT ").append(query.getLimit());
		}
		
		if (query.getStart() > 0) {
			sql.append(" OFFSET ").append(query.getStart());
		}
	}
}
