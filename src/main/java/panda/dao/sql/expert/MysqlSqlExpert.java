package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.JdbcTypes;
import panda.lang.Strings;

public class MysqlSqlExpert extends SqlExpert {

	private static final String META_ENGINE = "mysql-engine";

	private static final String META_CHARSET = "mysql-charset";

	protected String escapeTable(String table) {
		return '`' + table + '`'; 
	}
	
	protected String escapeColumn(String column) {
		return '`' + column + '`'; 
	}
	
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
			else if (JdbcTypes.TIMESTAMP.equals(ef.getJdbcType())) {
				sb.append(" NULL");
			}
			
			if (ef.isAutoIncrement()) {
				sb.append(" AUTO_INCREMENT");
			}

			if (JdbcTypes.TIMESTAMP.equals(ef.getJdbcType())) {
				if (ef.hasDefaultValue()) {
					sb.append(' ').append(ef.getDefaultValue());
				}
				else {
					if (ef.isNotNull()) {
						sb.append(" DEFAULT 0");
					}
					else {
						sb.append(" DEFAULT NULL");
					}
				}
			}
			else if (ef.hasDefaultValue()) {
				sb.append(" DEFAULT '").append(ef.getDefaultValue()).append('\'');
			}

			if (Strings.isNotEmpty(ef.getComment())) {
				sb.append(" COMMENT '").append(ef.getComment()).append("'");
			}
			sb.append(',');
		}

		addPrimaryKeys(sb, entity);
		sb.setCharAt(sb.length() - 1, ')');
		
		EntityField id = entity.getIdentity();
		if (id != null && id.isAutoIncrement() && id.getStartWith() > 1) {
			sb.append(" AUTO_INCREMENT=").append(id.getStartWith());
		}

		String engine = getEntityMeta(entity, META_ENGINE);
		if (Strings.isNotEmpty(engine)) {
			sb.append(" ENGINE=" + engine);
		}
		
		String charset = getEntityMeta(entity, META_CHARSET, "UTF8");
		sb.append(" CHARSET=" + charset);

		if (Strings.isNotEmpty(entity.getComment())) {
			sb.append(" COMMENT='").append(entity.getComment()).append("'");
		}
		sqls.add(sb.toString());

		// sometimes mysql needs alter table sql to change the identity start value
		if (id != null && id.isAutoIncrement() && id.getStartWith() > 1) {
			String sql = "ALTER TABLE " + entity.getTableName() + " AUTO_INCREMENT=" + id.getStartWith();
			sqls.add(sql);
		}

		addIndexes(sqls, entity);
		return sqls;
	}

	@Override
	public String dropTable(String tableName) {
		return "DROP TABLE IF EXISTS " + escapeTable(tableName);
	}

	/**
	 * @see http://dev.mysql.com/doc/refman/5.0/en/storage-requirements.html
	 */
	@Override
	protected String evalFieldType(EntityField ef) {
		int jdbcType = JdbcTypes.getType(ef.getJdbcType());
		switch (jdbcType) {
		case Types.TIMESTAMP:
			return "DATETIME";
		case Types.BLOB:
			return "LONGBLOB";
		case Types.LONGVARBINARY:
			return "MEDIUMBLOB";
		case Types.CLOB:
			return "LONGTEXT";
		case Types.LONGVARCHAR:
			return "MEDIUMTEXT";
		default:
			return super.evalFieldType(ef);
		}
	}

	/**
	 * @param sql sql
	 * @param query query
	 * @see http://hsqldb.org/doc/guide/ch09.html#select-section
	 */
	@Override
	protected void limit(StringBuilder sql, Query query) {
		if (query.getStart() > 0 || query.getLimit() > 0) {
			sql.append(" LIMIT ");
			if (query.getStart() > 0) {
				sql.append(query.getStart()).append(',');
			}
			sql.append(query.getLimit() > 0 ? query.getLimit() : Integer.MAX_VALUE);
		}
	}
}
