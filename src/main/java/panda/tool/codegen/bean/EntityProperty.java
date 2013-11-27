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
 * Java class for ModelProperty complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;EntityProperty&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;validator&quot; type=&quot;{panda.tool.codegen}Validator&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "EntityProperty")
public class EntityProperty implements Comparable<EntityProperty> {

	@XmlElement(name = "validator")
	private List<Validator> validatorList;

	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String label;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public EntityProperty() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param property source property
	 */
	public EntityProperty(EntityProperty property) {
		this.tooltip = property.tooltip;
		this.label = property.label;
		this.name = property.name;

		validatorList = new ArrayList<Validator>();
		for (Validator v : property.getValidatorList()) {
			validatorList.add(new Validator(v));
		}
	}

	/**
	 * extend property
	 * 
	 * @param src source property
	 * @param parent extend property
	 * @return property
	 */
	public static EntityProperty extend(EntityProperty src, EntityProperty parent) {
		EntityProperty me = new EntityProperty(parent);

		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.label != null) {
			me.label = src.label;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		me.getValidatorList().addAll(src.getValidatorList());

		return me;
	}

	/**
	 * @return the validatorList
	 */
	public List<Validator> getValidatorList() {
		if (validatorList == null) {
			validatorList = new ArrayList<Validator>();
		}
		return this.validatorList;
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Gets the value of the label property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value of the label property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setLabel(String value) {
		this.label = value;
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
	 * Gets the value of the uname property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getUname() {
		if (name == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			}
			else {
				sb.append(Character.toUpperCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

	protected int compareByName(EntityProperty o) {
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(EntityProperty o) {
		if (this == o) {
			return 0;
		}
		return compareByName(o);
	}
}
