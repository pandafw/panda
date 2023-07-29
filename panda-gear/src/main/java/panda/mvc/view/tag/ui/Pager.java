package panda.mvc.view.tag.ui;

import panda.bind.json.JsonObject;
import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.util.TextProvider;


/**
 * Render an HTML pager.
 * <pre>
 * LinkStyle: 
 *  <: <ul>
 *  p: previous page (hidden)
 *  P: previous page (disabled)
 *  n: next page (hidden)
 *  N: next page (disabled)
 *  f: first page (hidden)
 *  F: first page (disabled)
 *  l: last page (hidden)
 *  L: last page (disabled)
 *  1: #1 first page (depends on '#')
 *  #: page number links
 *  x: #x last page (depends on '#')
 *  .: ellipsis
 *  >: </ul>
 *  i: pager info text
 *  s: limit size select
 * </pre>
 */
@IocBean(singleton=false)
public class Pager extends UIBean {
	private final static String DEFAULT_LINK_HREF = "#";
	private final static int DEFAULT_LINK_SIZE = 5;
	private final static String DEFAULT_PAGER_STYLE = "is<fp#nl>";
	private final static String DEFAULT_LIMIT_LIST = "{ '20': '20 Items', '50': '50 Items', '100': '100 Items' }";

	private final static String LABEL_EMPTY = "pager-label-empty";
	private final static String LABEL_INFO = "pager-label-info";
	private final static String LABEL_INFOZ = "pager-label-infoz";
	private final static String LABEL_FIRST = "pager-label-first";
	private final static String LABEL_LAST = "pager-label-last";
	private final static String LABEL_PREV = "pager-label-prev";
	private final static String LABEL_NEXT = "pager-label-next";
	private final static String LABEL_LIMIT = "pager-label-limit";
	private final static String TOOLTIP_FIRST = "pager-tooltip-first";
	private final static String TOOLTIP_LAST = "pager-tooltip-last";
	private final static String TOOLTIP_PREV = "pager-tooltip-prev";
	private final static String TOOLTIP_NEXT = "pager-tooltip-next";
	private final static String TOOLTIP_LIMIT = "pager-tooltip-limit";
	private final static String LIMIT_LIST = "pager-limit-list";
	private final static String LINK_SIZE = "pager-link-size";
	private final static String PAGER_STYLE = "pager-style";

	// attributes
	protected panda.mvc.bean.Pager pager = new panda.mvc.bean.Pager();
	
	protected String emptyText;
	protected String infoText;
	protected String firstText;
	protected String lastText;
	protected String prevText;
	protected String nextText;
	protected String firstTooltip;
	protected String lastTooltip;
	protected String prevTooltip;
	protected String nextTooltip;
	protected String limitTooltip;

	protected String linkHref;
	protected Integer linkSize;
	protected String pagerStyle;
	protected String onLinkClick;

	protected String limitName;
	protected String limitLabel;
	protected Object limitList;
	protected String onLimitChange;

	private static int sequence = 0;

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		if (id == null) {
			id = "pager_" + (sequence++);
		}
		
		pager.normalize();

		TextProvider txt = context.getText();

		if (emptyText == null) {
			emptyText = txt.getText(LABEL_EMPTY, "", pager);
		}

		if (firstText == null) {
			firstText = txt.getText(LABEL_FIRST, "&lt;&lt;", pager);
		}
		
		if (lastText == null) {
			lastText = txt.getText(LABEL_LAST, "&gt;&gt;", pager);
		}
		
		if (prevText == null) {
			prevText = txt.getText(LABEL_PREV, "&lt;", pager);
		}
		
		if (nextText == null) {
			nextText = txt.getText(LABEL_NEXT, "&gt;", pager);
		}

		if (firstTooltip == null) {
			firstTooltip = txt.getText(TOOLTIP_FIRST, "&lt;&lt; First Page", pager);
		}
		
		if (lastTooltip == null) {
			lastTooltip = txt.getText(TOOLTIP_LAST, "Last Page &gt;&gt;", pager);
		}
		
		if (prevTooltip == null) {
			prevTooltip = txt.getText(TOOLTIP_PREV, "&lt; Previous ${top.limit}", pager);
		}
		
		if (nextTooltip == null) {
			nextTooltip = txt.getText(TOOLTIP_NEXT, "Next ${top.limit} &gt;", pager);
		}

		if (linkSize == null) {
			linkSize = txt.getTextAsInt(LINK_SIZE, DEFAULT_LINK_SIZE);
		}

		if (pagerStyle == null) {
			pagerStyle = txt.getText(PAGER_STYLE, DEFAULT_PAGER_STYLE);
		}

		if (linkHref == null) {
			linkHref = DEFAULT_LINK_HREF;
		}

		if (limitLabel == null) {
			limitLabel = txt.getText(LABEL_LIMIT, "");
		}

		if (limitTooltip == null) {
			limitTooltip = txt.getText(TOOLTIP_LIMIT, "Display Items");
		}
		
		if (limitList == null) {
			limitList = JsonObject.fromJson(txt.getText(LIMIT_LIST, DEFAULT_LIMIT_LIST));
		}

		if (infoText == null) {
			infoText = txt.getText(
				pager.getTotal() == null ? LABEL_INFOZ : LABEL_INFO, 
				pager.getTotal() == null ? "${top.begin}~${top.end}" : "${top.begin}~${top.end}/${top.total}",
				pager);
		}
	}

	/**
	 * @return the pager
	 */
	public panda.mvc.bean.Pager getPager() {
		return pager;
	}

	/**
	 * @param pager the pager to set
	 */
	public void setPager(panda.mvc.bean.Pager pager) {
		if (pager != null) {
			this.pager = pager.clone();
		}
	}

	/**
	 * @return the begin page no
	 */
	public Long getBegin() {
		return pager.getBegin();
	}
	
	/**
	 * @return the end page no
	 */
	public Long getEnd() {
		return pager.getEnd();
	}
	
	/**
	 * @return the page
	 */
	public Long getPage() {
		return pager.getPage();
	}

	/**
	 * @return the start
	 */
	public Long getStart() {
		return pager.getStart();
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return pager.getCount();
	}

	/**
	 * @return the limit
	 */
	public Long getLimit() {
		return pager.getLimit();
	}

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return pager.getTotal();
	}

	/**
	 * @return the pages
	 */
	public Long getPages() {
		return pager.getPages();
	}
	
	/**
	 * @return the emptyText
	 */
	public String getEmptyText() {
		return emptyText;
	}

	/**
	 * @return the infoText
	 */
	public String getInfoText() {
		return infoText;
	}

	/**
	 * @return the firstText
	 */
	public String getFirstText() {
		return firstText;
	}

	/**
	 * @return the lastText
	 */
	public String getLastText() {
		return lastText;
	}

	/**
	 * @return the prevText
	 */
	public String getPrevText() {
		return prevText;
	}

	/**
	 * @return the nextText
	 */
	public String getNextText() {
		return nextText;
	}

	/**
	 * @return the firstTooltip
	 */
	public String getFirstTooltip() {
		return firstTooltip;
	}

	/**
	 * @return the lastTooltip
	 */
	public String getLastTooltip() {
		return lastTooltip;
	}

	/**
	 * @return the prevTooltip
	 */
	public String getPrevTooltip() {
		return prevTooltip;
	}

	/**
	 * @return the nextTooltip
	 */
	public String getNextTooltip() {
		return nextTooltip;
	}

	/**
	 * @return the linkHref
	 */
	public String getLinkHref() {
		return linkHref;
	}

	/**
	 * @return the linkSize
	 */
	public Integer getLinkSize() {
		return linkSize;
	}

	/**
	 * @return the pageStyle
	 */
	public String getPagerStyle() {
		return pagerStyle;
	}

	/**
	 * @return the onLinkClick
	 */
	public String getOnLinkClick() {
		return onLinkClick;
	}

	/**
	 * @return the limitName
	 */
	public String getLimitName() {
		return limitName;
	}

	/**
	 * @return the limitLabel
	 */
	public String getLimitLabel() {
		return limitLabel;
	}

	/**
	 * @return the limitTooltip
	 */
	public String getLimitTooltip() {
		return limitTooltip;
	}

	/**
	 * @return the limitList
	 */
	public Object getLimitList() {
		return limitList;
	}

	/**
	 * @return the onLimitChange
	 */
	public String getOnLimitChange() {
		return onLimitChange;
	}

	/**
	 * @param emptyText the emptyText to set
	 */
	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}

	/**
	 * @param infoText the infoText to set
	 */
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	/**
	 * @param firstText the firstText to set
	 */
	public void setFirstText(String firstText) {
		this.firstText = firstText;
	}

	/**
	 * @param lastText the lastText to set
	 */
	public void setLastText(String lastText) {
		this.lastText = lastText;
	}

	/**
	 * @param prevText the prevText to set
	 */
	public void setPrevText(String prevText) {
		this.prevText = prevText;
	}

	/**
	 * @param nextText the nextText to set
	 */
	public void setNextText(String nextText) {
		this.nextText = nextText;
	}

	/**
	 * @param firstTooltip the firstTooltip to set
	 */
	public void setFirstTooltip(String firstTooltip) {
		this.firstTooltip = firstTooltip;
	}

	/**
	 * @param lastTooltip the lastTooltip to set
	 */
	public void setLastTooltip(String lastTooltip) {
		this.lastTooltip = lastTooltip;
	}

	/**
	 * @param prevTooltip the prevTooltip to set
	 */
	public void setPrevTooltip(String prevTooltip) {
		this.prevTooltip = prevTooltip;
	}

	/**
	 * @param nextTooltip the nextTooltip to set
	 */
	public void setNextTooltip(String nextTooltip) {
		this.nextTooltip = nextTooltip;
	}

	/**
	 * @param linkSize the linkSize to set
	 */
	public void setLinkSize(Integer linkSize) {
		this.linkSize = linkSize;
	}

	/**
	 * @param pagerStyle the pagerStyle to set
	 */
	public void setPagerStyle(String pagerStyle) {
		this.pagerStyle = pagerStyle;
	}

	/**
	 * @param linkHref the linkHref to set
	 */
	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	/**
	 * @param onLinkClick the onLinkClick to set
	 */
	public void setOnLinkClick(String onLinkClick) {
		this.onLinkClick = onLinkClick;
	}

	/**
	 * @param limitLabel the limitLabel to set
	 */
	public void setLimitLabel(String limitLabel) {
		this.limitLabel = limitLabel;
	}

	/**
	 * @param limitTooltip the limitTooltip to set
	 */
	public void setLimitTooltip(String limitTooltip) {
		this.limitTooltip = limitTooltip;
	}

	/**
	 * @param limitName the limitName to set
	 */
	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}

	/**
	 * @param limitList the limitList to set
	 */
	public void setLimitList(Object limitList) {
		this.limitList = limitList;
	}

	/**
	 * @param onLimitChange the onLimitChange to set
	 */
	public void setOnLimitChange(String onLimitChange) {
		this.onLimitChange = onLimitChange;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Long page) {
		pager.setPage(page);
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Long start) {
		pager.setStart(start);
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		pager.setCount(count);
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Long limit) {
		pager.setLimit(limit);
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		pager.setTotal(total);
	}

	public boolean isLimitSelective() {
		return Strings.contains(pagerStyle, 's');
	}
}
