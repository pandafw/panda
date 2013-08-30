package panda.dao;


/**
 * @param <T> model type
 * @author yf.frank.wang@gmail.com
 */
public interface ModelMetaData<T> extends ModelAccessor<T> {

	/**
	 * @return the model bean class
	 */
	Class getModelBeanClass();
	
	/**
	 * @return the model dao class
	 */
	Class getModelDAOClass();

	/**
	 * @return the model example class
	 */
	Class getModelExampleClass();
	
	/**
	 * @return the table name
	 */
	String getTableName();

	/**
	 * @return the table alias
	 */
	String getTableAlias();

	/**
	 * @return the property names
	 */
	String[] getPropertyNames();

	/**
	 * @return the field names
	 */
	String[] getFieldNames();

	/**
	 * @return the identity property name
	 */
	String getIdentityName();
	
	/**
	 * @return the primary key property names
	 */
	String[] getPrimaryKeys();
	
	/**
	 * @return the primary key column names
	 */
	String[] getPrimaryKeyColumnNames();
	
	/**
	 * @return the primary key column aliases
	 */
	String[] getPrimaryKeyColumnAliases();
	
	/**
	 * @return the foreign key constraint array
	 */
	String[][] getForeignKeyConstraints();
	
	/**
	 * @param propertyName propertyName
	 * @return true if the property is a primary key
	 */
	boolean isPrimaryKey(String propertyName);

	/**
	 * @return the unique key constraints
	 */
	String[][] getUniqueKeyConstraints();
	
	/**
	 * getPropertyName
	 * @param fieldName fieldName
	 * @return property name
	 */
	String getPropertyName(String fieldName);
	
	/**
	 * getFieldName
	 * @param propertyName propertyName
	 * @return field name
	 */
	String getFieldName(String propertyName);

	/**
	 * getColumnName
	 * @param propertyName propertyName
	 * @return column name
	 */
	String getColumnName(String propertyName);
	
	/**
	 * getColumnAlias
	 * @param propertyName propertyName
	 * @return column alias
	 */
	String getColumnAlias(String propertyName);
	
}
