package panda.mvc.view.tag.ui;

import java.util.List;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.view.tag.ListColumn;

/**
 * Render an HTML List View.
 */
@IocBean(singleton=false)
public class ListView extends UIBean {
	public static class ItemLink {
		public String action;
		public Map<String, Object> params;
	}
	
	public static final int THRESHOLD = 10;
	
	// attributes
	protected Object list;
	protected List<ListColumn> columns;

	protected String queryer;

	protected Boolean sortable;
	protected String cssColumn;

	// form attributes
	protected String action;
	protected String method;
	protected String target;
	protected String onsubmit;
	protected String onreset;

	//
	protected boolean autosize = true;
	protected boolean script = true;

	//--------------------------------------------
	// style option
	// p : pager
	// t : tools
	// a : addon
	// 
	protected String header;
	protected String footer;

	protected String tools;
	protected String addon;

	protected ItemLink link;

	protected Boolean hideCheckAll;
	protected boolean singleSelect;
	protected boolean untoggleSelect;
	protected String onrowclick;
	
	protected String hiddens;
	
	private static int sequence = 0;

	private Map boolFilterMap;
	private Map dateFilterMap;
	private Map stringFilterMap;
	private Map numberFilterMap;
	private Map filterMethodMap;
	
	/**
	 * getTextAsMap
	 * @param name resource name
	 * @return map value
	 */
	private Map getTextAsMap(String name) {
		return context.getText().getTextAsMap(name, Collections.EMPTY_MAP);
	}

	/**
	 * getTextAsBoolean
	 * @param name resource name
	 * @param defv default value
	 * @return boolean value
	 */
	private Boolean getTextAsBoolean(String name, Boolean defv) {
		return context.getText().getTextAsBoolean(name, defv);
	}

	/**
	 * @return the getFilterMethodMap
	 */
	public Map getFilterMethodMap() {
		if (filterMethodMap == null) {
			filterMethodMap = getTextAsMap("listview-filter-methods");
		}
		return filterMethodMap;
	}

	/**
	 * @return the boolFilterMap
	 */
	public Map getBoolFilterMap() {
		if (boolFilterMap == null) {
			boolFilterMap = getTextAsMap("listview-filter-bools");
		}
		return boolFilterMap;
	}

	/**
	 * @return the dateFilterMap
	 */
	public Map getDateFilterMap() {
		if (dateFilterMap == null) {
			dateFilterMap = getTextAsMap("listview-filter-dates");
		}
		return dateFilterMap;
	}

	/**
	 * @return the numberFilterMap
	 */
	public Map getNumberFilterMap() {
		if (numberFilterMap == null) {
			numberFilterMap = getTextAsMap("listview-filter-numbers");
		}
		return numberFilterMap;
	}

	/**
	 * @return the stringFilterMap
	 */
	public Map getStringFilterMap() {
		if (stringFilterMap == null) {
			stringFilterMap = getTextAsMap("listview-filter-strings");
		}
		return stringFilterMap;
	}

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
			id = "list_" + (sequence++);
		}
	}

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		populateComponentHtmlId();
		
		if (sortable == null) {
			sortable = getTextAsBoolean("listview-sortable", true);
		}

		if (hideCheckAll == null) {
			hideCheckAll = getTextAsBoolean("listview-hideCheckAll", false);
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

	private boolean isShowHeadPart(int part, int count) {
		if (Strings.isEmpty(header)) {
			return count > THRESHOLD;
		}
		return Strings.contains(header, part);
	}

	private boolean isShowFootPart(int part, int count) {
		if (Strings.isEmpty(footer)) {
			return count > THRESHOLD;
		}
		return Strings.contains(footer, part);
	}
	
	public boolean isShowHeadPager(int count) {
		return isShowHeadPart('p', count);
	}
	
	public boolean isShowHeadTools(int count) {
		return Strings.isNotEmpty(tools) && isShowHeadPart('t', count);
	}
	
	public boolean isShowHeadAddon(int count) {
		return Strings.isNotEmpty(addon) && isShowHeadPart('a', count);
	}
	
	public boolean isShowFootPager(int count) {
		return isShowFootPart('p', count);
	}
	
	public boolean isShowFootTools(int count) {
		return Strings.isNotEmpty(tools) && isShowFootPart('t', count);
	}
	
	public boolean isShowFootAddon(int count) {
		return Strings.isNotEmpty(addon) && isShowFootPart('a', count);
	}
	
	/**
	 * @return the list
	 */
	public Object getList() {
		return list;
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
	 * @return the cssColumn
	 */
	public String getCssColumn() {
		return cssColumn;
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
	 * @return the autosize
	 */
	public boolean isAutosize() {
		return autosize;
	}

	/**
	 * @return the script
	 */
	public boolean isScript() {
		return script;
	}

	/**
	 * @return the tools
	 */
	public String getTools() {
		return tools;
	}

	/**
	 * @return the addon
	 */
	public String getAddon() {
		return addon;
	}

	/**
	 * @return the link
	 */
	public ItemLink getLink() {
		return link;
	}

	/**
	 * @return the sortable
	 */
	public Boolean getSortable() {
		return sortable;
	}

	/**
	 * @return the singleSelect
	 */
	public boolean isSingleSelect() {
		return singleSelect;
	}

	/**
	 * @return the untoggleSelect
	 */
	public boolean isUntoggleSelect() {
		return untoggleSelect;
	}

	/**
	 * @return the onrowclick
	 */
	public String getOnrowclick() {
		return onrowclick;
	}

	/**
	 * @return the showCheckAll
	 */
	public boolean isHideCheckAll() {
		return hideCheckAll;
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
	 * @param list the list to set
	 */
	public void setList(Object list) {
		this.list = list;
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
	 * @param link the link to set
	 */
	public void setLink(ItemLink link) {
		this.link = link;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * @param autosize the autosize to set
	 */
	public void setAutosize(boolean autosize) {
		this.autosize = autosize;
	}

	/**
	 * @param script the script to set
	 */
	public void setScript(boolean script) {
		this.script = script;
	}

	/**
	 * @param cssColumn the cssColumn to set
	 */
	public void setCssColumn(String cssColumn) {
		this.cssColumn = cssColumn;
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
	 * @param tools the tools to set
	 */
	public void setTools(String tools) {
		this.tools = tools;
	}

	/**
	 * @param addon the addon to set
	 */
	public void setAddon(String addon) {
		this.addon = addon;
	}

	/**
	 * @param singleSelect the singleSelect to set
	 */
	public void setSingleSelect(boolean singleSelect) {
		this.singleSelect = singleSelect;
	}

	/**
	 * @param untoggleSelect the untoggleSelect to set
	 */
	public void setUntoggleSelect(boolean untoggleSelect) {
		this.untoggleSelect = untoggleSelect;
	}

	/**
	 * @param onrowclick the onrowclick to set
	 */
	public void setOnrowclick(String onrowclick) {
		this.onrowclick = onrowclick;
	}

	/**
	 * @param hideCheckAll the hideCheckAll to set
	 */
	public void setHideCheckAll(boolean hideCheckAll) {
		this.hideCheckAll = hideCheckAll;
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
	
	
}
