package panda.dao.sql.expert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.castor.Castors;
import panda.dao.DB;
import panda.dao.DatabaseMeta;
import panda.dao.criteria.Expression;
import panda.dao.criteria.Operator;
import panda.dao.criteria.Order;
import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityIndex;
import panda.dao.sql.Sql;
import panda.dao.sql.Sqls;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.Texts;


/**
 * SQL expert for general JDBC
 * <p>
 * !! thread-safe !!
 */
public abstract class SqlExpert {
	protected DatabaseMeta databaseMeta;
	protected Map<String, Object> properties;
	protected Castors castors;

	/**
	 * @return the databaseMeta
	 */
	public DatabaseMeta getDatabaseMeta() {
		return databaseMeta;
	}

	/**
	 * @param name database name
	 * @param version database version
	 */
	public void setDatabaseMeta(String name, String version) {
		databaseMeta = new DatabaseMeta(getDatabaseType(), name, version);
	}

	/**
	 * @return the database type
	 */
	public abstract DB getDatabaseType();

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
	}

	/**
	 * @param castors the castors to set
	 */
	public void setCastors(Castors castors) {
		this.castors = castors;
	}

	protected String getEntityMeta(Entity<?> entity, String name) {
		return getEntityMeta(entity, name, null);
	}
	
	protected String getEntityMeta(Entity<?> entity, String name, String defval) {
		String v = entity.getMeta(name);
		if (v == null && properties != null) {
			Object o = properties.get(name);
			if (o != null) {
				v = o.toString();
			}
		}
		return Strings.isEmpty(v) ? defval : v;
	}
	
	public boolean isSupportAutoIncrement() {
		return true;
	}

	public boolean isSupportPaginate() {
		return true;
	}

	public boolean isSupportScroll() {
		return true;
	}

	public boolean isSupportDropIfExists() {
		return false;
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

	public String identityInsertOn(Entity<?> entity) {
		return null;
	}
	
	public String identityInsertOff(Entity<?> entity) {
		return null;
	}
	
	public String prepIdentity(Entity<?> entity) {
		return null;
	}
	
	public String postIdentity(Entity<?> entity) {
		return null;
	}
	
	public abstract List<String> create(Entity<?> entity);

	public Sql count(Entity<?> entity, Query query) {
		Sql sql = new Sql();
		sql.append("SELECT COUNT(*) FROM ").append(escapeTable(entity.getTableName()));
		where(sql, entity, query);
		order(sql, entity, query);
		return sql;
	}

	public Sql count(String table, Query query) {
		Sql sql = new Sql();
		sql.append("SELECT COUNT(*) FROM ").append(table);
		where(sql, query);
		order(sql, query);
		return sql;
	}

	public Sql select(Entity<?> entity, Query query) {
		Sql sql = new Sql();
		sql.append("SELECT");
		for (EntityField ef : entity.getFields()) {
			if (query != null && query.shouldExclude(ef.getName())) {
				continue;
			}
			sql.append(' ').append(ef.getColumn()).append(" AS ").append(ef.getName()).append(',');
		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append("FROM ").append(entity.getViewName());
		where(sql, entity, query);
		order(sql, entity, query);
		_limit(sql, query);
		return sql;
	}

	public Sql select(String table, Query query) {
		Sql sql = new Sql();
		sql.append("SELECT");
		if (query != null && query.hasIncludes()) {
			for (String column : query.getIncludes()) {
				sql.append(' ').append(column).append(',');
			}
			sql.setCharAt(sql.length() - 1, ' ');
		}
		else {
			sql.append(" * ");
		}
		sql.append("FROM ").append(table);
		where(sql, query);
		order(sql, query);
		_limit(sql, query);
		return sql;
	}

	public Sql delete(Entity<?> entity, Query query) {
		Sql sql = new Sql();
		sql.append("DELETE FROM ").append(escapeTable(entity.getTableName()));
		where(sql, entity, query);
		return sql;
	}

	public Sql delete(String table, Query query) {
		Sql sql = new Sql();
		sql.append("DELETE FROM ").append(table);
		where(sql, query);
		return sql;
	}

	protected Object getFieldValue(EntityField ef, Object data) {
		return ef.getValue(data);
	}

	public Sql insert(Entity<?> entity, Object data, boolean autoId) {
		if (!isSupportAutoIncrement()) {
			autoId = false;
		}
		
		Sql sql = new Sql();
		sql.append("INSERT INTO ").append(escapeTable(entity.getTableName()));
		sql.append('(');
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || (ef.isAutoIncrement() && autoId)) {
				continue;
			}
			sql.append(' ').append(ef.getColumn()).append(',');
		}
		sql.setCharAt(sql.length() - 1, ')');

		sql.append(" VALUES (");
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || (ef.isAutoIncrement() && autoId)) {
				continue;
			}
			sql.append("?,");
			Object v = getFieldValue(ef, data);
			if (v == null && Strings.isNotEmpty(ef.getDefaultValue())) {
				v = Texts.transform(ef.getDefaultValue(), data);
				v = castors.cast(v, ef.getType());
			}
			sql.addParam(v);
		}
		sql.setCharAt(sql.length() - 1, ')');
		return sql;
	}

	public Sql update(Entity<?> entity, Object data, Query query) {
		Sql sql = new Sql();
		sql.append("UPDATE ").append(escapeTable(entity.getTableName()));
		sql.append(" SET");
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || (query != null && query.shouldExclude(ef.getName()))) {
				continue;
			}
			sql.append(' ').append(ef.getColumn()).append("=?,");
			sql.addParam(getFieldValue(ef, data));
		}
		sql.setLength(sql.length() - 1);
		where(sql, entity, query);
		return sql;
	}
	
	protected void where(Sql sql, Entity<?> entity, Query query) {
		if (query == null || !query.hasConditions()) {
			return;
		}
		
		sql.append(" WHERE");
		for (Expression exp : query.getExpressions()) {
			if (exp instanceof Expression.ValueCompare) {
				Expression.ValueCompare evc = (Expression.ValueCompare)exp;
				EntityField ef = entity.getField(evc.getField());
				if (ef == null) {
					throw new IllegalArgumentException("invalid where field '" + evc.getField() + "' of entity " + entity.getType());
				}

				whereValueCompare(sql, ef.getColumn(), evc);
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

				sql.append(' ').append(ef.getColumn()).append(' ').append(efc.getOperator()).append(ef2.getColumn());
			}
			else if (exp instanceof Expression.Simple) {
				Expression.Simple es = (Expression.Simple)exp;
				EntityField ef = entity.getField(es.getField());
				if (ef == null) {
					throw new IllegalArgumentException("invalid where field '" + es.getField() + "' of entity " + entity.getType());
				}

				sql.append(' ').append(ef.getColumn()).append(' ').append(es.getOperator());
			}
			else {
				sql.append(' ').append(exp.toString());
			}
		}
	}
	
	protected void where(Sql sql, Query query) {
		if (query == null || !query.hasConditions()) {
			return;
		}
		
		sql.append(" WHERE");
		for (Expression exp : query.getExpressions()) {
			if (exp instanceof Expression.ValueCompare) {
				Expression.ValueCompare evc = (Expression.ValueCompare)exp;
				
				whereValueCompare(sql, evc.getField(), evc);
			}
			else {
				sql.append(' ').append(exp.toString());
			}
		}
	}
	
	protected void whereValueCompare(Sql sql, String column, Expression.ValueCompare evc) {
		sql.append(' ').append(column).append(' ');

		Operator op = evc.getOperator();
		if (op == Operator.BETWEEN || op == Operator.NOT_BETWEEN) {
			sql.append(op).append(" ? AND ?");
			sql.addParam(evc.getValue(0));
			sql.addParam(evc.getValue(1));
		}
		else if (op == Operator.IN || op == Operator.NOT_IN) {
			sql.append(op).append('(');
			Iterator it = Objects.toIterator(evc.getValue());
			while (it.hasNext()) {
				sql.append('?').append(',');
				sql.addParam(it.next());
			}
			sql.setCharAt(sql.length() - 1, ')');
		}
		else if (op == Operator.MATCH) {
			sql.append("LIKE ?");
			sql.addParam(Sqls.stringLike((String)evc.getValue()));
		}
		else if (op == Operator.NOT_MATCH) {
			sql.append("NOT LIKE ?");
			sql.addParam(Sqls.stringLike((String)evc.getValue()));
		}
		else if (op == Operator.LEFT_MATCH) {
			sql.append("LIKE ?");
			sql.addParam(Sqls.startsLike((String)evc.getValue()));
		}
		else if (op == Operator.NOT_LEFT_MATCH) {
			sql.append("NOT LIKE ?");
			sql.addParam(Sqls.startsLike((String)evc.getValue()));
		}
		else if (op == Operator.RIGHT_MATCH) {
			sql.append("LIKE ?");
			sql.addParam(Sqls.endsLike((String)evc.getValue()));
		}
		else if (op == Operator.NOT_RIGHT_MATCH) {
			sql.append("NOT LIKE ?");
			sql.addParam(Sqls.endsLike((String)evc.getValue()));
		}
		else {
			sql.append(op).append(' ');
			sql.append('?');
			sql.addParam(evc.getValue());
		}
	}
	
	protected void order(Sql sql, Entity<?> entity, Query query) {
		if (query == null || !query.hasOrders()) {
			return;
		}
		
		sql.append(" ORDER BY");
		for (Entry<String, Order> en : query.getOrders().entrySet()) {
			EntityField ef = entity.getField(en.getKey());
			if (ef == null) {
				throw new IllegalArgumentException("invalid order field '" + en.getKey() + "' of entity " + entity.getType());
			}
			sql.append(' ').append(ef.getColumn()).append(' ').append(en.getValue()).append(',');
		}
		sql.setCharAt(sql.length() - 1, ' ');
	}
	
	protected void order(Sql sql, Query query) {
		if (query == null || !query.hasOrders()) {
			return;
		}
		
		sql.append(" ORDER BY");
		for (Entry<String, Order> en : query.getOrders().entrySet()) {
			sql.append(' ').append(en.getKey()).append(' ').append(en.getValue()).append(',');
		}
		sql.setCharAt(sql.length() - 1, ' ');
	}
	
	//-----------------------------------------------------------------------
	private void _limit(Sql sql, Query query) {
		if (query != null && query.needsPaginate() && isSupportPaginate()) {
			limit(sql, query);
		}
	}
	
	protected abstract void limit(Sql sql, Query query);

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
		if (Strings.isEmpty(type)) {
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
	
	protected void addPrimaryKeysConstraint(StringBuilder sql, Entity<?> entity) {
		List<EntityField> pks = entity.getPrimaryKeys();
		if (!pks.isEmpty()) {
			sql.append('\n');
			sql.append("CONSTRAINT ").append(entity.getTableName()).append("_PK PRIMARY KEY (");
			for (EntityField pk : pks) {
				sql.append(pk.getColumn()).append(',');
			}
			sql.setCharAt(sql.length() - 1, ')');
			sql.append("\n ");
		}
	}
	
	protected String alterPrimaryKeys(Entity<?> entity) {
		List<EntityField> pks = entity.getPrimaryKeys();
		if (pks.isEmpty()) {
			return Strings.EMPTY;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ")
			.append(escapeTable(entity.getTableName()))
			.append(" ADD CONSTRAINT ")
			.append(entity.getTableName())
			.append("_PK PRIMARY KEY (");
		for (EntityField pk : pks) {
			sb.append(pk.getColumn()).append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		return sb.toString();
	}

	protected void addComments(List<String> sqls, Entity<?> entity) {
		if (Strings.isNotEmpty(entity.getComment())) {
			String sql = "COMMENT ON TABLE " 
					+ escapeTable(entity.getTableName()) 
					+ " IS '" 
					+ Sqls.escapeString(entity.getComment())
					+ '\'';
			sqls.add(sql);
		}

		for (EntityField ef : entity.getFields()) {
			if (Strings.isNotEmpty(ef.getComment())) {
				String sql = "COMMENT ON COLUMN " 
						+ entity.getTableName() + '.' + ef.getColumn() 
						+ " IS '"
						+ Sqls.escapeString(ef.getComment()) 
						+ '\'';
				sqls.add(sql);
			}
		}
	}

	protected void addIndexes(List<String> sqls, Entity<?> entity) {
		Collection<EntityIndex> indexs = entity.getIndexes();
		if (indexs == null || indexs.isEmpty()) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (EntityIndex index : indexs) {
			if (!index.isReal()) {
				continue;
			}
			
			sb.setLength(0);
			sb.append("Create");
			if (index.isUnique())
				sb.append(" UNIQUE");
			sb.append(" Index ");
			sb.append(entity.getTableName())
				.append("_")
				.append(index.isUnique() ? "UX" : "IX")
				.append('_')
				.append(index.getName());
			sb.append(" ON ").append(entity.getTableName()).append("(");
			for (EntityField ef : index.getFields()) {
				sb.append(escapeColumn(ef.getColumn())).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');
			sqls.add(sb.toString());
		}
	}

	protected void addForeignKeysConstraint(StringBuilder sql, Entity<?> entity) {
		Collection<EntityFKey> fks = entity.getForeignKeys();
		if (fks == null || fks.isEmpty()) {
			return;
		}
		
		for (EntityFKey fk: fks) {
			sql.append('\n');
			sql.append("CONSTRAINT ")
				.append(entity.getTableName())
				.append("_FK_")
				.append(fk.getName())
				.append(" FOREIGN KEY (");
			for (EntityField ef : fk.getFields()) {
				sql.append(ef.getColumn()).append(',');
			}
			sql.setCharAt(sql.length() - 1, ')');

			Entity<?> ref = fk.getReference();
			sql.append(" REFERENCES ").append(ref.getTableName()).append(" (");
			for (EntityField ef : ref.getPrimaryKeys()) {
				sql.append(ef.getColumn()).append(',');
			}
			sql.setCharAt(sql.length() - 1, ')');
			sql.append(',');
		}
	}
	
	protected void addForeignKeys(List<String> sqls, Entity<?> entity) {
		Collection<EntityFKey> fks = entity.getForeignKeys();
		if (fks == null || fks.isEmpty()) {
			return;
		}
		
		for (EntityFKey fk: fks) {
			StringBuilder sb = new StringBuilder();
			sb.append("ALTER TABLE ")
			.append(entity.getTableName())
			.append(" ADD CONSTRAINT ")
			.append(entity.getTableName())
			.append("_FK_")
			.append(fk.getName())
			.append(" FOREIGN KEY (");
			for (EntityField ef : fk.getFields()) {
				sb.append(ef.getColumn()).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');

			Entity<?> ref = fk.getReference();
			sb.append(" REFERENCES ").append(ref.getTableName()).append(" (");
			for (EntityField ef : ref.getPrimaryKeys()) {
				sb.append(ef.getColumn()).append(',');
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
