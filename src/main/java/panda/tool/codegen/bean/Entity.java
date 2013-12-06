package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Strings;

/**
 * <p>
 * Java class for Entity complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Entity&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;property&quot; type=&quot;{panda.tool.codegen}Property&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;identityIncrement&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;identityStart&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;identity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;table&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;trimString&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;baseBeanClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;baseQueryClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;generate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;comment&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Entity")
public class Entity {

	@XmlElement(name = "property")
	private List<EntityProperty> propertyList;

	@XmlAttribute
	private Boolean generate = true;
	@XmlAttribute
	private String identitySequence;
	@XmlAttribute
	private Integer identityIncrement;
	@XmlAttribute
	private Integer identityStart;
	@XmlAttribute
	private String identity;
	@XmlAttribute
	private String table;
	@XmlAttribute
	private String trimString;
	@XmlAttribute
	private String baseBeanClass;
	@XmlAttribute
	private String baseQueryClass;
	@XmlAttribute(name = "package")
	private String _package;
	@XmlAttribute
	private String comment;
	@XmlAttribute(required = true)
	private String name;

	private EntityProperty identityProperty;
	private List<EntityProperty> primaryKeyList;
	private Map<String, List<EntityProperty>> uniqueKeyMap;
	private Map<String, List<EntityProperty>> foreignKeyMap;
	private List<EntityProperty> columnList;
	private Map<String, List<EntityProperty>> joinMap;

	/**
	 * Constructor
	 */
	public Entity() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param model source model
	 */
	public Entity(Entity model) {
		this.identitySequence = model.identitySequence;
		this.identityIncrement = model.identityIncrement;
		this.identityStart = model.identityStart;
		this.identity = model.identity;
		this.table = model.table;
		this.trimString = model.trimString;
		this.baseBeanClass = model.baseBeanClass;
		this.baseQueryClass = model.baseQueryClass;
		this._package = model._package;
		this.generate = model.generate;
		this.comment = model.comment;
		this.name = model.name;

		propertyList = new ArrayList<EntityProperty>();
		for (EntityProperty p : model.getPropertyList()) {
			propertyList.add(new EntityProperty(p));
		}
	}

	/**
	 * throw a exception
	 * @param msg msg
	 */
	public void error(String msg) {
		throw new RuntimeException("entity<" + name + ">: "+ msg);
	}
	
	/**
	 * log a message
	 * @param msg msg
	 */
	public void log(String msg) {
		System.out.println("entity<" + name + ">: "+ msg);
	}

	/**
	 * prepare model
	 * 
	 * @throws Exception if an error occurs
	 */
	public void prepare() throws Exception {
		prepareIdentityProperty();
		preparePrimaryKeyList();
		if (identityProperty != null) {
			Asserts.isTrue(primaryKeyList.size() == 1 && identityProperty == primaryKeyList.get(0), 
					"Illegal Identity Primary Key definition: " + getName());
		}
		
		prepareUniqueKeyMap();
		prepareForeignKeyMap();
		prepareColumnList();
		prepareJoinMap();
	}

	/**
	 * @return the propertyList
	 */
	public List<EntityProperty> getPropertyList() {
		if (propertyList == null) {
			propertyList = new ArrayList<EntityProperty>();
		}
		return this.propertyList;
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

	/**
	 * @return the generate
	 */
	public Boolean getGenerate() {
		return generate;
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
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param generate the generate to set
	 */
	public void setGenerate(Boolean generate) {
		this.generate = generate;
	}

	/**
	 * @return the identitySequence
	 */
	public String getIdentitySequence() {
		return identitySequence;
	}

	/**
	 * @param identitySequence the identitySequence to set
	 */
	public void setIdentitySequence(String identitySequence) {
		this.identitySequence = identitySequence;
	}

	/**
	 * @return the identityIncrement
	 */
	public Integer getIdentityIncrement() {
		return identityIncrement;
	}

	/**
	 * @param identityIncrement the identityIncrement to set
	 */
	public void setIdentityIncrement(Integer identityIncrement) {
		this.identityIncrement = identityIncrement;
	}

	/**
	 * @return the identityStart
	 */
	public Integer getIdentityStart() {
		return identityStart;
	}

	/**
	 * @param identityStart the identityStart to set
	 */
	public void setIdentityStart(Integer identityStart) {
		this.identityStart = identityStart;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
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
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the trimString
	 */
	public String getTrimString() {
		return trimString;
	}

	/**
	 * @param trimString the trimString to set
	 */
	public void setTrimString(String trimString) {
		this.trimString = trimString;
	}

	/**
	 * @return the baseBeanClass
	 */
	public String getBaseBeanClass() {
		return baseBeanClass;
	}

	/**
	 * @param baseBeanClass the baseBeanClass to set
	 */
	public void setBaseBeanClass(String baseBeanClass) {
		this.baseBeanClass = baseBeanClass;
	}

	/**
	 * @return the baseQueryClass
	 */
	public String getBaseQueryClass() {
		return baseQueryClass;
	}

	/**
	 * @param baseQueryClass the baseQueryClass to set
	 */
	public void setBaseQueryClass(String baseQueryClass) {
		this.baseQueryClass = baseQueryClass;
	}

	/**
	 * @return the _package
	 */
	public String getPackage() {
		return _package;
	}

	/**
	 * @param _package the _package to set
	 */
	public void setPackage(String _package) {
		this._package = _package;
	}

	/**
	 * @return the identityProperty
	 */
	public EntityProperty getIdentityProperty() {
		return identityProperty;
	}

	//-----------------------------------------------------------------------
	private void prepareIdentityProperty() {
		if (!Strings.isBlank(getIdentity())) {
			for (EntityProperty p : getPropertyList()) {
				if (getIdentity().equals(p.getName())) {
					identityProperty = p;
					return;
				}
			}
			throw new IllegalArgumentException("Identity column [" + getIdentity() + "] not found: " + name);
		}
	}

	private void preparePrimaryKeyList() {
		primaryKeyList = new ArrayList<EntityProperty>();
		for (EntityProperty p : getPropertyList()) {
			if (Boolean.TRUE.equals(p.getPrimaryKey())) {
				primaryKeyList.add(p);
			}
		}
		if (primaryKeyList.isEmpty()) {
			throw new IllegalArgumentException("Primary key not defined: " + name);
		}
	}

	private void prepareUniqueKeyMap() {
		uniqueKeyMap = new HashMap<String, List<EntityProperty>>();

		Set<String> us = new HashSet<String>();
		for (EntityProperty p : getPropertyList()) {
			if (p.getUniqueKeys() != null) {
				for (String uk : p.getUniqueKeys()) {
					us.add(uk);
				}
			}
		}

		for (String u : us) {
			List<EntityProperty> list = new ArrayList<EntityProperty>();
			for (EntityProperty p : getPropertyList()) {
				if (Arrays.contains(p.getUniqueKeys(), u)) {
					list.add(p);
				}
			}
			uniqueKeyMap.put(u, list);
		}
	}
	
	public boolean hasComplexUniqueKey() {
		for (List<EntityProperty> eps : uniqueKeyMap.values()) {
			if (eps.size() > 1) {
				return true;
			}
		}
		return false;
	}

	private void prepareForeignKeyMap() {
		int i = 0;
		
		foreignKeyMap = new HashMap<String, List<EntityProperty>>();
		for (EntityProperty p : getPropertyList()) {
			if (Strings.isEmpty(p.getForeignKey()) && Strings.isEmpty(p.getForeignEntity())) {
				continue;
			}

			String fk = Strings.isEmpty(p.getForeignKey()) ? "__" + (i++) : p.getForeignKey();
			
			List<EntityProperty> eps = foreignKeyMap.get(p.getForeignKey());
			if (eps == null) {
				eps = new ArrayList<EntityProperty>();
				foreignKeyMap.put(fk, eps);
			}
			eps.add(p);
		}
	}

	public String getForeignKeyDefine(List<EntityProperty> eps) {
		StringBuilder sb = new StringBuilder();
		
		String name = null;
		String target = null;
		for (EntityProperty ep : eps) {
			name = ep.getForeignKey();
			if (Strings.isNotEmpty(ep.getForeignEntity())) {
				target = ep.getForeignEntity();
			}
		}
		
		if (Strings.isEmpty(target)) {
			throw new IllegalArgumentException("Missing foreignEntity: " + getName() + "/" + name);
		}
		
		if (Strings.isNotEmpty(name)) {
			sb.append("name=\"").append(name).append("\", ");
		}
		
		sb.append("target=");
		String pkg = Classes.getPackageName(target);
		String cls = Classes.getSimpleClassName(target);
		if (Strings.isNotEmpty(pkg) && !Strings.equals(pkg, _package)) {
			sb.append(pkg).append('.');
		}
		sb.append(cls).append(".class");

		sb.append(", fields={ ");
		for (EntityProperty ep : eps) {
			sb.append('"').append(ep.getName()).append("\", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(" }");
		
		return sb.toString();
	}
	
	public String getJoinDefine(List<EntityProperty> eps) {
		StringBuilder sb = new StringBuilder();
		
		String name = null;
		String type = null;
		String target = null;
		String keys = null;
		String refs = null;
		for (EntityProperty ep : eps) {
			name = ep.getJoinName();
			if (Strings.isNotEmpty(ep.getJoinType())) {
				type = ep.getJdbcType();
			}
			if (Strings.isNotEmpty(ep.getJoinEntity())) {
				target = ep.getJoinEntity();
			}
			if (Strings.isNotEmpty(ep.getJoinKeys())) {
				keys = ep.getJoinKeys();
			}
			if (Strings.isNotEmpty(ep.getJoinRefs())) {
				refs = ep.getJoinRefs();
			}
		}
		
		if (Strings.isEmpty(name)) {
			throw new IllegalArgumentException("Missing joinName: " + getName());
		}
		if (Strings.isEmpty(target)) {
			throw new IllegalArgumentException("Missing joinEntity: " + getName() + "/" + name);
		}
		if (Strings.isEmpty(keys)) {
			throw new IllegalArgumentException("Missing joinKeys: " + getName() + "/" + name);
		}
		
		sb.append("name=\"").append(name).append("\", ");
		if (Strings.isNotEmpty(type)) {
			sb.append("type=\"").append(type).append("\", ");
		}
		
		sb.append("target=");
		String pkg = Classes.getPackageName(target);
		String cls = Classes.getSimpleClassName(target);
		if (Strings.isNotEmpty(pkg) && !Strings.equals(pkg, _package)) {
			sb.append(pkg).append('.');
		}
		sb.append(cls).append(".class, ");

		sb.append("keys=\"").append(keys).append("\"");
		if (Strings.isNotEmpty(refs)) {
			sb.append(", refs=\"").append(refs).append("\"");
		}
		
		return sb.toString();
	}
	
	private void prepareJoinMap() {
		joinMap = new HashMap<String, List<EntityProperty>>();
		for (EntityProperty p : getPropertyList()) {
			if (Strings.isEmpty(p.getJoinName())) {
				continue;
			}

			List<EntityProperty> eps = joinMap.get(p.getJoinName());
			if (eps == null) {
				eps = new ArrayList<EntityProperty>();
				joinMap.put(p.getJoinName(), eps);
			}
			eps.add(p);
		}
	}

	private void prepareColumnList() {
		columnList = new ArrayList<EntityProperty>();
		for (EntityProperty p : getPropertyList()) {
			if (p.isDbColumn()) {
				columnList.add(p);
			}
		}
	}

	/**
	 * @return the primaryKeyList
	 */
	public List<EntityProperty> getPrimaryKeyList() {
		return primaryKeyList;
	}

	/**
	 * @param name property name
	 * @return true if the property name is primary key
	 */
	public boolean isPrimaryKey(String name) {
		for (EntityProperty mp : primaryKeyList) {
			if (name.equals(mp.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isIdentity(String name) {
		return (identityProperty != null && identityProperty.getName().equals(name));
	}
	
	public String getIdentityDefine() {
		StringBuilder sb = new StringBuilder();
		if (identityStart != null) {
			sb.append("start=").append(identityStart);
		}
		return sb.toString();
	}
	
	/**
	 * @return the uniqueKeyMap
	 */
	public Map<String, List<EntityProperty>> getUniqueKeyMap() {
		return uniqueKeyMap;
	}

	/**
	 * @return the foreignKeyMap
	 */
	public Map<String, List<EntityProperty>> getForeignKeyMap() {
		return foreignKeyMap;
	}

	/**
	 * @return the columnList
	 */
	public List<EntityProperty> getColumnList() {
		return columnList;
	}

	/**
	 * @return the joinMap
	 */
	public Map<String, List<EntityProperty>> getJoinMap() {
		return joinMap;
	}
}
