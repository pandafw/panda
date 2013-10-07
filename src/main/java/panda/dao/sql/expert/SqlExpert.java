package panda.dao.sql.expert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.dao.criteria.Expression;
import panda.dao.criteria.Order;
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
	protected Map<String, String> meta;
	
	/**
	 * @return the meta
	 */
	public Map<String, String> getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	protected String getEntityMeta(Entity<?> entity, String name) {
		return getEntityMeta(entity, name, null);
	}
	
	protected String getEntityMeta(Entity<?> entity, String name, String defval) {
		String v = entity.getMeta(name);
		if (v == null && meta != null) {
			v = meta.get(name);
		}
		return Strings.isEmpty(v) ? defval : v;
	}
	
	public boolean isSupportAutoIncrement() {
		return true;
	}

	public boolean isSupportPaginate() {
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
		return "DROP TABLE " + escapeTable(tableName);
	}

	public abstract List<String> create(Entity<?> entity);

	public String count(Entity<?> entity, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) FROM ").append(entity.getTableName());
		where(sb, entity, query);
		order(sb, entity, query);
		return sb.toString();
	}

	public String count(String table, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) FROM ").append(table);
		where(sb, query);
		order(sb, query);
		return sb.toString();
	}

	public String select(Entity<?> entity, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		for (EntityField ef : entity.getFields()) {
			if (query != null && query.shouldExclude(ef.getName())) {
				continue;
			}
			sb.append(' ').append(ef.getColumn()).append(" AS ").append(ef.getName()).append(',');
		}
		sb.setCharAt(sb.length() - 1, ' ');
		sb.append("FROM ").append(entity.getViewName());
		where(sb, entity, query);
		order(sb, entity, query);
		limit(sb, query);
		return sb.toString();
	}

	public String select(String table, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		if (query != null && query.hasIncludes()) {
			for (String column : query.getIncludes()) {
				sb.append(' ').append(column).append(',');
			}
			sb.setCharAt(sb.length() - 1, ' ');
		}
		else {
			sb.append(" * ");
		}
		sb.append("FROM ").append(table);
		where(sb, query);
		order(sb, query);
		limit(sb, query);
		return sb.toString();
	}

	public String delete(Entity<?> entity, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(entity.getTableName());
		where(sb, entity, query);
		return sb.toString();
	}

	public String delete(String table, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(table);
		where(sb, query);
		return sb.toString();
	}

	public String insert(Entity<?> entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(entity.getTableName());
		sb.append('(');
		for (EntityField ef : entity.getFields()) {
			if (!ef.isReadonly()) {
				sb.append(' ').append(ef.getColumn()).append(',');
			}
		}
		sb.setCharAt(sb.length() - 1, ')');

		sb.append(" VALUES (");
		for (EntityField ef : entity.getFields()) {
			if (!ef.isReadonly()) {
				sb.append(" :").append(ef.getName()).append(':').append(ef.getJdbcType()).append(',');
			}
		}
		sb.setCharAt(sb.length() - 1, ')');
		return sb.toString();
	}

	public String update(Entity<?> entity, Query query) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(entity.getTableName());
		sb.append(" SET");
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || (query != null && query.shouldExclude(ef.getName()))) {
				continue;
			}
			sb.append(' ').append(ef.getColumn()).append("=:").append(ef.getName()).append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		where(sb, entity, query);
		return sb.toString();
	}
	
	//-----------------------------------------------------------------------
	protected abstract void limit(StringBuilder sql, Query query);

	protected void where(StringBuilder sb, Entity<?> entity, Query query) {
		if (query == null || !query.hasConditions()) {
			return;
		}
		
		int i = 0;
		for (Expression exp : query.getExpressions()) {
			if (exp instanceof Expression.ValueCompare) {
				Expression.ValueCompare evc = (Expression.ValueCompare)exp;
				EntityField ef = entity.getField(evc.getField());
				if (ef == null) {
					throw new IllegalArgumentException("invalid where field '" + evc.getField() + "' of entity " + entity.getType());
				}

				sb.append(' ').append(ef.getColumn()).append(' ').append(evc.getOperator()).append(" :p").append(++i);
				query.addParam("p" + i, evc.getValue());
			}
			else if (exp instanceof Expression.FieldCompare) {
				Expression.FieldCompare efc = (Expression.FieldCompare)exp;
				EntityField ef = entity.getField(efc.getField());
				if (ef == null) {
					throw new IllegalArgumentException("invalid where field '" + efc.getField() + "' of entity " + entity.getType());
				}

				EntityField ef2 = entity.getField(efc.getValue());
				if (ef2 == null) {
					throw new IllegalArgumentException("invalid compare field '" + efc.getValue() + "' of entity " + entity.getType());
				}

				sb.append(' ').append(ef.getColumn()).append(' ').append(efc.getOperator()).append(ef2.getColumn());
			}
			else if (exp instanceof Expression.Simple) {
				Expression.Simple es = (Expression.Simple)exp;
				EntityField ef = entity.getField(es.getField());
				if (ef == null) {
					throw new IllegalArgumentException("invalid where field '" + es.getField() + "' of entity " + entity.getType());
				}

				sb.append(' ').append(ef.getColumn()).append(' ').append(es.getOperator());
			}
			else {
				sb.append(' ').append(exp.toString());
			}
		}
	}
	
	protected void where(StringBuilder sb, Query query) {
		if (query == null || !query.hasConditions()) {
			return;
		}
		
		int i = 0;
		for (Expression exp : query.getExpressions()) {
			if (exp instanceof Expression.ValueCompare) {
				Expression.ValueCompare evc = (Expression.ValueCompare)exp;
				sb.append(' ').append(evc.getField()).append(' ').append(evc.getOperator()).append(" :p").append(++i);
				query.addParam("p" + i, evc.getValue());
			}
			else {
				sb.append(' ').append(exp.toString());
			}
		}
	}
	
	protected void order(StringBuilder sb, Entity<?> entity, Query query) {
		if (query == null || !query.hasOrders()) {
			return;
		}
		
		sb.append(" ORDER BY");
		for (Entry<String, Order> en : query.getOrders().entrySet()) {
			EntityField ef = entity.getField(en.getKey());
			if (ef == null) {
				throw new IllegalArgumentException("invalid order field '" + en.getKey() + "' of entity " + entity.getType());
			}
			sb.append(' ').append(ef.getColumn()).append(' ').append(en.getValue()).append(',');
		}
		sb.setCharAt(sb.length() - 1, ' ');
	}
	
	protected void order(StringBuilder sb, Query query) {
		if (query == null || !query.hasOrders()) {
			return;
		}
		
		sb.append(" ORDER BY");
		for (Entry<String, Order> en : query.getOrders().entrySet()) {
			sb.append(' ').append(en.getKey()).append(' ').append(en.getValue()).append(',');
		}
		sb.setCharAt(sb.length() - 1, ' ');
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
	
	protected void addPrimaryKeysConstraint(StringBuilder sb, Entity<?> entity) {
		List<EntityField> pks = entity.getPrimaryKeys();
		if (!pks.isEmpty()) {
			sb.append('\n');
			sb.append("CONSTRAINT ").append(entity.getTableName()).append("_PK PRIMARY KEY (");
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

	protected String escapeTable(String table) {
		return table;
	}

	protected String escapeColumn(String column) {
		return column;
	}
	
}
