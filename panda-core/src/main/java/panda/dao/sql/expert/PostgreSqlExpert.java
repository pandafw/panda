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
	public boolean isSupportAutoIncrement() {
		return false;
	}
	
	@Override
	public String prepIdentity(Entity<?> entity) {
		return "SELECT NEXTVAL('" + getSequence(entity) + "') AS ID";
	}

	@Override
	public String dropTable(String tableName) {
		return "DROP TABLE IF EXISTS " + escapeTable(tableName);
	}

	@Override
	public List<String> drop(Entity<?> entity) {
		List<String> sqls = super.drop(entity);
		
		EntityField eid = entity.getIdentity();
		if (eid != null && eid.isAutoIncrement()) {
			String sql = "DROP SEQUENCE IF EXISTS " + getSequence(entity);
			sqls.add(sql);
		}
		return sqls;
	}

	@Override
	public List<String> create(Entity<?> entity) {
		List<String> sqls = new ArrayList<String>();

		StringBuilder sb = new StringBuilder("CREATE TABLE " + escapeTable(client.getTableName(entity)) + " (");
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly()) {
				continue;
			}

			sb.append(Streams.LINE_SEPARATOR);
			sb.append(escapeColumn(ef.getColumn()));

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
			sb.append(',');
		}

		// append primary keys
		addPrimaryKeysConstraint(sb, entity);
		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		// add sequence
		EntityField eid = entity.getIdentity();
		if (eid != null && eid.isAutoIncrement()) {
			String sql = "CREATE SEQUENCE " + getSequence(entity) + " START WITH " + eid.getIdStartWith();
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
