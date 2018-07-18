package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.io.Settings;
import panda.lang.Arrays;

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
 *         &lt;element name=&quot;entity&quot; type=&quot;{panda.tool.codegen}Entity&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;action&quot; type=&quot;{panda.tool.codegen}Action&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;resource&quot; type=&quot;{panda.tool.codegen}Resource&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Module")
public class Module {
	@XmlElement(name = "entity")
	protected List<Entity> entityList;

	@XmlElement(name = "action")
	protected List<Action> actionList;

	@XmlElement(name = "resource")
	protected List<Resource> resourceList;

	protected Settings props;
	
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
		entityList = new LinkedList<Entity>();
		for (Entity e : module.getEntityList()) {
			entityList.add(new Entity(e));
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
			for (Entity entity : getEntityList()) {
				if (Arrays.isEmpty(entity.getExtends())) {
					continue;
				}
				
				Entity na = entity;
				for (String et : entity.getExtends()) {
//					System.out.println("Extend entity[" + et + "] of " + entity.getName());

					Entity parent = null;
					for (Entity a2 : getEntityList()) {
						if (a2.getName().equals(et)) {
							parent = a2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend entity[" + et + "] of " + entity.getName());
					}
					if (parent == entity) {
						throw new Exception("Can not extend self entity[" + et + "] of " + entity.getName());
					}
					
					na = Entity.extend(entity, parent);
				}

				getEntityList().remove(entity);
				getEntityList().add(na);
				extend = true;
				break;
			}
		}
		for (Entity entity : getEntityList()) {
			entity.prepare();
		}

		extend = true;
		while (extend) {
			extend = false;
			for (Action action : getActionList()) {
				if (Arrays.isEmpty(action.getExtends())) {
					continue;
				}
				
				Action na = action;
				for (String et : action.getExtends()) {
//					System.out.println("Extend action[" + et + "] of " + action.getName());

					Action parent = null;
					for (Action a2 : getActionList()) {
						if (a2.getName().equals(et)) {
							parent = a2;
							break;
						}
					}
					if (parent == null) {
						throw new Exception("Can not find extend action[" + et + "] of " + action.getName());
					}
					if (parent == action) {
						throw new Exception("Can not extend self action[" + et + "] of " + action.getName());
					}
					
					na = Action.extend(action, parent);
				}

				getActionList().remove(action);
				getActionList().add(na);
				extend = true;
				break;
			}
		}
		for (Action action : getActionList()) {
			action.prepare();
		}
	}

	/**
	 * @return the props
	 */
	public Settings getProps() {
		if (props == null) {
			props = new Settings();
		}
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(Settings props) {
		this.props = props;
	}

	/**
	 * @return the entityList
	 */
	public List<Entity> getEntityList() {
		if (entityList == null) {
			entityList = new ArrayList<Entity>();
		}
		return this.entityList;
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
