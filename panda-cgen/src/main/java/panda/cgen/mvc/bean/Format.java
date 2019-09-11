package panda.cgen.mvc.bean;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Objects;

/**
 * <p>
 * Java class for Format complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Format&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;param&quot; type=&quot;{panda.cgen.mvc}Param&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;type&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;pattern&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Format")
public class Format {

	@XmlElement(name = "param")
	private List<Param> paramList;

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String pattern;

	/**
	 * Constructor
	 */
	public Format() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param format source format
	 */
	public Format(Format format) {
		this.type = format.type;
		this.pattern = format.pattern;

		paramList = new LinkedList<Param>();
		for (Param p : format.getParamList()) {
			paramList.add(new Param(p));
		}
	}

	/**
	 * @return the paramList
	 */
	public List<Param> getParamList() {
		if (paramList == null) {
			paramList = new LinkedList<Param>();
		}
		return this.paramList;
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
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public int hashCode() {
		return Objects.hash(paramList, pattern, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Format rhs = (Format)obj;
		return Objects.equalsBuilder()
				.append(paramList, rhs.paramList)
				.append(pattern, rhs.pattern)
				.append(type, rhs.type)
				.isEquals();
	}

}
