package panda.dao.sql.expert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityIndex;
import panda.dao.sql.SqlUtils;
import panda.lang.Strings;


/**
 * SQL expert for general JDBC
 * <p>
 * !! thread-safe !!
 */
public abstract class SqlExpert {
	public boolean isSupportAutoIncrement() {
		return true;
	}

	public String meta(String tableName) {
		return "SELECT * FROM " + tableName + " where 1!=1";
	}

	public String exists(String tableName) {
		return "SELECT COUNT(1) FROM " + tableName + " where 1!=1";
	}

	public List<String> drop(Entity<?> entity) {
		List<String> sqls = new ArrayList<String>(1);
		sqls.add(dropTable(entity.getTableName()));
		return sqls;
	}

	public String dropTable(String tableName) {
		return "DROP TABLE " + tableName + " IF EXISTS";
	}

	public abstract List<String> create(Entity<?> entity);

	public String count(Entity<?> entity, Query query) {
		//TODO
		return "";
	}

	public String count(String table, Query query) {
		//TODO
		return "";
	}

	public String select(Entity<?> entity, Query query) {
		//TODO
		return "";
	}

	public String select(String table, Query query) {
		//TODO
		return "";
	}

	public String delete(Entity<?> entity, Query query) {
		//TODO
		return "";
	}

	public String delete(String table, Query query) {
		//TODO
		return "";
	}

	public String insert(Entity<?> entity, Object obj) {
		//TODO
		return "";
	}

	public String update(Entity<?> entity, Object obj) {
		//TODO
		return "";
	}
	
	//-----------------------------------------------------------------------
	protected String evalFieldType(String type, int size, int scale) {
		if (size == 0 && scale == 0) {
			return type;
		}
		if (scale == 0) {
			return type + '(' + size + ')';
		}
		return type + '(' + size + ',' + scale + ')';
	}
	
	protected String evalFieldType(EntityField ef) {
		String type = ef.getDbType();
		if (Strings.isNotEmpty(type)) {
			type = ef.getJdbcType();
		}

		return evalFieldType(type, ef.getSize(), ef.getScale());

//		int jdbcType = JdbcTypes.getType(ef.getJdbcType());
//		switch (jdbcType) {
//		case Types.BIGINT:
//			return ef.getJdbcType();
//		case Types.BINARY:
//		case Types.BIT:
//			return ef.getJdbcType();
//		case Types.BLOB:
//			return ef.getJdbcType();
//		case Types.BOOLEAN:
//			return ef.getJdbcType();
//		case Types.CHAR:
//			return ef.getJdbcType() + "(" + ef.getSize() + ")";
//		case Types.CLOB:
//			return ef.getJdbcType();
//		case Types.DATE:
//			return ef.getJdbcType();
//		case Types.DECIMAL:
//			if (ef.getSize() > 0 && ef.getScale() > 0) {
//				return "NUMERIC(" + ef.getSize() + "," + ef.getScale() + ")";
//			}
//		case Types.DOUBLE:
//			return ef.getJdbcType();
//		case Types.FLOAT:
//			return ef.getJdbcType();
//		case Types.INTEGER:
//			return ef.getJdbcType();
//		case Types.LONGVARBINARY:
//			return JdbcTypes.BLOB;
//		case Types.LONGVARCHAR:
//			return JdbcTypes.CLOB;
//		case Types.NUMERIC:
//		case Types.SMALLINT:
//			return ef.getJdbcType();
//		case Types.TIME:
//			return ef.getJdbcType();
//		case Types.TIMESTAMP:
//			return ef.getJdbcType();
//		case Types.TINYINT:
//			return ef.getJdbcType();
//		case Types.VARBINARY:
//			return JdbcTypes.BLOB;
//		case Types.VARCHAR:
//			return ef.getJdbcType() + "(" + ef.getSize() + ")";
//		default:
//			throw new DaoException("Unsupport jdbcType '" + ef.getJdbcType() + "' of field '" + ef.getName() + "' in '" + ef.getEntity().getClass() + "'");
//		}
	}

	/**
	 * @param sql sql
	 * @param query query
	 * @see http://hsqldb.org/doc/guide/ch09.html#select-section
	 */
	protected void setLimitAndOffset(StringBuilder sql, Query query) {
		if (query.getLimit() > 0) {
			sql.append(" LIMIT ").append(query.getLimit());
			
			if (query.getStart() > 0) {
				sql.append(" OFFSET ").append(query.getStart());
			}
		}
	}

	protected void addPrimaryKeys(StringBuilder sb, Entity<?> entity) {
		List<EntityField> pks = entity.getPrimaryKeys();
		if (!pks.isEmpty()) {
			sb.append('\n');
			sb.append("PRIMARY KEY (");
			for (EntityField pk : pks) {
				sb.append(pk.getColumn()).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');
			sb.append("\n ");
		}
	}

	protected String alterPrimaryKeys(Entity<?> entity) {
		StringBuilder sb = new StringBuilder();
		List<EntityField> pks = entity.getPrimaryKeys();
		if (!pks.isEmpty()) {
			sb.append("ALTER TABLE ")
				.append(entity.getTableName())
				.append(" ADD CONSTRAINT ")
				.append(entity.getTableName())
				.append("_PK PRIMARY KEY (");
			for (EntityField pk : pks) {
				sb.append(pk.getColumn()).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');
		}
		return sb.toString();
	}

	protected void addComments(List<String> sqls, Entity<?> entity) {
		if (Strings.isNotEmpty(entity.getComment())) {
			String sql = "COMMENT ON TABLE" + entity.getTableName() + " IS '" + SqlUtils.escapeSql(entity.getComment())
					+ '\'';
			sqls.add(sql);
		}

		for (EntityField ef : entity.getFields()) {
			if (Strings.isNotEmpty(ef.getComment())) {
				String sql = "COMMENT ON COLUMN " + entity.getTableName() + '.' + ef.getColumn() + " IS '"
						+ SqlUtils.escapeSql(ef.getComment()) + '\'';
				sqls.add(sql);
			}
		}
	}

	protected void addIndexes(List<String> sqls, Entity<?> entity) {
		StringBuilder sb = new StringBuilder();
		Collection<EntityIndex> indexs = entity.getIndexes();
		for (EntityIndex index : indexs) {
			sb.setLength(0);
			if (index.isUnique())
				sb.append("Create UNIQUE Index ");
			else
				sb.append("Create Index ");
			sb.append(index.getName());
			sb.append(" ON ").append(entity.getTableName()).append("(");
			for (EntityField ef : index.getFields()) {
				sb.append(escapeColumn(ef.getColumn())).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');
			sqls.add(sb.toString());
		}
	}

	protected String escapeColumn(String column) {
		return column;
	}
	
}
