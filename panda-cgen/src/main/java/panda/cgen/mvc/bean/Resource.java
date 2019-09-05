package panda.cgen.mvc.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name=&quot;entity&quot; type=&quot;{panda.cgen.mvc}Entity&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;action&quot; type=&quot;{panda.cgen.mvc}Action&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;locale&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Resource")
public class Resource {

	@XmlElement(name = "entity")
	protected List<Entity> entityList;

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
		
		entityList = new LinkedList<Entity>();
		for (Entity m : resource.getEntityList()) {
			entityList.add(new Entity(m));
		}

		actionList = new LinkedList<Action>();
		for (Action a : resource.getActionList()) {
			actionList.add(new Action(a));
		}
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
