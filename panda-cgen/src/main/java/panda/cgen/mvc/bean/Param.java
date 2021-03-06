package panda.cgen.mvc.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import panda.bind.json.Jsons;
import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * <p>
 * Java class for Param complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Param&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;attribute name=&quot;value&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Param")
public class Param {

	@XmlAttribute(required = true)
	private String value;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public Param() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param param source param
	 */
	public Param(Param param) {
		this.name = param.name;
		this.value = param.value;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link String }
	 */
	public Object getValues() {
		if (value != null) {
			try {
				return Jsons.fromJson(value);
			}
			catch (Throwable e) {
				System.out.println("the value of <Param name='" + name + "'> is not a JSON format: " + value);
				throw Exceptions.wrapThrow(e);
			}
		}
		return value;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setValue(String value) {
		this.value = value;
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
	 * Gets the value of the capitalized name property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getCname() {
		return Strings.capitalize(name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Param rhs = (Param)obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(value, rhs.value)
				.isEquals();
	}
}
