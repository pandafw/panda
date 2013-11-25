package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ListQuery complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ListQuery&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;filter&quot; type=&quot;{nuts.tools.codegen}Filter&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;display&quot; default=&quot;true&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;order&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ListFilter")
public class ListQuery implements Comparable<ListQuery> {

	@XmlElement(name = "filter")
	private List<Filter> filterList;

	@XmlAttribute
	private Boolean display;
	@XmlAttribute
	private Integer order;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public ListQuery() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param lq source list query
	 */
	public ListQuery(ListQuery lq) {
		this.display = lq.display;
		this.order = lq.order;
		this.name = lq.name;

		filterList = new ArrayList<Filter>();
		for (Filter f : lq.getFilterList()) {
			filterList.add(new Filter(f));
		}

	}

	/**
	 * extend list query
	 * 
	 * @param src source list query
	 * @param parent extend list query
	 * @return ListQuery
	 */
	public static ListQuery extend(ListQuery src, ListQuery parent) {
		ListQuery me = new ListQuery(parent);

		if (src.display != null) {
			me.display = src.display;
		}
		if (src.order != null) {
			me.order = src.order;
		}
		if (src.name != null) {
			me.name = src.name;
		}

		List<Filter> mfList = me.getFilterList();
		List<Filter> sfList = src.getFilterList();
		for (Filter sf : sfList) {
			boolean add = false;
			for (int i = 0; i < mfList.size(); i++) {
				Filter mf = mfList.get(i);
				if (mf.getName().equals(sf.getName())) {
					mfList.set(i, mf);
					add = true;
					break;
				}
			}
			if (!add) {
				mfList.add(new Filter(sf));
			}
		}

		return me;
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

	/**
	 * @return the filterList
	 */
	public List<Filter> getFilterList() {
		if (filterList == null) {
			filterList = new ArrayList<Filter>();
		}
		return filterList;
	}

	/**
	 * @return the display filter list which Filter.display is not false
	 */
	public Set<Filter> getDisplayFilterList() {
		Set<Filter> set = new TreeSet<Filter>();
		List<Filter> list = getFilterList();
		for (int i = 0; i < list.size(); i++) {
			Filter f = list.get(i);
			if (f.getOrder() == null) {
				f.setOrder((i + 1) * 100);
			}

			if (!Boolean.FALSE.equals(f.getDisplay())) {
				set.add(f);
			}
		}
		return set;
	}

	protected int compareByName(ListQuery o) {
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(ListQuery o) {
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
