package panda.mvc.view.tag.ui;

import java.util.ArrayList;
import java.util.List;

import panda.ioc.annotation.IocBean;

/**
 * <!-- START SNIPPET: javadoc --> Render an HTML input tag of type select. <!-- END SNIPPET:
 * javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * 
 * &lt;s:select label=&quot;Pets&quot;
 *        name=&quot;petIds&quot;
 *        list=&quot;petDao.pets&quot;
 *        listKey=&quot;id&quot;
 *        listValue=&quot;name&quot;
 *        multiple=&quot;true&quot;
 *        size=&quot;3&quot;
 *        required=&quot;true&quot;
 *        value=&quot;%{petDao.pets.{id}}&quot;
 * /&gt;
 * 
 * &lt;s:select label=&quot;Months&quot;
 *        name=&quot;months&quot;
 *        headerKey=&quot;-1&quot; headerValue=&quot;Select Month&quot;
 *        list=&quot;#{'01':'Jan', '02':'Feb', [...]}&quot;
 *        value=&quot;selectedMonth&quot;
 *        required=&quot;true&quot;
 * /&gt;
 * 
 * // The month id (01, 02, ...) returned by the getSelectedMonth() call
 * // against the stack will be auto-selected
 * 
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 * <p/>
 * <!-- START SNIPPET: exnote --> Note: For any of the tags that use lists (select probably being
 * the most ubiquitous), which uses the OGNL list notation (see the "months" example above), it
 * should be noted that the map key created (in the months example, the '01', '02', etc.) is typed.
 * '1' is a char, '01' is a String, "1" is a String. This is important since if the value returned
 * by your "value" attribute is NOT the same type as the key in the "list" attribute, they WILL NOT
 * MATCH, even though their String values may be equivalent. If they don't match, nothing in your
 * list will be auto-selected.
 * <p/>
 * <!-- END SNIPPET: exnote -->
 */
@IocBean(singleton=false)
public class Select extends ListUIBean {
	protected boolean emptyOption;
	protected String headerKey;
	protected String headerValue;
	protected boolean multiple;
	protected String size;
	
	protected List<OptGroup> optGroups;

	/**
	 * @return the optGroups
	 */
	public List<OptGroup> getOptGroups() {
		return optGroups;
	}

	/**
	 * @param optGroup the optGroup to add
	 */
	public void addOptGroup(OptGroup optGroup) {
		if (optGroups == null) {
			optGroups = new ArrayList<OptGroup>();
		}
		optGroups.add(optGroup);
	}

	/**
	 * @return the emptyOption
	 */
	public boolean isEmptyOption() {
		return emptyOption;
	}

	/**
	 * @return the headerKey
	 */
	public String getHeaderKey() {
		return headerKey;
	}

	/**
	 * @return the headerValue
	 */
	public String getHeaderValue() {
		return headerValue;
	}

	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Whether or not to add an empty (--) option after the header option
	 */
	public void setEmptyOption(boolean emptyOption) {
		this.emptyOption = emptyOption;
	}

	/**
	 * Key for first item in list. Must not be empty! '-1' and '' is correct, '' is bad.
	 */
	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	/**
	 * Value expression for first item in list
	 */
	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	/**
	 * Creates a multiple select. The tag will pre-select multiple values
	 * if the values are passed as an Array or a Collection(of appropriate types) via the value attribute. 
	 * If one of the keys equals one of the values in the Collection or Array it wil be selected
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * Size of the element box (# of elements to show)
	 */
	public void setSize(String size) {
		this.size = size;
	}
}