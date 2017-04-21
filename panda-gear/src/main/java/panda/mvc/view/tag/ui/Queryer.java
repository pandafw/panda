package panda.mvc.view.tag.ui;

import java.util.List;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.view.tag.ListColumn;

/**
 * Render an HTML Queryer.
 */
@IocBean(singleton=false)
public class Queryer extends UIBean {
	protected List<ListColumn> columns;

	protected String queryer;

	protected String cssLabel;
	protected String cssInput;
	protected String labelCaption;
	protected String labelAddFilter;
	protected String labelMethod;
	protected String labelBtnClear;
	protected String labelBtnQuery;

	// form attributes
	protected String action;
	protected String method;
	protected String target;
	protected String onsubmit;
	protected String onreset;

	/**
	 * expand query filters:
	 * default
	 *  - show: has input filter
	 *  - hide: no input filter
	 * fixed
	 *  - show: has input filter | has fixed filter
	 * always
	 *  - show always
	 * none
	 *  - hide always
	 */
	protected char fsexpand = 'd';

	protected String pagerStyle;

	protected String hiddens;
	
	private Map boolFilterMap;
	private Map dateFilterMap;
	private Map stringFilterMap;
	private Map numberFilterMap;
	private Map filterMethodMap;
	
	private static int sequence = 0;

	/**
	 * Override UIBean's implementation, such that component Html id is determined in the following
	 * order :-
	 * <ol>
	 * <li>This component id attribute</li>
	 * <li>list_[an increasing sequential number]</li>
	 * </ol>
	 */
	protected void populateComponentHtmlId() {
		if (id == null) {
			id = "queryer_" + (sequence++);
		}
	}

	/**
	 * getTextAsMap
	 * @param name resource name
	 * @return map value
	 */
	private Map getTextAsMap(String name) {
		return context.getText().getTextAsMap(name, Collections.EMPTY_MAP);
	}

	/**
	 * @return the getFilterMethodMap
	 */
	public Map getFilterMethodMap() {
		if (filterMethodMap == null) {
			filterMethodMap = getTextAsMap("query-methods");
		}
		return filterMethodMap;
	}

	/**
	 * @return the boolFilterMap
	 */
	public Map getBoolFilterMap() {
		if (boolFilterMap == null) {
			boolFilterMap = getTextAsMap("query-booleans");
		}
		return boolFilterMap;
	}

	/**
	 * @return the dateFilterMap
	 */
	public Map getDateFilterMap() {
		if (dateFilterMap == null) {
			dateFilterMap = getTextAsMap("query-dates");
		}
		return dateFilterMap;
	}

	/**
	 * @return the numberFilterMap
	 */
	public Map getNumberFilterMap() {
		if (numberFilterMap == null) {
			numberFilterMap = getTextAsMap("query-numbers");
		}
		return numberFilterMap;
	}

	/**
	 * @return the stringFilterMap
	 */
	public Map getStringFilterMap() {
		if (stringFilterMap == null) {
			stringFilterMap = getTextAsMap("query-strings");
		}
		return stringFilterMap;
	}

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		populateComponentHtmlId();

		if (cssLabel == null) {
			cssLabel = context.getText().getText("query-css-label", "col-sm-3");
		}
		
		if (cssInput == null) {
			cssInput = context.getText().getText("query-css-input", "col-sm-9");
		}
		
		if (labelCaption == null) {
			labelCaption = context.getText().getText("query-lbl-caption", "Filters");
		}
		
		if (labelAddFilter == null) {
			labelAddFilter = context.getText().getText("query-lbl-add-filter", "Add filter");
		}
		
		if (labelMethod == null) {
			labelMethod = context.getText().getText("query-lbl-method", "Query method");
		}
		
		if (labelBtnClear == null) {
			labelBtnClear = context.getText().getText("query-btn-clear", "Clear");
		}
		
		if (labelBtnQuery == null) {
			labelBtnQuery = context.getText().getText("query-btn-search", "Search");
		}
	}

	/**
	 * @param obj1 object 1
	 * @param obj2 object 2
	 * @return true if obj2 exists in obj1
	 */
	public boolean contains(Object obj1, Object obj2) {
		return Objects.contains(obj1, obj2);
	}

	/**
	 * @return the columns
	 */
	public List<ListColumn> getColumns() {
		return columns;
	}

	/**
	 * @return the queryer
	 */
	public String getQueryer() {
		return queryer;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the onsubmit
	 */
	public String getOnsubmit() {
		return onsubmit;
	}

	/**
	 * @return the onreset
	 */
	public String getOnreset() {
		return onreset;
	}

	/**
	 * @param boolFilterMap the boolFilterMap to set
	 */
	public void setBoolFilterMap(Map boolFilterMap) {
		this.boolFilterMap = boolFilterMap;
	}

	/**
	 * @param dateFilterMap the dateFilterMap to set
	 */
	public void setDateFilterMap(Map dateFilterMap) {
		this.dateFilterMap = dateFilterMap;
	}

	/**
	 * @param stringFilterMap the stringFilterMap to set
	 */
	public void setStringFilterMap(Map stringFilterMap) {
		this.stringFilterMap = stringFilterMap;
	}

	/**
	 * @param numberFilterMap the numberFilterMap to set
	 */
	public void setNumberFilterMap(Map numberFilterMap) {
		this.numberFilterMap = numberFilterMap;
	}

	/**
	 * @param filterMethodMap the filterMethodMap to set
	 */
	public void setFilterMethodMap(Map filterMethodMap) {
		this.filterMethodMap = filterMethodMap;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ListColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @param queryer the queryer to set
	 */
	public void setQueryer(String queryer) {
		this.queryer = queryer;
	}

	/**
	 * @param action the action to set
	 */

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @param onsubmit the onsubmit to set
	 */
	public void setOnsubmit(String onsubmit) {
		this.onsubmit = onsubmit;
	}

	/**
	 * @param onreset the onreset to set
	 */
	public void setOnreset(String onreset) {
		this.onreset = onreset;
	}

	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		return hiddens;
	}

	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}

	/**
	 * @param fsexpand the fsexpand to set
	 */
	public void setFsexpand(String fsexpand) {
		if (Strings.isEmpty(fsexpand)) {
			return;
		}
		this.fsexpand = Character.toLowerCase(fsexpand.charAt(0));
	}
	
	public boolean isFsExpandAlways() {
		return fsexpand == 'a';
	}
	
	public boolean isFsExpandDefault() {
		return fsexpand == 'd';
	}
	
	public boolean isFsExpandFixed() {
		return fsexpand == 'f';
	}
	
	public boolean isFsExpandNone() {
		return fsexpand == 'n';
	}

	/**
	 * @return the cssLabel
	 */
	public String getCssLabel() {
		return cssLabel;
	}

	/**
	 * @param cssLabel the cssLabel to set
	 */
	public void setCssLabel(String cssLabel) {
		this.cssLabel = cssLabel;
	}

	/**
	 * @return the cssInput
	 */
	public String getCssInput() {
		return cssInput;
	}

	/**
	 * @param cssInput the cssInput to set
	 */
	public void setCssInput(String cssInput) {
		this.cssInput = cssInput;
	}

	/**
	 * @return the labelCaption
	 */
	public String getLabelCaption() {
		return labelCaption;
	}

	/**
	 * @param labelCaption the labelCaption to set
	 */
	public void setLabelCaption(String labelCaption) {
		this.labelCaption = labelCaption;
	}

	/**
	 * @return the labelAddFilter
	 */
	public String getLabelAddFilter() {
		return labelAddFilter;
	}

	/**
	 * @param labelAddFilter the labelAddFilter to set
	 */
	public void setLabelAddFilter(String labelAddFilter) {
		this.labelAddFilter = labelAddFilter;
	}

	/**
	 * @return the labelMethod
	 */
	public String getLabelMethod() {
		return labelMethod;
	}

	/**
	 * @param labelMethod the labelMethod to set
	 */
	public void setLabelMethod(String labelMethod) {
		this.labelMethod = labelMethod;
	}

	/**
	 * @return the labelBtnClear
	 */
	public String getLabelBtnClear() {
		return labelBtnClear;
	}

	/**
	 * @param labelBtnClear the labelBtnClear to set
	 */
	public void setLabelBtnClear(String labelBtnClear) {
		this.labelBtnClear = labelBtnClear;
	}

	/**
	 * @return the labelBtnQuery
	 */
	public String getLabelBtnQuery() {
		return labelBtnQuery;
	}

	/**
	 * @param labelBtnQuery the labelBtnQuery to set
	 */
	public void setLabelBtnQuery(String labelBtnQuery) {
		this.labelBtnQuery = labelBtnQuery;
	}

	/**
	 * @return the pagerStyle
	 */
	public String getPagerStyle() {
		return pagerStyle;
	}

	/**
	 * @param pagerStyle the pagerStyle to set
	 */
	public void setPagerStyle(String pagerStyle) {
		this.pagerStyle = pagerStyle;
	}

}

