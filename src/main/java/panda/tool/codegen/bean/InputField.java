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
 *       &lt;attribute name=&quot;display&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;modelField&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
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
	private Boolean display;
	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private Boolean modelField = true;
	@XmlAttribute
	private Boolean required;
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
		this.display = ifd.display;
		this.tooltip = ifd.tooltip;
		this.label = ifd.label;
		this.modelField = ifd.modelField;
		this.required = ifd.required;
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
		if (src.display != null) {
			me.display = src.display;
		}
		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.label != null) {
			me.label = src.label;
		}
		if (src.modelField != null) {
			me.modelField = src.modelField;
		}
		if (src.required != null) {
			me.required = src.required;
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
	 * @return the modelField
	 */
	public Boolean getModelField() {
		return modelField;
	}

	/**
	 * @param modelField the modelField to set
	 */
	public void setModelField(Boolean modelField) {
		this.modelField = modelField;
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

}
