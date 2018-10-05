package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.bean.Filter;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.Queryer;
import panda.mvc.view.tag.ui.theme.AbstractEndExRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.mvc.view.util.ListColumn;
import panda.mvc.view.util.ListFilter;

public class QueryerRenderer extends AbstractEndExRenderer<Queryer> {
	protected static final String T_RADIO = "radio";
	protected static final String T_CHECKLIST = "checklist";
	protected static final String T_TIME = "time";
	protected static final String T_DATE = "date";
	protected static final String T_DATETIME = "datetime";
	protected static final String T_NUMBER = "number";
	protected static final String T_BOOLEAN = "boolean";
	protected static final String T_STRING = "string";
	protected static final String T_SELECT = "select";

	protected static final Set<String> TWO_INPUT_TYPES = Arrays.toSet(T_TIME, T_DATE, T_DATETIME, T_NUMBER);
	
	private String id;

	private String prefix = "";
	private panda.mvc.bean.Queryer queryer;

	private List<ListColumn> columns;
	private List<ListFilter> filters;
	
	// filters define list
	private Set<String> fsdefines = new HashSet<String>();
	
	// filters input/fixed list
	private Set<String> fsinputs = new HashSet<String>();
	
	// has input filter
	private boolean fsinput = false;
	
	// has fixed filter
	private boolean fsfixed = false;
	
	public QueryerRenderer(RenderingContext context) {
		super(context);
	}

	public void render() throws IOException {
		initVars();
		
		Attributes attr = new Attributes();
		attr.id(id)
			.cssClass(tag, "p-qr")
			.cssStyle(tag)
			.disabled(tag)
			.tabindex(tag)
			.tooltip(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		
		stag("div", attr);

		writeFilters();
		
		etag("div");
	}

	private void initVars() {
		id = tag.getId();

		if (Strings.isEmpty(tag.getQueryer())) {
			if (context.getParams() instanceof panda.mvc.bean.Queryer) {
				queryer = (panda.mvc.bean.Queryer)context.getParams();
			}
		}
		else {
			queryer = (panda.mvc.bean.Queryer)context.getParameter(tag.getQueryer());
			prefix = tag.getQueryer() + '.';
		}
		
		columns = tag.getColumns();

		filters = tag.getFilters();
		if (filters == null) {
			filters = new ArrayList<ListFilter>();

			Map<String, List<String>> fes = context.getParamAlert().getErrors();
			Map<String, Filter> qfs = (queryer == null ? null : queryer.getFilters());
			
			String _pf = prefix + "f.";
			for (ListColumn c : columns) {
				if (!c.filterable) {
					continue;
				}
	
				ListFilter cf = c.filter;
				if (cf == null) {
					continue;
				}

				filters.add(cf);
				if (cf.name == null) {
					cf.name = c.name;
				}
				if (cf.label == null) {
					cf.label = c.header;
				}
				if (cf.tooltip == null) {
					cf.tooltip = c.tooltip;
				}
				
				String _fn = _pf + c.name;
	
				boolean _hfe = false;
				if (Collections.isNotEmpty(fes)) {
					String _fn_d = _fn + '.';
					for (Entry<String, List<String>> en2 : fes.entrySet()) {
						if (en2.getKey().startsWith(_fn_d)) {
							_hfe = true;
							break;
						}
					}
				}
	
				Filter qf = (qfs == null ? null : qfs.get(c.name));
				if (_hfe || (qf != null && Strings.isNotEmpty(qf.getC()))) {
					fsinputs.add(c.name);
					if (!qf.isEmpty()) {
						fsinput = true;
					}
				}
				else if (cf.fixed) {
					fsinputs.add(c.name);
					fsfixed = true;
				}
	
				if (qf == null) {
					continue;
				}
				
				if (isFiltered(cf, qf)) {
					fsdefines.add(c.name);
				}
			}
		}
	}

	public static boolean isFiltered(ListFilter cf, Filter qf) {
		boolean r = false;

		if (T_STRING.equals(cf.type)) {
			r = Strings.isNotEmpty(qf.getSv());
		}
		else if (T_BOOLEAN.equals(cf.type)) {
			r = qf.getBv() != null || qf.getBv2() != null;
		}
		else if (T_NUMBER.equals(cf.type)) {
			r = qf.getNv() != null || qf.getNv2() != null;
		}
		else if (T_DATETIME.equals(cf.type)) {
			r = qf.getEv() != null || qf.getEv2() != null;
		}
		else if (T_DATE.equals(cf.type)) {
			r = qf.getDv() != null || qf.getDv2() != null;
		}
		else if (T_TIME.equals(cf.type)) {
			r = qf.getTv() != null || qf.getTv2() != null;
		}
		else if (T_CHECKLIST.equals(cf.type)) {
			r = Collections.isNotEmpty(qf.getSvs());
		}
		else if (T_RADIO.equals(cf.type) || T_SELECT.equals(cf.type)) {
			r = Strings.isNotEmpty(qf.getSv());
		}
		
		return r;
	}
	
	private void writeFilters() throws IOException {
		if (Collections.isEmpty(filters)) {
			return;
		}

		boolean collapsed = false;
		write("<fieldset class=\"p-qr-filters ui-collapsible");
		if (tag.isExpandNone()
				|| (tag.isExpandDefault() && !fsinput)
				|| (tag.isExpandFixed() && !fsinput && !fsfixed)) {
			collapsed = true;
			write(" ui-collapsed");
		}
		write("\" data-spy=\"fieldset\"><legend>");
		write("<i class=\"ui-fieldset-icon fa fa-caret-" + (collapsed ? "right" : "down") + "\"></i>");
		write(tag.getLabelCaption());
		write("</legend>");

		// write Form
		Form form = context.getIoc().get(Form.class);
		form.setId(id + "_fsform");
		form.setCssClass("p-qr-form form-horizontal");
		form.setAction(tag.getAction());
		form.setMethod(defs(tag.getMethod(), "get"));
		form.setTarget(tag.getTarget());
		form.setOnsubmit(tag.getOnsubmit());
		form.setOnreset(tag.getOnreset());
		form.setLoadmask(false);
		if (tag.isExpandNone()
				|| (tag.isExpandDefault() && !fsinput)
				|| (tag.isExpandFixed() && !fsinput && !fsfixed)) {
			form.setCssStyle("display: none");
		}
		form.start(writer);

		// write hiddens
		write(tag.getHiddens());
		if (queryer != null) {
			if (queryer.getPager() != null && queryer.getPager().getLimit() != null && isPagerLimitSelective()) {
				writeHidden(id + "_fsform_limit", prefix + "p.l", queryer.getPager().getLimit());
			}
			if (queryer.getSorter() != null) {
				if (Strings.isNotEmpty(queryer.getSorter().getColumn())) {
					writeHidden(id + "_fsform_sort", prefix + "s.c", queryer.getSorter().getColumn());
				}
				if (Strings.isNotEmpty(queryer.getSorter().getDirection())) {
					writeHidden(id + "_fsform_dir", prefix + "s.d", queryer.getSorter().getDirection());
				}
			}
		}

		writeFilterItems();
		
		write("<div class=\"p-qr-sep\"></div>");

		writeFilterMethod();
		writeFilterButtons();
		
		form.end(writer, "");
		
		writeFilterSelect(collapsed);
		
		write("</fieldset>");
	}

	private boolean isPagerLimitSelective() {
		Pager pg = newTag(Pager.class);

		pg.setLinkStyle(tag.getPagerStyle());
		pg.evaluateParams();

		return pg.isLimitSelective();
	}

	@SuppressWarnings("unchecked")
	private void writeFilterItems() throws IOException {
		Map<String, String> stringFilterMap = tag.getStringFilterMap();
		Map<String, String> boolFilterMap = tag.getBoolFilterMap(); 
		Map<String, String> numberFilterMap = tag.getNumberFilterMap();
		Map<String, String> dateFilterMap = tag.getDateFilterMap();

		String _pf = prefix + "f.";

		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		Map<String, Filter> qfs = (queryer == null ? null : queryer.getFilters());
		
		for (ListFilter _f : filters) {
			String _name = _f.name;
			String _hname = html(_name);
			Filter qf = qfs == null ? null : qfs.get(_name);
			
			String _fn = _pf + _name;
			String _ifn = id + "_fsf_" + _name;
			boolean _fd = fsinputs.contains(_name);

			boolean _hfe = false;
			if (Collections.isNotEmpty(fieldErrors)) {
				for (Entry<String, List<String>> en2 : fieldErrors.entrySet()) {
					if (en2.getKey().startsWith(_fn + '.')) {
						_hfe = true;
						break;
					}
				}
			}

			write("<div class=\"p-qr-fsi-" 
					+ _hname
					+ (_fd ? "" : " p-hidden")
					+ " form-group"
					+ (TWO_INPUT_TYPES.contains(_f.type) || !_hfe ? "" : " has-error")
					+ "\" data-item=\""
					+ _hname
					+ "\">");

			if (!_f.fixed) {
				write(icon("p-qr-remove fa fa-minus-circle"));
			}

			write("<label for=\"" + _ifn + "_v\" class=\"");
			write(tag.getCssLabel());
			write(" control-label " + (_hfe ? "p-error" : "") + "\">");
			if (Strings.isNotEmpty(_f.label)) {
				write(html(_f.label));
				write(":");
			}
			write("</label>");

			write("<div class=\"");
			write(tag.getCssInput());
			write(" p-qr-inputgroup\"");
			if (Strings.isNotEmpty(_f.tooltip)) {
				write("title=\"");
				write(html(_f.tooltip));
				write("\"");
			}
			write(">");

			String _fv;
			if (T_STRING.equals(_f.type)) {
				_fv = _fn + ".sv";
				
				String _fvv = qf == null ? null : qf.getSv();
				writeTextField("form-control p-qr-f-string-v", _fv, _ifn + "_v", _fvv, false);
				
				_fvv = qf == null ? null : qf.getC();
				writeSelect("form-control p-qr-f-string-c", _fn + ".c", _ifn + "_c", stringFilterMap, _fvv);
			}
			else if (T_BOOLEAN.equals(_f.type)) {
				_fv = _fn + ".bv";
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				write("<div class=\"p-checkboxlist\">");

				Boolean _fvv = qf == null ? null : qf.getBv();
				write("<label class=\"checkbox-inline\">");
				Attributes ia = new Attributes();
				ia.add("type", "checkbox")
				  .add("class", "p-qr-f-boolean-true")
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
				  .add("class", "p-qr-f-boolean-false")
				  .add("name", _fv)
				  .add("id", _ifn + "_v2")
				  .add("value", "false")
				  .addIfTrue("checked", Boolean.FALSE.equals(_fvv));
				xtag("input", ia);
				write(boolFilterMap.get("false"));
				write("</label>");
				
				write("</div>");
			}
			else if (T_NUMBER.equals(_f.type)) {
				_fv = _fn + ".nv";
				Number _fvv = qf == null ? null : qf.getNv();

				write("<div class=\"p-qr-f-g1");
				write(Collections.containsKey(fieldErrors, _fv) ? " has-error" : "");
				write("\">");
				writeTextField("form-control p-qr-f-number-v", _fv, _ifn + "_v", _fvv, false);
				write("</div>");
				
				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-qr-f-number-c", _fn + ".c", _ifn + "_c", numberFilterMap, _fvc);
				
				_fv = _fn + ".nv2";
				_fvv = qf == null ? null : qf.getNv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-qr-f-g2");
				write(Collections.containsKey(fieldErrors, _fv) ? " has-error" : "");
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeTextField("form-control p-qr-f-number-v2", _fv, _ifn + "_v2", _fvv, d);
				write("</div>");
			}
			else if (T_DATETIME.equals(_f.type)) {
				_fv = _fn + ".ev";
				Date _fvv = qf == null ? null : qf.getEv();

				write("<div class=\"p-qr-f-g1");
				write(Collections.containsKey(fieldErrors, _fv) ? " has-error" : "");
				write("\">");
				writeDateTimePicker("form-control p-qr-f-datetime-v", _fv, _ifn + "_v", _f.type, _fvv, false);
				write("</div>");

				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-qr-f-datetime-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".ev2";
				_fvv = qf == null ? null : qf.getEv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-qr-f-g2" + (Collections.containsKey(fieldErrors, _fv) ? " has-error" : ""));
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeDateTimePicker("form-control p-qr-f-datetime-v2", _fv, _ifn + "_v2", _f.type, _fvv, d);
				write("</div>");
			}
			else if (T_DATE.equals(_f.type)) {
				_fv = _fn + ".dv";
				Date _fvv = qf == null ? null : qf.getDv();

				write("<div class=\"p-qr-f-g1");
				write(Collections.containsKey(fieldErrors, _fv) ? " has-error" : "");
				write("\">");
				writeDatePicker("form-control p-qr-f-date-v", _fv, _ifn + "_v", _f.type, _fvv, false);
				write("</div>");

				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-qr-f-date-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".dv2";
				_fvv = qf == null ? null : qf.getDv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-qr-f-g2" + (Collections.containsKey(fieldErrors, _fv) ? " has-error" : ""));
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeDatePicker("form-control p-qr-f-date-v2", _fv, _ifn + "_v2", _f.type, _fvv, d);
				write("</div>");
			}
			else if (T_TIME.equals(_f.type)) {
				_fv = _fn + ".tv";
				Date _fvv = qf == null ? null : qf.getTv();

				write("<div class=\"p-qr-f-g1");
				write(Collections.containsKey(fieldErrors, _fv) ? " has-error" : "");
				write("\">");
				writeTimePicker("form-control p-qr-f-time-v", _fv, _ifn + "_v", _f.type, _fvv, false);
				write("</div>");

				String _fvc = qf == null ? null : qf.getC();
				if (_fvc == null) {
					_fvc = Collections.firstKey(numberFilterMap);
				}
				writeSelect("form-control p-qr-f-time-c", _fn + ".c", _ifn + "_c", dateFilterMap, _fvc);
				
				_fv = _fn + ".tv2";
				_fvv = qf == null ? null : qf.getTv2();
				boolean d = !Filter.BETWEEN.equals(_fvc);
				write("<div class=\"p-qr-f-g2" + (Collections.containsKey(fieldErrors, _fv) ? " has-error" : ""));
				if (d) {
					write(" p-hidden");
				}
				write("\">");
				writeTimePicker("form-control p-qr-f-time-v2", _fv, _ifn + "_v2", _f.type, _fvv, d);
				write("</div>");
			}
			else if (T_CHECKLIST.equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"in\"/>");
				
				_fv = _fn + ".svs";
				List<String> _fvv = qf == null ? null : qf.getSvs();
				writeCheckboxList("p-qr-f-checklist", _fv, _ifn + "_v", _f.list, _fvv);
			}
			else if (T_RADIO.equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"eq\"/>");

				_fv = _fn + ".sv";
				String _fvv = qf == null ? null : qf.getSv();
				writeRadio("p-qr-f-checklist", _fv, _ifn + "_v", _f.list, _fvv);
			}
			else if (T_SELECT.equals(_f.type)) {
				write("<input type=\"hidden\" name=\"" + _fn + ".c\" value=\"eq\"/>");
				
				_fv = _fn + ".sv";
				String _fvv = qf == null ? null : qf.getSv();
				writeSelect("form-control p-qr-f-select", _fv, _ifn + "_v", _f.list, _fvv, true);
			}
			
			if (Collections.isNotEmpty(fieldErrors)) {
				for (Entry<String, List<String>> fen : fieldErrors.entrySet()) {
					if (fen.getKey().startsWith(_fn + ".")) {
						write("<ul errorFor=\"" + html(_ifn) + "\" class=\"");
						write(FieldErrorRenderer.UL_CLASS);
						write("\">");
						for (String m : fen.getValue()) {
							write("<li class=\"");
							write(FieldErrorRenderer.LI_CLASS);
							write("\">");
							write(icon(FieldErrorRenderer.ICON_CLASS));
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

	@SuppressWarnings("unchecked")
	private void writeFilterMethod() throws IOException {
		Map<String, String> filterMethodMap = (Map<String, String>)tag.getFilterMethodMap();
		if (Collections.isEmpty(filterMethodMap)) {
			return;
		}

		write("<div class=\"form-group p-qr-methods\">");
		write("<label class=\"");
		write(tag.getCssLabel());
		write(" control-label\">");
		write(tag.getLabelMethod());
		write(":</label>");
		write("<div class=\"");
		write(tag.getCssInput());
		write("\">");

		String mv = (queryer != null && queryer.getMethod() != null ? queryer.getMethod() : panda.mvc.bean.Queryer.AND);
		writeRadio("p-qr-method", prefix + "m", id + "_fsform_filterm", filterMethodMap, mv);
		
		write("</div></div>");
	}
	
	private void writeFilterButtons() throws IOException {
		write("<div class=\"form-group p-qr-buttons\">");
		write("<label class=\"");
		write(tag.getCssLabel());
		write(" control-label\"></label>");

		write("<div class=\"");
		write(tag.getCssInput());
		write("\">");

		// buttons
		write(button(tag.getLabelBtnQuery(), "icon-search", "p-qr-search"));
		write(' ');
		write(button(tag.getLabelBtnClear(), "icon-clear", "p-qr-clear"));
		write(' ');

		write("</div></div>");
	}
	
	private void writeFilterSelect(boolean collapsed) throws IOException {
		// selectable filter
		boolean empty = true;
		for (ListFilter _f : filters) {
			if (_f.fixed) {
				continue;
			}
			if (empty) {
				empty = false;
				write("<select id=\"" + id + "_fsform_fsadd" + "\"");
				write(" class=\"form-control p-qr-select\" onclick=\"return false;\"");
				write(collapsed ? " style=\"display: none\"" : "");
				write("><option value=\"\">-- ");
				write(tag.getLabelAddFilter());
				write(" --</option>");
			}

			String _name = _f.name;

			boolean _fd = fsinputs.contains(_name);

			write("<option value=\"" + html(_name) + "\"");
			if (_fd) {
				write(" disabled");
			}
			write(">");
			write(html(_f.label));
			write("</option>");
		}
		
		if (!empty) {
			write("</select>");
		}
	}
}


