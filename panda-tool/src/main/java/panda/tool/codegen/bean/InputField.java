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
 * Java class for InputField complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;InputField&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;header&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;content&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;editTag&quot; type=&quot;{panda.tool.codegen}Tag&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;viewTag&quot; type=&quot;{panda.tool.codegen}Tag&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;footer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;validator&quot; type=&quot;{panda.tool.codegen}Validator&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;generate&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;display&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;requiredrefer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;requiredvalidate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;order&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "InputField")
public class InputField implements Comparable<InputField> {

	@XmlElement
	private String header;
	@XmlElement
	private String content;
	@XmlElement
	private Tag editTag;
	@XmlElement
	private Tag viewTag;
	@XmlElement
	private String footer;
	@XmlElement(name = "validator")
	private List<Validator> validatorList;

	@XmlAttribute
	private Boolean generate;
	@XmlAttribute
	private Boolean display;
	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private Boolean required;
	@XmlAttribute
	private String requiredrefer;
	@XmlAttribute
	private Boolean requiredvalidate = true;
	@XmlAttribute
	private Integer order;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public InputField() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param ifd source input field
	 */
	public InputField(InputField ifd) {
		this.header = ifd.header;
		this.footer = ifd.footer;
		this.content = ifd.content;
		this.generate = ifd.generate;
		this.display = ifd.display;
		this.tooltip = ifd.tooltip;
		this.label = ifd.label;
		this.required = ifd.required;
		this.requiredrefer = ifd.requiredrefer;
		this.requiredvalidate = ifd.requiredvalidate;
		this.order = ifd.order;
		this.name = ifd.name;

		this.content = ifd.content;
		if (ifd.editTag != null) {
			this.editTag = new Tag(ifd.editTag);
		}
		if (ifd.viewTag != null) {
			this.viewTag = new Tag(ifd.viewTag);
		}

		validatorList = new LinkedList<Validator>();
		for (Validator v : ifd.getValidatorList()) {
			validatorList.add(new Validator(v));
		}
	}

	/**
	 * extend input field
	 * 
	 * @param src source input field
	 * @param parent extend input field
	 * @return input field
	 */
	public static InputField extend(InputField src, InputField parent) {
		InputField me = new InputField(parent);

		if (src.header != null) {
			me.header = src.header;
		}
		if (src.footer != null) {
			me.footer = src.footer;
		}
		if (src.content != null) {
			me.content = src.content;
		}
		if (src.generate != null) {
			me.generate = src.generate;
		}
		if (src.display != null) {
			me.display = src.display;
		}
		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.label != null) {
			me.label = src.label;
		}
		if (src.required != null) {
			me.required = src.required;
		}
		if (src.requiredrefer != null) {
			me.requiredrefer = src.requiredrefer;
		}
		if (src.requiredvalidate != null) {
			me.requiredvalidate = src.requiredvalidate;
		}
		if (src.order != null) {
			me.order = src.order;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		if (src.content != null) {
			me.content = src.content;
		}
		if (src.editTag != null) {
			me.editTag = new Tag(src.editTag);
		}
		if (src.viewTag != null) {
			me.viewTag = new Tag(src.viewTag);
		}

		me.getValidatorList().addAll(src.getValidatorList());

		return me;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the editTag
	 */
	public Tag getEditTag() {
		return editTag;
	}

	/**
	 * @param editTag the editTag to set
	 */
	public void setEditTag(Tag editTag) {
		this.editTag = editTag;
	}

	/**
	 * @return the viewTag
	 */
	public Tag getViewTag() {
		return viewTag;
	}

	/**
	 * @param viewTag the viewTag to set
	 */
	public void setViewTag(Tag viewTag) {
		this.viewTag = viewTag;
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
	 * @return the generate
	 */
	public Boolean getGenerate() {
		return generate;
	}

	/**
	 * @param generate the generate to set
	 */
	public void setGenerate(Boolean generate) {
		this.generate = generate;
	}

	/**
	 * @return the display
	 */
	public Boolean getDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(Boolean display) {
		this.display = display;
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the requiredrefer
	 */
	public String getRequiredrefer() {
		return requiredrefer;
	}

	/**
	 * @param requiredrefer the requiredrefer to set
	 */
	public void setRequiredrefer(String requiredrefer) {
		this.requiredrefer = requiredrefer;
	}

	/**
	 * @return the requiredvalidate
	 */
	public Boolean getRequiredvalidate() {
		return requiredvalidate;
	}

	/**
	 * @param requiredvalidate the requiredvalidate to set
	 */
	public void setRequiredvalidate(Boolean requiredvalidate) {
		this.requiredvalidate = requiredvalidate;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @return the name
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
		return EntityProperty.upname(name);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	protected int compareByName(InputField o) {
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(InputField o) {
		if (this == o) {
			return 0;
		}
		if (this.order == null && o.order == null) {
			return compareByName(o);
		}
		if (this.order == null) {
			return -1;
		}
		if (o.order == null) {
			return 1;
		}
		int i = this.order.compareTo(o.order);
		return i == 0 ? compareByName(o) : i;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((generate == null) ? 0 : generate.hashCode());
		result = prime * result + ((display == null) ? 0 : display.hashCode());
		result = prime * result + ((editTag == null) ? 0 : editTag.hashCode());
		result = prime * result + ((footer == null) ? 0 : footer.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((required == null) ? 0 : required.hashCode());
		result = prime * result + ((requiredrefer == null) ? 0 : requiredrefer.hashCode());
		result = prime * result + ((requiredvalidate == null) ? 0 : requiredvalidate.hashCode());
		result = prime * result + ((tooltip == null) ? 0 : tooltip.hashCode());
		result = prime * result + ((validatorList == null) ? 0 : validatorList.hashCode());
		result = prime * result + ((viewTag == null) ? 0 : viewTag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputField other = (InputField)obj;
		if (content == null) {
			if (other.content != null)
				return false;
		}
		else if (!content.equals(other.content))
			return false;
		if (generate == null) {
			if (other.generate != null)
				return false;
		}
		else if (!generate.equals(other.generate))
			return false;
		if (display == null) {
			if (other.display != null)
				return false;
		}
		else if (!display.equals(other.display))
			return false;
		if (editTag == null) {
			if (other.editTag != null)
				return false;
		}
		else if (!editTag.equals(other.editTag))
			return false;
		if (footer == null) {
			if (other.footer != null)
				return false;
		}
		else if (!footer.equals(other.footer))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		}
		else if (!header.equals(other.header))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		}
		else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		}
		else if (!order.equals(other.order))
			return false;
		if (required == null) {
			if (other.required != null)
				return false;
		}
		else if (!required.equals(other.required))
			return false;
		if (requiredrefer == null) {
			if (other.requiredrefer != null)
				return false;
		}
		else if (!requiredrefer.equals(other.requiredrefer))
			return false;
		if (requiredvalidate == null) {
			if (other.requiredvalidate != null)
				return false;
		}
		else if (!requiredvalidate.equals(other.requiredvalidate))
			return false;
		if (tooltip == null) {
			if (other.tooltip != null)
				return false;
		}
		else if (!tooltip.equals(other.tooltip))
			return false;
		if (validatorList == null) {
			if (other.validatorList != null)
				return false;
		}
		else if (!validatorList.equals(other.validatorList))
			return false;
		if (viewTag == null) {
			if (other.viewTag != null)
				return false;
		}
		else if (!viewTag.equals(other.viewTag))
			return false;
		return true;
	}


}
