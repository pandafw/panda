package panda.dao.sql.expert;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panda.dao.DB;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.Query;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.Sql;
import panda.lang.Strings;

public class Mssql2005SqlExpert extends SqlExpert {
	@Override
	public DB getDatabaseType() {
		return DB.MSSQL;
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
			
			if (ef.isAutoIncrement()) {
				sb.append(" IDENTITY");
			}
			else if (ef.hasDefaultValue()) {
				sb.append(" DEFAULT '").append(ef.getDefaultValue()).append('\'');
			}
			sb.append(',');
		}
		
		// append primary keys
		addPrimaryKeys(sb, entity);
		sb.setCharAt(sb.length() - 1, ')');
		sqls.add(sb.toString());

		// add comments & constraints
		addComments(sqls, entity);
		addIndexes(sqls, entity);
		addForeignKeys(sqls, entity);
		return sqls;
	}

	@Override
	protected void addComments(List<String> slqs, Entity<?> entity) {
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

	public String identityInsertOn(Entity<?> entity) {
		return "SET IDENTITY_INSERT " + entity.getTableName() + " ON";
	}
	
	public String identityInsertOff(Entity<?> entity) {
		return "SET IDENTITY_INSERT " + entity.getTableName() + " OFF";
	}
	
	/**
	 * @param sql sql
	 * @param query query
	 */
	@Override
	protected void limit(Sql sql, Query query) {
		// very rough, but works
		if (query.getStart() > 0) {
			StringBuilder beg = new StringBuilder(); 
			beg.append("SELECT * FROM (");
			beg.append("SELECT ROW_NUMBER() OVER(ORDER BY __tc__) __rn__, * FROM (");
			beg.append("SELECT");
			if (query.getLimit() > 0) {
				beg.append(" TOP ").append(query.getStart() + query.getLimit());
			}
			beg.append(" 0 __tc__, * FROM (");

			sql.insert(0, beg);
			sql.append(") t1 ) t2 ) t3 WHERE __rn__ > ").append(query.getStart());
		}
		else if (query.getLimit() > 0) {
			sql.insert(0, "SELECT TOP " + query.getLimit() + " * FROM (");
			sql.append(") t1");
		}
	}
}
