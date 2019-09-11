package panda.mvc.view.tag.ui;

import java.util.List;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.mvc.view.util.ListColumn;

/**
 * Render an HTML List View.
 */
@IocBean(singleton=false)
public class ListView extends UIBean {
	private final static String HEADER_STYLE = "listview-header-style";
	private final static String FOOTER_STYLE = "listview-footer-style";
	private final static String HEADER_THRESHOLD = "listview-header-threshold";
	private final static String FOOTER_THRESHOLD = "listview-footer-threshold";
	private final static String SORTABLE = "listview-sortable";
	private final static String HIDE_CHECK_ALL = "listview-hideCheckAll";

	public static class ItemLink {
		public String href;
		public String action;
		public String icon;
		public String label;
		public String tooltip;
		public String target;
		public String onclick;
		public Map<String, Object> params;
	}
	
	// attributes
	protected Object list;
	protected List<ListColumn> columns;

	protected String queryer;

	protected Boolean sortable;
	protected String cssColumn;
	protected String cssTable;

	// form attributes
	protected String action;
	protected String method;
	protected String target;
	protected String onsubmit;
	protected String onreset;

	//--------------------------------------------
	// style option
	// p : pager
	// t : tools
	// a : addon
	// 
	protected String headerStyle;
	protected String footerStyle;

	protected Integer headerThreshold;
	protected Integer footerThreshold;

	protected String tools;
	protected String addon;

	protected String pagerStyle;
	
	protected ItemLink link;

	protected Boolean hideCheckAll;
	protected boolean singleSelect;
	protected boolean untoggleSelect;
	protected String onrowclick;
	
	protected String hiddens;
	
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

		if (headerStyle == null) {
			headerStyle = context.getText().getText(HEADER_STYLE, "pta");
		}
		if (headerThreshold == null) {
			headerThreshold = context.getText().getTextAsInt(HEADER_THRESHOLD, 0);
		}

		if (footerStyle == null) {
			footerStyle = context.getText().getText(FOOTER_STYLE, "tap");
		}
		if (footerThreshold == null) {
			footerThreshold = context.getText().getTextAsInt(FOOTER_THRESHOLD, 10);
		}

		if (sortable == null) {
			sortable = context.getText().getTextAsBoolean(SORTABLE, true);
		}

		if (hideCheckAll == null) {
			hideCheckAll = context.getText().getTextAsBoolean(HIDE_CHECK_ALL, false);
		}
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
	 * @return the cssTablle
	 */
	public String getCssTable() {
		return cssTable;
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
	 * @param cssColumn the cssColumn to set
	 */
	public void setCssColumn(String cssColumn) {
		this.cssColumn = cssColumn;
	}

	/**
	 * @param cssTable the cssTable to set
	 */
	public void setCssTable(String cssTable) {
		this.cssTable = cssTable;
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
	 * @return the header style
	 */
	public String getHeaderStyle() {
		return headerStyle;
	}

	/**
	 * @param headerStyle the header style to set
	 */
	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	/**
	 * @return the headerThreshold
	 */
	public int getHeaderThreshold() {
		return headerThreshold;
	}

	/**
	 * @param headerThreshold the headerThreshold to set
	 */
	public void setHeaderThreshold(int headerThreshold) {
		this.headerThreshold = headerThreshold;
	}

	/**
	 * @return the footer style
	 */
	public String getFooterStyle() {
		return footerStyle;
	}

	/**
	 * @param footerStyle the footer style to set
	 */
	public void setFooterStyle(String footerStyle) {
		this.footerStyle = footerStyle;
	}

	/**
	 * @return the footerThreshold
	 */
	public int getFooterThreshold() {
		return footerThreshold;
	}

	/**
	 * @param footerThreshold the footerThreshold to set
	 */
	public void setFooterThreshold(int footerThreshold) {
		this.footerThreshold = footerThreshold;
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
