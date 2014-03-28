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

public class OracleSqlExpert extends SqlExpert {
	@Override
	public DB getDatabaseType() {
		return DB.ORACLE;
	}

	@Override
	public List<String> drop(Entity<?> entity) {
		List<String> sqls = super.drop(entity);
		
		EntityField eid = entity.getIdentity();
		if (eid != null && eid.isAutoIncrement()) {
			String sql = "DROP SEQUENCE " + entity.getTableName() + '_' + eid.getColumn() + "_SEQ";
			sqls.add(sql);
		}
		return sqls;
	}

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
				sb.append(" Check (").append(ef.getColumn()).append(" >= 0)");
			}
			sb.append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		// add multiple pks
		List<EntityField> pks = entity.getPrimaryKeys();
		if (pks.size() > 1) {
			sqls.add(alterPrimaryKeys(entity));
		}

		// add sequence
		EntityField eid = entity.getIdentity();
		if (eid != null && eid.isAutoIncrement()) {
			String sql = "CREATE SEQUENCE " + entity.getTableName() + '_' + eid.getColumn() + "_SEQ START WITH " + eid.getStartWith();
			sqls.add(sql);
		}

		// add comments & constraints
		addComments(sqls, entity);
		addIndexes(sqls, entity);
		addForeignKeys(sqls, entity);
		return sqls;
	}
	
	@Override
	public String prepIdentity(Entity<?> entity) {
		EntityField eid = entity.getIdentity();
		return "SELECT " + entity.getTableName() + '_' + eid.getColumn() + "_SEQ.NEXTVAL AS ID FROM DUAL";
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
			return "CHAR(1)";
		case Types.BINARY:
		case Types.VARBINARY:
			return evalFieldType("RAW", ef.getSize(), ef.getScale());
		case Types.BLOB:
			return DaoTypes.BLOB;
		case Types.LONGVARBINARY:
			return "LONG RAW";
		case Types.CLOB:
		case Types.LONGVARCHAR:
			return "NCLOB";
		case Types.REAL:
		case Types.FLOAT:
		case Types.DOUBLE:
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
	 */
	@Override
	protected void limit(Sql sql, Query query) {
		if (query.getStart() > 0) {
			sql.insert(0, "SELECT * FROM (SELECT T.*, ROWNUM RN_ FROM (");
			sql.append(") T");
			if (query.getLimit() > 0) {
				sql.append(" WHERE ROWNUM <= ").append(query.getStart() + query.getLimit());
			}
			sql.append(") T2 WHERE RN_ > ").append(query.getStart());
		}
		else if (query.getLimit() > 0) {
			sql.insert(0, "SELECT T.*, ROWNUM RN_ FROM (");
			sql.append(") T WHERE ROWNUM <= ").append(query.getLimit());
		}
	}
}
