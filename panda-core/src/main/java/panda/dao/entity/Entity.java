package panda.dao.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bind.json.JsonObject;
import panda.dao.DB;
import panda.lang.Collections;
import panda.lang.Objects;

/**
 * @param <T> class type
 */
public class Entity<T> {
	protected Class<T> type;
	
	/** database table name */
	protected String table;
	
	/** database view name */
	protected String view;

	protected String comment;
	
	protected JsonObject options;
	
	protected EntityField identity;
	
	protected Map<String, EntityField> fields;
	protected Map<String, EntityField> columns;
	
	/** primary keys */
	protected List<EntityField> pkeys;
	protected Map<String, EntityIndex> indexes;
	/** foreign keys */
	protected Map<String, EntityFKey> fkeys;
	protected Map<String, EntityJoin> joins;

	/** prepare sqls */
	protected Map<DB, String> prepSqls;
	
	/** post sqls */
	protected Map<DB, String> postSqls;
	
	protected BeanHandler<T> beanHandler;
	
	/**
	 * constructor
	 */
	protected Entity(Class<T> type) {
		this.type = type;
		this.fields = new LinkedHashMap<String, EntityField>();
		this.columns = new LinkedHashMap<String, EntityField>();
	}

	/**
	 * constructor
	 * 
	 * @param entity the Entity
	 */
	public Entity(Entity<T> entity) {
		this.type = entity.type;
		this.table = entity.table;
		this.view = entity.view;
		this.comment = entity.comment;
		this.identity = entity.identity;
		this.fields = entity.fields;
		this.columns = entity.columns;
		this.pkeys = entity.pkeys;
		this.indexes = entity.indexes;
		this.fkeys = entity.fkeys;
		this.joins = entity.joins;
		this.options = entity.options;
		this.beanHandler = entity.beanHandler;
	}

	/**
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	protected void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the view
	 */
	public String getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	protected void setView(String view) {
		this.view = view;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	protected void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the props
	 */
	public JsonObject getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	protected void setOptions(JsonObject options) {
		this.options = options;
	}

	/**
	 * @return the identity
	 */
	public EntityField getIdentity() {
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	protected void setIdentity(EntityField identity) {
		this.identity = identity;
	}

	/**
	 * @return the fields
	 */
	public Collection<EntityField> getFields() {
		return fields.values();
	}

	/**
	 * @return the columns
	 */
	public Collection<EntityField> getColumns() {
		return columns.values();
	}

	/**
	 * @param field the field to add
	 */
	protected void addField(EntityField field) {
		field.setEntity(this);
		fields.put(field.getName(), field);
		columns.put(field.getColumn(), field);
	}

	/**
	 * @return the pkeys
	 */
	public List<EntityField> getPrimaryKeys() {
		return pkeys;
	}

	/**
	 * @param ef entity field
	 */
	protected void addPrimaryKey(EntityField ef) {
		if (pkeys == null) {
			pkeys = new ArrayList<EntityField>();
		}
		pkeys.add(ef);
	}

	/**
	 * @return the indexes
	 */
	@SuppressWarnings("unchecked")
	public Collection<EntityIndex> getIndexes() {
		return indexes == null ? Collections.EMPTY_LIST : indexes.values();
	}

	/**
	 * @param index the index to add
	 */
	protected void addIndex(EntityIndex index) {
		if (indexes == null) {
			indexes = new LinkedHashMap<String, EntityIndex>();
		}
		indexes.put(index.getName(), index);
	}

	/**
	 * get index by name
	 * 
	 * @param name key name
	 * @return index
	 */
	public EntityIndex getIndex(String name) {
		return indexes.get(name);
	}


	/**
	 * @return the foreign keys
	 */
	@SuppressWarnings("unchecked")
	public Collection<EntityFKey> getForeignKeys() {
		return fkeys == null ? Collections.EMPTY_LIST : fkeys.values();
	}

	/**
	 * @param fkey the foreign key to add
	 */
	protected void addForeignKey(EntityFKey fkey) {
		if (fkeys == null) {
			fkeys = new LinkedHashMap<String, EntityFKey>();
		}
		fkeys.put(fkey.getName(), fkey);
	}

	/**
	 * get a foreign key by name
	 * 
	 * @param name key name
	 * @return foreign key
	 */
	public EntityFKey getForeignKey(String name) {
		return fkeys.get(name);
	}


	/**
	 * @return the joins
	 */
	@SuppressWarnings("unchecked")
	public Collection<EntityJoin> getJoins() {
		return joins == null ? Collections.EMPTY_LIST : joins.values();
	}

	/**
	 * @param join the join to add
	 */
	protected void addJoin(EntityJoin join) {
		if (joins == null) {
			joins = new LinkedHashMap<String, EntityJoin>();
		}
		joins.put(join.getName(), join);
	}

	/**
	 * get a join by name
	 * 
	 * @param name key name
	 * @return join
	 */
	public EntityJoin getJoin(String name) {
		return joins.get(name);
	}

	/**
	 * get a entity field by name
	 * 
	 * @param name field name
	 * @return entity field
	 */
	public EntityField getField(String name) {
		return fields.get(name);
	}

	/**
	 * get a entity field by column name
	 * 
	 * @param name column name
	 * @return entity field
	 */
	public EntityField getColumn(String name) {
		return columns.get(name);
	}

	/**
	 * @param name the option name
	 * @return option
	 */
	public Object getOption(String name) {
		if (options == null) {
			return null;
		}
		return options.opt(name);
	}

	/**
	 * add property
	 * @param name property name
	 * @param value property value
	 */
	protected void addProperty(String name, String value) {
		if (options == null) {
			options = new JsonObject();
		}
		options.set(name, value);
	}

	/**
	 * add properties map
	 * @param map properties map
	 */
	protected void addProperties(Map<? extends String, ? extends Object> map) {
		if (options == null) {
			options = new JsonObject();
		}
		options.putAll(map);
	}

	/**
	 * @param db the DB object
	 * @return the prepSqls
	 */
	public String getPrepSql(DB db) {
		if (prepSqls == null) {
			return null;
		}
		return prepSqls.get(db);
	}

	/**
	 * @param db database type
	 * @param sql sql
	 */
	protected void addPrepSql(DB db, String sql) {
		if (prepSqls == null) {
			prepSqls = new HashMap<DB, String>();
		}
		prepSqls.put(db, sql);
	}

	/**
	 * @param db the DB object
	 * @return the postSql
	 */
	public String getPostSql(DB db) {
		if (postSqls == null) {
			return null;
		}
		return postSqls.get(db);
	}

	/**
	 * @param db database type
	 * @param sql sql
	 */
	protected void addPostSql(DB db, String sql) {
		if (postSqls == null) {
			postSqls = new HashMap<DB, String>();
		}
		postSqls.put(db, sql);
	}

	/**
	 * @return the beanHandler
	 */
	public BeanHandler<T> getBeanHandler() {
		return beanHandler;
	}

	/**
	 * @param beanHandler the beanHandler to set
	 */
	protected void setBeanHandler(BeanHandler<T> beanHandler) {
		this.beanHandler = beanHandler;
	}

	/**
	 * get field value of data
	 * @param data POJO
	 * @param name field name
	 * @return field value
	 */
	@SuppressWarnings("unchecked")
	public Object getFieldValue(Object data, String name) {
		return beanHandler.getPropertyValue((T)data, name);
	}

	/**
	 * set field value to data
	 * @param data POJO
	 * @param name field name
	 * @param value field value
	 * @return true if set successfully
	 */
	@SuppressWarnings("unchecked")
	public boolean setFieldValue(Object data, String name, Object value) {
		return beanHandler.setPropertyValue((T)data, name, value);
	}
	
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("type", type)
				.append("table", table)
				.toString();
	}
}
