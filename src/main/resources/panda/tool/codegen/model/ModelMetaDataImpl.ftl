<#--
/*
 * This file is part of Nuts Framework.
 * Copyright(C) 2009-2012 Nuts Develop Team.
 *
 * Nuts Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 * 
 * Nuts Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nuts Framework. If not, see <http://www.gnu.org/licenses/>.
 */
-->
	/**
	 * static instance
	 */
	private static final ${modelMetaDataClass} INSTANCE = new ${modelMetaDataClass}();
	
	/**
	 * @return ModelMetaData
	 */
	public static ${modelMetaDataClass} getInstance() {
		return INSTANCE;
	}

	/**
	 * properties
	 */	
	protected Map<String, Object[]> properties;

	/**
	 * fields
	 */	
	protected Map<String, String> fields;

	/**
	 * Constructor
	 */
	public ${modelMetaDataClass}() {
		properties = new HashMap<String, Object[]>();
<#list model.propertyList as p>
<#if p.column?has_content>
		properties.put(PN_${p.uname}, new Object[] { ${(p_index + 1)?c}, ${p.simpleGenericJavaType}.class, FN_${p.column?upper_case}, CN_${model.tableAlias?has_content?string(model.tableAlias!, model.table)?upper_case}_${p.column?upper_case}, CA_${p.columnAlias?has_content?string(p.columnAlias!, p.column)?upper_case} });
<#elseif p.joinColumn?has_content>
		properties.put(PN_${p.uname}, new Object[] { ${(p_index + 1)?c}, ${p.simpleGenericJavaType}.class, null, CN_${p.joinTableAlias?has_content?string(p.joinTableAlias!, p.joinTable)?upper_case}_${p.joinColumn?upper_case}, CA_${p.columnAlias?has_content?string(p.columnAlias!, p.joinColumn)?upper_case} });
<#elseif p.sqlExpression?has_content>
		properties.put(PN_${p.uname}, new Object[] { ${(p_index + 1)?c}, ${p.simpleGenericJavaType}.class, null, CN_${p.columnAlias?upper_case}, CA_${p.columnAlias?upper_case} });
<#else>
		properties.put(PN_${p.uname}, new Object[] { ${(p_index + 1)?c}, ${p.simpleGenericJavaType}.class, null, null, null });
</#if>
</#list>

		fields = new HashMap<String, String>();
<#list model.columnList as p>
		fields.put(FN_${p.column?upper_case}, PN_${p.uname});
</#list>
	}

	/**
	 * @return the model bean class
	 */
	public Class<${modelBeanClass}> getModelBeanClass() {
		return ${modelBeanClass}.class;
	}
	
	/**
	 * @return the model dao class
	 */
	public Class<${modelDaoClass}> getModelDAOClass() {
		return ${modelDaoClass}.class;
	}

	/**
	 * @return the model example class
	 */
	public Class<${modelExampleClass}> getModelExampleClass() {
		return ${modelExampleClass}.class;
	}
	
	/**
	 * @return the table name
	 */
	public String getTableName() {
		return TABLE_NAME;
	}

	/**
	 * @return the table alias
	 */
	public String getTableAlias() {
		return TABLE_ALIAS;
	}

	/**
	 * @return the property names
	 */
	public String[] getPropertyNames() {
		return PROPERTY_NAMES;
	}

	/**
	 * @return the field names
	 */
	public String[] getFieldNames() {
		return FIELD_NAMES;
	}

	/**
	 * @return the identity property name
	 */
	public String getIdentityName() {
		return IDENTITY;
	}
	
	/**
	 * @return the primary key property names
	 */
	public String[] getPrimaryKeys() {
		return PRIMARY_KEYS;
	}

	/**
	 * @return the primary key column names
	 */
	public String[] getPrimaryKeyColumnNames() {
		return PRIMARY_KEY_CNS;
	}
	
	/**
	 * @return the primary key column aliases
	 */
	public String[] getPrimaryKeyColumnAliases() {
		return PRIMARY_KEY_CAS;
	}

	/**
	 * @return the foreign key constraint array
	 */
	public String[][] getForeignKeyConstraints() {
		return FOREIGN_KEY_CONSTRAINTS;
	}

	/**
	 * @param propertyName propertyName
	 * @return true if the property is a primary key
	 */
	public boolean isPrimaryKey(String propertyName) {
		for (String f : PRIMARY_KEYS) {
			if (f.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the unique key constraints
	 */
	public String[][] getUniqueKeyConstraints() {
		return UNIQUE_KEY_CONSTRAINTS;
	}

	/**
	 * getPropertyName
	 * @param fieldName fieldName
	 * @return property name
	 */
	public String getPropertyName(String fieldName) {
		return fields.get(fieldName);
	}

	/**
	 * getFieldName
	 * @param propertyName propertyName
	 * @return field name
	 */
	public String getFieldName(String propertyName) {
		return (String)getPropertyItem(properties, propertyName, 2);
	}

	/**
	 * getColumnName
	 * @param propertyName propertyName
	 * @return column name
	 */
	public String getColumnName(String propertyName) {
		return (String)getPropertyItem(properties, propertyName, 3);
	}

	/**
	 * getColumnAlias
	 * @param propertyName propertyName
	 * @return column alias
	 */
	public String getColumnAlias(String propertyName) {
		return (String)getPropertyItem(properties, propertyName, 4);
	}

	//--------------------------------------------------------------
	// ModelAccessor
	//--------------------------------------------------------------
	/**
	 * create bean object
	 * @return bean instance 
	 */
	public ${modelBeanClass} createObject() {
		return new ${modelBeanClass}();
	}

	/**
	 * get read property names
	 * @param data data object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(${modelBeanClass} data) {
		return PROPERTY_NAMES;
	}

	/**
	 * get write property names
	 * @param data data object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(${modelBeanClass} data) {
		return PROPERTY_NAMES;
	}

	/**
	 * get property type
	 * @param propertyName property name
	 * @return property type
	 */
	public Class<?> getPropertyType(String propertyName) {
		return (Class<?>)getPropertyItem(properties, propertyName, 1);
	}

	/**
	 * get property type
	 * @param data data object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public Class<?> getPropertyType(${modelBeanClass} data, String propertyName) {
		return (Class<?>)getPropertyItem(properties, propertyName, 1);
	}
	
	/**
	 * get property value 
	 * @param data data object
	 * @param propertyName property name
	 * @return value
	 */
	public Object getPropertyValue(${modelBeanClass} data, String propertyName) {
		Integer i = (Integer)getPropertyItem(properties, propertyName, 0);
		if (i == null) {
			throw illegalPropertyException(propertyName);
		}

		switch (i) {
	<#list model.propertyList as p>
		case ${(p_index + 1)?c}: return data.get${p.name?cap_first}();
	</#list>
		default: 
			throw illegalPropertyException(propertyName);
		}
	}
	
	/**
	 * set property value 
	 * @param data data object
	 * @param propertyName property name
	 * @param value value
	 */
	public void setPropertyValue(${modelBeanClass} data, String propertyName, Object value) {
		Integer i = (Integer)getPropertyItem(properties, propertyName, 0);
		if (i == null) {
			throw illegalPropertyException(propertyName);
		}

		switch (i) {
	<#list model.propertyList as p>
		case ${(p_index + 1)?c}: data.set${p.name?cap_first}((${p.simpleJavaType})value); return;
	</#list>
		default: 
			throw illegalPropertyException(propertyName);
		}
	}

	/**
	 * set data properties values from map
	 * @param data data
	 * @param map map
	 */
	public void setDataProperties(${modelBeanClass} data, Map<String, Object> map) {
	<#list model.propertyList as p>
		if (map.containsKey(PN_${p.uname})) {
			data.set${p.name?cap_first}((${p.simpleJavaType})map.get(PN_${p.uname}));
		}
	</#list>
	}
	
	/**
	 * get data property values map
	 * @param data data
	 * @return property values map
	 */
	public Map<String, Object> getDataProperties(${modelBeanClass} data) {
		Map<String, Object> map = new HashMap<String, Object>();
	<#list model.propertyList as p>
		map.put(PN_${p.uname}, data.get${p.name?cap_first}());
	</#list>
		return map;
	}
