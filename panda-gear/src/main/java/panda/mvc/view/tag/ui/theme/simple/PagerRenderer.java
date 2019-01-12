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
		
		Attributes attr = new Attributes();
		attr.add("id", id)
			.cssClass(tag, "p-pager clearfix")
			.data("start", defs(start))
			.data("count", defs(count))
			.data("limit", defs(limit))
			.data("total", defs(total))
			.data("pages", defs(pages))
			.data("style", tag.getPagerStyle())
			.data("click", tag.getOnLinkClick())
			.data("spy", "ppager")
			.cssStyle(tag);
		stag("div", attr);

		String style = tag.getPagerStyle();
		if (Strings.isNotEmpty(style)) {
			if (total > 0 || count > 0 || start > 0) {
				for (int i = 0; i < style.length(); i++) {
					switch (style.charAt(i)) {
					case '<':
						write("<ul class=\"pagination\">");
						break;
					case 'f':
						writePagerLinkFirst(true);
						break;
					case 'F':
						writePagerLinkFirst(false);
						break;
					case 'p':
						writePagerLinkPrev(total > 0);
						break;
					case 'P':
						writePagerLinkPrev(false);
						break;
					case '#':
						if (total > 0 || count > 0) {
							writePagerLinkNums();
						}
						break;
					case 'n':
						if (total > 0) {
							writePagerLinkNext(true);
						}
						else if (count > 0) {
							writePagerLinkNext(false);
						}
						break;
					case 'N':
						if (total > 0 || count > 0) {
							writePagerLinkNext(false);
						}
						break;
					case 'l':
						if (total > 0) {
							writePagerLinkLast(true);
						}
						break;
					case 'L':
						if (total > 0) {
							writePagerLinkLast(false);
						}
						break;
					case '>':
						if (total > 0 || count > 0 || start > 0) {
							write("</ul>");
						}
						break;
					case 'i':
						if (total > 0 || count > 0) {
							writePagerTextInfo();
						}
						else {
							writePagerEmptyInfo();
						}
						break;
					case 's':
						if (total > 0 || count > 0 || start > 0) {
							writePagerLimit();
						}
						break;
					default:
						break;
					}
				}
			}
			else {
				if (Strings.contains(style, 'i')){
					writePagerEmptyInfo();
				}
			}
		}

		etag("div");
	}

	private void writePagerInfo(String info) throws IOException {
		write("<div class=\"p-pager-info\">");
		write(info);
		write("</div>");
	}

	private void writePagerEmptyInfo() throws IOException {
		writePagerInfo(tag.getEmptyText());
	}
	
	private void writePagerTextInfo() throws IOException {
		writePagerInfo(tag.getInfoText());
	}

	private void writePagerLimit() throws IOException {
		if (limit == null || limit < 1 || Strings.isEmpty(tag.getOnLimitChange())) {
			return;
		}

		write("<div class=\"p-pager-limit\">");
		write(tag.getLimitLabel());

		Select select = context.getIoc().get(Select.class);
		select.setTheme("simple");
		select.setId(id + "_limit");
		select.setName(Strings.isEmpty(tag.getLimitName()) ? id + "_limit" : tag.getLimitName());
		select.setCssClass("select form-control");
		select.setValue(limit.toString());
		select.setList(tag.getLimitList());
		select.setTitle(tag.getLimitTooltip());
		select.setOnchange(tag.getOnLimitChange());
		
		select.start(writer);
		select.end(writer, "");
		write("</div>");
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
	
	private void writePagerLinkFirst(boolean hidden) throws IOException {
		write("<li class=\"p-pager-first");
		if (page <= 1) {
			write(hidden? " hidden" : " disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(1));
		write('"');
		write(" pageno=\"1");
		write("\" title=\"");
		write(tag.getFirstTooltip());
		write('"');
		if (page <= 1) {
			write(" onclick=\"return false\"");
		}
		write('>');
		write(tag.getFirstText());
		write("</a></li>");
	}
	
	private void writePagerLinkPrev(boolean hidden) throws IOException {
		int p = page - 1;
		if (p < 1) {
			p = 1;
		}

		write("<li class=\"p-pager-prev");
		if (page <= 1) {
			write(hidden? " hidden" : " disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(p));
		write("\" pageno=\"");
		write(String.valueOf(p));
		write("\" title=\"");
		write(tag.getPrevTooltip());
		write('"');
		if (page <= 1) {
			write(" onclick=\"return false\"");
		}
		write('>');
		write(tag.getPrevText());
		write("</a></li>");
	}

	private void writePagerLinkNext(boolean hidden) throws IOException {
		boolean hasNext = (limit == null || (count >= limit && total < 1) || page < pages);
		write("<li class=\"p-pager-next");
		if (!hasNext) {
			write(hidden? " hidden" : " disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(page + 1));
		write("\" pageno=\"");
		write(String.valueOf(page + 1));
		write("\" title=\"");
		write(tag.getNextTooltip());
		write('"');
		if (!hasNext) {
			write(" onclick=\"return false\"");
		}
		write('>');
		write(tag.getNextText());
		write("</a></li>");
	}

	private void writePagerLinkLast(boolean hidden) throws IOException {
		write("<li class=\"p-pager-last");
		if (page >= pages) {
			write(hidden? " hidden" : " disabled");
		}
		write("\"><a href=\"");
		write(getLinkHref(pages));
		write("\" pageno=\"");
		write(pages.toString());
		write("\" title=\"");
		write(tag.getLastTooltip());
		write('"');
		if (page >= pages) {
			write(" onclick=\"return false\"");
		}
		write('>');
		write(tag.getLastText());
		write("</a></li>");
	}

	private void writePagerLinkNums() throws IOException {
		boolean pe = Strings.contains(tag.getPagerStyle(),  '.');
		boolean p1 = Strings.contains(tag.getPagerStyle(),  '1');
		boolean px = Strings.contains(tag.getPagerStyle(),  'x');

		int linkSize = defi(tag.getLinkSize());
		int linkMax = linkSize;
		if (p1) {
			linkMax += 2;
		}
		else if (pe) {
			linkMax++;
		}
		if (px) {
			linkMax += 2;
		}
		else if (pe) {
			linkMax++;
		}

		if (linkMax >= pages) {
			for (int p = 1; p <= pages; p++) {
				linkp(p);
			}
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


		if (p1) {
			if (p > 1) {
				linkp(1);
			}
			
			if (p == 3) {
				linkp(2);
			}
			else if (p > 3) {
				ellipsis(true, true);
			}
		}
		else {
			ellipsis(true, pe && p > 2);
		}

		for (int i = 0; i < linkSize && p <= pages; i++, p++) {
			linkp(p);
		}

		if (px) {
			if (p < pages - 1) {
				ellipsis(false, pe);
				linkp(pages);
			}
			else if (p == pages - 1) {
				linkp(p);
				linkp(pages);
			}
			else if (p == pages) {
				linkp(pages);
			}
		}
		else {
			ellipsis(false, pe && p < pages);
		}
	}

	private void ellipsis(boolean left, boolean show) throws IOException {
		write("<li class=\"p-pager-ellipsis-");
		write(left ? "left" : "right");
		if (!show) {
			write(" hidden");
		}
		write("\"><span>&hellip;</span></li>");
	}

	private void linkp(int p) throws IOException {
		write("<li class=\"p-pager-page");
		if (page == p) {
			write(" active");
		}
		write("\"><a href=\"");
		write(getLinkHref(p));
		write("\" pageno=\"");
		write(String.valueOf(p));
		write("\">");
		write(String.valueOf(p));
		write("</a></li>");
	}
}
