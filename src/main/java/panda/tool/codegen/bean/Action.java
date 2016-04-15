package panda.tool.codegen.bean;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Classes;
import panda.lang.Strings;

/**
 * <p>
 * Java class for Action complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Action&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;property&quot; type=&quot;{panda.tool.codegen}Property&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;list&quot; type=&quot;{panda.tool.codegen}ListUI&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;input&quot; type=&quot;{panda.tool.codegen}InputUI&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;generate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;trimString&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;trimList&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;dataListFieldName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;dataFieldName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;entity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;actionBaseClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;actionClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;extend&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;title&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;path&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;auth&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Action")
public class Action {

	@XmlElement(name = "property")
	private List<ActionProperty> propertyList;
	@XmlElement(name = "list")
	private List<ListUI> listUIList;
	@XmlElement(name = "input")
	private List<InputUI> inputUIList;

	@XmlAttribute
	private Boolean generate;
	@XmlAttribute
	private String trimString;
	@XmlAttribute
	private String trimList;
	@XmlAttribute
	private String dataListFieldName;
	@XmlAttribute
	private String dataFieldName;
	@XmlAttribute
	private String entity;
	@XmlAttribute
	private String actionBaseClass;
	@XmlAttribute
	private String actionClass;
	@XmlAttribute
	private String extend;
	@XmlAttribute
	private String title;
	@XmlAttribute
	private String path;
	@XmlAttribute
	private String auth;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public Action() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param action source action
	 */
	public Action(Action action) {
		this.generate = action.generate;
		this.trimString = action.trimString;
		this.trimList = action.trimList;
		this.dataListFieldName = action.dataListFieldName;
		this.dataFieldName = action.dataFieldName;
		this.entity = action.entity;
		this.actionBaseClass = action.actionBaseClass;
		this.actionClass = action.actionClass;
		this.extend = action.extend;
		this.title = action.title;
		this.path = action.path;
		this.auth = action.auth;
		this.name = action.name;

		propertyList = new LinkedList<ActionProperty>();
		for (ActionProperty p : action.getPropertyList()) {
			propertyList.add(new ActionProperty(p));
		}

		listUIList = new LinkedList<ListUI>();
		for (ListUI lui : action.getListUIList()) {
			listUIList.add(new ListUI(lui));
		}

		inputUIList = new LinkedList<InputUI>();
		for (InputUI iui : action.getInputUIList()) {
			inputUIList.add(new InputUI(iui));
		}
	}

	/**
	 * throw a exception
	 * @param msg msg
	 */
	public void error(String msg) {
		throw new RuntimeException("action<" + name + ">: "+ msg);
	}

	/**
	 * log a message
	 * @param msg msg
	 */
	public void log(String msg) {
		System.out.println("action<" + name + ">: "+ msg);
	}

	/**
	 * prepare
	 * 
	 * @throws Exception if an error occurs
	 */
	public void prepare() throws Exception {
		boolean extend = true;

		while (extend) {
			extend = false;
			for (ListUI lui : getListUIList()) {
				if (Strings.isNotEmpty(lui.getExtend())) {
					ListUI parent = null;
					for (ListUI lui2 : getListUIList()) {
						if (lui2.getName().equals(lui.getExtend())) {
							parent = lui2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend ListUI[" + lui.getExtend()
								+ "] of " + lui.getName());
					}

					getListUIList().remove(lui);
					getListUIList().add(ListUI.extend(lui, parent));

					extend = true;
					break;
				}
			}
		}

		extend = true;
		while (extend) {
			extend = false;
			for (InputUI iui : getInputUIList()) {
				if (Strings.isNotEmpty(iui.getExtend())) {
					InputUI parent = null;
					for (InputUI iui2 : getInputUIList()) {
						if (iui2.getName().equals(iui.getExtend())) {
							parent = iui2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend InputUI[" + iui.getExtend()
								+ "] of " + iui.getName());
					}

					getInputUIList().remove(iui);
					getInputUIList().add(InputUI.extend(iui, parent));

					extend = true;
					break;
				}
			}
		}
	}

	/**
	 * extend action
	 * 
	 * @param src source action
	 * @param parent extend action
	 * @return action
	 */
	public static Action extend(Action src, Action parent) {
		Action me = new Action(parent);

		if (src.generate != null) {
			me.generate = src.generate;
		}
		if (src.trimString != null) {
			me.trimString = src.trimString;
		}
		if (src.trimList != null) {
			me.trimList = src.trimList;
		}
		if (src.dataListFieldName != null) {
			me.dataListFieldName = src.dataListFieldName;
		}
		if (src.dataFieldName != null) {
			me.dataFieldName = src.dataFieldName;
		}
		if (src.entity != null) {
			me.entity = src.entity;
		}
		if (src.actionBaseClass != null) {
			me.actionBaseClass = src.actionBaseClass;
		}
		if (src.actionClass != null) {
			me.actionClass = src.actionClass;
		}
		if (src.title != null) {
			me.title = src.title;
		}
		if (src.path != null) {
			me.path = src.path;
		}
		if (src.auth != null) {
			me.auth = src.auth;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		List<ActionProperty> mplist = me.getPropertyList();
		List<ActionProperty> splist = src.getPropertyList();
		for (ActionProperty sp : splist) {
			boolean add = false;
			for (ActionProperty mp : mplist) {
				if (mp.getName().equals(sp.getName())) {
					mplist.remove(mp);
					mplist.add(ActionProperty.extend(sp, mp));
					add = true;
					break;
				}
			}
			if (!add) {
				mplist.add(new ActionProperty(sp));
			}
		}

		List<ListUI> mluiList = me.getListUIList();
		List<ListUI> sluiList = src.getListUIList();
		for (ListUI slui : sluiList) {
			for (ListUI mlui : mluiList) {
				if (mlui.getName().equals(slui.getName())) {
					mluiList.remove(mlui);
				}
			}
			mluiList.add(new ListUI(slui));
		}

		List<InputUI> miuiList = me.getInputUIList();
		List<InputUI> siuiList = src.getInputUIList();
		for (InputUI siui : siuiList) {
			for (InputUI miui : miuiList) {
				if (miui.getName().equals(siui.getName())) {
					miuiList.remove(miui);
				}
			}
			miuiList.add(new InputUI(siui));
		}

		return me;
	}

	/**
	 * @return the propertyList
	 */
	public List<ActionProperty> getPropertyList() {
		if (propertyList == null) {
			propertyList = new LinkedList<ActionProperty>();
		}
		return this.propertyList;
	}

	/**
	 * @return the listUIList
	 */
	public List<ListUI> getListUIList() {
		if (listUIList == null) {
			listUIList = new LinkedList<ListUI>();
		}
		return this.listUIList;
	}

	/**
	 * @return the sortedListUIList
	 */
	public List<ListUI> getSortedListUIList() {
		List<ListUI> list = new LinkedList<ListUI>();
		for (ListUI iui : getListUIList()) {
			if (Boolean.TRUE.equals(iui.getGenerate())) {
				list.add(iui);
			}
		}
		return list;
	}

	public Set<String> getListUIColumns() {
		Set<String> cs = new LinkedHashSet<String>();
		for (ListUI lui : getSortedListUIList()) {
			for (ListColumn lc : lui.getDisplayColumnList()) {
				cs.add(lc.getUname());
			}
		}
		return cs;
	}

	/**
	 * @return the inputUIList
	 */
	public List<InputUI> getInputUIList() {
		if (inputUIList == null) {
			inputUIList = new LinkedList<InputUI>();
		}
		return this.inputUIList;
	}

	/**
	 * @return the inputUIList
	 */
	public List<InputUI> getSortedInputUIList() {
		List<InputUI> list = new LinkedList<InputUI>();
		for (InputUI iui : getInputUIList()) {
			if (Boolean.TRUE.equals(iui.getGenerate())) {
				list.add(iui);
			}
		}
		return list;
	}

	public Set<String> getInputUIFields() {
		Set<String> cs = new LinkedHashSet<String>();
		for (InputUI iui : getSortedInputUIList()) {
			for (InputField inf : iui.getDisplayFieldList()) {
				if (inf.getActionField().booleanValue()) {
					cs.add("." + inf.getName());
				}
				else {
					cs.add(inf.getUname());
				}
			}
		}
		return cs;
	}

	/**
	 * @return the generate
	 */
	public Boolean getGenerate() {
		return generate;
	}

	/**
	 * @param generate the generate to set
	 */
	public void setGenerate(Boolean generate) {
		this.generate = generate;
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
	 * @return the trimList
	 */
	public String getTrimList() {
		return trimList;
	}

	/**
	 * @param trimList the trimList to set
	 */
	public void setTrimList(String trimList) {
		this.trimList = trimList;
	}

	/**
	 * @return the dataListFieldName
	 */
	public String getDataListFieldName() {
		return dataListFieldName;
	}

	/**
	 * @param dataListFieldName the dataListFieldName to set
	 */
	public void setDataListFieldName(String dataListFieldName) {
		this.dataListFieldName = dataListFieldName;
	}

	/**
	 * @return the dataFieldName
	 */
	public String getDataFieldName() {
		return dataFieldName;
	}

	/**
	 * @param dataFieldName the dataFieldName to set
	 */
	public void setDataFieldName(String dataFieldName) {
		this.dataFieldName = dataFieldName;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the actionBaseClass
	 */
	public String getActionBaseClass() {
		return actionBaseClass;
	}

	/**
	 * @param actionBaseClass the actionBaseClass to set
	 */
	public void setActionBaseClass(String actionBaseClass) {
		this.actionBaseClass = actionBaseClass;
	}

	/**
	 * @return the actionClass
	 */
	public String getActionClass() {
		return actionClass;
	}

	/**
	 * @param actionClass the actionClass to set
	 */
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}

	/**
	 * @return the simple name of actionClass
	 */
	public String getSimpleActionClass() {
		return Classes.getSimpleClassName(actionClass);
	}

	/**
	 * @return the package of actionClass
	 */
	public String getActionPackage() {
		return Classes.getPackageName(actionClass);
	}

	/**
	 * @return the extend
	 */
	public String getExtend() {
		return extend;
	}

	/**
	 * @param extend the extend to set
	 */
	public void setExtend(String extend) {
		this.extend = extend;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the auth
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * @param auth the auth to set
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
