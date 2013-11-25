package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Arrays;
import panda.lang.Strings;

/**
 * <p>
 * Java class for Model complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Model&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;property&quot; type=&quot;{nuts.tools.codegen}Property&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;identitySequence&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;identityIncrement&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;identityStart&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;identity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;tableAlias&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;table&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;trimString&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;baseBeanClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;baseDaoClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;baseExampleClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;baseMetaDataClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modelBeanClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modelDaoClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modelExampleClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modelMetaDataClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modelSqlmapClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;package&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;extend&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;generate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Model")
public class Model {

	private Model parent;
	
	private List<ModelProperty> orgPropertyList;

	@XmlElement(name = "property")
	private List<ModelProperty> propertyList;

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
	private String tableAlias;
	@XmlAttribute
	private String table;
	@XmlAttribute
	private String trimString;
	@XmlAttribute
	private String baseDaoClass;
	@XmlAttribute
	private String baseExampleClass;
	@XmlAttribute
	private String baseBeanClass;
	@XmlAttribute
	private String baseMetaDataClass;
	@XmlAttribute
	private String modelDaoClass;
	@XmlAttribute
	private String modelExampleClass;
	@XmlAttribute
	private String modelBeanClass;
	@XmlAttribute
	private String modelMetaDataClass;
	@XmlAttribute
	private String modelSqlmapClass;
	@XmlAttribute(name = "package")
	private String _package;
	@XmlAttribute
	private String extend;
	@XmlAttribute
	private String label;
	@XmlAttribute(required = true)
	private String name;

	private ModelProperty identityProperty;
	private List<ModelProperty> primaryKeyList;
	private Map<String, List<ModelProperty>> uniqueKeyMap;
	private List<List<ModelProperty>> foreignKeyList;
	private List<ModelProperty> columnList;
	private List<ModelProperty> sqlExpressionList;
	private List<ModelProperty> joinColumnList;
	private List<ModelProperty> joinTableList;
	private List<ModelProperty> joinConditionList;

	/**
	 * Constructor
	 */
	public Model() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param model source model
	 */
	public Model(Model model) {
		this.identitySequence = model.identitySequence;
		this.identityIncrement = model.identityIncrement;
		this.identityStart = model.identityStart;
		this.identity = model.identity;
		this.tableAlias = model.tableAlias;
		this.table = model.table;
		this.trimString = model.trimString;
		this.baseBeanClass = model.baseBeanClass;
		this.baseDaoClass = model.baseDaoClass;
		this.baseExampleClass = model.baseExampleClass;
		this.baseMetaDataClass = model.baseMetaDataClass;
		this.modelBeanClass = model.modelBeanClass;
		this.modelDaoClass = model.modelDaoClass;
		this.modelExampleClass = model.modelExampleClass;
		this.modelMetaDataClass = model.modelMetaDataClass;
		this.modelSqlmapClass = model.modelSqlmapClass;
		this._package = model._package;
		this.extend = model.extend;
		this.generate = model.generate;
		this.label = model.label;
		this.name = model.name;

		propertyList = new ArrayList<ModelProperty>();
		for (ModelProperty p : model.getPropertyList()) {
			propertyList.add(new ModelProperty(p));
		}
	}

	/**
	 * throw a exception
	 * @param msg msg
	 */
	public void error(String msg) {
		throw new RuntimeException("model<" + name + ">: "+ msg);
	}
	
	/**
	 * log a message
	 * @param msg msg
	 */
	public void log(String msg) {
		System.out.println("action<" + name + ">: "+ msg);
	}

	/**
	 * extend model
	 * 
	 * @param src source model
	 * @param parent extend model
	 * @return model
	 */
	public static Model extend(Model src, Model parent) {
		Model me = new Model(parent);

		if (src.identitySequence != null) {
			me.identitySequence = src.identitySequence;
		}
		if (src.identityIncrement != null) {
			me.identityIncrement = src.identityIncrement;
		}
		if (src.identityStart != null) {
			me.identityStart = src.identityStart;
		}
		if (src.identity != null) {
			me.identity = src.identity;
		}
		if (src.tableAlias != null) {
			me.tableAlias = src.tableAlias;
		}
		if (src.table != null) {
			me.table = src.table;
		}
		if (src.trimString != null) {
			me.trimString = src.trimString;
		}
		if (src.baseBeanClass != null) {
			me.baseBeanClass = src.baseBeanClass;
		}
		if (src.baseDaoClass != null) {
			me.baseDaoClass = src.baseDaoClass;
		}
		if (src.baseExampleClass != null) {
			me.baseExampleClass = src.baseExampleClass;
		}
		if (src.baseMetaDataClass != null) {
			me.baseMetaDataClass = src.baseMetaDataClass;
		}
		if (src.modelBeanClass != null) {
			me.modelBeanClass = src.modelBeanClass;
		}
		if (src.modelDaoClass != null) {
			me.modelDaoClass = src.modelDaoClass;
		}
		if (src.modelExampleClass != null) {
			me.modelExampleClass = src.modelExampleClass;
		}
		if (src.modelMetaDataClass != null) {
			me.modelMetaDataClass = src.modelMetaDataClass;
		}
		if (src.modelSqlmapClass != null) {
			me.modelSqlmapClass = src.modelSqlmapClass;
		}
		if (src._package != null) {
			me._package = src._package;
		}
		if (src.label != null) {
			me.label = src.label;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		if (parent.generate) {
			me.parent = parent;
			me.baseBeanClass = parent.getModelBeanClass();
			me.baseDaoClass = parent.getBaseDaoClass();
			me.baseExampleClass = parent.getBaseExampleClass();
			me.baseMetaDataClass = parent.getBaseMetaDataClass();
			me.orgPropertyList = src.getPropertyList();
		}

		List<ModelProperty> mplist = me.getPropertyList();
		List<ModelProperty> splist = src.getPropertyList();
		for (ModelProperty sp : splist) {
			boolean add = false;
			for (int i = 0; i < mplist.size(); i++) {
				ModelProperty mp = mplist.get(i);
				if (mp.getName().equals(sp.getName())) {
					mplist.set(i, ModelProperty.extend(sp, mp));
					add = true;
					break;
				}
			}
			if (!add) {
				mplist.add(new ModelProperty(sp));
			}
		}

		return me;
	}

	/**
	 * prepare model
	 * 
	 * @throws Exception if an error occurs
	 */
	public void prepare() throws Exception {
		prepare(false);
	}
	
	/**
	 * prepare model
	 * 
	 * @throws Exception if an error occurs
	 */
	public void prepare(boolean disableJoin) throws Exception {
		Set<ModelProperty> ps = new TreeSet<ModelProperty>();
		List<ModelProperty> pl = getPropertyList();
		for (int i = 0; i < pl.size(); i++) {
			ModelProperty p = pl.get(i);
			if (p.getOrder() == null) {
				p.setOrder((i + 1) * 100);
			}
			if (disableJoin) {
				p.setColumnAlias(null);
				p.setJoinColumn(null);
				p.setJoinTable(null);
				p.setJoinCondition(null);
				p.setJoinTableAlias(null);
				p.setJoinType(null);
			}
			ps.add(p);
		}
		propertyList.clear();
		propertyList.addAll(ps);

		prepareIdentityProperty();
		preparePrimaryKeyList();
		prepareUniqueKeyMap();
		prepareForeignKeyList();
		prepareColumnList();
		prepareSqlExpressionList();
		prepareJoinTableList();
		prepareJoinColumnList();
		prepareJoinConditionList();
	}

	/**
	 * @return the propertyList
	 */
	public List<ModelProperty> getPropertyList() {
		if (propertyList == null) {
			propertyList = new ArrayList<ModelProperty>();
		}
		return this.propertyList;
	}

	/**
	 * @return the orgPropertyList
	 */
	public List<ModelProperty> getOrgPropertyList() {
		if (orgPropertyList == null) {
			orgPropertyList = getPropertyList();
		}
		return this.orgPropertyList;
	}

	/**
	 * @return the parent
	 */
	public Model getParent() {
		return parent;
	}

	/**
	 * Gets the value of the identitySequence property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getIdentitySequence() {
		return identitySequence;
	}

	/**
	 * Sets the value of the identitySequence property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setIdentitySequence(String value) {
		this.identitySequence = value;
	}

	/**
	 * Gets the value of the identityIncrement property.
	 * 
	 * @return possible object is {@link Integer }
	 */
	public Integer getIdentityIncrement() {
		return identityIncrement;
	}

	/**
	 * Sets the value of the identityIncrement property.
	 * 
	 * @param value allowed object is {@link Integer }
	 */
	public void setIdentityIncrement(Integer value) {
		this.identityIncrement = value;
	}

	/**
	 * Gets the value of the identityStart property.
	 * 
	 * @return possible object is {@link Integer }
	 */
	public Integer getIdentityStart() {
		return identityStart;
	}

	/**
	 * Sets the value of the identityStart property.
	 * 
	 * @param value allowed object is {@link Integer }
	 */
	public void setIdentityStart(Integer value) {
		this.identityStart = value;
	}

	/**
	 * Gets the value of the identity property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Sets the value of the identity property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setIdentity(String value) {
		this.identity = value;
	}

	/**
	 * Gets the value of the tableAlias property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTableAlias() {
		return tableAlias;
	}

	/**
	 * Gets the value of the tableAlias property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getAsTableAlias() {
		if (Strings.isEmpty(tableAlias)) {
			return Strings.EMPTY;
		}
		else {
			return "AS " + tableAlias;
		}
	}

	/**
	 * Gets the value of the tableAlias property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTableOrAlias() {
		if (Strings.isEmpty(tableAlias)) {
			return table;
		}
		else {
			return tableAlias;
		}
	}

	/**
	 * Sets the value of the tableAlias property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setTableAlias(String value) {
		this.tableAlias = value;
	}

	/**
	 * Gets the value of the table property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the value of the table property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setTable(String value) {
		this.table = value;
	}

	/**
	 * Gets the value of the trimString property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTrimString() {
		return trimString;
	}

	/**
	 * Sets the value of the trimString property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setTrimString(String value) {
		this.trimString = value;
	}

	/**
	 * Gets the value of the daoExtendClass property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getBaseDaoClass() {
		return baseDaoClass;
	}

	/**
	 * Sets the value of the daoExtendClass property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setBaseDaoClass(String value) {
		this.baseDaoClass = value;
	}

	/**
	 * @return the baseExampleClass
	 */
	public String getBaseExampleClass() {
		return baseExampleClass;
	}

	/**
	 * @param expBaseClass the expBaseClass to set
	 */
	public void setBaseExampleClass(String expBaseClass) {
		this.baseExampleClass = expBaseClass;
	}

	/**
	 * Gets the value of the baseBeanClass property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getBaseBeanClass() {
		return baseBeanClass;
	}

	/**
	 * Sets the value of the baseBeanClass property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setBaseBeanClass(String value) {
		this.baseBeanClass = value;
	}

	/**
	 * @return the baseMetaClass
	 */
	public String getBaseMetaDataClass() {
		return baseMetaDataClass;
	}

	/**
	 * @param metaBaseClass the metaBaseClass to set
	 */
	public void setBaseMetaDataClass(String metaBaseClass) {
		this.baseMetaDataClass = metaBaseClass;
	}

	/**
	 * @return modelBeanClass
	 */
	public String getModelBeanClass() {
		if (Strings.isEmpty(modelBeanClass)) {
			modelBeanClass = this.getPackage() + ".bean." + this.getName();
		}
		return modelBeanClass;
	}

	/**
	 * @param modelBeanClass the modelBeanClass to set
	 */
	public void setModelBeanClass(String modelBeanClass) {
		this.modelBeanClass = modelBeanClass;
	}

	/**
	 * @return the modelDaoClass
	 */
	public String getModelDaoClass() {
		if (Strings.isEmpty(modelDaoClass)) {
			modelDaoClass = this.getPackage() + ".dao." + this.getName() + "DAO";
		}
		return modelDaoClass;
	}

	/**
	 * @param modelDaoClass the modelDaoClass to set
	 */
	public void setModelDaoClass(String modelDaoClass) {
		this.modelDaoClass = modelDaoClass;
	}

	/**
	 * @return the modelExampleClass
	 */
	public String getModelExampleClass() {
		if (Strings.isEmpty(modelExampleClass)) {
			modelExampleClass = this.getPackage() + ".example." + this.getName() + "Example";
		}
		return modelExampleClass;
	}

	/**
	 * @param modelExampleClass the modelExampleClass to set
	 */
	public void setModelExampleClass(String modelExampleClass) {
		this.modelExampleClass = modelExampleClass;
	}

	/**
	 * @return the modelMetaDataClass
	 */
	public String getModelMetaDataClass() {
		if (Strings.isEmpty(modelMetaDataClass)) {
			modelMetaDataClass = this.getPackage() + ".metadata." + this.getName() + "MetaData";
		}
		return modelMetaDataClass;
	}

	/**
	 * @param modelMetaDataClass the modelMetaDataClass to set
	 */
	public void setModelMetaDataClass(String modelMetaDataClass) {
		this.modelMetaDataClass = modelMetaDataClass;
	}

	/**
	 * @return the modelSqlmapClass
	 */
	public String getModelSqlmapClass() {
		if (Strings.isEmpty(modelSqlmapClass)) {
			modelSqlmapClass = this.getPackage() + ".sqlmap." + this.getName() + "Sqlmap";
		}
		return modelSqlmapClass;
	}

	/**
	 * @param modelSqlmapClass the modelSqlmapClass to set
	 */
	public void setModelSqlmapClass(String modelSqlmapClass) {
		this.modelSqlmapClass = modelSqlmapClass;
	}

	/**
	 * Gets the value of the package property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getPackage() {
		return _package;
	}

	/**
	 * Sets the value of the package property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setPackage(String value) {
		this._package = value;
	}

	/**
	 * Gets the value of the extend property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getExtend() {
		return extend;
	}

	/**
	 * Sets the value of the extend property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setExtend(String value) {
		this.extend = value;
	}

	/**
	 * Gets the value of the generate property.
	 * 
	 * @return possible object is {@link Boolean }
	 */
	public Boolean getGenerate() {
		return generate;
	}

	/**
	 * Sets the value of the generate property.
	 * 
	 * @param value allowed object is {@link Boolean }
	 */
	public void setGenerate(Boolean value) {
		this.generate = value;
	}

	/**
	 * @return the label
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public final void setLabel(String label) {
		this.label = label;
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
	 * @return the identityProperty
	 */
	public ModelProperty getIdentityProperty() {
		return identityProperty;
	}

	/**
	 * @param identityProperty the identityProperty to set
	 */
	public void setIdentityProperty(ModelProperty identityProperty) {
		this.identityProperty = identityProperty;
	}

	private void prepareIdentityProperty() throws Exception {
		if (!Strings.isBlank(getIdentity())) {
			for (ModelProperty p : getPropertyList()) {
				if (getIdentity().equals(p.getName())) {
					setIdentityProperty(p);
					return;
				}
			}
			throw new Exception("Identity column [" + getIdentity() + "] not found!");
		}
	}

	private void preparePrimaryKeyList() {
		primaryKeyList = new ArrayList<ModelProperty>();
		for (ModelProperty p : getPropertyList()) {
			if (Boolean.TRUE.equals(p.getPrimaryKey())) {
				primaryKeyList.add(p);
			}
		}
	}

	private void prepareUniqueKeyMap() {
		uniqueKeyMap = new HashMap<String, List<ModelProperty>>();

		Set<String> us = new HashSet<String>();
		for (ModelProperty p : getPropertyList()) {
			if (p.getUniqueKeys() != null) {
				for (String uk : p.getUniqueKeys()) {
					us.add(uk);
				}
			}
		}

		for (String u : us) {
			List<ModelProperty> list = new ArrayList<ModelProperty>();
			for (ModelProperty p : getPropertyList()) {
				if (Arrays.contains(p.getUniqueKeys(), u)) {
					list.add(p);
				}
			}
			uniqueKeyMap.put(u, list);
		}
	}

	private void prepareForeignKeyList() {
		foreignKeyList = new ArrayList<List<ModelProperty>>();

		List<String> fs = new ArrayList<String>();
		for (ModelProperty p : getPropertyList()) {
			if (!Strings.isBlank(p.getForeignKey()) && !fs.contains(p.getForeignKey())) {
				fs.add(p.getForeignKey());
			}
		}

		for (String f : fs) {
			List<ModelProperty> list = new ArrayList<ModelProperty>();
			for (ModelProperty p : getPropertyList()) {
				if (f.equals((p.getForeignKey()))) {
					list.add(p);
				}
			}
			foreignKeyList.add(list);
		}
	}

	private void prepareJoinTableList() {
		joinTableList = new ArrayList<ModelProperty>();
		for (ModelProperty p : getPropertyList()) {
			if (!Strings.isBlank(p.getJoinTable())) {
				boolean fd = false;
				for (ModelProperty j : joinTableList) {
					if (j.getJoinTable().equals(p.getJoinTable())) {
						fd = true;
					}
				}
				if (!fd) {
					joinTableList.add(p);
				}
			}
		}
	}

	private void prepareJoinConditionList() {
		joinConditionList = new ArrayList<ModelProperty>();
		for (ModelProperty p : getPropertyList()) {
			if (!Strings.isBlank(p.getJoinCondition())) {
				joinConditionList.add(p);
			}
		}
	}

	private void prepareColumnList() {
		columnList = new ArrayList<ModelProperty>();
		for (ModelProperty p : getPropertyList()) {
			if (!Strings.isBlank(p.getColumn())) {
				columnList.add(p);
			}
		}
	}

	private void prepareSqlExpressionList() {
		sqlExpressionList = new ArrayList<ModelProperty>();
		for (ModelProperty p : getPropertyList()) {
			if (!Strings.isBlank(p.getSqlExpression())) {
				sqlExpressionList.add(p);
			}
		}
	}

	private void prepareJoinColumnList() {
		joinColumnList = new ArrayList<ModelProperty>();
		for (ModelProperty p : getPropertyList()) {
			if (!Strings.isBlank(p.getJoinColumn())) {
				joinColumnList.add(p);
			}
		}
	}

	/**
	 * @return the primaryKeyList
	 */
	public List<ModelProperty> getPrimaryKeyList() {
		return primaryKeyList;
	}

	/**
	 * @param name property name
	 * @return true if the property name is primary key
	 */
	public boolean isPrimaryKey(String name) {
		for (ModelProperty mp : primaryKeyList) {
			if (name.equals(mp.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the uniqueKeyMap
	 */
	public Map<String, List<ModelProperty>> getUniqueKeyMap() {
		return uniqueKeyMap;
	}

	/**
	 * @return the foreignKeyList
	 */
	public List<List<ModelProperty>> getForeignKeyList() {
		return foreignKeyList;
	}

	/**
	 * @return the columnList
	 */
	public List<ModelProperty> getColumnList() {
		return columnList;
	}

	/**
	 * @return the sqlExpressionList
	 */
	public List<ModelProperty> getSqlExpressionList() {
		return sqlExpressionList;
	}

	/**
	 * @return the joinColumnList
	 */
	public List<ModelProperty> getJoinColumnList() {
		return joinColumnList;
	}

	/**
	 * @return the joinTableList
	 */
	public List<ModelProperty> getJoinTableList() {
		return joinTableList;
	}

	/**
	 * @return the joinConditionList
	 */
	public List<ModelProperty> getJoinConditionList() {
		return joinConditionList;
	}

}
