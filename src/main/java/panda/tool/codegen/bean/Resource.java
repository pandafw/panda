package panda.tool.codegen.bean;

import java.util.ArrayList;
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
 * Java class for Resource complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Resource&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;eimport java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Strings;
&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Resource")
public class Resource {

	@XmlElement(name = "model")
	protected List<Model> modelList;

	@XmlElement(name = "action")
	protected List<Action> actionList;

	@XmlAttribute
	private String locale;

	/**
	 * Constructor
	 */
	public Resource() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param resource source resource
	 */
	public Resource(Resource resource) {
		this.locale = resource.locale;
		
		modelList = new LinkedList<Model>();
		for (Model m : resource.getModelList()) {
			modelList.add(new Model(m));
		}

		actionList = new LinkedList<Action>();
		for (Action a : resource.getActionList()) {
			actionList.add(new Action(a));
		}
	}

	/**
	 * merge
	 * @param module module
	 */
	public void merge(Module module) {
		List<Model> modelList = new ArrayList<Model>();
		for (Model model : module.getModelList()) {
			boolean add = false;
			for (Model rm : this.getModelList()) {
				if (model.getName().equals(rm.getName())) {
					modelList.add(Model.extend(rm, model));
					add = true;
					break;
				}
			}
			if (!add) {
				modelList.add(new Model(model));
			}
		}
		this.modelList = modelList;
		
		List<Action> actionList = new ArrayList<Action>();
		for (Action action : module.getActionList()) {
			boolean add = false;
			for (Action ra : this.getActionList()) {
				if (action.getName().equals(ra.getName())) {
					if (Strings.isNotEmpty(ra.getPackage())) {
						if (ra.getPackage().equals(action.getPackage())) {
							actionList.add(Action.extend(ra, action));
							add = true;
						}
					}
					else {
						actionList.add(Action.extend(ra, action));
						add = true;
					}
				}
			}
			if (!add) {
				actionList.add(new Action(action));
			}
		}
		this.actionList = actionList;
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
			for (Model model : getModelList()) {
				if (Strings.isNotEmpty(model.getExtend())) {
					Model parent = null;
					for (Model m2 : getModelList()) {
						if (m2.getName().equals(model.getExtend())) {
							parent = m2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend module[" + model.getExtend()
								+ "] of " + model.getName());
					}

					getModelList().remove(model);
					getModelList().add(Model.extend(model, parent));

					extend = true;
					break;
				}
			}
		}
		for (Model model : getModelList()) {
			model.prepare();
		}

		extend = true;
		while (extend) {
			extend = false;
			for (Action action : getActionList()) {
				if (Strings.isNotEmpty(action.getExtend())) {
					Action parent = null;
					for (Action a2 : getActionList()) {
						if (a2.getName().equals(action.getExtend())) {
							parent = a2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend action[" + action.getExtend()
								+ "] of " + action.getName());
					}

					getActionList().remove(action);
					getActionList().add(Action.extend(action, parent));

					extend = true;
					break;
				}
			}
		}
		for (Action action : getActionList()) {
			action.prepare();
		}
	}

	/**
	 * @return the modelList
	 */
	public List<Model> getModelList() {
		if (modelList == null) {
			modelList = new ArrayList<Model>();
		}
		return this.modelList;
	}

	/**
	 * @return the actionList
	 */
	public List<Action> getActionList() {
		if (actionList == null) {
			actionList = new ArrayList<Action>();
		}
		return this.actionList;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

}
