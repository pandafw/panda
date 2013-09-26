package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.JdbcTypes;
import panda.lang.Strings;

public class OracleSqlExpert extends SqlExpert {
	@Override
	public List<String> create(Entity<?> entity) {
		List<String> sqls = new ArrayList<String>();

		StringBuilder sb = new StringBuilder("CREATE TABLE " + entity.getTableName() + "(");
		for (EntityField ef : entity.getFields()) {
			sb.append('\n').append(ef.getColumn());
			sb.append(' ').append(evalFieldType(ef));

			if (ef.isPrimaryKey() && entity.getPrimaryKeys().size() == 1) {
				sb.append(" PRIMARY KEY ");
			}
			if (ef.isNotNull()) {
				sb.append(" NOT NULL");
			}
			if (ef.hasDefaultValue()) {
				sb.append(" DEFAULT '").append(ef.getDefaultValue()).append('\'');
			}
			if (ef.isUnsigned()) {
				sb.append(" Check ( ").append(ef.getColumn()).append(" >= 0)");
			}
			sb.append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		List<EntityField> pks = entity.getPrimaryKeys();
		if (pks.size() > 1) {
			sqls.add(alterPrimaryKeys(entity));
		}

		EntityField id = entity.getIdentity();
		if (id != null && id.isAutoIncrement()) {
			String sql = "CREATE SEQUENCE " + entity.getTableName() + '_' + id.getColumn() + "_SEQ START WITH " + id.getStartWith();
			sqls.add(sql);
		}
		
		addIndexes(sqls, entity);
		addComments(sqls, entity);
		return sqls;
	}

	@Override
	public List<String> drop(Entity<?> entity) {
		List<String> sqls = super.drop(entity);
		
		EntityField id = entity.getIdentity();
		if (id != null && id.isAutoIncrement()) {
			String sql = "DROP SEQUENCE " + entity.getTableName() + '_' + id.getColumn() + "_SEQ";
			sqls.add(sql);
		}
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
			return "CHAR(1)";
		case Types.BINARY:
		case Types.BLOB:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			return "BYTEA";
		case Types.CLOB:
		case Types.LONGVARCHAR:
			return "CLOB";
		case Types.DOUBLE:
		case Types.FLOAT:
			return evalFieldType("NUMBER", ef.getSize(), ef.getScale());
		case Types.TINYINT:
			return "NUMBER(3)";
		case Types.SMALLINT:
			return "NUMBER(5)";
		case Types.INTEGER:
			return "NUMBER(10)";
		case Types.BIGINT:
			return "NUMBER(19)";
		case Types.VARCHAR:
			return "NVARCHAR2(" + ef.getSize() + ")";
		case Types.TIME:
			return "DATE";
		default:
			return super.evalFieldType(ef);
		}
	}

	@Override
	public boolean isSupportAutoIncrement() {
		return false;
	}

	/**
	 * @param sql sql
	 * @param query query
	 * @see http://hsqldb.org/doc/guide/ch09.html#select-section
	 */
	@Override
	protected void setLimitAndOffset(StringBuilder sql, Query query) {
		if (query.getLimit() > 0) {
			int start = query.getStart() > 0 ? query.getStart() : 0;

			sql.insert(0, "SELECT * FROM (SELECT T.*, ROWNUM RN_ FROM (");
			sql.append(") T WHERE ROWNUM <= ").append(start + query.getLimit());
			sql.append(") WHERE RN_ > ").append(start);
		}
	}
}
