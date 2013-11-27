package panda.tool.codegen.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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
 *       &lt;attribute name=&quot;display&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;tooltip&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;link&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;nowrap&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;width&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;group&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;filterable&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;sortable&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;hidden&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
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
	private Boolean display;
	@XmlAttribute
	private String tooltip;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private String link;
	@XmlAttribute
	private Boolean nowrap;
	@XmlAttribute
	private String width;
	@XmlAttribute
	private Boolean group;
	@XmlAttribute
	private Boolean sortable;
	@XmlAttribute
	private Boolean filterable;
	@XmlAttribute
	private Boolean hidden;
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
		this.display = lc.display;
		this.tooltip = lc.tooltip;
		this.link = lc.link;
		this.nowrap = lc.nowrap;
		this.width = lc.width;
		this.group = lc.group;
		this.sortable = lc.sortable;
		this.filterable = lc.filterable;
		this.hidden = lc.hidden;
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

		if (src.display != null) {
			me.display = src.display;
		}
		if (src.tooltip != null) {
			me.tooltip = src.tooltip;
		}
		if (src.link != null) {
			me.link = src.link;
		}
		if (src.nowrap != null) {
			me.nowrap = src.nowrap;
		}
		if (src.width != null) {
			me.width = src.width;
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
			me.format = new Format(src.format);
		}

		if (src.filter != null) {
			me.filter = new Filter(src.filter);
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
	 * @return the nowrap
	 */
	public Boolean getNowrap() {
		return nowrap;
	}

	/**
	 * @param nowrap the nowrap to set
	 */
	public void setNowrap(Boolean nowrap) {
		this.nowrap = nowrap;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
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
	public Boolean getFilterable() {
		return filterable;
	}

	/**
	 * @param filterable the filterable to set
	 */
	public void setFilterable(Boolean filterable) {
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

}
