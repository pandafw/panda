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
import panda.lang.Strings;

public class SQLiteSqlExpert extends SqlExpert {
	@Override
	public DB getDatabaseType() {
		return DB.SQLITE;
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
			sb.append('\n').append(escapeColumn(ef.getColumn()));
			if (ef.isAutoIncrement()) {
				sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
				continue;
			}

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
		if (entity.getIdentity() == null) {
			addPrimaryKeysConstraint(sb, entity);
		}

		// append foreign keys
		this.addForeignKeysConstraint(sb, entity);

		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		// add indexes
		addIndexes(sqls, entity);
		return sqls;
	}

	@Override
	protected String evalFieldType(EntityField ef) {
		if (Strings.isNotEmpty(ef.getNativeType())) {
			return super.evalFieldType(ef);
		}
		
		int jdbcType = DaoTypes.getType(ef.getJdbcType());
		switch (jdbcType) {
		case Types.BIT:
		case Types.BOOLEAN:
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.CLOB:
		case Types.LONGVARCHAR:
			return "TEXT";
		case Types.REAL:
		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.DECIMAL:
		case Types.NUMERIC:
			return DaoTypes.REAL;
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
			return DaoTypes.INTEGER;
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return DaoTypes.INTEGER;
		case Types.BLOB:
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			return DaoTypes.BLOB;
		default:
			return super.evalFieldType(ef);
		}
	}

	/**
	 * @param sql sql
	 * @param query query
	 * @see <a href="http://sqlite.org/lang_select.html">http://sqlite.org/lang_select.html</a>
	 */
	@Override
	protected void limit(Sql sql, Query query) {
		if (query.getStart() > 0 || query.getLimit() > 0) {
			sql.append(" LIMIT ").append(query.getLimit() > 0 ? query.getLimit() : Integer.MAX_VALUE);
			
			if (query.getStart() > 0) {
				sql.append(" OFFSET ").append(query.getStart());
			}
		}
	}
}
