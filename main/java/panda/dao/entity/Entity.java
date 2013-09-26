package panda.dao.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.bind.json.JsonObject;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Entity<T> {
	protected Class<T> type;
	protected String tableName;
	protected JsonObject tableMeta;
	protected String viewName;
	protected String comment;
	
	protected EntityField identity;
	
	protected Map<String, EntityField> fieldMap;
	protected Map<String, EntityField> columnMap;
	
	protected List<EntityField> primaryKeys;
	protected Map<String, EntityIndex> indexMap;
	protected Map<String, EntityFKey> foreignKeyMap;

	/**
	 * constructor
	 */
	protected Entity(Class<T> type) {
		this.type = type;
	}

	/**
	 * constructor
	 */
	public Entity(Entity<T> entity) {
		this.type = entity.type;
		this.tableName = entity.tableName;
		this.tableMeta = entity.tableMeta;
		this.viewName = entity.viewName;
		this.comment = entity.comment;
		this.identity = entity.identity;
		this.fieldMap = entity.fieldMap;
		this.columnMap = entity.columnMap;
		this.primaryKeys = entity.primaryKeys;
		this.indexMap = entity.indexMap;
		this.foreignKeyMap = entity.foreignKeyMap;
	}

	/**
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	protected void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the tableMeta
	 */
	public JsonObject getTableMeta() {
		return tableMeta;
	}

	/**
	 * @param tableMeta the tableMeta to set
	 */
	protected void setTableMeta(JsonObject tableMeta) {
		this.tableMeta = tableMeta;
	}

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName the viewName to set
	 */
	protected void setViewName(String viewName) {
		this.viewName = viewName;
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
		return fieldMap.values();
	}

	/**
	 * @param fields the fields to set
	 */
	protected void addField(EntityField field) {
		if (fieldMap == null) {
			fieldMap = new LinkedHashMap<String, EntityField>();
		}
		if (columnMap == null) {
			columnMap = new LinkedHashMap<String, EntityField>();
		}

		field.setEntity(this);
		fieldMap.put(field.getName(), field);
		columnMap.put(field.getColumn(), field);
	}

	/**
	 * @return the columns
	 */
	public Collection<EntityField> getColumns() {
		return columnMap.values();
	}

	/**
	 * @return the primaryKeys
	 */
	public List<EntityField> getPrimaryKeys() {
		return primaryKeys;
	}

	/**
	 * @param primaryKeys the primaryKeys to set
	 */
	protected void addPrimaryKey(EntityField ef) {
		if (primaryKeys == null) {
			primaryKeys = new ArrayList<EntityField>();
		}
		primaryKeys.add(ef);
	}

	/**
	 * @return the indexes
	 */
	public Collection<EntityIndex> getIndexes() {
		return indexMap.values();
	}

	/**
	 * @param index the index to add
	 */
	protected void addIndex(EntityIndex index) {
		if (indexMap == null) {
			indexMap = new LinkedHashMap<String, EntityIndex>();
		}
		indexMap.put(index.getName(), index);
	}

	/**
	 * get index by name
	 * 
	 * @param name key name
	 * @return index
	 */
	public EntityIndex getIndex(String name) {
		return indexMap.get(name);
	}


	/**
	 * @return the foreign keys
	 */
	public Collection<EntityFKey> getForeignKeys() {
		return foreignKeyMap.values();
	}

	/**
	 * @param index the index to add
	 */
	protected void addForeignKey(EntityFKey fkey) {
		if (foreignKeyMap == null) {
			foreignKeyMap = new LinkedHashMap<String, EntityFKey>();
		}
		foreignKeyMap.put(fkey.getName(), fkey);
	}

	/**
	 * get a foreign key by name
	 * 
	 * @param name key name
	 * @return foreign key
	 */
	public EntityFKey getForeignKey(String name) {
		return foreignKeyMap.get(name);
	}

	/**
	 * get a entity field by name
	 * 
	 * @param name field name
	 * @return entity field
	 */
	public EntityField getField(String name) {
		return fieldMap.get(name);
	}

	/**
	 * get a entity field by column name
	 * 
	 * @param name column name
	 * @return entity field
	 */
	public EntityField getColumn(String name) {
		return columnMap.get(name);
	}

	/**
	 * @return meta
	 */
	protected JsonObject getMetas() {
		return tableMeta;
	}

	/**
	 * @return meta
	 */
	public String getMeta(String name) {
		return tableMeta.optString(name);
	}
}
