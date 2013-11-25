package panda.tool.codegen.bean;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name=&quot;property&quot; type=&quot;{nuts.tools.codegen}Property&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;list&quot; type=&quot;{nuts.tools.codegen}ListUI&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;input&quot; type=&quot;{nuts.tools.codegen}InputUI&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;generate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;trimString&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;dataListFieldName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;dataFieldName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;model&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;package&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;actionBaseClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;actionClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;extend&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;title&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
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
	private String dataListFieldName;
	@XmlAttribute
	private String dataFieldName;
	@XmlAttribute
	private String model;
	@XmlAttribute
	private String actionBaseClass;
	@XmlAttribute
	private String actionClass;
	@XmlAttribute(name = "package")
	private String _package;
	@XmlAttribute
	private String extend;
	@XmlAttribute
	private String title;
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
		this.dataListFieldName = action.dataListFieldName;
		this.dataFieldName = action.dataFieldName;
		this.model = action.model;
		this._package = action._package;
		this.actionBaseClass = action.actionBaseClass;
		this.actionClass = action.actionClass;
		this.extend = action.extend;
		this.title = action.title;
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
		if (src.dataListFieldName != null) {
			me.dataListFieldName = src.dataListFieldName;
		}
		if (src.dataFieldName != null) {
			me.dataFieldName = src.dataFieldName;
		}
		if (src.model != null) {
			me.model = src.model;
		}
		if (src._package != null) {
			me._package = src._package;
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
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
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
	 * @return the fullActionClass
	 */
	public String getFullActionClass() {
		return (Strings.isEmpty(_package) ? "" : _package + '.') + name + '.' + actionClass;
	}

	/**
	 * @return the actionClass
	 */
	public String getActionClass() {
		if (actionClass == null) {
			return Strings.capitalize(name) + "Action";
		}
		return actionClass;
	}

	/**
	 * @param actionClass the actionClass to set
	 */
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
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
