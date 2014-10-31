package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.bean.Beans;
import panda.lang.Collections;
import panda.lang.Iterators;
import panda.lang.Strings;
import panda.mvc.bean.CompositeQuery;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Sorter;
import panda.mvc.util.PermissionProvider;
import panda.mvc.view.tag.CUrl;
import panda.mvc.view.tag.ListColumn;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ListViewRenderer extends AbstractEndRenderer<ListView> {

	private String id;

	private panda.mvc.bean.Pager pager;
	private Sorter sorter;
	private CompositeQuery query;

	private List<ListColumn> columns;
	private String[] cssColumns;
	private Collection list;
	private String listn;
	private int listz;
	
	private Set<String> _fsdl = new HashSet<String>();
	private Set<String> _fsvl = new HashSet<String>();
	
	private PermissionProvider _permit;
	
	private Map<String, Map> codemaps = new HashMap<String, Map>();

	public ListViewRenderer(RenderingContext context) {
		super(context);
	}

	private void initVars() {
		_permit = context.getAction() instanceof PermissionProvider ? (PermissionProvider)context.getAction() : null;

		id = tag.getId();

		if (Strings.isNotEmpty(tag.getPager())) {
			pager = (panda.mvc.bean.Pager)context.getParameter(tag.getPager());
		}

		if (Strings.isNotEmpty(tag.getSorter())) {
			sorter = (Sorter)context.getParameter(tag.getSorter());
		}
		
		if (Strings.isNotEmpty(tag.getQuery())) {
			query = (CompositeQuery)context.getParameter(tag.getQuery());
		}
		
		listn = tag.getList();
		Object v = context.getParameter(listn);
		if (v != null) {
			if (v instanceof Collection) {
				list = (Collection)v;
			}
			else if (v.getClass().isArray()) {
				list = Arrays.asList((Object[])v);
			}
			throw new IllegalArgumentException("Invalid list parameter: " + listn + " - " + v.getClass());
		}

		if (list != null) {
			listz = list.size();
		}
		
		columns = tag.getColumns();
		cssColumns = Strings.split(defs(tag.getCssColumn()));
	}

	public void render() throws IOException {
		initVars();
		
		Attributes a = new Attributes();
		
		a.add("id", id)
		 .cssClass(tag, "p-lv")
		 .data("singleSelect", tag.getSingleSelect())
		 .data("toggleSelect", tag.getToggleSelect())
		 .data("onrowclick", tag.getOnrowclick())
		 .cssStyle(tag);
		stag("div", a);
		
		writeListViewForm();

		writeListViewTools();
		
		writeListViewHeader();
		
		writeListViewTable();

		writeListViewFooter();
		
		etag("div");
	}

	protected String uiClass(String cls) {
		return cls;
	}
	
	protected void writeJsc(String js) throws IOException {
		if (tag.isScript()) {
			super.writeJsc(js);
		}
	}

	private void writeListViewHiddens() throws IOException {
		if (pager != null) {
			writeHidden(id + "_start", tag.getPager() + ".s", pager.getStart());
			writeHidden(id + "_limit", tag.getPager() + ".l", pager.getLimit());
			writeHidden(id + "_total", tag.getPager() + ".t", pager.getTotal());
		}
		if (sorter != null) {
			writeHidden(id + "_sort", tag.getSorter() + ".c", sorter.getColumn());
			writeHidden(id + "_dir", tag.getSorter() + ".d", sorter.getDirection());
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
		
		if (query != null) {
			String _fs = tag.getQuery() + ".fs.";
			Map<String, Filter> _fsm = query.getFilters();
			if (Collections.isNotEmpty(_fsm)) {
				for (Entry<String, Filter> en : _fsm.entrySet()) {
					String k = en.getKey();
					Filter f = en.getValue();
					
					writeHidden(id + "_fs_" + k + "_c", _fs + k + ".c", f.getComparator());
					String t = defs(f.getType(), "s");
					List vs = f.getValues();
					if (vs != null) {
						int i = 0;
						String d = id + "_fs_" + k + "_v_" + i;
						String n = _fs + k + "." + t + "vs";
						for (Object v : vs) {
							writeHidden(d, n, escapeValue(v));
						}
					}
				}
			}
		}
		
		Attributes a = new Attributes();
		a.type("submit")
		 .id(id + "_submit");
		xtag("input", a);
		
		form.end(writer, "");
	}

	private void writeHidden(String id, String name, Object value) throws IOException {
		Attributes ha = new Attributes();
		ha.add("type", "hidden")
		  .add("id", id)
		  .add("name", name)
		  .add("value", value, false);
		xtag("input", ha);
	}
	
	private Object getBeanProperty(Object bean, String name) {
		try {
			return Beans.getBean(bean, name);
		}
		catch (Exception e) {
			return null;
		}
	}

	private void writePager(String pos) throws IOException {
		if (pager == null) {
			return;
		}
		
		Pager pg = newTag(Pager.class);

		pg.setId(id + "_pager_" + pos);
		pg.setCssClass("p-lv-pager");
		pg.setStart(pager.getStart());
		pg.setCount((long)listz);
		pg.setLimit(pager.getLimit());
		pg.setTotal(pager.getTotal());
		pg.setOnLinkClick("_plv_goto('" + id + "', #)");
		pg.setOnLimitChange("_plv_limit('" + id + "', this.value)");
		
		pg.start(writer);
		pg.end(writer, "");
	}
	
	private void writeListViewTools() throws IOException {
		write("<div id=\"");
		write(id);
		write("_tools\" class=\"p-lv-tools\">");
		
		writeListViewFilters();
		
		write("</div>");
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
		
		if (p) {
			write("<div class=\"p-lv-pager-header\">");
			writePager("header");
			write("</div>");
		}

		if (t) {
			write("<div class=\"p-lv-tools-header\">");
			writeCheckAllButton();
			write(tag.getTools());
			write("</div>");
		}

		if (a) {
			write("<div class=\"p-lv-addon-header\">");
			write(tag.getAddon());
			write("</div>");
		}

		write("</div>");
	}
	
	private void writeCheckAllButton() throws IOException {
		if (Attributes.isTrue(tag.getShowCheckAll(), true)) {
			for (ListColumn c : columns) {
				if ("check".equals(c.type)) {
					String textSelectAll =  getText("button-select-all");
					String textSelectNone =  getText("button-select-none");
					String iconSelectAll =  getText("icon-select-all");
					String iconSelectNone =  getText("icon-select-none");

					Attributes as = new Attributes();
					as.cssClass("p-lv-cab");
					as.data("textSelectAll", textSelectAll);
					as.data("textSelectNone", textSelectNone);
					as.data("iconSelectAll", iconSelectAll);
					as.data("iconSelectNone", iconSelectNone);
					as.add("onclick", "return _nlv_onAllClick(this);");
					
					write(button(textSelectAll, ticon(iconSelectAll), null, as.attrs()));
					break;
				}
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
		
		write("<div class=\"p-lv-toolbar" + uiClass("ui-widget-header") + "\">");
		write("<table class=\"p-lv-toolbar-tb\">");
		write("<tr>");
		
		if (p) {
			write("<td class=\"p-lv-td-pager\">");
			writePager("footer");
			write("</td>");
		}

		if (t) {
			write("<td class=\"p-lv-td-tools\">");
			write("<div class=\"p-lv-tools\">");
			writeCheckAllButton();
			write(tag.getTools());
			write("</div>");
			write("</td>");
		}

		if (a) {
			write("<td class=\"p-lv-td-addon\">");
			write("<div class=\"p-lv-addon\">");
			write(tag.getAddon());
			write("</div>");
			write("</td>");
		}

		write("</tr>");
		write("</table>");
		write("</div>");

		write("</div>");
	}

	@SuppressWarnings("unchecked")
	private void writeListViewFiltersItems(Map<String, ListColumn.Filter> fm) throws IOException {
		Map<String, String> stringFilterMap = tag.getStringFilterMap();
		Map<String, String> boolFilterMap = tag.getBoolFilterMap(); 
		Map<String, String> numberFilterMap = tag.getNumberFilterMap();
		Map<String, String> dateFilterMap = tag.getDateFilterMap();

		String _fs = tag.getQuery() + ".fs.";

		Map<String, List<String>> fieldErrors = context.getParamAware().getErrors();
		Map<String, Filter> qfs = query.getFilters();
		
		for (Entry<String, ListColumn.Filter> en : fm.entrySet()) {
			ListColumn.Filter _f = en.getValue();
			String _name = en.getKey();
			
			Filter qf = qfs.get(_name);
			
			String _fn = _fs + _name;
			String _ifn = id + "_fsf_" + _name;
			boolean _fd = _fsvl.contains(_name);

			write("<tr class=\"p-lv-input p-lv-fsi-" 
					+ html(_name) 
					+ (_fd ? "" : " p-hidden")
					+ "\">");

			boolean _hfe = false;
			if (fieldErrors != null) {
				for (Entry<String, List<String>> en2 : fieldErrors.entrySet()) {
					if (en2.getKey().startsWith(_fn + '.')) {
						_hfe = true;
						break;
					}
				}
			}

			write("<td class=\"p-lv-label\">");
			write("<label for=\"" + _ifn + "_v\" class=\"" + (_hfe ? "p-error" : "") + "\">");
			if (Strings.isNotEmpty(_f.label)) {
				write(html(_f.label));
				write(":");
			}
			write("</label></td>");

			write("<td class=\"p-lv-input\"");
			if (Strings.isNotEmpty(_f.tooltip)) {
				write("title=\"");
				write(html(_f.tooltip));
				write("\"");
			}
			write(">");

			String _fv;
			if ("string".equals(_f.type)) {
				_fv = _fn + ".sv";
				
				String _fvv = qf == null ? null : qf.getSv();
				writeTextField("form-control p-lv-f-string-v", _fv, _ifn + "_v", _fvv);
				
				_fvv = qf == null ? null : qf.getC();
				writeSelect("form-control p-lv-f-string-c", _fn + ".c", _ifn + "_c", stringFilterMap, _fvv);
			}
			else if ("boolean".equals(_f.type)) {
				_fv = _fn + ".bv";
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				write("<div class=\"p-checkboxlist\">");

				Boolean _fvv = qf == null ? null : qf.getBv();
				write("<span class=\"p-checkbox-item\">");
				Attributes ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "checkbox p-lv-f-boolean-true")
				  .add("name", _fv)
				  .add("id", _ifn + "_v")
				  .add("value", "true")
				  .addIfTrue("checked", _fvv);
				xtag("input", ia);
				write("<label class=\"p-checkbox-label\" for=\"" + _ifn + "_v\">");
				write(boolFilterMap.get("true"));
				write("</label></span>");
				
				_fv = _fn + ".bv2";
				_fvv = qf == null ? null : qf.getBv2();
				write("<span class=\"p-checkbox-item\">");
				ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "checkbox p-lv-f-boolean-false")
				  .add("name", _fv)
				  .add("id", _ifn + "_v2")
				  .add("value", "false")
				  .addIfTrue("checked", Boolean.FALSE.equals(_fvv));
				xtag("input", ia);
				write("<label class=\"p-checkbox-label\" for=\"" + _ifn + "_v2\">");
				write(boolFilterMap.get("false"));
				write("</label></span>");
				
				write("</div>");
			}
			else if ("number".equals(_f.type)) {
				_fv = _fn + ".nv";
				Number _fvv = qf == null ? null : qf.getNv();

				writeTextField("form-control p-lv-f-number-v", _fv, _ifn + "_v", _fvv);
				
				String _fvc = qf == null ? null : qf.getC();
				writeSelect("form-control p-lv-f-number-c", _fn + ".c", _ifn + "_c", numberFilterMap, _fvc);
				
				_fv = _fn + ".nv2";
				_fvv = qf == null ? null : qf.getNv2();
				writeTextField("form-control p-lv-f-number-v2", _fv, _ifn + "_v2", _fvv);
			}
			else if ("date".equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				writeDatePicker("form-control p-lv-f-date-v", _fv, _ifn + "_v", "date", _fvv);

				String _fvc = qf == null ? null : qf.getC();
				writeSelect("form-control p-lv-f-date-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				writeDatePicker("form-control p-lv-f-date-v2", _fv, _ifn + "_v2", "date", _fvv);
			}
			else if ("datetime".equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				writeDateTimePicker("form-control p-lv-f-datetime-v", _fv, _ifn + "_v", "datetime", _fvv);

				String _fvc = qf == null ? null : qf.getC();
				writeSelect("form-control p-lv-f-datetime-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				writeDateTimePicker("form-control p-lv-f-datetime-v2", _fv, _ifn + "_v2", "datetime", _fvv);
			}
			else if ("time".equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				writeTimePicker("form-control p-lv-f-time-v", _fv, _ifn + "_v", "time", _fvv);

				String _fvc = qf == null ? null : qf.getC();
				writeSelect("form-control p-lv-f-time-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				writeTimePicker("form-control p-lv-f-time-v2", _fv, _ifn + "_v2", "time", _fvv);
			}
			else if ("checklist".equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				
				_fv = _fn + ".svs";
				List<String> _fvv = qf == null ? null : qf.getSvs();
				writeCheckboxList("p-lv-f-checklist", _fv, _ifn + "_v", _f.list, _fvv);
			}
			else if ("radio".equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"eq\"/>");

				_fv = _fn + ".sv";
				String _fvv = qf == null ? null : qf.getSv();
				writeRadio("p-lv-f-checklist", _fv, _ifn + "_v", _f.list, _fvv);
			}
			else if ("select".equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"eq\"/>");
				
				_fv = _fn + ".sv";
				String _fvv = qf == null ? null : qf.getSv();
				writeSelect("form-control p-lv-f-select", _fv, _ifn + "_v", _f.list, _fvv, true);
			}
			
			if (Collections.isNotEmpty(fieldErrors)) {
				for (Entry<String, List<String>> fen : fieldErrors.entrySet()) {
					if (fen.getKey().startsWith(_fn + ".")) {
						write("<ul errorFor=\"" + html(_ifn) + "\" class=\"p-field-errors\">");
						for (String m : fen.getValue()) {
							write("<li class=\"p-field-error\">");
							write(icon("p-icon p-icon-error p-field-error"));
							write(html(m));
							write("</li>");
						}
						write("</ul>");
					}
				}
			}
			write("</td>");
			write("<td class=\"p-lv-remove\">");
			if (!_f.fixed) {
				write(icon("fa fa-minus-circle"));
			}
			write("</td>");
			write("</tr>");
		}
	}

	@SuppressWarnings("unchecked")
	private void writeListViewFiltersOptions(Map<String, ListColumn.Filter> fm) throws IOException {
		// get editable filters
		Map<String, ListColumn.Filter> fm2 = new LinkedHashMap<String, ListColumn.Filter>();
		for (Entry<String, ListColumn.Filter> en : fm.entrySet()) {
			String _name = en.getKey();
			ListColumn.Filter _f = en.getValue();
			if (!_f.fixed) {
				fm2.put(_name, _f);
			}
		}

		Map<String, String> filterMethodMap = (Map<String, String>)tag.getFilterMethodMap();
		if (Collections.isEmpty(fm2) && Collections.isEmpty(filterMethodMap)) {
			return;
		}

		write("<tr class=\"p-lv-filters-sep\"><td colspan=\"3\"><div></div></td></tr>");

		write("<tr class=\"p-lv-submit\">");
		write("<td class=\"p-lv-label\">");
		write(getText("listview-filters-label-method"));
		write(":</td>");
		write("<td class=\"p-lv-input\" colspan=\"2\">");
		
		write("<table class=\"p-lv-fs-tb-options\"><tbody><tr>");
		if (Collections.isNotEmpty(filterMethodMap)) {
			write("<td>");
			writeRadio("p-lv-f-method", tag.getQuery() + ".m", id + "_fsform_filterm", filterMethodMap, null);
			write("</td>");
		}
		if (Collections.isNotEmpty(fm2)) {
			write("<td>");
			write("<select id=\"" + id + "_fsform_fsadd" + "\"");
			write(" class=\"form-control p-lv-fs-select\" onclick=\"return false;\" onchange=\"_nlv_onAddFilter(this)\">");
			write("<option value=\"\">-- ");
			write(getText("listview-filters-label-add"));
			write(" --</option>");
	
			for (Entry<String, ListColumn.Filter> en : fm2.entrySet()) {
				String _name = en.getKey();
				ListColumn.Filter _f = en.getValue();
	
				boolean _fd = _fsvl.contains(_name);
	
				write("<option value=\"" + html(_name) + "\"");
				if (_fd) {
					write(" disabled");
				}
				write(">");
				write(html(_f.label));
				write("</option>");
			}
			write("</select>");
			write("</td>");
		}
		write("</tr></table>");

		write("</td></tr>");
	}
	
	private void writeListViewFiltersButtons() throws IOException {
		write("<tr class=\"p-lv-submit\">");
		write("<td class=\"p-lv-label\"></td>");
		write("<td class=\"p-lv-submit\" colspan=\"2\">");
		write(button(getText("listview-filters-button-query"), "icon-search"));
		write(' ');
		write(button(getText("listview-filters-button-clear"), "icon-clear", null, "return _nlv_onClearFilters(this);"));
		write("</td></tr>");
	}

	private void writeListViewFilters() throws IOException {
		Map<String, ListColumn.Filter> fm = new LinkedHashMap<String, ListColumn.Filter>();
		Map<String, List<String>> fieldErrors = context.getParamAware().getErrors();
		Map<String, Filter> qfs = query.getFilters();
		
		String _fs = tag.getQuery() + ".fs";
		for (ListColumn c : columns) {
			if (!c.filterable) {
				continue;
			}

			ListColumn.Filter of = c.filter;
			if (of != null && of.display) {
				fm.put(c.name, of);

				if (of.label == null) {
					of.label = c.header;
				}
				if (of.tooltip == null) {
					of.tooltip = c.tooltip;
				}
				
				String _fn = _fs + '.' + c.name;

				boolean _hfe = false;
				if (fieldErrors != null) {
					for (Entry<String, List<String>> en2 : fieldErrors.entrySet()) {
						if (en2.getKey().startsWith(_fn + '.')) {
							_hfe = true;
							break;
						}
					}
				}

				Filter qf = qfs.get(c.name);
				if (of.fixed || _hfe || (qf != null && Strings.isNotEmpty(qf.getC()))) {
					_fsvl.add(c.name);
				}

				if (qf == null) {
					continue;
				}
				
				boolean _fd = false;

				if ("string".equals(of.type)) {
					_fd = Strings.isNotEmpty(qf.getSv());
				}
				else if ("boolean".equals(of.type)) {
					_fd = qf.getBv() != null || qf.getBv2() != null;
				}
				else if ("number".equals(of.type)) {
					_fd = qf.getNv() != null || qf.getNv2() != null;
				}
				else if ("date".equals(of.type) || "datetime".equals(of.type) || "time".equals(of.type)) {
					_fd = qf.getDv() != null || qf.getDv2() != null;
				}
				else if ("checklist".equals(of.type)) {
					_fd = Collections.isNotEmpty(qf.getSvs());
				}
				else if ("radio".equals(of.type) || "select".equals(of.type)) {
					_fd = Strings.isNotEmpty(qf.getSv());
				}
				
				if (_fd) {
					_fsdl.add(c.name);
				}
			}
		}
		
		if (Collections.isEmpty(fm)) {
			return;
		}
		
		write("<fieldset class=\"p-lv-filters ui-collapsible");
		if (_fsvl.isEmpty()) {
			write(" ui-collapsed");
		}
		write("\" data-spy=\"fieldset\"><legend>");
		write(getText("listview-filters-caption"));
		write("</legend>");

		Form form = context.getIoc().get(Form.class);

		form.setId(id + "_fsform");
		form.setCssClass("p-lv-fsform");
		form.setAction(tag.getAction());
		form.setMethod(defs(tag.getMethod(), "get"));
		form.setOnsubmit(tag.getOnsubmit());
		form.setOnreset(tag.getOnreset());
		form.setLoadmask(false);
		if (_fsvl.isEmpty()) {
			form.setCssStyle("display: none");
		}

		form.start(writer);

		if (pager != null) {
			writeHidden(id + "_fsform_limit", tag.getPager() + ".l", pager.getLimit());
		}
		if (sorter != null) {
			writeHidden(id + "_fsform_sort", tag.getSorter() + ".c", sorter.getColumn());
			writeHidden(id + "_fsform_dir", tag.getSorter() + ".d", sorter.getDirection());
		}

		write("<table class=\"p-lv-filters-t\" cellspacing=\"0\"><tbody>");
		writeListViewFiltersItems(fm);
		writeListViewFiltersOptions(fm);
		writeListViewFiltersButtons();

		write("</tbody></table>");
		
		form.end(writer, "");
		
		write("</fieldset>");
	}

	private void writeListViewTableHeader() throws IOException {
		write("<thead class=\"p-lv-thead p-lv-chead\">");
		write("<tr class=\"p-lv-tr\">");

		for (ListColumn c : columns) {
			if (!c.display) {
				continue;
			}

			String ctype = defs(c.type, "column");
			
			Attributes tha = new Attributes();
			tha.add("column", c.name)
				.cssClass("p-lv-" + ctype 
					+ ("column".equals(ctype) ? " p-lv-cm-" + c.name : "")
					+ (tag.getSortable() && c.sortable ? " p-lv-sortable" + uiClass("p-sortable") : "")
					+ ((sorter != null && c.name.equals(sorter.getColumn())) ? " p-sorted p-lv-sort-" + sorter.getDirection() : "")
					+ (_fsdl.contains(c.name) ? " p-lv-filtered" : "")
					+ (c.hidden ? " p-lv-hidden" : ""))
				.title(tag);
			stag("th", tha);
			
			write("<div class=\"p-lv-cell\"");
			if (c.width > 0) {
				write(" style=\"width:" + c.width + ";\"");
			}
			write(">");

			if ("check".equals(ctype)) {
				if (c.header != null) {
					write(c.header);
				}
				else {
					String sa = getText("tooltip-select-all", "");
					String sn = getText("tooltip-select-none", "");
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
			
			if (sorter != null && c.name.equals(sorter.getColumn())) {
				write("<span class=\"ui-icon ui-icon-triangle-1-" 
						+ (Sorter.ASC.equals(sorter.getDirection()) ? 'n' : 's')
						+ " p-lv-sort-" + sorter.getDirection()
						+ "\"></span>");
			}
			else if (tag.getSortable() && c.sortable) {
				write("<span class=\"ui-icon ui-icon-triangle-2-n-s p-lv-sort-off\"></span>");
			}
			write("</div>");
			etag("th");
		}
		write("</tr></thead>");
	}

	private String getCssColumnClass(Object d) {
		StringBuilder ccc = new StringBuilder();//(StringUtils.isEmpty(_cc) ? "" : " p-lv-cc-" + _cc);
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
		
		write("<table id=\"" + id + "_table\" class=\"p-lv-table table\">");
		writeListViewTableHeader();
		
		if (listz > 0) {
			write("<tbody class=\"p-lv-tbody\">");

			int inx = 0;
			Object prev_d = null;
			
			for (Object d : list) {
				String oe = (inx % 2 == 0 ? "odd" : "even");
				write("<tr class=\"p-lv-tr p-lv-tr-" + oe + uiClass("p-tr-" + oe)
						+ getCssColumnClass(d)
						+ "\">");

				for (ListColumn c : columns) {
					if (!c.display) {
						continue;
					}

					String ctype = defs(c.type, "column");
					
					Attributes tda = new Attributes();
					tda.add("class", "p-lv-" + ctype 
							   + ("column".equals(ctype) ? " p-lv-cm-" + c.name : "")
							   + (c.hidden ? " p-lv-hidden" : "")
							   + (c.nowrap ? " p-lv-nowrap" : ""));
					stag(c.fixed ? "th" : "td", tda);
					
					write("<div class=\"p-lv-cell" + (c.nowrap ? " p-lv-nowrap\"" : "\""));
					
					if (c.width > 0) {
						write(" style=\"width:" + c.width + ";\"");
					}
					write(">");

					if ("rownum".equals(ctype)) {
						write(String.valueOf(inx + 1));
					}
					else if ("number".equals(ctype)) {
						write(String.valueOf(pager == null ? 0 : pager.getStart() + inx + 1));
					}
					else if ("check".equals(ctype)) {
						write("<input type=\"checkbox\" class=\"checkbox p-lv-cb\" value=\"" + inx + "\"/>");
					}
					else if ("actions".equals(ctype)) {
						if (Collections.isNotEmpty(c.actions)) {
							for (ListColumn.Action a : c.actions) {
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
							ha.add("type", "hidden")
							  .add("class", "p-lv-cv" + (c.pkey ? " p-lv-ck" : ""))
							  .add("name", listn + "[" + inx + "]." + c.name)
							  .addIfTrue("disabled", !c.enabled)
							  .add("value", escapeValue(getBeanProperty(d, c.name)), false);
							xtag("input", ha);
						}
						
						if (!(c.group && prev_d != null 
								&& defs(getBeanProperty(prev_d, c.name)).equals(defs(getBeanProperty(d, c.name))))) {
							
							boolean wa = false;
							
							if (c.link instanceof Boolean && ((Boolean)c.link).booleanValue()) {
								c.link = tag.getLink();
							}
							if (c.link instanceof ListColumn.Action) {
								wa = writeAlink("p-lv-a", d, (ListColumn.Action)c.link);
							}

							if (c.format != null) {
								if ("code".equals(c.format.type)) {
									Object v = getBeanProperty(d, c.name);
									Iterator iv = Iterators.asIterator(v);
									if (iv != null) {
										Map codemap = getCodeMap(c.format.codemap);
										while (iv.hasNext()) {
											writeCodeText(codemap, iv.next());
											if (iv.hasNext()) {
												write(" ");
											}
										}
									}
								}
								else if ("expression".equals(c.format.type)) {
									Object v = tag.findValue(c.format.expression, d);
									if (v != null) {
										String escape = c.format.escape;
										write(escapeValue(v, escape));
									}
								}
								else {
									Object v = getBeanProperty(d, c.name);
									if (v != null) {
										Property p = newTag(Property.class);
										p.setValue(v);
										p.setFormat(c.format.type);
										write(p.formatValue());
									}
								}
							}

							if (wa) {
								write("</a>");
							}
						}
					}
					
					write("</div>");
					etag(c.fixed ? "th" : "td");
				}
				
				write("</tr>");
				prev_d = d;
				
				inx++;
			}
			write("</tbody>");
		}
		
		write("</table>");

		writeJsc("$(function() {"
				+ "_plv_init_table('" + jsstr(id) + "', {"
				+ "autosize: " + tag.isAutosize()
				+ "}); });");

		form.end(writer, "");
		
		write("</div>");
	}
	
	private void writeLinkUrl(ListColumn.Action link, Object d) throws IOException {
		CUrl url = newTag(CUrl.class);

		url.copyParams(link.params, d);

		url.start(writer);
		url.end(writer, "");
	}
	
	private boolean writeAlink(String cls, Object d, ListColumn.Action link) throws IOException {
		if (_permit != null) {
			if (Strings.isNotEmpty(link.action)) {
				if (!_permit.hasDataPermission(d, link.action)) {
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
			write(xicon(link.icon + " p-lv-i"));
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

	private void writeCodeText(Map cm, Object k) throws IOException {
		Object v = cm.get(k);
		if (v == null && k != null) {
			v = cm.get(k.toString());
		}
		write(v == null ? null : v.toString());
	}
}
