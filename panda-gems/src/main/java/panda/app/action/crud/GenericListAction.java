package panda.app.action.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.app.bean.ListResult;
import panda.app.constant.RES;
import panda.app.constant.VAL;
import panda.dao.Dao;
import panda.dao.query.DataQuery;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.bean.Sorter;
import panda.mvc.util.DataQueryHelper;
import panda.mvc.util.MvcURLBuilder;
import panda.mvc.util.StateProvider;
import panda.mvc.view.Views;
import panda.mvc.view.util.CsvExporter;
import panda.mvc.view.util.ListColumn;
import panda.mvc.view.util.TsvExporter;
import panda.mvc.view.util.XlsExporter;
import panda.mvc.view.util.XlsxExporter;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;


/**
 * @param <T> data type
 */
public abstract class GenericListAction<T> extends GenericBaseAction<T> {
	/**
	 * STATE_LIST = "list";
	 */
	public final static String STATE_LIST = "list";

	//------------------------------------------------------------
	// config properties
	//------------------------------------------------------------
	private Boolean _load;
	private Boolean _save;
	private Boolean listCountable;

	/**
	 * Constructor 
	 */
	public GenericListAction() {
	}

	//------------------------------------------------------------
	// endpoint methods
	//------------------------------------------------------------
	/**
	 * list
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object list(Queryer qr) {
		set_save(true);
		return doList(qr, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_print
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object list_print(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_popup
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object list_popup(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VAL.DEFAULT_POPUP_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_json
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object list_json(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_xml
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object list_xml(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_pdf
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object list_pdf(Queryer qr) {
		return doPdf(qr, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}

	
	/**
	 * list_csv
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object list_csv(Queryer qr, List<ListColumn> columns) {
		return doCsv(qr, columns, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_tsv
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object list_tsv(Queryer qr, List<ListColumn> columns) {
		return doTsv(qr, columns, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_xls
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object list_xls(Queryer qr, List<ListColumn> columns) {
		return doXls(qr, columns, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * list_xlsx
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object list_xlsx(Queryer qr, List<ListColumn> columns) {
		return doXlsx(qr, columns, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}

	//------------------------------------------------------------
	// Export methods
	//------------------------------------------------------------
	/**
	 * expo_json
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object expo_json(QueryerEx qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}
	
	/**
	 * expo_xml
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object expo_xml(QueryerEx qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}
	
	/**
	 * expo_pdf
	 * @param qr queryer
	 * @return data list or view
	 */
	protected Object expo_pdf(QueryerEx qr) {
		return doPdf(qr, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}

	
	/**
	 * expo_csv
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object expo_csv(QueryerEx qr, List<ListColumn> columns) {
		return doCsv(qr, columns, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}
	
	/**
	 * expo_tsv
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object expo_tsv(QueryerEx qr, List<ListColumn> columns) {
		return doTsv(qr, columns, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}
	
	/**
	 * expo_xls
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object expo_xls(QueryerEx qr, List<ListColumn> columns) {
		return doXls(qr, columns, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}
	
	/**
	 * expo_xlsx
	 * @param qr queryer
	 * @param columns list columns
	 * @return data list or view
	 */
	protected Object expo_xlsx(QueryerEx qr, List<ListColumn> columns) {
		return doXlsx(qr, columns, VAL.DEFAULT_EXPORT_PAGESIZE, VAL.MAXIMUM_EXPORT_PAGESIZE);
	}

	//------------------------------------------------------------
	// option properties
	//------------------------------------------------------------
	/**
	 * @return the load
	 */
	protected Boolean get_load() {
		return _load;
	}

	/**
	 * @param load the load to set
	 */
	protected void set_load(Boolean load) {
		this._load = load;
	}

	/**
	 * @return the save
	 */
	protected Boolean get_save() {
		return _save;
	}

	/**
	 * @param save the save to set
	 */
	protected void set_save(Boolean save) {
		this._save = save;
	}

	//------------------------------------------------------------
	// protected getter & setter
	//------------------------------------------------------------
	/**
	 * @return the listCountable
	 */
	protected Boolean getListCountable() {
		return listCountable;
	}

	/**
	 * @param listCountable the listCountable to set
	 */
	protected void setListCountable(Boolean listCountable) {
		this.listCountable = listCountable;
	}

	//------------------------------------------------------------
	// internal methods
	//------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private void removeRedundantParams(Map params) {
		List keys = new ArrayList();
		Set<Entry> s = params.entrySet();
		for (Entry e : s) {
			if (e.getKey() != null) {
				if (e.getKey().toString().startsWith("_") || Objects.isEmpty(e.getValue())) {
					keys.add(e.getKey());
				}
			}
		}
		for (Object key : keys) {
			params.remove(key);
		}
	}

	/**
	 * @param qr queryer
	 * @return true - if need load list parameters
	 */
	protected boolean isNeedLoadListParameters(Queryer qr) {
		if (_load == null) {
			return Strings.isEmpty(HttpServlets.getRequestQuery(getRequest()));
		}
		return _load;
	}
	

	/**
	 * load list parameters
	 * @param qr queryer
	 * @return null if redirect url is sent
	 */
	protected Queryer loadListParameters(Queryer qr) {
		Queryer q = loadListParameters();
		if (q == null) {
			return q;
		}
		if (qr == null) {
			qr = q;
			getContext().setParams(qr);
		}
		else {
			qr.copy(q);
		}
		return qr;
	}
	
	/**
	 * load list parameters
	 * @return null if redirect url is sent
	 */
	protected Queryer loadListParameters() {
		StateProvider sp = getState();
		if (sp == null) {
			return new Queryer();
		}
		
		String qs = (String)sp.loadState(STATE_LIST);
		if (Strings.isNotBlank(qs)) {
			Map params = URLHelper.parseQueryString(qs);
			removeRedundantParams(params);
			if (Collections.isNotEmpty(params)) {
				String url = MvcURLBuilder.buildURL(getContext(), params);
				HttpServlets.sendRedirect(getResponse(), url);
				return null;
			}
		}

		return new Queryer();
	}
	
	/**
	 * save list parameters
	 * @param qr queryer
	 */
	protected void saveListParameters(Queryer qr) {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
		
		sp.saveState(STATE_LIST, getListParametersString(qr));
	}

	/**
	 * @param qr queryer
	 * @return list parameters string
	 */
	protected String getListParametersString(Queryer qr) {
		Map<String, Object> params = getListParameters(qr);
		return MvcURLBuilder.buildQueryString(getContext(), params);
	}
	
	/**
	 * @param qr queryer
	 * @return list parameters string
	 */
	protected Map<String, Object> getListParameters(Queryer qr) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (qr.getPager().getStart() > 0) {
			params.put("p.s", qr.getPager().getStart());
		}
		if (qr.getPager().hasLimit()) {
			params.put("p.l", qr.getPager().getLimit());
		}
		
		if (Strings.isNotBlank(qr.getSorter().getColumn())) {
			params.put("s.c", qr.getSorter().getColumn());
		}
		if (Strings.isNotBlank(qr.getSorter().getDirection())) {
			params.put("s.d", qr.getSorter().getDirection());
		}

		if (Strings.isNotEmpty(qr.getMethod())) {
			params.put("m", qr.getMethod());
		}
		if (Collections.isNotEmpty(qr.getFilters())) {
			for (Entry<String, Filter> e : qr.getFilters().entrySet()) {
				Filter f = e.getValue();
				if (f != null && Collections.isNotEmpty(f.getValues())) {
					String prex = "f." + e.getKey() + "."; 
					params.put(prex + "c", f.getComparator());
					
					List<?> vs = f.getValues();
					params.put(prex + f.getType() + "vs", vs);
				}
			}
		}

		return params;
	}

	/**
	 * do list
	 * @param qr queryer
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 * @return data list or view
	 */
	protected Object doList(Queryer qr, long defLimit, long maxLimit) {
		if (isNeedLoadListParameters(qr)) {
			qr = loadListParameters(qr);
			if (qr == null) {
				return Views.none(getContext());
			}
		}

		if (Boolean.TRUE.equals(_save)) {
			saveListParameters(qr);
		}

		ListResult<T> r = new ListResult<T>();

		queryList(r, qr, defLimit, maxLimit);
		
		return r;
	}

	/**
	 * do pdf
	 * @param qr queryer
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 * @return data list or view
	 */
	protected Object doPdf(Queryer qr, long defLimit, long maxLimit) {
		set_load(false);
		set_save(false);
		return doList(qr, defLimit, maxLimit);
	}

	/**
	 * do csv
	 * @param qr queryer
	 * @param columns list columns
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 * @return data list or view
	 */
	protected Object doCsv(Queryer qr, List<ListColumn> columns, long defLimit, long maxLimit) {
		set_load(false);
		set_save(false);

		Object r = doList(qr, defLimit, maxLimit);
		if (r instanceof Views) {
			return r;
		}
		
		CsvExporter csv = getContext().getIoc().get(CsvExporter.class);
		csv.setList(((ListResult)r).getList());
		csv.setColumns(columns);

		getContext().setResult(csv);

		return Views.csv(getContext(), getText(RES.TITLE) + '_' + assist().getCsvFileTime() + ".csv");
	}
	

	/**
	 * do tsv
	 * @param qr queryer
	 * @param columns list columns
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 * @return data list or view
	 */
	protected Object doTsv(Queryer qr, List<ListColumn> columns, long defLimit, long maxLimit) {
		set_load(false);
		set_save(false);

		Object r = doList(qr, defLimit, maxLimit);
		if (r instanceof Views) {
			return r;
		}
		
		TsvExporter tsv = getContext().getIoc().get(TsvExporter.class);
		tsv.setList(((ListResult)r).getList());
		tsv.setColumns(columns);

		getContext().setResult(tsv);
		
		return Views.tsv(getContext(), getText(RES.TITLE) + '_' + assist().getCsvFileTime() + ".tsv");
	}


	/**
	 * do xls
	 * @param qr queryer
	 * @param columns list columns
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 * @return data list or view
	 */
	protected Object doXls(Queryer qr, List<ListColumn> columns, long defLimit, long maxLimit) {
		set_load(false);
		set_save(false);

		Object r = doList(qr, defLimit, maxLimit);
		if (r instanceof Views) {
			return r;
		}
		
		XlsExporter xls = getContext().getIoc().get(XlsExporter.class);
		xls.setList(((ListResult)r).getList());
		xls.setColumns(columns);

		getContext().setResult(xls);
		
		return Views.xls(getContext(), getText(RES.TITLE) + '_' + assist().getCsvFileTime() + ".xls");
	}


	/**
	 * do xlsx
	 * @param qr queryer
	 * @param columns list columns
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 * @return data list or view
	 */
	protected Object doXlsx(Queryer qr, List<ListColumn> columns, long defLimit, long maxLimit) {
		set_load(false);
		set_save(false);

		Object r = doList(qr, defLimit, maxLimit);
		if (r instanceof Views) {
			return r;
		}
		
		XlsxExporter xlsx = getContext().getIoc().get(XlsxExporter.class);
		xlsx.setList(((ListResult)r).getList());
		xlsx.setColumns(columns);

		getContext().setResult(xlsx);
		
		return Views.xlsx(getContext(), getText(RES.TITLE) + '_' + assist().getCsvFileTime() + ".xlsx");
	}

	/**
	 * queryList
	 * @param r list result
	 * @param qr queryer
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 */
	protected void queryList(final ListResult<T> r, final Queryer qr, long defLimit, long maxLimit) {
		final DataQuery<T> dq = getDataQuery();

		addQueryColumns(dq);
		addQueryJoins(dq);
		addQueryFilters(dq, qr);
		addQueryOrders(dq, qr.getSorter());
		addLimitToPager(qr.getPager(), defLimit, maxLimit);
		
		if (listCountable == null) {
			listCountable = getTextAsBoolean(RES.UI_LIST_COUNTABLE, true);
		}

		queryList(r, qr, dq);
	}
	
	protected void queryList(final ListResult<T> r, final Queryer qr, final DataQuery<T> dq) {
		r.setPage(qr.getPager());
		
		final Dao dao = getDao();
		dao.exec(new Runnable() {
			public void run() {
				if (listCountable) {
					qr.getPager().setTotal(dao.count(dq));
					qr.getPager().normalize();
					if (qr.getPager().getTotal() < 1) {
						r.setList(new ArrayList<T>());
						return;
					}
				}
				dq.setStart(qr.getPager().getStart());
				dq.setLimit(qr.getPager().getLimit());
				r.setList(dao.select(dq));
				r.setList(trimDataList(r.getList()));
			}
		});
	}

	//-------------------------------------------------------------
	// called by view
	//-------------------------------------------------------------
	/**
	 * @param field field name
	 * @return true if the field is filterable
	 */
	public boolean filterField(String field) {
		return displayField(field);
	}

	/**
	 * @return true if listview-actions-align=l
	 */
	public boolean isActionsAlignLeft() {
		String a = getText("listview-actions-align");
		if (Strings.isEmpty(a)) {
			return true;
		}
		return a.charAt(0) == 'l';
	}
	

	/**
	 * @return true if listview-actions-align=r
	 */
	public boolean isActionsAlignRight() {
		String a = getText("listview-actions-align");
		if (Strings.isEmpty(a)) {
			return false;
		}
		return a.charAt(0) == 'r';
	}

	//-------------------------------------------------------------
	// protected methods (can be override by sub class)
	//-------------------------------------------------------------
	/**
	 * trim data list
	 * @param ds data list
	 * @return data list
	 */
	protected List<T> trimDataList(List<T> ds) {
		return ds;
	}

	/**
	 * add query columns
	 * @param dq DataQuery
	 */
	protected void addQueryColumns(DataQuery<T> dq) {
		DataQueryHelper.addQueryColumns(dq, getDisplayFields());
	}

	/**
	 * add query joins
	 * @param dq DataQuery
	 */
	protected void addQueryJoins(DataQuery<T> dq) {
	}
	
	
	/**
	 * @param dq DataQuery
	 * @param qr queryer
	 */
	protected void addQueryFilters(DataQuery<T> dq, Queryer qr) {
		DataQueryHelper.addQueryFilters(getEntity(), dq, qr);
	}

	/**
	 * @param dq query
	 * @param sorter sorter
	 */
	protected void addQueryOrders(DataQuery<T> dq, Sorter sorter) {
		if (Strings.isEmpty(sorter.getColumn())) {
			String sc = getText(getMethodName() + RES.SORTER_SUFFIX, (String)null);
			assist().castToSorter(sorter, sc);
		}
		DataQueryHelper.addQueryOrders(dq, sorter, getDisplayFields());
	}

	/**
	 * add limit options to queryer
	 * @param pager pager
	 * @param def default limit
	 * @param max maximum limit
	 */
	protected void addLimitToPager(Pager pager, long def, long max) {
		assist().setLimitToPager(pager, def, max);
	}

}
