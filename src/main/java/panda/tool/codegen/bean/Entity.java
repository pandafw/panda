package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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

	@XmlAttribute(required = true)
	private String name;

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
}
