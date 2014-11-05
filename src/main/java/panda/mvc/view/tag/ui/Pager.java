package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.lang.Texts;
import panda.mvc.util.TextProvider;


/**
 * Render an HTML pager.
 * <pre>
 * LinkStyle: 
 *  p: previous page
 *  n: next page
 *  f: seek to first page
 *  l: seek to last page
 *  1: #1 first page (depends on '#')
 *  #: page number links
 *  x: #x last page (depends on '#')
 *  .: ellipsis
 *  i: info
 *  s: limit size select
 *  h: hide disabled link
 * </pre>
 */
@IocBean(singleton=false)
public class Pager extends UIBean {
	private final static String DEFAULT_LINK_HREF = "#";
	private final static int DEFAULT_LINK_SIZE = 5;
	private final static String DEFAULT_LINK_STYLE = "p1#xn";
	private final static String DEFAULT_LIMIT_LIST = "10,20,30,40,50,60,70,80,90,100";

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
	private final static String LIMIT_LIST = "pager-limit-list";
	private final static String LINK_SIZE = "pager-link-size";
	private final static String LINK_STYLE = "pager-link-style";

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

	protected String linkHref;
	protected Integer linkSize;
	protected String linkStyle;
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
			emptyText = txt.getText(LABEL_EMPTY, "", this);
		}

		if (firstText == null) {
			firstText = txt.getText(LABEL_FIRST, "&nbsp;≪&nbsp;", this);
		}
		
		if (lastText == null) {
			lastText = txt.getText(LABEL_LAST, "&nbsp;≫&nbsp;", this);
		}
		
		if (prevText == null) {
			prevText = txt.getText(LABEL_PREV, "&nbsp;&lt;&nbsp;", this);
		}
		
		if (nextText == null) {
			nextText = txt.getText(LABEL_NEXT, "&nbsp;&gt;&nbsp;", this);
		}

		if (firstTooltip == null) {
			firstTooltip = txt.getText(TOOLTIP_FIRST, "≪ First Page", this);
		}
		
		if (lastTooltip == null) {
			lastTooltip = txt.getText(TOOLTIP_LAST, "Last Page ≫", this);
		}
		
		if (prevTooltip == null) {
			prevTooltip = txt.getText(TOOLTIP_PREV, "&lt; Previous ${limit}", this);
		}
		
		if (nextTooltip == null) {
			nextTooltip = txt.getText(TOOLTIP_NEXT, "Next ${limit} &gt;", this);
		}

		if (linkSize == null) {
			linkSize = txt.getTextAsInt(LINK_SIZE, DEFAULT_LINK_SIZE);
		}

		if (linkStyle == null) {
			linkStyle = txt.getText(LINK_STYLE, DEFAULT_LINK_STYLE);
		}

		if (linkHref == null) {
			linkHref = DEFAULT_LINK_HREF;
		}

		if (limitLabel == null) {
			limitLabel = txt.getText(LABEL_LIMIT, "Display:");
		}
		
		if (limitList == null) {
			limitList = Texts.parseCsv(txt.getText(LIMIT_LIST, DEFAULT_LIMIT_LIST));
		}

		if (infoText == null) {
			infoText = txt.getText(
				pager.getTotal() == null ? LABEL_INFOZ : LABEL_INFO, 
				pager.getTotal() == null ? "${begin}~${end}" : "${begin}~${end}/${total}",
				this);
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
	 * @return the linkStyle
	 */
	public String getLinkStyle() {
		return linkStyle;
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
	 * @param linkStyle the linkStyle to set
	 */
	public void setLinkStyle(String linkStyle) {
		this.linkStyle = linkStyle;
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

}