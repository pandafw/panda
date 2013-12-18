package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.dao.sql.JdbcTypes;
import panda.lang.Asserts;
import panda.lang.Strings;

/**
 * <p>
 * Java class for EntityProperty complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;EntityProperty&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;validator&quot; type=&quot;{panda.tool.codegen}Validator&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinRefs&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinKeys&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinField&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;joinEntity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;foreignEntity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;foreignName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;uniqueKey&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;primaryKey&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;defaultValue&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;notNull&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;scale&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;size&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;fieldKind&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;dbType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;jdbcType&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;comment&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;column&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
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
@XmlType(name = "EntityProperty")
public class EntityProperty implements Comparable<EntityProperty> {

	@XmlElement(name = "validator")
	private List<Validator> validatorList;

	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String joinRefs;
	@XmlAttribute
	private String joinKeys;
	@XmlAttribute
	private String joinType;
	@XmlAttribute
	private String joinField;
	@XmlAttribute
	private String joinName;
	@XmlAttribute
	private String joinEntity;
	@XmlAttribute
	private String foreignEntity;
	@XmlAttribute
	private String foreignKey;
	@XmlAttribute
	private String uniqueKey;
	@XmlAttribute
	private Boolean primaryKey = false;
	@XmlAttribute
	private String defaultValue;
	@XmlAttribute
	private Boolean notNull = false;
	@XmlAttribute
	private Integer scale;
	@XmlAttribute
	private Integer size;
	@XmlAttribute
	private String fieldKind;
	@XmlAttribute
	private String dbType;
	private String nativeType;
	@XmlAttribute
	private String jdbcType;
	@XmlAttribute
	private String comment;
	@XmlAttribute
	private String column;
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
	private String modifier = "protected";
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String label;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public EntityProperty() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param property source property
	 */
	public EntityProperty(EntityProperty property) {
		this.tooltip = property.tooltip;
		this.joinRefs = property.joinRefs;
		this.joinKeys = property.joinKeys;
		this.joinType = property.joinType;
		this.joinField = property.joinField;
		this.joinName = property.joinName;
		this.joinEntity = property.joinEntity;
		this.foreignEntity = property.foreignEntity;
		this.foreignKey = property.foreignKey;
		this.uniqueKey = property.uniqueKey;
		this.primaryKey = property.primaryKey;
		this.defaultValue = property.defaultValue;
		this.notNull = property.notNull;
		this.scale = property.scale;
		this.size = property.size;
		this.fieldKind = property.fieldKind;
		this.dbType = property.dbType;
		this.nativeType = property.nativeType;
		this.jdbcType = property.jdbcType;
		this.comment = property.comment;
		this.column = property.column;
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
	public static EntityProperty extend(EntityProperty src, EntityProperty parent) {
		EntityProperty me = new EntityProperty(parent);

		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.joinRefs != null) {
			me.joinRefs = src.joinRefs;
		}
		if (src.joinKeys != null) {
			me.joinKeys = src.joinKeys;
		}
		if (src.joinType != null) {
			me.joinType = src.joinType;
		}
		if (src.joinField != null) {
			me.joinField = src.joinField;
		}
		if (src.joinName != null) {
			me.joinName = src.joinName;
		}
		if (src.joinEntity != null) {
			me.joinEntity = src.joinEntity;
		}
		if (src.foreignEntity != null) {
			me.foreignEntity = src.foreignEntity;
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
		if (src.scale != null) {
			me.scale = src.scale;
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
		if (src.comment != null) {
			me.comment = src.comment;
		}
		if (src.column != null) {
			me.column = src.column;
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
	public String getSimpleJavaWrapType() {
		return TypeUtils.getSimpleJavaWrapType(type, name);
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
	 * Gets the value of the joinRefs property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinRefs() {
		return joinRefs;
	}

	/**
	 * Sets the value of the joinRefs property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinRefs(String value) {
		this.joinRefs = value;
	}

	/**
	 * Gets the value of the joinKeys property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinKeys() {
		return joinKeys;
	}

	/**
	 * Sets the value of the joinKeys property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinKeys(String value) {
		this.joinKeys = value;
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
	 * Gets the value of the joinField property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinField() {
		return joinField;
	}

	/**
	 * Sets the value of the joinField property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinField(String value) {
		this.joinField = value;
	}

	/**
	 * Gets the value of the joinName property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinName() {
		return joinName;
	}

	/**
	 * Sets the value of the joinName property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinName(String value) {
		this.joinName = value;
	}

	/**
	 * Gets the value of the joinEntity property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getJoinEntity() {
		return joinEntity;
	}

	/**
	 * Sets the value of the joinEntity property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setJoinEntity(String value) {
		this.joinEntity = value;
	}

	/**
	 * Gets the value of the foreignEntity property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getForeignEntity() {
		return foreignEntity;
	}

	/**
	 * Sets the value of the foreignEntity property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setForeignEntity(String value) {
		this.foreignEntity = value;
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
	 * Gets the value of the scale property.
	 * 
	 * @return possible object is {@link Integer }
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * Sets the value of the scale property.
	 * 
	 * @param value allowed object is {@link Integer }
	 */
	public void setScale(Integer value) {
		if (value < 1) {
			throw new IllegalArgumentException("Illegal scale value: " + value);
		}
		this.size = value;
	}

	/**
	 * Gets the value of the size property.
	 * 
	 * @return possible object is {@link Integer }
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Sets the value of the size property.
	 * 
	 * @param value allowed object is {@link Integer }
	 */
	public void setSize(Integer value) {
		if (value < 1) {
			throw new IllegalArgumentException("Illegal size value: " + value);
		}
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
			else if (JdbcTypes.CHAR.equalsIgnoreCase(jdbcType)
					|| JdbcTypes.VARCHAR.equalsIgnoreCase(jdbcType)) {
				fieldKind = "string";
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
	 * Gets the value of the comment property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the value of the comment property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setComment(String value) {
		this.comment = value;
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
	 * Sets the value of the name property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

	//-------------------------------------------------------------------
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

	public boolean isDbColumn() {
		return !("0".equals(column) || "false".equals(column)) && !isJoinColumn();
	}

	public boolean isJoinColumn() {
		if (Strings.isNotEmpty(joinName)) {
			Asserts.notEmpty(joinField, "Missing joinField: " + joinName);
			return true;
		}
		return false;
	}

	public String getColumnDefine() {
		StringBuilder sb = new StringBuilder();
		
		if (Strings.isNotEmpty(column)) {
			sb.append("value=\"").append(column).append("\", ");
		}
		if (Strings.isNotEmpty(jdbcType)) {
			sb.append("type=JdbcTypes.").append(jdbcType).append(", ");
		}
		if (Strings.isNotEmpty(dbType)) {
			sb.append("dbType=\"").append(dbType).append("\", ");
		}
		if (size != null) {
			sb.append("size=").append(size).append(", ");
		}
		if (scale != null) {
			sb.append("scale=").append(scale).append(", ");
		}
		if (Boolean.TRUE.equals(notNull)) {
			sb.append("notNull=").append(notNull).append(", ");
		}
		if (Strings.isNotEmpty(defaultValue)) {
			sb.append("defaults=\"").append(defaultValue).append("\", ");
		}

		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}
	
	
	protected int compareByName(EntityProperty o) {
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(EntityProperty o) {
		if (this == o) {
			return 0;
		}
		return compareByName(o);
	}
}
