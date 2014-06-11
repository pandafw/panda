package panda.dao.sql.expert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.castor.Castors;
import panda.dao.DB;
import panda.dao.DaoClient;
import panda.dao.DaoNamings;
import panda.dao.DatabaseMeta;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityIndex;
import panda.dao.query.Filter;
import panda.dao.query.Filter.ComboFilter;
import panda.dao.query.Join;
import panda.dao.query.Operator;
import panda.dao.query.Order;
import panda.dao.query.Query;
import panda.dao.sql.Sql;
import panda.dao.sql.Sqls;
import panda.lang.Iterators;
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
	protected DaoClient client;

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

	/**
	 * @return the client
	 */
	public DaoClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(DaoClient client) {
		this.client = client;
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
		return "SELECT * FROM " + escapeTable(tableName) + " where 1!=1";
	}

	public String exists(String tableName) {
		return "SELECT COUNT(1) FROM " + escapeTable(tableName) + " where 1!=1";
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

	private String normalizeColumn(String table, String column) {
		if (column.charAt(0) == '(') {
			return column;
		}
		if (Strings.isEmpty(table)) {
			return escapeColumn(column);
		}
		return table + '.' + escapeColumn(column);
	}
	
	public Sql count(Query<?> query) {
		return count(query, "c_");
	}
	
	protected Sql count(Query<?> query, String alias) {
		Sql sql = new Sql();
		sql.append("SELECT COUNT(*) FROM ").append(escapeTable(query.getTable()));
		if (Strings.isNotEmpty(alias)) {
			sql.append(' ').append(alias);
		}
		join(sql, query, alias);
		where(sql, query, alias);
		return sql;
	}

	public Sql select(Query<?> query) {
		return select(query, "s_");
	}
	
	protected Sql select(Query<?> query, String alias) {
		Sql sql = new Sql();

		select(sql, query, alias);
		join(sql, query, alias);
		where(sql, query, alias);
		group(sql, query, alias);
		order(sql, query);
		_limit(sql, query);

		return sql;
	}

	public Sql delete(Query<?> query) {
		Sql sql = new Sql();
		sql.append("DELETE FROM ").append(escapeTable(query.getTable()));
		where(sql, query, null);
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
			sql.append(' ').append(escapeColumn(ef.getColumn())).append(',');
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
				v = Texts.translate(ef.getDefaultValue(), data);
				v = castors.cast(v, ef.getType());
			}
			sql.addParam(v);
		}
		sql.setCharAt(sql.length() - 1, ')');
		return sql;
	}

	public Sql update(Object data, Query<?> query) {
		Sql sql = new Sql();

		sql.append("UPDATE ").append(escapeTable(query.getTable()));
		sql.append(" SET");
		
		Entity<?> entity = query.getEntity();
		boolean set = false;
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || query.shouldExclude(ef.getName())) {
				continue;
			}
			sql.append(' ').append(escapeColumn(ef.getColumn())).append("=?,");
			sql.addParam(getFieldValue(ef, data));
			set = true;
		}
		if (!set) {
			throw new IllegalArgumentException("Nothing to UPDATE!");
		}

		sql.setLength(sql.length() - 1);
		where(sql, query, null);
		return sql;
	}
	
	protected EntityField getEntityField(Entity<?> entity, String field, String name) {
		EntityField ef = entity.getField(field);
		if (ef == null) {
			throw new IllegalArgumentException("invalid " + name + " field '" + field + "' of entity " + entity.getType());
		}
		return ef;
	}
	
	protected void select(Sql sql, Query<?> query, String alias) {
		sql.append("SELECT");

		if (query.isDistinct()) {
			sql.append(" DISTINCT");
		}

		Entity<?> entity = query.getEntity();
		if (entity != null) {
			boolean sel = false;
			for (EntityField ef : entity.getFields()) {
				if (query.shouldExclude(ef.getName())) {
					continue;
				}
				
				String col = query.getColumn(ef.getName());
				if (Strings.isEmpty(col)) {
					col = ef.getColumn();
					// skip join column which has not joined
					if (Strings.isEmpty(col)) {
						continue;
					}
				}
				else {
					col = '(' + col + ')';
				}
				
				sql.append(' ')
					.append(normalizeColumn(alias, col))
					.append(" AS ")
					.append(escapeColumn(DaoNamings.javaName2ColumnLabel(ef.getName())))
					.append(',');
				sel = true;
			}
			if (!sel) {
				throw new IllegalArgumentException("Nothing to SELECT!");
			}
			sql.setCharAt(sql.length() - 1, ' ');
			sql.append("FROM ").append(escapeTable(entity.getViewName()));
			if (Strings.isNotEmpty(alias)) {
				sql.append(' ').append(alias);
			}
		}
		else {
			boolean sel = false;
			if (query.hasColumns()) {
				for (Entry<String, String> en : query.getColumns().entrySet()) {
					String col = en.getValue();
					if (col == null) {
						continue;
					}
					
					if (col.length() == 0) {
						col = en.getKey();
					}
					else {
						col = '(' + col + ')';
					}

					sql.append(' ')
						.append(normalizeColumn(alias, col))
						.append(" AS ")
						.append(escapeColumn(DaoNamings.javaName2ColumnLabel(en.getKey())))
						.append(',');
					sel = true;
				}
				if (sel) {
					sql.setCharAt(sql.length() - 1, ' ');
				}
			}

			if (!sel) {
				if (Strings.isNotEmpty(alias)) {
					sql.append(' ').append(alias).append('.');
				}
				sql.append("* ");
			}
			sql.append("FROM ").append(escapeTable(query.getTable())).append(' ').append(alias);
		}
	}
	
	protected void join(Sql sql, Query<?> query, String talias) {
		if (!query.hasJoins()) {
			return;
		}
		
		for (Entry<String, Join> en : query.getJoins().entrySet()) {
			String jalias = en.getKey();
			Join join = en.getValue();
			
			sql.append(' ').append(join.getType()).append(" JOIN ");

			Query<?> jq = join.getQuery();
			if (jq.hasFilters()) {
				sql.append('(');
				sql.append(select(jq));
				sql.append(')');
			}
			else {
				sql.append(escapeTable(jq.getTable()));
			}
			sql.append(' ').append(jalias);
			sql.append(" ON (");
			for (String s : join.getConditions()) {
				
				int d = s.indexOf('=');
				int l = 1;
				if (d < 0) {
					d = s.indexOf("<>");
					l = 2;
				}
				if (d < 0) {
					throw new IllegalArgumentException("Invalid join condition: " + s);
				}

				// main table alias
				sql.append(talias).append('.');
				// main table column
				sql.append(escapeColumn(Strings.trim(s.substring(0, d))));
				// operator
				sql.append(s.substring(d, d + l));
				// join table alias
				sql.append(jalias).append('.');
				// join table column
				sql.append(escapeColumn(Strings.trim(s.substring(d + l))));
				
				sql.append(" AND ");
			}
			// remove last " AND ";
			sql.setLength(sql.length() - 5);
			sql.append(')');
		}
	}
	
	private void whereCompositeFilter(Sql sql, Entity<?> entity, String alias, ComboFilter cf) {
		Iterator<Filter> it = cf.getFilters().iterator();
		while (it.hasNext()) {
			Filter exp = it.next();
			if (exp instanceof Filter.ValueFilter) {
				Filter.ValueFilter evc = (Filter.ValueFilter)exp;
				EntityField ef = getEntityField(entity, evc.getField(), "where");
				whereValueFilter(sql, alias, ef.getColumn(), evc);
			}
			else if (exp instanceof Filter.ReferFilter) {
				Filter.ReferFilter efc = (Filter.ReferFilter)exp;
				EntityField ef = getEntityField(entity, efc.getField(), "where");
				EntityField ef2 = getEntityField(entity, efc.getRefer(), "compare");
				sql.append(escapeColumn(alias, ef.getColumn()))
					.append(efc.getOperator()).append(escapeColumn(alias, ef2.getColumn()));
			}
			else if (exp instanceof Filter.SimpleFilter) {
				Filter.SimpleFilter es = (Filter.SimpleFilter)exp;
				EntityField ef = getEntityField(entity, es.getField(), "simple");
				sql.append(escapeColumn(alias, ef.getColumn())).append(' ').append(es.getOperator());
			}
			else if (exp instanceof Filter.ComboFilter) {
				// AND/OR
				sql.append('(');
				whereCompositeFilter(sql, entity, alias, (ComboFilter)exp);
				sql.append(')');
			}
			if (it.hasNext()) {
				sql.append(' ').append(cf.getLogical()).append(' ');
			}
		}
	}
	
	private void whereCompositeFilter(Sql sql, String alias, ComboFilter cf) {
		Iterator<Filter> it = cf.getFilters().iterator();
		while (it.hasNext()) {
			Filter f = it.next();
			if (f instanceof Filter.ValueFilter) {
				Filter.ValueFilter vf = (Filter.ValueFilter)f;
				whereValueFilter(sql, alias, vf.getField(), vf);
			}
			else if (f instanceof Filter.ReferFilter || f instanceof Filter.SimpleFilter) {
				sql.append(f.toString());
			}
			else if (f instanceof Filter.ComboFilter) {
				// AND/OR
				sql.append('(');
				whereCompositeFilter(sql, alias, (ComboFilter)f);
				sql.append(')');
			}

			if (it.hasNext()) {
				sql.append(' ').append(cf.getLogical()).append(' ');
			}
		}
	}

	protected void where(Sql sql, Query<?> query, String alias) {
		if (!query.hasFilters()) {
			return;
		}
		
		sql.append(" WHERE ");
		
		Entity<?> entity = query.getEntity();
		if (entity != null) {
			whereCompositeFilter(sql, entity, alias, query.getFilters());
		}
		else {
			whereCompositeFilter(sql, alias, query.getFilters());
		}
	}
	
	protected void whereValueFilter(Sql sql, String table, String column, Filter.ValueFilter vf) {
		sql.append(escapeColumn(table, column)).append(' ');

		Operator op = vf.getOperator();
		if (op == Operator.BETWEEN || op == Operator.NOT_BETWEEN) {
			sql.append(op).append(" ? AND ?");
			sql.addParam(vf.getValue(0));
			sql.addParam(vf.getValue(1));
		}
		else if (op == Operator.IN || op == Operator.NOT_IN) {
			sql.append(op).append('(');
			for (Object v : Iterators.asIterable(vf.getValue())) {
				sql.append('?').append(',');
				sql.addParam(v);
			}
			sql.setCharAt(sql.length() - 1, ')');
		}
		else if (op == Operator.MATCH) {
			sql.append("LIKE ?");
			sql.addParam(Sqls.stringLike((String)vf.getValue()));
		}
		else if (op == Operator.NOT_MATCH) {
			sql.append("NOT LIKE ?");
			sql.addParam(Sqls.stringLike((String)vf.getValue()));
		}
		else if (op == Operator.LEFT_MATCH) {
			sql.append("LIKE ?");
			sql.addParam(Sqls.startsLike((String)vf.getValue()));
		}
		else if (op == Operator.NOT_LEFT_MATCH) {
			sql.append("NOT LIKE ?");
			sql.addParam(Sqls.startsLike((String)vf.getValue()));
		}
		else if (op == Operator.RIGHT_MATCH) {
			sql.append("LIKE ?");
			sql.addParam(Sqls.endsLike((String)vf.getValue()));
		}
		else if (op == Operator.NOT_RIGHT_MATCH) {
			sql.append("NOT LIKE ?");
			sql.addParam(Sqls.endsLike((String)vf.getValue()));
		}
		else {
			sql.append(op).append(' ');
			sql.append('?');
			sql.addParam(vf.getValue());
		}
	}
	
	protected void order(Sql sql, Query<?> query) {
		order(sql.getSqlBuilder(), query);
	}
	
	protected void order(StringBuilder sql, Query<?> query) {
		if (!query.hasOrders()) {
			return;
		}
		
		sql.append(" ORDER BY");
		
		Entity<?> entity = query.getEntity();
		if (entity != null) {
			for (Entry<String, Order> en : query.getOrders().entrySet()) {
				EntityField ef = getEntityField(entity, en.getKey(), "order");
				sql.append(' ').append(escapeColumn(ef.getColumn())).append(' ').append(en.getValue()).append(',');
			}
		}
		else {
			for (Entry<String, Order> en : query.getOrders().entrySet()) {
				sql.append(' ').append(escapeColumn(en.getKey())).append(' ').append(en.getValue()).append(',');
			}
		}
		sql.setCharAt(sql.length() - 1, ' ');
	}
	
	protected void group(Sql sql, Query<?> query, String alias) {
		if (!query.hasGroups()) {
			return;
		}
		
		sql.append(" GROUP BY");
		
		Entity<?> entity = query.getEntity();
		if (entity != null) {
			for (String k : query.getGroups()) {
				EntityField ef = getEntityField(entity, k, "order");
				sql.append(' ').append(escapeColumn(alias, ef.getColumn())).append(' ');
			}
		}
		else {
			for (String k : query.getGroups()) {
				sql.append(' ').append(escapeColumn(k));
			}
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
		String type = ef.getNativeType();
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
				sb.append(escapeColumn(pk.getColumn())).append(',');
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
			sb.append(" ON ").append(escapeTable(entity.getTableName())).append(" (");
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
				sql.append(escapeColumn(ef.getColumn())).append(',');
			}
			sql.setCharAt(sql.length() - 1, ')');

			Entity<?> ref = fk.getReference();
			sql.append(" REFERENCES ").append(escapeTable(ref.getTableName())).append(" (");
			for (EntityField ef : ref.getPrimaryKeys()) {
				sql.append(escapeColumn(ef.getColumn())).append(',');
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
			.append(escapeTable(entity.getTableName()))
			.append(" ADD CONSTRAINT ")
			.append(entity.getTableName())
			.append("_FK_")
			.append(fk.getName())
			.append(" FOREIGN KEY (");
			for (EntityField ef : fk.getFields()) {
				sb.append(escapeColumn(ef.getColumn())).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');

			Entity<?> ref = fk.getReference();
			sb.append(" REFERENCES ").append(escapeTable(ref.getTableName())).append(" (");
			for (EntityField ef : ref.getPrimaryKeys()) {
				sb.append(escapeColumn(ef.getColumn())).append(',');
			}
			sb.setCharAt(sb.length() - 1, ')');
			sqls.add(sb.toString());
		}
	}

	protected String escapeTable(String table) {
		return table;
	}

	protected String escapeColumn(String table, String column) {
		if (Strings.isEmpty(table)) {
			return escapeColumn(column);
		}
		return table + '.' + escapeColumn(column);
	}

	protected String escapeColumn(String column) {
		return column;
	}
	
}
