package panda.tool.codegen.bean;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Strings;

/**
 * <p>
 * Java class for ListColumn complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name=&quot;ListColumn&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;format&quot; type=&quot;{panda.tool.codegen}Format&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;filter&quot; type=&quot;{panda.tool.codegen}Filter&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;cssClass&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;generate&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;link&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;group&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;filterable&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;sortable&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;hidden&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;value&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;order&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ListColumn")
public class ListColumn implements Comparable<ListColumn> {

	@XmlElement
	private Format format;

	@XmlElement
	private Filter filter;

	@XmlAttribute
	private String cssClass;
	@XmlAttribute
	private Boolean generate;
	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private String link;
	@XmlAttribute
	private Boolean group;
	@XmlAttribute
	private Boolean sortable;
	@XmlAttribute
	private String filterable;
	@XmlAttribute
	private Boolean hidden;
	@XmlAttribute
	private Boolean value;
	@XmlAttribute
	private Integer order;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public ListColumn() {
	}

	/**
	 * Constructor - copy properties from source
	 *
	 * @param lc source list column
	 */
	public ListColumn(ListColumn lc) {
		this.generate = lc.generate;
		this.tooltip = lc.tooltip;
		this.link = lc.link;
		this.cssClass = lc.cssClass;
		this.group = lc.group;
		this.sortable = lc.sortable;
		this.filterable = lc.filterable;
		this.hidden = lc.hidden;
		this.value = lc.value;
		this.order = lc.order;
		this.label = lc.label;
		this.name = lc.name;

		if (lc.format != null) {
			this.format = new Format(lc.format);
		}

		if (lc.filter != null) {
			this.filter = new Filter(lc.filter);
		}
	}

	/**
	 * extend list column
	 *
	 * @param src source list column
	 * @param parent extend list column
	 * @return listcolumn
	 */
	public static ListColumn extend(ListColumn src, ListColumn parent) {
		ListColumn me = new ListColumn(parent);

		if (src.generate != null) {
			me.generate = src.generate;
		}
		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.link != null) {
			me.link = src.link;
		}
		if (src.cssClass != null) {
			me.cssClass = src.cssClass;
		}
		if (src.group != null) {
			me.group = src.group;
		}
		if (src.sortable != null) {
			me.sortable = src.sortable;
		}
		if (src.filterable != null) {
			me.filterable = src.filterable;
		}
		if (src.hidden != null) {
			me.hidden = src.hidden;
		}
		if (src.value != null) {
			me.value = src.value;
		}
		if (src.order != null) {
			me.order = src.order;
		}
		if (src.label != null) {
			me.label = src.label;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		if (src.format != null) {
			me.format = "none".equalsIgnoreCase(src.format.getType()) ? null : new Format(src.format);
		}

		if (src.filter != null) {
			me.filter = "none".equalsIgnoreCase(src.filter.getType()) ? null : new Filter(src.filter);
		}

		return me;
	}

	/**
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
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
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the group
	 */
	public Boolean getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(Boolean group) {
		this.group = group;
	}

	/**
	 * @return the sortable
	 */
	public Boolean getSortable() {
		return sortable;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * @return the filterable
	 */
	public String getFilterable() {
		return filterable;
	}

	/**
	 * @param filterable the filterable to set
	 */
	public void setFilterable(String filterable) {
		if (Strings.isNotEmpty(filterable) && !"true".equals(filterable) && !"false".equals(filterable)) {
			throw new IllegalArgumentException("Invalid filterable value: " + filterable);
		}
		this.filterable = filterable;
	}

	/**
	 * @return the hidden
	 */
	public Boolean getHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the value
	 */
	public Boolean getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Boolean value) {
		this.value = value;
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

	public boolean isVirtualColumn() {
		return Strings.isEmpty(name) || !Character.isLetter(name.charAt(0));
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

	protected int compareByName(ListColumn o) {
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(ListColumn o) {
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
		return Objects.hash(cssClass, generate, filter, filterable, format, group, hidden, label, link, name, order, sortable, tooltip, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListColumn other = (ListColumn)obj;
		if (cssClass == null) {
			if (other.cssClass != null)
				return false;
		}
		else if (!cssClass.equals(other.cssClass))
			return false;
		if (generate == null) {
			if (other.generate != null)
				return false;
		}
		else if (!generate.equals(other.generate))
			return false;
		if (filter == null) {
			if (other.filter != null)
				return false;
		}
		else if (!filter.equals(other.filter))
			return false;
		if (filterable == null) {
			if (other.filterable != null)
				return false;
		}
		else if (!filterable.equals(other.filterable))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		}
		else if (!format.equals(other.format))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		}
		else if (!group.equals(other.group))
			return false;
		if (hidden == null) {
			if (other.hidden != null)
				return false;
		}
		else if (!hidden.equals(other.hidden))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		}
		else if (!label.equals(other.label))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		}
		else if (!link.equals(other.link))
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
		if (sortable == null) {
			if (other.sortable != null)
				return false;
		}
		else if (!sortable.equals(other.sortable))
			return false;
		if (tooltip == null) {
			if (other.tooltip != null)
				return false;
		}
		else if (!tooltip.equals(other.tooltip))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}

	
}
