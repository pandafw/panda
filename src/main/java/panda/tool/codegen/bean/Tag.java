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
 *         &lt;element name=&quot;param&quot; type=&quot;{nuts.tools.codegen}Param&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;cssClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
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

	private String cssClass;
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
		this.cssClass = tag.cssClass;
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
	 * @return the cssClass
	 */
	public String getCssClass() {
//		if (cssClass == null) {
//			if (name != null) {
//				int i = name.lastIndexOf('.');
//				if (i >= 0) {
//					cssClass = name.substring(i + 1);
//				}
//				else {
//					cssClass = name;
//				}
//				if ("textarea".equals(cssClass)) {
//					String layout = getParams().get("layout");
//					if (StringUtils.isNotEmpty(layout)) {
//						cssClass = StringUtils.replace(cssClass, "text", layout);
//					}
//				}
//			}
//		}
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
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
