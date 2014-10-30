package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.net.URL;
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
import panda.lang.Strings;
import panda.mvc.bean.Sorter;
import panda.mvc.util.PermissionProvider;
import panda.mvc.view.tag.ListColumn;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ListViewRenderer extends AbstractTagRenderer<ListView> {

	private String id;

	private panda.mvc.bean.Pager pager;
	private Sorter sorter;

	private List<ListColumn> columns;
	private String[] cssColumns;
	private String listn;
	private int listz;
	
	private Set<String> _fsdl = new HashSet<String>();
	private Set<String> _fsvl = new HashSet<String>();
	
	private PermissionProvider _permit;
	
	private Map<String, Map> codemaps = new HashMap<String, Map>();

	public ListViewRenderer(RenderingContext context) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	private void initVars() {
		_permit = context.getAction() instanceof PermissionProvider ? (PermissionProvider)context.getAction() : null;

		id = tag.getId();

		if (Strings.isNotEmpty(tag.getPager())) {
			pager = (panda.mvc.bean.Pager)context.getParameter(tag.getPager());
		}

		if (Strings.isNotEmpty(tag.getSorter())) {
			sorter = (Sorter)context.getParameter(tag.getSorter());
		}
		
		listn = tag.getList();

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

	@SuppressWarnings("unchecked")
	private void writeListViewForm() throws IOException {
		Form form = new Form(stack,
				StrutsContextUtils.getServletRequest(),
				StrutsContextUtils.getServletResponse());

		StrutsContextUtils.getContainer(stack).inject(form);

		form.setId(id + "_form");
		form.setCssClass("p-lv-form");
		form.setAction(tag.get("action"));
		form.setMethod(defs(tag.get("method"), "get"));
		form.setOnsubmit(tag.get("onsubmit"));
		form.setOnreset(tag.get("onreset"));
		form.setLoadmask("false");

		form.start(writer);

		writeListViewHiddens();
		
		String _fs = qfilters;
		if (Strings.isNotEmpty(_fs)) {
			Map<String, Object> _fsm = (Map)findValue(_fs);
			if (Collections.isNotEmpty(_fsm)) {
				for (Entry<String, Object> en : _fsm.entrySet()) {
					writeHidden(id + "_fs_" + en.getKey() + "_c",
							_fs + "." + en.getKey() + ".c");
					String t = defs(findString(_fs + "." + en.getKey() + ".t"), "s");
					Object vs = findValue(_fs + "." + en.getKey() + ".vs");
					if (vs != null) {
						int i = 0;
						String d = id + "_fs_" + en.getKey() + "_v_" + i;
						String n = _fs + "." + en.getKey() + "." + t + "vs";
						for (Object v : XIterators.asIterable(vs)) {
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

	@SuppressWarnings("unchecked")
	private <T> T getBeanProperty(Object bean, String name, T defv) {
		T v = null;
		try {
			v = (T)Beans.getBean(bean, name);
		}
		catch (Exception e) {
		}
		return v == null ? defv : v;
	}

	private void writePager(String pos) throws IOException {
		Pager pager = new Pager(stack,
				StrutsContextUtils.getServletRequest(),
				StrutsContextUtils.getServletResponse());

		StrutsContextUtils.getContainer(stack).inject(pager);
		
		pager.setId(id + "_pager_" + pos);
		pager.setCssClass("p-lv-pager");
		pager.setStart(start);
		pager.setCount(listz);
		pager.setLimit(limit);
		pager.setTotal(total);
		pager.setOnLinkClick("_plv_goto('" + id + "', #)");
		pager.setOnLimitChange("_plv_limit('" + id + "', this.value)");
		
		pager.start(writer);
		pager.end(writer, "");
	}
	
	private void writeListViewTools() throws IOException {
		write("<div id=\"");
		write(id);
		write("_tools\" class=\"p-lv-tools\">");
		
		writeListViewFilters();
		
		write("</div>");
	}
	
	private void writeListViewHeader() throws IOException {
		boolean pager = Attributes.isTrue(tag.get("headPager"), false);
		String tools = tag.get("headTools");
		String addon = tag.get("headAddon");

		if (!pager && Strings.isEmpty(tools) && Strings.isEmpty(addon)) {
			return;
		}

		write("<div id=\"");
		write(ids);
		write("_header\" class=\"p-lv-header\">");
		
		if (pager) {
			write("<div class=\"p-lv-pager-header\">");
			writePager("header");
			write("</div>");
		}

		if (Strings.isNotEmpty(tools)) {
			write("<div class=\"p-lv-tools-header\">");
			writeCheckAllButton();
			write(tools);
			write("</div>");
		}

		if (Strings.isNotEmpty(addon)) {
			write("<div class=\"p-lv-addon-header\">");
			write(addon);
			write("</div>");
		}

		write("</div>");
	}
	
	private void writeCheckAllButton() throws IOException {
		if (Attributes.isTrue(tag.get("checkAllToolButton"), true)) {
			for (Object c : XIterators.asIterable(columns)) {
				String ctype = (String)getBeanProperty(c, "type");
				if ("check".equals(ctype)) {
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
		boolean pager = Attributes.isTrue(tag.get("footPager"), false);
		String tools = tag.get("footTools");
		String addon = tag.get("footAddon");

		if (!pager && Strings.isEmpty(tools) && Strings.isEmpty(addon)) {
			return;
		}

		write("<div id=\"");
		write(ids);
		write("_footer\" class=\"p-lv-footer\">");
		
		write("<div class=\"p-lv-toolbar" + uiClass("ui-widget-header") + "\">");
		write("<table class=\"p-lv-toolbar-tb\">");
		write("<tr>");
		
		if (pager) {
			write("<td class=\"p-lv-td-pager\">");
			writePager("footer");
			write("</td>");
		}

		if (Strings.isNotEmpty(tools)) {
			write("<td class=\"p-lv-td-tools\">");
			write("<div class=\"p-lv-tools\">");
			writeCheckAllButton();
			write(tools);
			write("</div>");
			write("</td>");
		}

		if (Strings.isNotEmpty(addon)) {
			write("<td class=\"p-lv-td-addon\">");
			write("<div class=\"p-lv-addon\">");
			write(addon);
			write("</div>");
			write("</td>");
		}

		write("</tr>");
		write("</table>");
		write("</div>");

		write("</div>");
	}

	@SuppressWarnings("unchecked")
	private void writeListViewFiltersItems(Map<String, Filter> fm) throws IOException {
		ListView tag = (ListView)context.getTag();

		Map<String, String> stringFilterMap = tag.getStringFilterMap();
		Map<String, String> boolFilterMap = tag.getBoolFilterMap(); 
		Map<String, String> numberFilterMap = tag.getNumberFilterMap();
		Map<String, String> dateFilterMap = tag.getDateFilterMap();

		String _fs = tag.get("filters");

		for (Entry<String, Filter> en : fm.entrySet()) {
			Filter _f = en.getValue();
			String _name = en.getKey();
			String _fn = _fs + '.' + _name;
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

			String _fv, _fvv;
			if ("string".equals(_f.type)) {
				_fv = _fn + ".sv";
				_fvv = "%{" + _fn + ".sv}";
				writeTextField("form-control p-lv-f-string-v",
						_fv, _ifn + "_v", _fvv);
				
				_fvv = "%{" + _fn + ".c}";
				
				writeSelect("form-control p-lv-f-string-c",
						_fn + ".c", _ifn + "_c", 
						stringFilterMap, _fvv);
			}
			else if ("boolean".equals(_f.type)) {
				_fv = _fn + ".bv";
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				write("<div class=\"p-checkboxlist\">");

				write("<span class=\"p-checkbox-item\">");
				Attributes ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "checkbox p-lv-f-boolean-true")
				  .add("name", _fv)
				  .add("id", _ifn + "_v")
				  .add("value", "true")
				  .addIfTrue("checked", findValue(_fv));
				xtag("input", ia);
				write("<label class=\"p-checkbox-label\" for=\"" + _ifn + "_v\">");
				write(boolFilterMap.get("true"));
				write("</label></span>");
				
				_fv = _fn + ".bv2";
				write("<span class=\"p-checkbox-item\">");
				ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "checkbox p-lv-f-boolean-false")
				  .add("name", _fv)
				  .add("id", _ifn + "_v2")
				  .add("value", "false")
				  .addIfTrue("checked", "false".equals(findValue(_fv)));
				xtag("input", ia);
				write("<label class=\"p-checkbox-label\" for=\"" + _ifn + "_v2\">");
				write(boolFilterMap.get("false"));
				write("</label></span>");
				
				write("</div>");
			}
			else if ("number".equals(_f.type)) {
				_fv = _fn + ".nv";
				_fvv = "%{" + _fn + ".nv}";

				writeTextField("form-control p-lv-f-number-v",
						_fv, _ifn + "_v", _fvv);
				
				_fvv = "%{" + _fn + ".c}";
				writeSelect("form-control p-lv-f-number-c",
						_fn + ".c", _ifn + "_c", 
						numberFilterMap, _fvv);
				
				_fv = _fn + ".nv2";
				_fvv = "%{" + _fn + ".nv2}";
				writeTextField("form-control p-lv-f-number-v2",
						_fv, _ifn + "_v2", _fvv);
			}
			else if ("date".equals(_f.type)) {
				_fv = _fn + ".dv";
				_fvv = "%{" + _fn + ".dv}";

				writeDatePicker("form-control p-lv-f-date-v",
						_fv, _ifn + "_v", "date", _fvv);

				_fvv = "%{" + _fn + ".c}";
				writeSelect("form-control p-lv-f-date-c",
						_fn + ".c", _ifn + "_c", 
						dateFilterMap, _fvv);
				
				_fv = _fn + ".dv2";
				_fvv = "%{" + _fn + ".dv2}";
				writeDatePicker("form-control p-lv-f-date-v2",
						_fv, _ifn + "_v2", "date", _fvv);
			}
			else if ("datetime".equals(_f.type)) {
				_fv = _fn + ".dv";
				_fvv = "%{" + _fn + ".dv}";

				writeDateTimePicker("form-control p-lv-f-datetime-v",
						_fv, _ifn + "_v", "datetime", _fvv);

				_fvv = "%{" + _fn + ".c}";
				writeSelect("form-control p-lv-f-datetime-c",
						_fn + ".c", _ifn + "_c", 
						dateFilterMap, _fvv);
				
				_fv = _fn + ".dv2";
				_fvv = "%{" + _fn + ".dv2}";
				writeDateTimePicker("form-control p-lv-f-datetime-v2",
						_fv, _ifn + "_v2", "datetime", _fvv);
			}
			else if ("time".equals(_f.type)) {
				_fv = _fn + ".dv";
				_fvv = "%{" + _fn + ".dv}";

				writeTimePicker("form-control p-lv-f-time-v",
						_fv, _ifn + "_v", "time", _fvv);

				_fvv = "%{" + _fn + ".c}";
				writeSelect("form-control p-lv-f-time-c",
						_fn + ".c", _ifn + "_c", 
						dateFilterMap, _fvv);
				
				_fv = _fn + ".dv2";
				_fvv = "%{" + _fn + ".dv2}";
				writeTimePicker("form-control p-lv-f-time-v2",
						_fv, _ifn + "_v2", "time", _fvv);
			}
			else if ("checklist".equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				
				_fv = _fn + ".svs";
				_fvv = "%{" + _fn + ".svs}";
				writeCheckboxList("p-lv-f-checklist", 
						_fv, _ifn + "_v", _f.list, _fvv);
			}
			else if ("radio".equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"eq\"/>");

				_fv = _fn + ".sv";
				_fvv = "%{" + _fn + ".sv}";
				writeRadio("p-lv-f-checklist", 
						_fv, _ifn + "_v", _f.list, _fvv);
			}
			else if ("select".equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"eq\"/>");
				
				_fv = _fn + ".sv";
				_fvv = "%{" + _fn + ".sv}";
				writeSelect("form-control p-lv-f-select",
						_fv, _ifn + "_v", 
						_f.list, _fvv, 
						true);
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
	private void writeListViewFiltersOptions(Map<String, Filter> fm) throws IOException {
		// get editable filters
		Map<String, Filter> fm2 = new LinkedHashMap<String, Filter>();
		for (Entry<String, Filter> en : fm.entrySet()) {
			String _name = en.getKey();
			Filter _f = en.getValue();
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
	
			for (Entry<String, Filter> en : fm2.entrySet()) {
				String _name = en.getKey();
				Filter _f = en.getValue();
	
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

	private static class Filter {
		String type;
		boolean fixed = false;
		String label;
		String tooltip;
		Object list;
	}
	
	private void writeListViewFilters() throws IOException {
		Map<String, Filter> fm = new LinkedHashMap<String, Filter>();
		
		String _fs = tag.get("filters");
		for (Object c : XIterators.asIterable(columns)) {
			boolean filterable = getBeanProperty(c, "filterable", true);
			if (filterable) {
				Filter of = new Filter();
				Object filter = getBeanProperty(c, "filter");
				if (filter != null && !Boolean.FALSE.equals(getBeanProperty(filter, "display"))) {
					String cname = (String)getBeanProperty(c, "name");
					fm.put(cname, of);

					of.type = (String)getBeanProperty(filter, "type");
					of.fixed = Boolean.TRUE.equals(getBeanProperty(filter, "fixed"));
					of.label = (String)getBeanProperty(filter, "label");
					if (of.label == null) {
						of.label = (String)getBeanProperty(c, "header");
					}
					of.tooltip = (String)getBeanProperty(filter, "tooltip");
					if (of.tooltip == null) {
						of.tooltip = (String)getBeanProperty(c, "tooltip");
					}
					of.list = getBeanProperty(filter, "list");
					
					String _fn = _fs + '.' + cname;

					boolean _hfe = false;
					if (fieldErrors != null) {
						for (Entry<String, List<String>> en2 : fieldErrors.entrySet()) {
							if (en2.getKey().startsWith(_fn + '.')) {
								_hfe = true;
								break;
							}
						}
					}
					if (of.fixed || _hfe || Strings.isNotEmpty((String)findValue(_fn + ".c", String.class))) {
						_fsvl.add(cname);
					}

					
					boolean _fd = false;

					String _fv, _fv2;
					if ("string".equals(of.type)) {
						_fv = _fn + ".sv";
						_fd = Strings.isNotEmpty((String)findValue(_fv, String.class));
					}
					else if ("boolean".equals(of.type)) {
						_fv = _fn + ".bv";
						_fv2 = _fn + ".bv2";
						_fd = findValue(_fv) != null || findValue(_fv2) != null;
					}
					else if ("number".equals(of.type)) {
						_fv = _fn + ".nv";
						_fv2 = _fn + ".nv2";
						_fd = findValue(_fv) != null || findValue(_fv2) != null;
					}
					else if ("date".equals(of.type)
							|| "datetime".equals(of.type)
							|| "time".equals(of.type)) {
						_fv = _fn + ".dv";
						_fv2 = _fn + ".dv2";
						_fd = findValue(_fv) != null || findValue(_fv2) != null;
					}
					else if ("checklist".equals(of.type)) {
						_fv = _fn + ".svs";
						_fd = Collections.isNotEmpty((List)findValue(_fv));
					}
					else if ("radio".equals(of.type)
							|| "select".equals(of.type)) {
						_fv = _fn + ".sv";
						_fd = Strings.isNotEmpty((String)findValue(_fv, String.class));
					}
					
					if (_fd) {
						_fsdl.add(cname);
					}
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

		if (Strings.isNotEmpty(sort)) {
			writeHidden(id + "_fsform_sort", sort, _sort);
		}
		if (Strings.isNotEmpty(dir)) {
			writeHidden(id + "_fsform_dir", dir, _dir);
		}
		if (Strings.isNotEmpty(limit)) {
			writeHidden(id + "_fsform_limit", limit, findString(limit));
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

		for (Object c : XIterators.asIterable(columns)) {
			boolean display = getBeanProperty(c, "display", true);
			if (!display) {
				continue;
			}

			String cname = (String)getBeanProperty(c, "name");
			String ctype = defs((String)getBeanProperty(c, "type"), "column");
			boolean csortable = getBeanProperty(c, "sortable", false);
			boolean chidden = getBeanProperty(c, "hidden", false);
			Object cwidth = getBeanProperty(c, "width");
			String cheader = (String)getBeanProperty(c, "header");
			
			Attributes tha = new Attributes();
			tha.add("column", cname)
				.cssClass("p-lv-" + ctype 
					+ ("column".equals(ctype) ? " p-lv-cm-" + cname : "")
					+ (sortable && csortable ? " p-lv-sortable" + uiClass("p-sortable") : "")
					+ (cname.equals(_sort) ? " p-sorted p-lv-sort-" + _dir : "")
					+ (_fsdl.contains(cname) ? " p-lv-filtered" : "")
					+ (chidden ? " p-lv-hidden" : ""))
				.title(tag);
			stag("th", tha);
			
			write("<div class=\"p-lv-cell\"");
			if (cwidth != null) {
				write(" style=\"width:" + cwidth + ";\"");
			}
			write(">");

			if ("check".equals(ctype)) {
				if (cheader != null) {
					write(cheader);
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
				write(cheader);
			}
			
			if (cname.equals(_sort)) {
				write("<span class=\"ui-icon ui-icon-triangle-1-" 
						+ ("asc".equals(_dir) ? 'n' : 's')
						+ " p-lv-sort-" + _dir
						+ "\"></span>");
			}
			else if (sortable && csortable) {
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
		write("<div id=\"" + ids + "_body\" class=\"p-lv-body\">");
		
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
		
		write("<table id=\"" + ids + "_table\" class=\"p-lv-table table\">");
		writeListViewTableHeader();
		
		Iterator list = XIterators.asIterator(tag.get("list"));
		if (list != null && list.hasNext()) {
			write("<tbody class=\"p-lv-tbody\">");

			int inx = 0;
			Object prev_d = null;
			
			while (list.hasNext()) {
				Object d = list.next();

				String oe = (inx % 2 == 0 ? "odd" : "even");
				write("<tr class=\"p-lv-tr p-lv-tr-" + oe + uiClass("p-tr-" + oe)
						+ getCssColumnClass(d)
						+ "\">");

				for (Object c : XIterators.asIterable(columns)) {
					boolean display = getBeanProperty(c, "display", true);
					if (!display) {
						continue;
					}

					String cname = (String)getBeanProperty(c, "name");
					String ctype = defs((String)getBeanProperty(c, "type"), "column");
					boolean cfixed = getBeanProperty(c, "fixed", false);
					boolean chidden = getBeanProperty(c, "hidden", false);
					boolean cnowrap = getBeanProperty(c, "nowrap", false);
					boolean cgroup = getBeanProperty(c, "group", false);
					boolean cvalue = getBeanProperty(c, "value", true);
					boolean cpkey = getBeanProperty(c, "pkey", false);
					boolean cenabled = getBeanProperty(c, "enabled", false);
					Object cwidth = getBeanProperty(c, "width");
					
					Attributes tda = new Attributes();
					tda.add("class", "p-lv-" + ctype 
							   + ("column".equals(ctype) ? " p-lv-cm-" + cname : "")
							   + (chidden ? " p-lv-hidden" : "")
							   + (cnowrap ? " p-lv-nowrap" : ""));
					stag(cfixed ? "th" : "td", tda);
					
					write("<div class=\"p-lv-cell" + (cnowrap ? " p-lv-nowrap\"" : "\""));
					
					if (cwidth != null) {
						write(" style=\"width:" + cwidth + ";\"");
					}
					write(">");

					if ("rownum".equals(ctype)) {
						write(String.valueOf(inx + 1));
					}
					else if ("number".equals(ctype)) {
						write(String.valueOf(_start + inx + 1));
					}
					else if ("check".equals(ctype)) {
						write("<input type=\"checkbox\" class=\"checkbox p-lv-cb\" value=\"" + inx + "\"/>");
					}
					else if ("actions".equals(ctype)) {
						for (Object a : XIterators.asIterable(getBeanProperty(c, "actions"))) {
							if (writeAlink("p-lv-ia", d, a)) {
								String label = (String)getBeanProperty(a, "label");
								if (Strings.isNotEmpty(label)) {
									write("<span class=\"p-lv-t\">");
									write(label);
									write("</span>");
								}
								write("</a>");
							}
						}
					}
					else {
						if (cvalue) {
							Attributes ha = new Attributes();
							ha.add("type", "hidden")
							  .add("class", "p-lv-cv" + (cpkey ? " p-lv-ck" : ""))
							  .add("name", listn + "[" + inx + "]." + cname)
							  .addIfTrue("disabled", !cenabled)
							  .add("value", escapeValue(getBeanProperty(d, cname)), false);
							xtag("input", ha);
						}
						
						if (!(cgroup && prev_d != null 
								&& defs(getBeanProperty(prev_d, cname)).equals(defs(getBeanProperty(d, cname))))) {
							Object clink = getBeanProperty(c, "link");
							if (clink instanceof Boolean && ((Boolean)clink).booleanValue()) {
								clink = _link;
							}
							if (clink instanceof Map) {
								writeAlink("p-lv-a", d, clink);
							}

							String cformat = null;

							Object oFormat = getBeanProperty(c, "format");
							if (oFormat != null) {
								if (oFormat instanceof String) {
									cformat = (String)oFormat;
								}
								else {
									cformat = (String)getBeanProperty(oFormat, "type");
								}
							}

							if ("code".equals(cformat)) {
								Object v = getBeanProperty(d, cname);
								Iterator iv = XIterators.asIterator(v);
								if (iv != null) {
									Object codemap = getBeanProperty(oFormat, "codemap");
									while (iv.hasNext()) {
										writeCodeText(codemap, iv.next());
										if (iv.hasNext()) {
											write(" ");
										}
									}
								}
							}
							else if ("expression".equals(cformat)) {
								try {
									stack.push(d);
									Object exp = getBeanProperty(oFormat, "expression");
									if (exp instanceof String) {
										Object v = findValue((String)exp);
										if (v != null) {
											String escape = (String)getBeanProperty(oFormat, "escape");
											write(escapeValue(v, escape));
										}
									}
								}
								finally {
									stack.pop();
								}
							}
							else {
								try {
									stack.push(d);
									Property p = new Property(stack);
									try {
										p.setName(cname);
										p.setFormat(cformat);
										write(p.formatValue());
									}
									finally {
										p.getComponentStack().pop();
									}
								}
								finally {
									stack.pop();
								}
							}

							if (clink instanceof Map) {
								write("</a>");
							}
						}
					}
					
					write("</div>");
					etag(cfixed ? "th" : "td");
				}
				
				write("</tr>");
				prev_d = d;
				
				inx++;
			}
			write("</tbody>");
		}
		
		write("</table>");

		writeJsc("$(function() {"
				+ "_nlv_init_table('" + jsstr(id) + "', {"
				+ "autosize: " + tag.isAutosize()
				+ "}); });");

		form.end(writer, "");
		
		write("</div>");
	}
	
	@SuppressWarnings("unchecked")
	private void writeLinkUrl(Object link) throws IOException {
		URL url = new URL(stack,
				StrutsContextUtils.getServletRequest(),
				StrutsContextUtils.getServletResponse());

		StrutsContextUtils.getContainer(stack).inject(url);
		
		url.setAction((String)getBeanProperty(link, "action"));
		url.setNamespace((String)getBeanProperty(link, "namespace"));
		url.setValue((String)getBeanProperty(link, "url"));
		
		Map<String, String> tag = (Map<String, String>)getBeanProperty(link, "tag");
		if (tag == null) {
			tag = (Map<String, String>)getBeanProperty(_link, "tag");
		}
		if (tag != null) {
			for (Entry<String, String> en : tag.entrySet()) {
				url.addParameter(en.getKey(), findValue(en.getValue()));
			}
		}

		url.start(writer);
		url.end(writer, "");
	}
	
	private boolean writeAlink(String cls, Object d, Object link) throws IOException {
		try {
			stack.push(d);
			
			if (_permit != null) {
				String action = (String)getBeanProperty(link, "action");
				if (Strings.isNotEmpty(action)) {
					if (!_permit.hasDataPermission(d, action)) {
						return false;
					}
				}
			}
			
			String icon = (String)getBeanProperty(link, "icon");
			String target = (String)getBeanProperty(link, "target");
			String tooltip = (String)getBeanProperty(link, "tooltip");
			String href = (String)getBeanProperty(link, "href");
			String onclick = (String)getBeanProperty(link, "onclick");

			write("<a class=\"");
			write(cls);
			write("\" href=\"");
			if (Strings.isNotEmpty(href)) {
				write(href);
			}
			else {
				writeLinkUrl(link);
			}
			write("\"");
			if (Strings.isNotEmpty(target)) {
				write(" target=\"" + html(target) + "\"");
			}
			if (Strings.isNotEmpty(tooltip)) {
				write(" title=\"" + html(tooltip) + "\"");
			}
			if (Strings.isNotEmpty(onclick)) {
				write(" onclick=\"" + (onclick) + "\"");
			}
			write(">");
			
			if (Strings.isNotEmpty(icon)) {
				write(xicon(icon + " p-lv-i"));
			}
			
			return true;
		}
		finally {
			stack.pop();
		}
	}
	
	private void writeCodeText(Object cm, Object k) throws IOException {
		if (cm instanceof String) {
			Map m = codemaps.get(cm);
			if (m == null) {
				m = (Map)findValue((String)cm, Map.class);
				codemaps.put((String)cm, m);
			}
			cm = m;
		}

		if (cm instanceof Map) {
			Object v = ((Map)cm).get(k);
			if (v == null && k != null) {
				v = ((Map)cm).get(k.toString());
			}
			write(v == null ? null : v.toString());
		}
	}
}
