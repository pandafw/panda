package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Strings;

/**
 * <p>
 * Java class for ModelProperty complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ModelProperty&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;validator&quot; type=&quot;{nuts.tools.codegen}Validator&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;sqlExpression&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinCondition&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinColumn&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinTableAlias&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinTable&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;foreignColumn&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;foreignTable&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;foreignKey&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;uniqueKey&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;primaryKey&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;defaultValue&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;notNull&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;scale&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;size&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;fieldKind&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;dbType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;jdbcType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;columnAlias&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;column&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;order&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;setterTrim&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;getterTrim&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;setterCode&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;getterCode&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;initValue&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modifier&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;type&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ModelProperty")
public class ModelProperty implements Comparable<ModelProperty> {

	@XmlElement(name = "validator")
	private List<Validator> validatorList;

	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String sqlExpression;
	@XmlAttribute
	private String joinCondition;
	@XmlAttribute
	private String joinType;
	@XmlAttribute
	private String joinColumn;
	@XmlAttribute
	private String joinTableAlias;
	@XmlAttribute
	private String joinTable;
	@XmlAttribute
	private String foreignColumn;
	@XmlAttribute
	private String foreignTable;
	@XmlAttribute
	private String foreignKey;
	@XmlAttribute
	private String uniqueKey;
	@XmlAttribute
	private Boolean primaryKey;
	@XmlAttribute
	private String defaultValue;
	@XmlAttribute
	private Boolean notNull;
	@XmlAttribute
	private String size;
	@XmlAttribute
	private String fieldKind;
	@XmlAttribute
	private String dbType;
	private String nativeType;
	@XmlAttribute
	private String jdbcType;
	@XmlAttribute
	private String columnAlias;
	@XmlAttribute
	private String column;
	@XmlAttribute
	private Integer order;
	@XmlAttribute
	private String getterTrim;
	@XmlAttribute
	private String setterTrim;
	@XmlAttribute
	private String setterCode;
	@XmlAttribute
	private String getterCode;
	@XmlAttribute
	private String initValue;
	@XmlAttribute
	private String modifier;
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String label;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public ModelProperty() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param property source property
	 */
	public ModelProperty(ModelProperty property) {
		this.tooltip = property.tooltip;
		this.sqlExpression = property.sqlExpression;
		this.joinCondition = property.joinCondition;
		this.joinType = property.joinType;
		this.joinColumn = property.joinColumn;
		this.joinTableAlias = property.joinTableAlias;
		this.joinTable = property.joinTable;
		this.foreignColumn = property.foreignColumn;
		this.foreignTable = property.foreignTable;
		this.foreignKey = property.foreignKey;
		this.uniqueKey = property.uniqueKey;
		this.primaryKey = property.primaryKey;
		this.defaultValue = property.defaultValue;
		this.notNull = property.notNull;
		this.size = property.size;
		this.fieldKind = property.fieldKind;
		this.dbType = property.dbType;
		this.nativeType = property.nativeType;
		this.jdbcType = property.jdbcType;
		this.columnAlias = property.columnAlias;
		this.column = property.column;
		this.order = property.order;
		this.setterTrim = property.setterTrim;
		this.getterTrim = property.getterTrim;
		this.setterCode = property.setterCode;
		this.getterCode = property.getterCode;
		this.initValue = property.initValue;
		this.modifier = property.modifier;
		this.type = property.type;
		this.label = property.label;
		this.name = property.name;

		validatorList = new ArrayList<Validator>();
		for (Validator v : property.getValidatorList()) {
			validatorList.add(new Validator(v));
		}
	}

	/**
	 * extend property
	 * 
	 * @param src source property
	 * @param parent extend property
	 * @return property
	 */
	public static ModelProperty extend(ModelProperty src, ModelProperty parent) {
		ModelProperty me = new ModelProperty(parent);

		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.sqlExpression != null) {
			me.sqlExpression = src.sqlExpression;
		}
		if (src.joinCondition != null) {
			me.joinCondition = src.joinCondition;
		}
		if (src.joinType != null) {
			me.joinType = src.joinType;
		}
		if (src.joinColumn != null) {
			me.joinColumn = src.joinColumn;
		}
		if (src.joinTableAlias != null) {
			me.joinTableAlias = src.joinTableAlias;
		}
		if (src.joinTable != null) {
			me.joinTable = src.joinTable;
		}
		if (src.foreignColumn != null) {
			me.foreignColumn = src.foreignColumn;
		}
		if (src.foreignTable != null) {
			me.foreignTable = src.foreignTable;
		}
		if (src.foreignKey != null) {
			me.foreignKey = src.foreignKey;
		}
		if (src.uniqueKey != null) {
			me.uniqueKey = src.uniqueKey;
		}
		if (src.primaryKey != null) {
			me.primaryKey = src.primaryKey;
		}
		if (src.defaultValue != null) {
			me.defaultValue = src.defaultValue;
		}
		if (src.notNull != null) {
			me.notNull = src.notNull;
		}
		if (src.size != null) {
			me.size = src.size;
		}
		if (src.fieldKind != null) {
			me.fieldKind = src.fieldKind;
		}
		if (src.dbType != null) {
			me.dbType = src.dbType;
		}
		if (src.nativeType != null) {
			me.nativeType = src.nativeType;
		}
		if (src.jdbcType != null) {
			me.jdbcType = src.jdbcType;
		}
		if (src.columnAlias != null) {
			me.columnAlias = src.columnAlias;
		}
		if (src.column != null) {
			me.column = src.column;
		}
		if (src.order != null) {
			me.order = src.order;
		}
		if (src.setterTrim != null) {
			me.setterTrim = src.setterTrim;
		}
		if (src.getterTrim != null) {
			me.getterTrim = src.getterTrim;
		}
		if (src.setterCode != null) {
			me.setterCode = src.setterCode;
		}
		if (src.getterCode != null) {
			me.getterCode = src.getterCode;
		}
		if (src.initValue != null) {
			me.initValue = src.initValue;
		}
		if (src.modifier != null) {
			me.modifier = src.modifier;
		}
		if (src.type != null) {
			me.type = src.type;
		}
		if (src.label != null) {
			me.label = src.label;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		me.getValidatorList().addAll(src.getValidatorList());

		return me;
	}

	/**
	 * Gets the value of the full java type.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getFullJavaType() {
		return TypeUtils.getFullJavaType(type, name);
	}

	/**
	 * Gets the value of the native java type.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getNativeJavaType() {
		return TypeUtils.getNativeJavaType(type, name);
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSimpleJavaType() {
		return TypeUtils.getSimpleJavaType(type, name);
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSimpleGenericJavaType() {
		return TypeUtils.getSimpleGenericJavaType(type, name);
	}

	/**
	 * @return the validatorList
	 */
	public List<Validator> getValidatorList() {
		if (validatorList == null) {
			validatorList = new ArrayList<Validator>();
		}
		return this.validatorList;
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Gets the value of the sqlExpression property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSqlExpression() {
		return sqlExpression;
	}

	/**
	 * Sets the value of the sqlExpression property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setSqlExpression(String value) {
		this.sqlExpression = value;
	}

	/**
	 * Gets the value of the joinCondition property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinCondition() {
		return joinCondition;
	}

	/**
	 * Sets the value of the joinCondition property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinCondition(String value) {
		this.joinCondition = value;
	}

	/**
	 * Gets the value of the joinType property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinType() {
		return joinType;
	}

	/**
	 * Sets the value of the joinType property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinType(String value) {
		this.joinType = value;
	}

	/**
	 * Gets the value of the joinColumn property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinColumn() {
		return joinColumn;
	}

	/**
	 * Sets the value of the joinColumn property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinColumn(String value) {
		this.joinColumn = value;
	}

	/**
	 * Gets the value of the joinTableAlias property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinTableAlias() {
		return joinTableAlias;
	}

	/**
	 * Sets the value of the joinTableAlias property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinTableAlias(String value) {
		this.joinTableAlias = value;
	}

	/**
	 * Gets the value of the joinTable property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinTable() {
		return joinTable;
	}

	/**
	 * Sets the value of the joinTable property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinTable(String value) {
		this.joinTable = value;
	}

	/**
	 * Gets the value of the foreignColumn property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getForeignColumn() {
		return foreignColumn;
	}

	/**
	 * Sets the value of the foreignColumn property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setForeignColumn(String value) {
		this.foreignColumn = value;
	}

	/**
	 * Gets the value of the foreignTable property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getForeignTable() {
		return foreignTable;
	}

	/**
	 * Sets the value of the foreignTable property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setForeignTable(String value) {
		this.foreignTable = value;
	}

	/**
	 * Gets the value of the foreignKey property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getForeignKey() {
		return foreignKey;
	}

	/**
	 * Sets the value of the foreignKey property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setForeignKey(String value) {
		this.foreignKey = value;
	}

	/**
	 * Gets the value of the uniqueKey property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}

	/**
	 * Gets the value of the uniqueKey property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String[] getUniqueKeys() {
		return Strings.split(uniqueKey);
	}

	/**
	 * Sets the value of the uniqueKey property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setUniqueKey(String value) {
		this.uniqueKey = value;
	}

	/**
	 * Gets the value of the primaryKey property.
	 * 
	 * @return possible object is {@link Boolean }
	 */
	public Boolean getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the value of the primaryKey property.
	 * 
	 * @param value allowed object is {@link Boolean }
	 */
	public void setPrimaryKey(Boolean value) {
		this.primaryKey = value;
	}

	/**
	 * Gets the value of the defaultValue property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the value of the defaultValue property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	/**
	 * Gets the value of the notNull property.
	 * 
	 * @return possible object is {@link Boolean }
	 */
	public Boolean getNotNull() {
		return notNull;
	}

	/**
	 * Sets the value of the notNull property.
	 * 
	 * @param value allowed object is {@link Boolean }
	 */
	public void setNotNull(Boolean value) {
		this.notNull = value;
	}

	/**
	 * Gets the value of the size property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Sets the value of the size property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setSize(String value) {
		this.size = value;
	}

	/**
	 * @return true if property is bin field
	 */
	public String getFieldKind() {
		if (fieldKind == null) {
			String sjt = getSimpleJavaType();
			if ("boolean".equalsIgnoreCase(sjt)) {
				fieldKind = "boolean";
			}
			else if ("byte".equalsIgnoreCase(sjt)
					|| "short".equalsIgnoreCase(sjt)
					|| "int".equalsIgnoreCase(sjt)
					|| "integer".equalsIgnoreCase(sjt)
					|| "long".equalsIgnoreCase(sjt)
					|| "float".equalsIgnoreCase(sjt)
					|| "double".equalsIgnoreCase(sjt)
					|| "bigint".equalsIgnoreCase(sjt)
					|| "bigdecimal".equalsIgnoreCase(sjt)) {
				fieldKind = "number";
			}
			else if ("date".equalsIgnoreCase(sjt)) {
				fieldKind = "date";
			}
			else if ("string".equalsIgnoreCase(sjt)
					|| "char".equalsIgnoreCase(sjt)
					|| "character".equalsIgnoreCase(sjt)) {
				fieldKind = "string";
			}
			else if ("byte[]".equalsIgnoreCase(sjt)
					|| "file".equalsIgnoreCase(sjt)
					|| sjt.startsWith("Upload")) {
				fieldKind = "bin";
			}
			else {
				fieldKind = "";
			}
		}
		return fieldKind;
	}

	/**
	 * @param fieldKind the fieldKind to set
	 */
	public void setFieldKind(String fieldKind) {
		this.fieldKind = fieldKind;
	}

	/**
	 * Gets the value of the dbType property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getDbType() {
		return dbType == null ? null : dbType.toUpperCase();
	}

	/**
	 * Sets the value of the dbType property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setDbType(String value) {
		this.dbType = value;
	}

	/**
	 * @return the nativeType
	 */
	public String getNativeType() {
		return nativeType;
	}

	/**
	 * @param nativeType the nativeType to set
	 */
	public void setNativeType(String nativeType) {
		this.nativeType = nativeType;
	}

	/**
	 * Gets the value of the jdbcType property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJdbcType() {
		return jdbcType == null ? null : jdbcType.toUpperCase();
	}

	/**
	 * Sets the value of the jdbcType property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJdbcType(String value) {
		this.jdbcType = value;
	}

	/**
	 * Gets the value of the columnAlias property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getColumnAlias() {
		return columnAlias;
	}

	/**
	 * Sets the value of the columnAlias property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setColumnAlias(String value) {
		this.columnAlias = value;
	}

	/**
	 * Gets the value of the column property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * Sets the value of the column property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setColumn(String value) {
		this.column = value;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @return the getterTrim
	 */
	public String getGetterTrim() {
		return getterTrim;
	}

	/**
	 * @param getterTrim the getterTrim to set
	 */
	public void setGetterTrim(String getterTrim) {
		this.getterTrim = getterTrim;
	}

	/**
	 * Gets the value of the setterTrim property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSetterTrim() {
		return setterTrim;
	}

	/**
	 * Sets the value of the setterTrim property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setSetterTrim(String value) {
		this.setterTrim = value;
	}

	/**
	 * Gets the value of the getterCode property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getGetterCode() {
		return getterCode;
	}

	/**
	 * Sets the value of the getterCode property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setGetterCode(String value) {
		this.getterCode = value;
	}

	/**
	 * Gets the value of the setterCode property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSetterCode() {
		return setterCode;
	}

	/**
	 * Sets the value of the setterCode property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setSetterCode(String value) {
		this.setterCode = value;
	}

	/**
	 * Gets the value of the initValue property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getInitValue() {
		return initValue;
	}

	/**
	 * Sets the value of the initValue property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setInitValue(String value) {
		this.initValue = value;
	}
	
	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setModifier(String value) {
		this.modifier = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the label property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value of the label property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setLabel(String value) {
		this.label = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the value of the uname property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getUname() {
		if (name == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			}
			else {
				sb.append(Character.toUpperCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

	protected int compareByName(ModelProperty o) {
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(ModelProperty o) {
		if (this == o) {
			return 0;
		}
		if (this.order == null && o.order == null) {
			return compareByName(o);
		}
		if (this.order == null) {
			return -1;
		}
		if (o.order == null) {
			return 1;
		}
		int i = this.order.compareTo(o.order);
		return i == 0 ? compareByName(o) : i;
	}

}
