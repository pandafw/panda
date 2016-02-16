package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class PagerRenderer extends AbstractEndRenderer<Pager> {

	private String id;
	/** start index */
	private Integer start;
	/** item count of current page */
	private Integer count;
	/** item limit per page */
	private Integer limit;
	/** total item count */
	private Integer total;
	/** total pages */
	private Integer pages;
	/** page no */
	private Integer page;
	
	private String linkHref;
	private Map<String, Object> linkBean;
	
	private boolean hiddenStyle;
	private int linkSize;

	public PagerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		id = tag.getId();
		start = defi(tag.getStart());
		count = defi(tag.getCount());
		limit = defi(tag.getLimit());
		total = defi(tag.getTotal());
		page = defi(tag.getPage());
		pages = defi(tag.getPages());

		linkHref = Strings.replaceChars(defs(tag.getLinkHref(), "#"), '!', '$');
		linkSize = defi(tag.getLinkSize());
		
		Attributes attr = new Attributes();
		attr.add("id", id)
			.cssClass(tag, "pagination p-pager")
			.data("start", defs(start))
			.data("count", defs(count))
			.data("limit", defs(limit))
			.data("total", defs(total))
			.data("click", tag.getOnLinkClick())
			.data("spy", "ppager")
			.cssStyle(tag);
		stag("ul", attr);
		
		if (total > 0) {
			writePagerTextInfo();
			writePagerLinkFirst();
			writePagerLinkPrev(false);
			writePagerLinkPages();
			writePagerLinkNext(false);
			writePagerLinkLast();
			writePagerLimit();
		}
		else if (count > 0) {
			writePagerLinkPrev(true);
			writePagerTextInfo();
			writePagerLinkNext(true);
			writePagerLimit();
		}
		else if (start > 0) {
			writePagerLinkFirst();
			writePagerLinkPrev(true);
			writePagerEmptyInfo();
			writePagerLimit();
		}
		else {
			writePagerEmptyInfo();
		}
		
		etag("ul");
	}

	private void writePagerInfo(String info) throws IOException {
		write("<li class=\"p-pager-info\"><span>");
		write(info);
		write("</span></li>");
	}

	private void writePagerEmptyInfo() throws IOException {
		if (tag.isRenderInfo()) {
			writePagerInfo(tag.getEmptyText());
		}
	}
	
	private void writePagerTextInfo() throws IOException {
		if (tag.isRenderInfo()) {
			writePagerInfo(tag.getInfoText());
		}
	}

	private void writePagerLimit() throws IOException {
		if (limit == null || limit < 1 || !tag.isLimitSelective()) {
			return;
		}

		String onLimitChange = tag.getOnLimitChange();
	
		write("<li class=\"p-pager-limit\"><span>");
		write(tag.getLimitLabel());
		if (Strings.isEmpty(onLimitChange)) {
			write(limit.toString());
		}
		else {
			Select select = context.getIoc().get(Select.class);
			
			select.setTheme("simple");
			select.setId(id + "_limit");
			String limitName = tag.getLimitName();
			if (Strings.isEmpty(limitName)) {
				select.setName(id + "_limit");
			}
			select.setCssClass("select");
			select.setValue(limit.toString());
			select.setList(tag.getLimitList());
			select.setOnchange(onLimitChange);
			
			select.start(writer);
			select.end(writer, "");
		}
		write("</span></li>");
	}

	private String getLinkHref(int pn) {
		if (linkBean == null) {
			linkBean = new HashMap<String, Object>();
			linkBean.put("limit", limit);
		}
		
		pn = pn < 1 ? 1 : (pn > pages ? pages : pn);
		linkBean.put("page", pn);
		linkBean.put("start", (pn - 1) * limit);
		return Mvcs.translate(linkHref, linkBean);
	}
	
	private void writePagerLinkFirst() throws IOException {
		boolean hasFirst = (page > 1);
		write("<li class=\"p-pager-first");
		if ((!hasFirst && hiddenStyle) || !tag.isRenderFirst()) {
			write(" hidden");
		}
		else if (!hasFirst) {
			write(" disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(1));
		write('"');
		write(" data-pageno=\"1");
		if (hasFirst) {
			write("\" data-toggle=\"tooltip\" title=\"");
			write(tag.getFirstTooltip());
			write('"');
		}
		else {
			write("\" onclick=\"return false\"");
		}
		write('>');
		write(tag.getFirstText());
		write("</a></li>");
	}
	
	private void writePagerLinkPrev(boolean force) throws IOException {
		boolean hasPrev = (page > 1);
		write("<li class=\"p-pager-prev");
		if ((!hasPrev && hiddenStyle) || (!force && !tag.isRenderPrev())) {
			write(" hidden");
		}
		else if (!hasPrev) {
			write(" disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(page - 1));
		write("\" data-pageno=\"");
		write(String.valueOf(page - 1));
		if (hasPrev) {
			write("\" data-toggle=\"tooltip\" title=\"");
			write(tag.getPrevTooltip());
			write('"');
		}
		else {
			write("\" onclick=\"return false\"");
		}
		write('>');
		write(tag.getPrevText());
		write("</a></li>");
	}

	private void writePagerLinkNext(boolean force) throws IOException {
		boolean hasNext = (limit == null || (count >= limit && total < 1) || page < pages);
		write("<li class=\"p-pager-next");
		if ((!hasNext && hiddenStyle) || (!force && !tag.isRenderNext())) {
			write(" hidden");
		}
		else if (!hasNext) {
			write(" disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(page + 1));
		write("\" data-pageno=\"");
		write(String.valueOf(page + 1));
		if (hasNext) {
			write("\" data-toggle=\"tooltip\" title=\"");
			write(tag.getNextTooltip());
			write('"');
		}
		else {
			write("\" onclick=\"return false\"");
		}
		write('>');
		write(tag.getNextText());
		write("</a></li>");
	}

	private void writePagerLinkLast() throws IOException {
		boolean hasLast = (page < pages);
		write("<li class=\"p-pager-last");
		if ((!hasLast && hiddenStyle) || !tag.isRenderLast()) {
			write(" hidden");
		}
		else if (!hasLast) {
			write(" disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(pages));
		write("\" data-pageno=\"");
		write(pages.toString());
		if (hasLast) {
			write("\" data-toggle=\"tooltip\" title=\"");
			write(tag.getLastTooltip());
			write('"');
		}
		else {
			write("\" onclick=\"return false\"");
		}
		write('>');
		write(tag.getLastText());
		write("</a></li>");
	}

	private void writePagerLinkPages() throws IOException {
		if (!tag.isRenderPageNo()) {
			return;
		}
		
		int p = 1;
		if (page > linkSize / 2) {
			p = page - (linkSize / 2);
		}
		if (p + linkSize > pages) {
			p = pages - linkSize + 1;
		}
		if (p < 1) {
			p = 1;
		}

		boolean ep = tag.isRenderPageEllipsis();
		boolean p1 = tag.isRenderPage1();
		boolean px = tag.isRenderPageX();

		if (p > 1 && p1) {
			linkp(1);
		}
		
		if (p > 2 && (p1 || ep)) {
			ellipsis();
		}

		for (int i = 0; i < linkSize && p <= pages; i++, p++) {
			linkp(p);
		}
		
		if (p < pages - 1) {
			if (ep || px) {
				ellipsis();
			}
			if (px) {
				linkp(pages);
			}
		}
		else if (p == pages - 1) {
			if (px) {
				linkp(p);
				linkp(pages);
			}
			else if (ep) {
				ellipsis();
			}
		}
		else if (p == pages) {
			if (px) {
				linkp(pages);
			}
			else if (ep) {
				ellipsis();
			}
		}
	}

	private void ellipsis() throws IOException {
		write("<li class=\"p-pager-ellipsis\"><span>&hellip;</span></li>");
	}

	private void linkp(int p) throws IOException {
		write("<li");
		if (page == p) {
			write(" class=\"active\"");
		}
		write("><a href=\"");
		write(getLinkHref(p));
		write("\" data-pageno=\"");
		write(String.valueOf(p));
		write("\">");
		write(String.valueOf(p));
		write("</a></li>");
	}
}
