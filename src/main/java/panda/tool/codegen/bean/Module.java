package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Strings;

/**
 * <p>
 * Java class for Module complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Module&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;include&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;model&quot; type=&quot;{panda.tool.codegen}Model&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;action&quot; type=&quot;{panda.tool.codegen}Action&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;resource&quot; type=&quot;{panda.tool.codegen}Action&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Module")
public class Module {
	@XmlElement(name = "model")
	protected List<Entity> modelList;

	@XmlElement(name = "action")
	protected List<Action> actionList;

	@XmlElement(name = "resource")
	protected List<Resource> resourceList;

	protected Properties props;
	
	/**
	 * Constructor
	 */
	public Module() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param module source module
	 */
	public Module(Module module) {
		modelList = new LinkedList<Entity>();
		for (Entity m : module.getModelList()) {
			modelList.add(new Entity(m));
		}

		actionList = new LinkedList<Action>();
		for (Action a : module.getActionList()) {
			actionList.add(new Action(a));
		}

		resourceList = new LinkedList<Resource>();
		for (Resource r : module.getResourceList()) {
			resourceList.add(new Resource(r));
		}
		
		props.putAll(module.getProps());
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
	 * @return the props
	 */
	public Properties getProps() {
		if (props == null) {
			props = new Properties();
		}
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(Properties props) {
		this.props = props;
	}

	/**
	 * @return the modelList
	 */
	public List<Entity> getModelList() {
		if (modelList == null) {
			modelList = new ArrayList<Entity>();
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
	 * @return the resourceList
	 */
	public List<Resource> getResourceList() {
		if (resourceList == null) {
			resourceList = new ArrayList<Resource>();
		}
		return this.resourceList;
	}

}
