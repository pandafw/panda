package panda.tool.codegen.bean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * <p>
 * Java class for Tag complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Tag&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;param&quot; type=&quot;{panda.tool.codegen}Param&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Tag")
public class Tag {

	@XmlElement(name = "param")
	private List<Param> paramList;

	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public Tag() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param tag source tag
	 */
	public Tag(Tag tag) {
		this.name = tag.name;

		paramList = new LinkedList<Param>();
		for (Param p : tag.getParamList()) {
			paramList.add(new Param(p));
		}
	}

	/**
	 * @param chars param name prefix chars
	 * @return true if param name starts with the prefix chars
	 */
	public boolean hasParamStartsWithAny(String chars) {
		for (Param p : getParamList()) {
			if (Strings.startsWithChars(p.getName(), chars)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param prefix param name prefix
	 * @return true if param name starts with the prefix
	 */
	public boolean hasParamStartsWith(String prefix) {
		for (Param p : getParamList()) {
			if (p.getName().startsWith(prefix)) {
				return true;
			}
		}
		return false;
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

	private Map<String, String> params;

	/**
	 * @return the params map
	 */
	public Map<String, String> getParams() {
		if (params == null) {
			params = new HashMap<String, String>();
			for (Param p : getParamList()) {
				params.put(p.getName(), p.getValue());
			}
		}
		return params;
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

	@Override
	public int hashCode() {
		return Objects.hash(name, paramList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag)obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (paramList == null) {
			if (other.paramList != null)
				return false;
		}
		else if (!paramList.equals(other.paramList))
			return false;
		return true;
	}

}
