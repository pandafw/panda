package panda.tool.codegen.bean;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ActionProperty complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ActionProperty&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;validator&quot; type=&quot;{panda.tool.codegen}Validator&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;setterTrim&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;getterTrim&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;setterCode&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;getterCode&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;initValue&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modifier&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;type&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ActionProperty")
public class ActionProperty implements Comparable<ActionProperty> {

	@XmlElement(name = "validator")
	private List<Validator> validatorList;

	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String getterTrim;
	@XmlAttribute
	private String setterTrim;
	@XmlAttribute
	private String setterCode;
	@XmlAttribute
	private String getterCode;
	@XmlAttribute
	private String initValue;
	@XmlAttribute
	private String modifier = "protected";
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String label;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public ActionProperty() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param property source property
	 */
	public ActionProperty(ActionProperty property) {
		this.tooltip = property.tooltip;
		this.setterTrim = property.setterTrim;
		this.getterTrim = property.getterTrim;
		this.setterCode = property.setterCode;
		this.getterCode = property.getterCode;
		this.initValue = property.initValue;
		this.modifier = property.modifier;
		this.type = property.type;
		this.label = property.label;
		this.name = property.name;

		validatorList = new LinkedList<Validator>();
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
	public static ActionProperty extend(ActionProperty src, ActionProperty parent) {
		ActionProperty me = new ActionProperty(parent);

		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.setterTrim != null) {
			me.setterTrim = src.setterTrim;
		}
		if (src.getterTrim != null) {
			me.getterTrim = src.getterTrim;
		}
		if (src.setterCode != null) {
			me.setterCode = src.setterCode;
		}
		if (src.getterCode != null) {
			me.getterCode = src.getterCode;
		}
		if (src.initValue != null) {
			me.initValue = src.initValue;
		}
		if (src.modifier != null) {
			me.modifier = src.modifier;
		}
		if (src.type != null) {
			me.type = src.type;
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
	 * Gets the value of the full java type.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getFullJavaType() {
		return TypeUtils.getFullJavaType(type, name);
	}

	/**
	 * Gets the value of the native java type.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getNativeJavaType() {
		return TypeUtils.getNativeJavaType(type, name);
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSimpleJavaType() {
		return TypeUtils.getSimpleJavaType(type, name);
	}

	/**
	 * @return the validatorList
	 */
	public List<Validator> getValidatorList() {
		if (validatorList == null) {
			validatorList = new LinkedList<Validator>();
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
	 * @return the getterTrim
	 */
	public String getGetterTrim() {
		return getterTrim;
	}

	/**
	 * @param getterTrim the getterTrim to set
	 */
	public void setGetterTrim(String getterTrim) {
		this.getterTrim = getterTrim;
	}

	/**
	 * @return the setterTrim
	 */
	public String getSetterTrim() {
		return setterTrim;
	}

	/**
	 * @param setterTrim the setterTrim to set
	 */
	public void setSetterTrim(String setterTrim) {
		this.setterTrim = setterTrim;
	}

	/**
	 * @return the setterCode
	 */
	public String getSetterCode() {
		return setterCode;
	}

	/**
	 * @param setterCode the setterCode to set
	 */
	public void setSetterCode(String setterCode) {
		this.setterCode = setterCode;
	}

	/**
	 * @return the getterCode
	 */
	public String getGetterCode() {
		return getterCode;
	}

	/**
	 * @param getterCode the getterCode to set
	 */
	public void setGetterCode(String getterCode) {
		this.getterCode = getterCode;
	}

	/**
	 * Gets the value of the initValue property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getInitValue() {
		return initValue;
	}

	/**
	 * Sets the value of the initValue property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setInitValue(String value) {
		this.initValue = value;
	}
	
	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setModifier(String value) {
		this.modifier = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setType(String value) {
		this.type = value;
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
	 * Sets the value of the name property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(ActionProperty o) {
		if (this == o) {
			return 0;
		}
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

}
