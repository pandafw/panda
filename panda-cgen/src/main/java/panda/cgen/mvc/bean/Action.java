package panda.cgen.mvc.bean;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Arrays;
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
 *         &lt;element name=&quot;property&quot; type=&quot;{panda.cgen.mvc}Property&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;list&quot; type=&quot;{panda.cgen.mvc}ListUI&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;input&quot; type=&quot;{panda.cgen.mvc}InputUI&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;autoJoin&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
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
	private String autoJoin;
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
		this.autoJoin = action.autoJoin;
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
				if (Arrays.isEmpty(lui.getExtends())) {
					continue;
				}
				
				ListUI nui = lui;
				for (String et : lui.getExtends()) {
//					System.out.println("Extend ListUI[" + et + "] of " + nui.getName());

					ListUI parent = null;
					for (ListUI lui2 : getListUIList()) {
						if (lui2.getName().equals(et)) {
							parent = lui2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend ListUI[" + et + "] of " + lui.getName());
					}
					if (parent == lui) {
						throw new Exception("Can not extend self ListUI[" + et + "] of " + lui.getName());
					}
					
					nui = ListUI.extend(nui, parent);
//					for (ListColumn lc : nui.getColumnList()) {
//						System.out.print(lc.getName() + ":" + lc.getHidden() + ",");
//					}
//					System.out.println();
				}

				getListUIList().remove(lui);
				getListUIList().add(nui);
				extend = true;
				break;
			}
		}

		extend = true;
		while (extend) {
			extend = false;
			for (InputUI iui : getInputUIList()) {
				if (Arrays.isEmpty(iui.getExtends())) {
					continue;
				}
				
				InputUI nui = iui;
				for (String et : iui.getExtends()) {
//					System.out.println("Extend InputUI[" + et + "] of " + nui.getName());

					InputUI parent = null;
					for (InputUI iui2 : getInputUIList()) {
						if (iui2.getName().equals(et)) {
							parent = iui2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend InputUI[" + et + "] of " + iui.getName());
					}
					if (parent == iui) {
						throw new Exception("Can not extend self InputUI[" + et + "] of " + iui.getName());
					}
					
					nui = InputUI.extend(nui, parent);
//					for (InputField inf : nui.getFieldList()) {
//						System.out.print(inf.getName()+ ",");
//					}
//					System.out.println();
				}

				getInputUIList().remove(iui);
				getInputUIList().add(nui);
				extend = true;
				break;
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

		if (src.autoJoin != null) {
			me.autoJoin = src.autoJoin;
		}
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

	public String getDisplayListColumns(Entity en) {
		String dlcs = "";
		for (ListUI lui : getSortedListUIList()) {
			String s = lui.getDisplayColumns(en);
			if (dlcs.isEmpty()) {
				dlcs = s;
			}
			else if (!dlcs.equals(s)) {
				return "";
			}
		}
		return dlcs;
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

	public String getDisplayInputFields(Entity en) {
		String difs = "";
		for (InputUI iui : getSortedInputUIList()) {
			String s = iui.getDisplayFields(en);
			if (difs.isEmpty()) {
				difs = s;
			}
			else if (!difs.equals(s)) {
				return "";
			}
			
		}
		return difs;
	}

	public String getAutoJoin() {
		return autoJoin;
	}

	/**
	 * left:X right:Y
	 * @return map { 'X': 'left', 'Y': 'right' }
	 */
	public Map<String, String> getAutoJoins() {
		Map<String, String> m = new LinkedHashMap<String, String>();
		String[] ss = Strings.split(autoJoin);
		for (String s : ss) {
			String[] js = Strings.split(s, ':');
			if (js.length > 1) {
				m.put(js[1].toUpperCase(), Strings.capitalize(js[0].toLowerCase()));
			}
			else {
				m.put(js[0].toUpperCase(), "");
			}
		}
		return m;
	}

	public void setAutoJoin(String autoJoin) {
		this.autoJoin = Strings.stripToLowerNull(autoJoin);
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
		return Classes.getShortClassName(actionClass);
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

	public String[] getExtends() {
		return Strings.split(extend, ", ");
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
