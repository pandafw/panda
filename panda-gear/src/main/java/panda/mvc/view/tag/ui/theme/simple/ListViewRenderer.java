package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.bean.Beans;
import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Iterators;
import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Queryer;
import panda.mvc.util.AccessControler;
import panda.mvc.view.tag.CUrl;
import panda.mvc.view.tag.Escapes;
import panda.mvc.view.tag.ListColumn;
import panda.mvc.view.tag.ListFilter;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.ListView.ItemLink;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.theme.AbstractEndExRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ListViewRenderer extends AbstractEndExRenderer<ListView> {
	private String id;

	private String prefix = "";
	private Queryer queryer;

	private List<ListColumn> columns;
	private String[] cssColumns;
	private Collection list;
	private int listz;
	
	// filters define list
	private Set<String> fsdefines = new HashSet<String>();
	
	private AccessControler controler;
	
	private Map<String, Map> codemaps = new HashMap<String, Map>();

	public ListViewRenderer(RenderingContext context) {
		super(context);
	}

	private void initVars() {
		controler = context.getAction() instanceof AccessControler ? (AccessControler)context.getAction() : null;

		id = tag.getId();

		if (Strings.isEmpty(tag.getQueryer())) {
			if (context.getParams() instanceof Queryer) {
				queryer = (Queryer)context.getParams();
			}
		}
		else {
			queryer = (Queryer)context.getParameter(tag.getQueryer());
			prefix = tag.getQueryer() + '.';
		}
		
		Object v = tag.getList();
		if (v != null) {
			if (v instanceof Collection) {
				list = (Collection)v;
			}
			else if (v.getClass().isArray()) {
				list = Arrays.asList((Object[])v);
			}
			else {
				throw new IllegalArgumentException("Invalid list " + v.getClass() + " of " + tag.getClass() + ", should be Collection/Array");
			}
		}
		if (list != null) {
			listz = list.size();
		}
		
		columns = tag.getColumns();
		for (ListColumn c : columns) {
			if (c.link instanceof Map) {
				c.link = Mvcs.castValue(context, c.link, ItemLink.class);
			}
			if (c.format != null && Strings.isEmpty(c.format.escape)) {
				c.format.escape = Escapes.ESCAPE_PHTML;
			}
		}
		cssColumns = Strings.split(defs(tag.getCssColumn()));

		Map<String, Filter> qfs = (queryer == null ? null : queryer.getFilters());
		if (qfs != null) {
			for (ListColumn c : columns) {
				if (!c.filterable) {
					continue;
				}
	
				ListFilter cf = c.filter;
				if (cf == null) {
					continue;
				}
	
				Filter qf = qfs.get(c.name);
				if (qf == null) {
					continue;
				}
				
				if (QueryerRenderer.isFiltered(cf, qf)) {
					fsdefines.add(c.name);
				}
			}
		}
	}

	public void render() throws IOException {
		initVars();
		
		Attributes attr = new Attributes();
		
		attr.id(id)
			.cssClass(tag, "p-lv")
			.data("onrowclick", tag.getOnrowclick())
			.cssStyle(tag)
			.disabled(tag)
			.tabindex(tag)
			.tooltip(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		if (tag.isSingleSelect()) {
			attr.data("singleSelect", "true");
		}
		if (tag.isUntoggleSelect()) {
			attr.data("untoggleSelect", "true");
		}
		if (tag.isAutosize()) {
			attr.data("autosize", "true");
		}
		stag("div", attr);
		
		writeListViewForm();

		writeListViewHeader();
		
		writeListViewTable();

		writeListViewFooter();
		
		etag("div");
	}

	private void writeListViewHiddens() throws IOException {
		if (queryer != null) {
			if (queryer.getPager() != null) {
				writeHidden(id + "_start", prefix + "p.s", queryer.getPager().getStart());
				if (isPagerLimitSelective()) {
					writeHidden(id + "_limit", prefix + "p.l", queryer.getPager().getLimit());
				}
				writeHidden(id + "_total", prefix + "p.t", queryer.getPager().getTotal(), true);
			}
			if (queryer.getSorter() != null) {
				writeHidden(id + "_sort", prefix + "s.c", queryer.getSorter().getColumn());
				writeHidden(id + "_dir", prefix + "s.d", queryer.getSorter().getDirection());
			}
		}
	}

	private void writeListViewForm() throws IOException {
		Form form = newTag(Form.class);

		form.setId(id + "_form");
		form.setCssClass("p-lv-form");
		form.setAction(tag.getAction());
		form.setMethod(defs(tag.getMethod(), "get"));
		form.setOnsubmit(tag.getOnsubmit());
		form.setOnreset(tag.getOnreset());
		form.setLoadmask(false);

		form.start(writer);

		writeListViewHiddens();
		
		if (queryer != null) {
			Map<String, Filter> _fsm = queryer.getFilters();
			if (Collections.isNotEmpty(_fsm)) {
				String p = prefix + "f.";
				for (Entry<String, Filter> en : _fsm.entrySet()) {
					String k = en.getKey();
					Filter f = en.getValue();
					
					writeHidden(id + "_f_" + k + "_c", p + k + ".c", f.getComparator());

					String t = defs(f.getType(), "s");
					List vs = f.getValues();
					if (vs != null) {
						int i = 0;
						String d = id + "_f_" + k + "_v_" + i;
						String n = p + k + "." + t + "vs";
						for (Object v : vs) {
							writeHidden(d, n, v);
						}
					}
				}
			}
		}
		
		Attributes a = new Attributes();
		a.type("submit").id(id + "_submit");
		xtag("input", a);
		
		form.end(writer, "");
	}

	private Object getBeanProperty(Object bean, String name) {
		try {
			return Beans.getBean(bean, name);
		}
		catch (Exception e) {
			return null;
		}
	}

	private boolean isPagerLimitSelective() {
		Pager pg = newTag(Pager.class);

		pg.setLinkStyle(tag.getPagerStyle());
		pg.evaluateParams();

		return pg.isLimitSelective();
	}

	private Pager createPager(String pos) {
		Pager pg = newTag(Pager.class);

		pg.setId(id + "_pager_" + pos);
		pg.setCssClass("p-lv-pager");
		pg.setPager(queryer.getPager());
		pg.setLinkStyle(tag.getPagerStyle());
		pg.setCount((long)listz);
		pg.setOnLinkClick("_plv_goto('" + id + "', #)");
		pg.setOnLimitChange("_plv_limit('" + id + "', this.value)");
		
		return pg;
	}
	
	private void writePager(String pos) throws IOException {
		if (queryer == null || queryer.getPager() == null) {
			return;
		}
		
		Pager pg = createPager(pos);
		pg.start(writer);
		pg.end(writer, "");
	}
	
	private void writeListViewHeader() throws IOException {
		boolean p = tag.isShowHeadPager(listz);
		boolean t = tag.isShowHeadTools(listz);
		boolean a = tag.isShowHeadAddon(listz);

		if (!p && !t && !a) {
			return;
		}

		write("<div id=\"");
		write(id);
		write("_header\" class=\"p-lv-header\">");

		write("<div class=\"p-lv-toolbar\">");
		if (p) {
			writePager("header");
		}
		if (t) {
			write("<div class=\"p-lv-tools\">");
			writeCheckAllButton();
			write(tag.getTools());
			write("</div>");
		}
		if (a) {
			write("<div class=\"p-lv-addon\">");
			write(tag.getAddon());
			write("</div>");
		}
		write("</div>");

		write("</div>");
	}
	
	private void writeCheckAllButton() throws IOException {
		if (tag.isHideCheckAll()) {
			return;
		}

		for (ListColumn c : columns) {
			if ("check".equals(c.type)) {
				String textSelectAll =  getText("btn-select-all");
				String textSelectNone =  getText("btn-select-none");
				String iconSelectAll =  getText("icon-select-all");
				String iconSelectNone =  getText("icon-select-none");

				Attributes as = new Attributes();
				as.cssClass("p-lv-cab");
				as.data("textSelectAll", textSelectAll);
				as.data("textSelectNone", textSelectNone);
				as.data("iconSelectAll", iconSelectAll);
				as.data("iconSelectNone", iconSelectNone);
				as.add("onclick", "return _plv_onAllClick(this);");
				
				write(button(textSelectAll, ticon(iconSelectAll), null, as.attrs()));
				break;
			}
		}
	}
	
	private void writeListViewFooter() throws IOException {
		boolean p = tag.isShowFootPager(listz);
		boolean t = tag.isShowFootTools(listz);
		boolean a = tag.isShowFootAddon(listz);

		if (!p && !t && !a) {
			return;
		}

		write("<div id=\"");
		write(id);
		write("_footer\" class=\"p-lv-footer\">");
		
		write("<div class=\"p-lv-toolbar\">");
		if (t) {
			write("<div class=\"p-lv-tools\">");
			writeCheckAllButton();
			write(tag.getTools());
			write("</div>");
		}
		if (a) {
			write("<div class=\"p-lv-addon\">");
			write(tag.getAddon());
			write("</div>");
		}
		if (p) {
			writePager("footer");
		}
		write("</div>");

		write("</div>");
	}

	private void writeListViewTableHeader() throws IOException {
		write("<thead class=\"p-lv-thead p-lv-chead\">");
		write("<tr class=\"p-lv-tr\">");

		for (ListColumn c : columns) {
			if (!c.display) {
				continue;
			}

			String ctype = defs(c.type, "cm");
			
			Attributes tha = new Attributes();
			tha.add("column", c.name)
				.cssClass("p-lv-" + ctype 
					+ ("cm".equals(ctype) ? " p-lv-cm-" + c.name : "")
					+ (tag.getSortable() && c.sortable ? " p-lv-sortable p-sortable" : "")
					+ ((queryer != null && queryer.getSorter() != null && c.name.equals(queryer.getSorter().getColumn())) ? " p-sorted p-lv-sort-" + queryer.getSorter().getDirection() : "")
					+ (fsdefines.contains(c.name) ? " p-lv-filtered" : "")
					+ (c.hidden ? " p-lv-hidden" : ""))
				.tooltip(tag);
			stag("th", tha);

			if ("check".equals(ctype)) {
				if (c.header != null) {
					write(c.header);
				}
				else {
					String sa = getText("tip-select-all", "");
					String sn = getText("tip-select-none", "");
					Attributes a = new Attributes();
					a.add("type", "checkbox")
					 .add("class", "checkbox p-lv-ca")
					 .add("title", sa)
					 .data("selectAll", sa)
					 .data("selectNone", sn);
					xtag("input", a);
				}
			}
			else {
				write(c.header);
			}
			
			if (queryer != null && queryer.getSorter() != null && c.name.equals(queryer.getSorter().getColumn())) {
				write(' ');
				write(xicon("icon-" + queryer.getSorter().getDirection() + " p-lv-sort-" + queryer.getSorter().getDirection()));
			}
			else if (tag.getSortable() && c.sortable) {
				write(' ');
				write(xicon("icon-sort p-lv-sort"));
			}
			etag("th");
		}
		write("</tr></thead>");
	}

	private String getCssColumnClass(Object d) {
		if (Arrays.isEmpty(cssColumns)) {
			return Strings.EMPTY;
		}
		
		StringBuilder ccc = new StringBuilder();
		for (String c : cssColumns) {
			Object v = getBeanProperty(d, c);
			if (v != null) {
				String s = Strings.strip(v.toString());
				if (Strings.isNotEmpty(s)) {
					ccc.append(" p-lv-cc-").append(html(c)).append('-').append(html(s));
				}
			}
		}
		return ccc.toString();
	}
	
	private void writeListViewTable() throws IOException {
		write("<div id=\"" + id + "_body\" class=\"p-lv-body\">");
		
		Form form = context.getIoc().get(Form.class);

		form.setId(id + "_bform");
		form.setCssClass("p-lv-bform");
		form.setAction(tag.getAction());
		form.setMethod(defs(tag.getMethod(), "post"));
		form.setTarget(tag.getTarget());
		form.setOnsubmit(tag.getOnsubmit());
		form.setOnreset(tag.getOnreset());
		form.setLoadmask(false);

		form.start(writer);

		write(tag.getHiddens());
		
		write("<div class=\"p-lv-table-wrap");
		if (tag.isAutosize()) {
			write(" table-responsive");
		}
		write("\">");
		write("<table id=\"" + id + "_table\" class=\"p-lv-table table");
		if (Strings.isNotEmpty(tag.getCssTable())) {
			write(' ');
			write(tag.getCssTable());
		}
		write("\">");
		writeListViewTableHeader();
		
		if (listz > 0) {
			write("<tbody class=\"p-lv-tbody\">");

			int inx = 0;
			Object prev_d = null;
			
			for (Object d : list) {
				String oe = (inx % 2 == 0 ? "odd" : "even");
				write("<tr class=\"p-lv-tr p-lv-tr-" + oe
						+ getCssColumnClass(d)
						+ "\">");

				for (ListColumn c : columns) {
					if (!c.display) {
						continue;
					}

					String ctype = defs(c.type, "cm");
					
					Attributes tda = new Attributes();
					tda.add("class", "p-lv-" + ctype 
							   + ("cm".equals(ctype) ? " p-lv-cm-" + c.name : "")
							   + (c.hidden ? " p-lv-hidden" : "")
							   + (Strings.isNotEmpty(c.cssClass) ? (" " + c.cssClass) : ""));
					stag(c.fixed ? "th" : "td", tda);
					

					if ("rownum".equals(ctype)) {
						write(String.valueOf(inx + 1));
					}
					else if ("number".equals(ctype)) {
						write(String.valueOf((queryer == null || queryer.getPager() == null) ? 0 : queryer.getPager().getStart() + inx + 1));
					}
					else if ("check".equals(ctype)) {
						write("<input type=\"checkbox\" class=\"checkbox p-lv-cb\" value=\"" + inx + "\"/>");
					}
					else if ("actions".equals(ctype)) {
						if (Collections.isNotEmpty(c.actions)) {
							for (ItemLink a : c.actions) {
								if (writeAlink("p-lv-ia", d, a)) {
									if (Strings.isNotEmpty(a.label)) {
										write("<span class=\"p-lv-t\">");
										write(a.label);
										write("</span>");
									}
									write("</a>");
								}
							}
						}
					}
					else {
						if (c.value) {
							Attributes ha = new Attributes();
							ha.type("hidden")
							  .cssClass("p-lv-cv" + (c.pkey ? " p-lv-ck" : ""))
							  .name(c.name)
							  .value(tag.castString(getBeanProperty(d, c.name)));
							xtag("input", ha);
						}
						
						if (!(c.group && prev_d != null 
								&& defs(getBeanProperty(prev_d, c.name)).equals(defs(getBeanProperty(d, c.name))))) {
							
							// for css width
							write("<div>");
							
							boolean wa = false;
							
							if (c.link instanceof Boolean && ((Boolean)c.link).booleanValue()) {
								c.link = tag.getLink();
							}
							if (c.link instanceof ItemLink) {
								wa = writeAlink("p-lv-a", d, (ItemLink)c.link);
							}

							if (c.format != null) {
								if ("code".equals(c.format.type)) {
									Object v = getBeanProperty(d, c.name);
									Iterator iv = Iterators.asIterator(v);
									if (iv != null) {
										Map codemap = getCodeMap(c.format.codemap);
										while (iv.hasNext()) {
											String s = Mvcs.getCodeText(codemap, iv.next());
											write(Escapes.escape(s, c.format.escape));
											if (iv.hasNext()) {
												write(" ");
											}
										}
									}
								}
								else if ("eval".equals(c.format.type)) {
									Asserts.notEmpty(c.format.expr, "The expression of [" + c.name + "] is empty");
									Object v = Mvcs.evaluate(context, c.format.expr);
									if (v != null) {
										v = tag.castString(v);
										write(Escapes.escape((String)v, c.format.escape));
									}
								}
								else if ("expr".equals(c.format.type)) {
									Asserts.notEmpty(c.format.expr, "The expression of [" + c.name + "] is empty");
									String v = tag.findString(c.format.expr, d);
									if (v != null) {
										write(Escapes.escape(v, c.format.escape));
									}
								}
								else if ("tran".equals(c.format.type)) {
									Asserts.notEmpty(c.format.expr, "The expression of [" + c.name + "] is empty");
									String v = Mvcs.translate(context, c.format.expr);
									if (v != null) {
										write(Escapes.escape(v, c.format.escape));
									}
								}
								else {
									Object v = getBeanProperty(d, c.name);
									if (v != null) {
										write(formatValue(v, c.format.type, c.format.pattern, c.format.escape));
									}
								}
							}
							else {
								Object v = getBeanProperty(d, c.name);
								if (v != null) {
									write(formatValue(v, null, Escapes.ESCAPE_PHTML));
								}
							}

							if (wa) {
								write("</a>");
							}
							write("</div>");
						}
					}
					
					etag(c.fixed ? "th" : "td");
				}
				
				write("</tr>");
				prev_d = d;
				
				inx++;
			}
			write("</tbody>");
		}
		
		write("</table>");
		write("</div>");

		form.end(writer, "");
		
		write("</div>");
	}
	
	private void writeLinkUrl(ItemLink link, Object d) throws IOException {
		CUrl url = newTag(CUrl.class);

		url.setAction(link.action);
		url.addParameters(link.params, d);

		url.start(writer);
		url.end(writer, "");
	}
	
	private boolean writeAlink(String cls, Object d, ItemLink link) throws IOException {
		if (controler != null) {
			if (Strings.isNotEmpty(link.action)) {
				if (!controler.canAccessData(link.action, d)) {
					return false;
				}
			}
		}
		
		write("<a class=\"");
		write(cls);
		write("\" href=\"");
		if (Strings.isNotEmpty(link.href)) {
			write(link.href);
		}
		else {
			writeLinkUrl(link, d);
		}
		write("\"");
		if (Strings.isNotEmpty(link.target)) {
			write(" target=\"" + html(link.target) + "\"");
		}
		if (Strings.isNotEmpty(link.tooltip)) {
			write(" title=\"" + html(link.tooltip) + "\"");
		}
		if (Strings.isNotEmpty(link.onclick)) {
			write(" onclick=\"" + (link.onclick) + "\"");
		}
		write(">");
		
		if (Strings.isNotEmpty(link.icon)) {
			write(xicon(link.icon));
		}
		
		return true;
	}

	private Map getCodeMap(Object cm) {
		if (cm instanceof String) {
			Map m = codemaps.get(cm);
			if (m == null) {
				m = (Map)tag.findValue((String)cm);
			}
			if (m == null) {
				throw new IllegalArgumentException("Null codemap: " + cm);
			}
			codemaps.put((String)cm, m);
			return m;
		}

		if (cm instanceof Map) {
			return (Map)cm;
		}

		throw new IllegalArgumentException("Invalid codemap: " + cm.getClass());
	}
}
