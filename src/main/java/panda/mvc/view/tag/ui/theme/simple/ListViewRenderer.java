package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
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
import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Iterators;
import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Queryer;
import panda.mvc.util.PermissionProvider;
import panda.mvc.view.tag.CUrl;
import panda.mvc.view.tag.Escapes;
import panda.mvc.view.tag.ListColumn;
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
	
	// filters input/fixed list
	private Set<String> fsinputs = new HashSet<String>();
	
	// has input filter
	private boolean fsinput = false;
	
	// has fixed filter
	private boolean fsfixed = false;
	
	private PermissionProvider permit;
	
	private Map<String, Map> codemaps = new HashMap<String, Map>();

	public ListViewRenderer(RenderingContext context) {
		super(context);
	}

	private void initVars() {
		permit = context.getAction() instanceof PermissionProvider ? (PermissionProvider)context.getAction() : null;

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
	}

	public void render() throws IOException {
		initVars();
		
		Attributes a = new Attributes();
		
		a.add("id", id)
		 .cssClass(tag, "p-lv")
		 .data("onrowclick", tag.getOnrowclick())
		 .cssStyle(tag);
		if (tag.isSingleSelect()) {
			a.data("singleSelect", "true");
		}
		if (tag.isUntoggleSelect()) {
			a.data("untoggleSelect", "true");
		}
		if (tag.isAutosize()) {
			a.data("autosize", "true");
		}
		stag("div", a);
		
		writeListViewForm();

		writeListViewQueryer();
		
		writeListViewHeader();
		
		writeListViewTable();

		writeListViewFooter();
		
		etag("div");
	}

	protected void writeJsc(String js) throws IOException {
		if (tag.isScript()) {
			super.writeJsc(js);
		}
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

	private void writeHidden(String id, String name, Object value) throws IOException {
		writeHidden(id, name, value, false);
	}
	
	private void writeHidden(String id, String name, Object value, boolean disabled) throws IOException {
		Attributes ha = new Attributes();
		ha.type("hidden")
		  .id(id)
		  .name(name)
		  .value(value)
		  .disabled(disabled);
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

	private boolean isPagerLimitSelective() {
		Pager pg = createPager("");
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
	
	private void writeListViewQueryer() throws IOException {
		write("<div id=\"");
		write(id);
		write("_queryer\" class=\"p-lv-queryer\">");
		
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

	@SuppressWarnings("unchecked")
	private void writeListViewFiltersItems(Map<String, ListColumn.Filter> fm) throws IOException {
		Map<String, String> stringFilterMap = tag.getStringFilterMap();
		Map<String, String> boolFilterMap = tag.getBoolFilterMap(); 
		Map<String, String> numberFilterMap = tag.getNumberFilterMap();
		Map<String, String> dateFilterMap = tag.getDateFilterMap();

		String _pf = prefix + "f.";

		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		Map<String, Filter> qfs = (queryer == null ? null : queryer.getFilters());
		
		for (Entry<String, ListColumn.Filter> en : fm.entrySet()) {
			ListColumn.Filter _f = en.getValue();
			String _name = en.getKey();
			
			Filter qf = qfs == null ? null : qfs.get(_name);
			
			String _fn = _pf + _name;
			String _ifn = id + "_fsf_" + _name;
			boolean _fd = fsinputs.contains(_name);

			write("<div class=\"p-lv-fsi-" 
					+ html(_name) 
					+ (_fd ? "" : " p-hidden")
					+ " form-group\">");

			boolean _hfe = false;
			if (fieldErrors != null) {
				for (Entry<String, List<String>> en2 : fieldErrors.entrySet()) {
					if (en2.getKey().startsWith(_fn + '.')) {
						_hfe = true;
						break;
					}
				}
			}

			if (!_f.fixed) {
				write(icon("p-lv-fs-remove fa fa-minus-circle"));
			}

			write("<label for=\"" + _ifn + "_v\" class=\"");
			write(tag.getCssFiltersLabel());
			write(" control-label " + (_hfe ? "p-error" : "") + "\">");
			if (Strings.isNotEmpty(_f.label)) {
				write(html(_f.label));
				write(":");
			}
			write("</label>");

			write("<div class=\"");
			write(tag.getCssFiltersInput());
			write(" p-lv-fs-inputgroup\"");
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
				writeTextField("form-control p-lv-f-string-v", _fv, _ifn + "_v", _fvv, null);
				
				_fvv = qf == null ? null : qf.getC();
				writeSelect("form-control p-lv-f-string-c", _fn + ".c", _ifn + "_c", stringFilterMap, _fvv);
			}
			else if ("boolean".equals(_f.type)) {
				_fv = _fn + ".bv";
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				write("<div class=\"p-checkboxlist\">");

				Boolean _fvv = qf == null ? null : qf.getBv();
				write("<label class=\"checkbox-inline\">");
				Attributes ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "p-lv-f-boolean-true")
				  .add("name", _fv)
				  .add("id", _ifn + "_v")
				  .add("value", "true")
				  .addIfTrue("checked", _fvv);
				xtag("input", ia);
				write(boolFilterMap.get("true"));
				write("</label>");
				
				_fv = _fn + ".bv2";
				_fvv = qf == null ? null : qf.getBv2();
				write("<label class=\"checkbox-inline\">");
				ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "p-lv-f-boolean-false")
				  .add("name", _fv)
				  .add("id", _ifn + "_v2")
				  .add("value", "false")
				  .addIfTrue("checked", Boolean.FALSE.equals(_fvv));
				xtag("input", ia);
				write(boolFilterMap.get("false"));
				write("</label>");
				
				write("</div>");
			}
			else if ("number".equals(_f.type)) {
				_fv = _fn + ".nv";
				Number _fvv = qf == null ? null : qf.getNv();

				write("<div class=\"p-lv-f-g1\">");
				writeTextField("form-control p-lv-f-number-v", _fv, _ifn + "_v", _fvv, false);
				write("</div>");
				
				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-lv-f-number-c", _fn + ".c", _ifn + "_c", numberFilterMap, _fvc);
				
				_fv = _fn + ".nv2";
				_fvv = qf == null ? null : qf.getNv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-lv-f-g2");
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeTextField("form-control p-lv-f-number-v2", _fv, _ifn + "_v2", _fvv, d);
				write("</div>");
			}
			else if ("date".equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				write("<div class=\"p-lv-f-g1\">");
				writeDatePicker("form-control p-lv-f-date-v", _fv, _ifn + "_v", "date", _fvv, false);
				write("</div>");

				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-lv-f-date-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-lv-f-g2");
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeDatePicker("form-control p-lv-f-date-v2", _fv, _ifn + "_v2", "date", _fvv, d);
				write("</div>");
			}
			else if ("datetime".equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				write("<div class=\"p-lv-f-g1\">");
				writeDateTimePicker("form-control p-lv-f-datetime-v", _fv, _ifn + "_v", "datetime", _fvv, false);
				write("</div>");

				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-lv-f-datetime-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-lv-f-g2");
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeDateTimePicker("form-control p-lv-f-datetime-v2", _fv, _ifn + "_v2", "datetime", _fvv, d);
				write("</div>");
			}
			else if ("time".equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				write("<div class=\"p-lv-f-g1\">");
				writeTimePicker("form-control p-lv-f-time-v", _fv, _ifn + "_v", "time", _fvv, false);
				write("</div>");

				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-lv-f-time-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-lv-f-g2");
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeTimePicker("form-control p-lv-f-time-v2", _fv, _ifn + "_v2", "time", _fvv, d);
				write("</div>");
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
			write("</div>");
			write("</div>");
		}
	}

	private Map<String, ListColumn.Filter> getEditableFilters(Map<String, ListColumn.Filter> fm) {
		// get editable filters
		Map<String, ListColumn.Filter> fm2 = new LinkedHashMap<String, ListColumn.Filter>();
		for (Entry<String, ListColumn.Filter> en : fm.entrySet()) {
			String _name = en.getKey();
			ListColumn.Filter _f = en.getValue();
			if (!_f.fixed) {
				fm2.put(_name, _f);
			}
		}
		return fm2;
	}

	@SuppressWarnings("unchecked")
	private void writeListViewFiltersOptions() throws IOException {
		Map<String, String> filterMethodMap = (Map<String, String>)tag.getFilterMethodMap();
		if (Collections.isEmpty(filterMethodMap)) {
			return;
		}

		write("<div class=\"form-group p-lv-submit\">");
		write("<label class=\"");
		write(tag.getCssFiltersLabel());
		write(" control-label\">");
		write(tag.getLabelFiltersMethod());
		write(":</label>");
		write("<div class=\"");
		write(tag.getCssFiltersInput());
		write("\">");

		String mv = (queryer != null && queryer.getMethod() != null ? queryer.getMethod() : Queryer.AND);
		writeRadio("p-lv-f-method", prefix + "m", id + "_fsform_filterm", filterMethodMap, mv);
		
		write("</div></div>");
	}
	
	private void writeListViewFiltersButtons(Map<String, ListColumn.Filter> fm) throws IOException {
		write("<div class=\"form-group p-lv-submit\">");
		write("<label class=\"");
		write(tag.getCssFiltersLabel());
		write(" control-label\"></label>");

		write("<div class=\"");
		write(tag.getCssFiltersInput());
		write("\">");

		// buttons
		write(button(tag.getLabelFiltersBtnQuery(), "icon-search"));
		write(' ');
		write(button(tag.getLabelFiltersBtnClear(), "icon-clear", "p-lv-fs-clear"));
		write(' ');

		// select
		Map<String, ListColumn.Filter> fm2 = getEditableFilters(fm);
		if (Collections.isNotEmpty(fm2)) {
			write("<select id=\"" + id + "_fsform_fsadd" + "\"");
			write(" class=\"form-control p-lv-fs-select\" onclick=\"return false;\">");
			write("<option value=\"\">-- ");
			write(tag.getLabelFiltersAddFilter());
			write(" --</option>");
	
			for (Entry<String, ListColumn.Filter> en : fm2.entrySet()) {
				String _name = en.getKey();
				ListColumn.Filter _f = en.getValue();
	
				boolean _fd = fsinputs.contains(_name);
	
				write("<option value=\"" + html(_name) + "\"");
				if (_fd) {
					write(" disabled");
				}
				write(">");
				write(html(_f.label));
				write("</option>");
			}
			write("</select>");
		}
		
		write("</div></div>");
	}

	private void writeListViewFilters() throws IOException {
		Map<String, ListColumn.Filter> fm = new LinkedHashMap<String, ListColumn.Filter>();
		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		Map<String, Filter> qfs = (queryer == null ? null : queryer.getFilters());
		
		String _pf = prefix + "f.";
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
				
				String _fn = _pf + c.name;

				boolean _hfe = false;
				if (fieldErrors != null) {
					for (Entry<String, List<String>> en2 : fieldErrors.entrySet()) {
						if (en2.getKey().startsWith(_fn + '.')) {
							_hfe = true;
							break;
						}
					}
				}

				Filter qf = qfs == null ? null : qfs.get(c.name);
				if (_hfe || (qf != null && Strings.isNotEmpty(qf.getC()))) {
					fsinputs.add(c.name);
					fsinput = true;
				}
				else if (of.fixed) {
					fsinputs.add(c.name);
					fsfixed = true;
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
					fsdefines.add(c.name);
				}
			}
		}
		
		if (Collections.isEmpty(fm)) {
			return;
		}
		
		write("<fieldset class=\"p-lv-filters ui-collapsible");
		if (tag.isFsExpandNone()
				|| (tag.isFsExpandDefault() && !fsinput)
				|| (tag.isFsExpandFixed() && !fsinput && !fsfixed)) {
			write(" ui-collapsed");
		}
		write("\" data-spy=\"fieldset\"><legend>");
		write(tag.getLabelFiltersCaption());
		write("</legend>");

		Form form = context.getIoc().get(Form.class);

		form.setId(id + "_fsform");
		form.setCssClass("p-lv-fsform form-horizontal");
		form.setAction(tag.getAction());
		form.setMethod(defs(tag.getMethod(), "get"));
		form.setOnsubmit(tag.getOnsubmit());
		form.setOnreset(tag.getOnreset());
		form.setLoadmask(false);
		if (tag.isFsExpandNone()
				|| (tag.isFsExpandDefault() && !fsinput)
				|| (tag.isFsExpandFixed() && !fsinput && !fsfixed)) {
			form.setCssStyle("display: none");
		}

		form.start(writer);

		if (queryer != null) {
			if (queryer.getPager() != null && isPagerLimitSelective()) {
				writeHidden(id + "_fsform_limit", prefix + "p.l", queryer.getPager().getLimit());
			}
			if (queryer.getSorter() != null) {
				writeHidden(id + "_fsform_sort", prefix + "s.c", queryer.getSorter().getColumn());
				writeHidden(id + "_fsform_dir", prefix + "s.d", queryer.getSorter().getDirection());
			}
		}

		writeListViewFiltersItems(fm);
		
		write("<div class=\"p-lv-filters-sep\"></div>");

		writeListViewFiltersOptions();
		writeListViewFiltersButtons(fm);
		
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

			String ctype = defs(c.type, "cm");
			
			Attributes tha = new Attributes();
			tha.add("column", c.name)
				.cssClass("p-lv-" + ctype 
					+ ("cm".equals(ctype) ? " p-lv-cm-" + c.name : "")
					+ (tag.getSortable() && c.sortable ? " p-lv-sortable p-sortable" : "")
					+ ((queryer != null && queryer.getSorter() != null && c.name.equals(queryer.getSorter().getColumn())) ? " p-sorted p-lv-sort-" + queryer.getSorter().getDirection() : "")
					+ (fsdefines.contains(c.name) ? " p-lv-filtered" : "")
					+ (c.hidden ? " p-lv-hidden" : ""))
				.title(tag);
			if (c.width > 0) {
				tha.cssStyle("width:" + c.width);
			}
			stag("th", tha);

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
							   + (c.wswrap ? " p-lv-wswrap" : ""));
					if (c.width > 0) {
						tda.cssStyle("width:" + c.width);
					}
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
							  .disabled(!c.enabled)
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
											String s = getCodeText(codemap, iv.next());
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
										write(formatValue(v, c.format.type));
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
		if (permit != null) {
			if (Strings.isNotEmpty(link.action)) {
				if (!permit.hasDataPermission(d, link.action)) {
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

	private String getCodeText(Map cm, Object k) throws IOException {
		Object v = cm.get(k);
		if (v == null && k != null && !(k instanceof String)) {
			v = cm.get(k.toString());
		}
		return (v == null ? null : v.toString());
	}
}
