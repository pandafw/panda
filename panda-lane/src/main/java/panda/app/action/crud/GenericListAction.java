package panda.app.action.crud;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.app.constant.RES;
import panda.app.constant.VAL;
import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.DataQuery;
import panda.dao.query.Join;
import panda.dao.query.Query;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.bean.Sorter;
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
	private final static Log log = Logs.getLog(GenericListAction.class);
	
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

	//------------------------------------------------------------
	// result properties
	//------------------------------------------------------------
	private List<T> dataList;
	
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

		queryList(qr, defLimit, maxLimit);
		
		return dataList;
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

		Object rv = doList(qr, defLimit, maxLimit);
		if (rv instanceof View) {
			return rv;
		}
		
		CsvExporter csv = getContext().getIoc().get(CsvExporter.class);
		csv.setList((List)rv);
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

		Object rv = doList(qr, defLimit, maxLimit);
		if (rv instanceof View) {
			return rv;
		}
		
		TsvExporter tsv = getContext().getIoc().get(TsvExporter.class);
		tsv.setList((List)rv);
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

		Object rv = doList(qr, defLimit, maxLimit);
		if (rv instanceof View) {
			return rv;
		}
		
		XlsExporter xls = getContext().getIoc().get(XlsExporter.class);
		xls.setList((List)rv);
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

		Object rv = doList(qr, defLimit, maxLimit);
		if (rv instanceof View) {
			return rv;
		}
		
		XlsxExporter xlsx = getContext().getIoc().get(XlsxExporter.class);
		xlsx.setList((List)rv);
		xlsx.setColumns(columns);

		getContext().setResult(xlsx);
		
		return Views.xlsx(getContext(), getText(RES.TITLE) + '_' + assist().getCsvFileTime() + ".xlsx");
	}

	/**
	 * queryList
	 * @param qr queryer
	 * @param defLimit default limit
	 * @param maxLimit default maximum limit
	 */
	protected void queryList(final Queryer qr, long defLimit, long maxLimit) {
		final DataQuery<T> dq = new DataQuery<T>(getEntity());

		addQueryColumns(dq);
		addQueryJoins(dq);
		addQueryFilters(dq, qr);
		addQueryOrders(dq, qr.getSorter());
		addLimitToPager(qr.getPager(), defLimit, maxLimit);
		
		if (listCountable == null) {
			listCountable = getTextAsBoolean(RES.UI_LIST_COUNTABLE, true);
		}

		final Dao dao = getDao();
		dao.exec(new Runnable() {
			public void run() {
				if (listCountable) {
					qr.getPager().setTotal(getDao().count(dq));
					qr.getPager().normalize();
					if (qr.getPager().getTotal() > 0) {
						dq.setStart(qr.getPager().getStart());
						dq.setLimit(qr.getPager().getLimit());
						dataList = getDao().select(dq);
						dataList = trimDataList(dataList);
					}
					else {
						dataList = new ArrayList<T>();
					}
				}
				else {
					dq.setStart(qr.getPager().getStart());
					dq.setLimit(qr.getPager().getLimit());
					dataList = getDao().select(dq);
					dataList = trimDataList(dataList);
				}
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
		Set<String> fs = getDisplayFields();
		if (Collections.isNotEmpty(fs)) {
			dq.excludeAll();
			dq.includePrimayKeys();
			dq.include(fs);
		}
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
	@SuppressWarnings("unchecked")
	protected void addQueryFilters(DataQuery<T> dq, Queryer qr) {
		qr.normalize();
		if (Collections.isEmpty(qr.getFilters())) {
			return;
		}
		
		if (qr.isOr()) {
			dq.or();
		}
		else {
			dq.and();
		}
		
		for (Entry<String, Filter> e : qr.getFilters().entrySet()) {
			Filter f = e.getValue();
			if (f == null) {
				continue;
			}

			List<?> values = f.getValues();
			if (values == null || values.isEmpty()) {
				continue;
			}

			Object value = values.get(0);
			if (value == null
					&& !Filter.IN.equals(f.getComparator())
					&& !Filter.BETWEEN.equals(f.getComparator())) {
				continue;
			}

			Entity<T> entity = getEntity();
			String name = Strings.isEmpty(f.getName()) ? e.getKey() : f.getName();
			EntityField ef = entity.getField(name);
			if (ef == null) {
				if (log.isWarnEnabled()) {
					log.warn("Invalid filter field [" + name + "] of entity " + entity);
				}
				continue;
			}
			if (!ef.isPersistent()) {
				if (qr.isOr()) {
					if (log.isDebugEnabled()) {
						log.debug("SKIP filter(OR) of non persistent field [" + name + "] of entity " + entity);
					}
					continue;
				}
				
				if (ef.isJoinField()) {
					Join join = dq.getJoin(ef.getJoinName());
					if (join == null) {
						if (log.isDebugEnabled()) {
							log.debug("SKIP filter of join field [" + name + "] of entity " + entity);
						}
						continue;
					}
					
					join.setType(Join.INNER);
					Query<?> jq = join.getQuery();
					DataQuery jgq;
					if (jq instanceof DataQuery) {
						jgq = (DataQuery)jq;
					}
					else {
						jgq = new DataQuery(jq);
						join.setQuery(jgq);
					}

					addQueryFilter(jgq, f, ef.getJoinField(), value, values);
					continue;
				}

				if (log.isDebugEnabled()) {
					log.debug("SKIP filter of non persistent field [" + name + "] of entity " + entity);
				}
				continue;
			}
			
			addQueryFilter(dq, f, name, value, values);
		}

		dq.end();
	}

	/**
	 * add filter to queryer
	 * @param dq DataQuery
	 * @param f filter
	 * @param name field name
	 * @param value field value
	 * @param values field values
	 */
	protected void addQueryFilter(DataQuery<T> dq, Filter f, String name, Object value, List<?> values) {
		if (Filter.EQUAL.equals(f.getComparator())) {
			if (value instanceof Date && Filter.VT_DATE.equals(f.getType())) {
				Date value2 = DateTimes.addMilliseconds(DateTimes.zeroCeiling((Date)value), -1);
				dq.between(name, value, value2);
			}
			else {
				dq.equalTo(name, value);
			}
		}
		else if (Filter.NOT_EQUAL.equals(f.getComparator())) {
			if (value instanceof Date && Filter.VT_DATE.equals(f.getType())) {
				Date value2 = DateTimes.addMilliseconds(DateTimes.zeroCeiling((Date)value), -1);
				dq.notBetween(name, value, value2);
			}
			else {
				dq.notEqualTo(name, value);
			}
		}
		else if (Filter.LESS_THAN.equals(f.getComparator())) {
			dq.lessThan(name, value);
		}
		else if (Filter.LESS_EQUAL.equals(f.getComparator())) {
			dq.lessThanOrEqualTo(name, value);
		}
		else if (Filter.GREATER_THAN.equals(f.getComparator())) {
			dq.greaterThan(name, value);
		}
		else if (Filter.GREATER_EQUAL.equals(f.getComparator())) {
			dq.greaterThanOrEqualTo(name, value);
		}
		else if (Filter.LIKE.equals(f.getComparator())) {
			dq.like(name, value.toString());
		}
		else if (Filter.NOT_LIKE.equals(f.getComparator())) {
			dq.notLike(name, value.toString());
		}
		else if (Filter.MATCH.equals(f.getComparator())) {
			dq.match(name, value.toString());
		}
		else if (Filter.NOT_MATCH.equals(f.getComparator())) {
			dq.notMatch(name, value.toString());
		}
		else if (Filter.LEFT_MATCH.equals(f.getComparator())) {
			dq.leftMatch(name, value.toString());
		}
		else if (Filter.NOT_LEFT_MATCH.equals(f.getComparator())) {
			dq.notLeftMatch(name, value.toString());
		}
		else if (Filter.RIGHT_MATCH.equals(f.getComparator())) {
			dq.rightMatch(name, value.toString());
		}
		else if (Filter.NOT_RIGHT_MATCH.equals(f.getComparator())) {
			dq.notRightMatch(name, value.toString());
		}
		else if (Filter.IN.equals(f.getComparator())) {
			dq.in(name, values);
		}
		else if (Filter.NOT_IN.equals(f.getComparator())) {
			dq.notIn(name, values);
		}
		else if (Filter.BETWEEN.equals(f.getComparator())) {
			Object v1 = values.get(0);
			Object v2 = values.size() > 1 ? values.get(1) : null;

			if (v1 == null && v2 == null) {
			}
			else if (v1 == null) {
				dq.lessThanOrEqualTo(name, v2);
			}
			else if (v2 == null) {
				dq.greaterThanOrEqualTo(name, v1);
			}
			else {
				if (v1 instanceof Date && v2 instanceof Date && Filter.VT_DATE.equals(f.getType())) {
					v2 = DateTimes.addMilliseconds(DateTimes.zeroCeiling((Date)v2), -1);
				}
				dq.between(name, v1, v2);
			}
		}
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
		if (Strings.isNotEmpty(sorter.getColumn())) {
			if (Collections.isEmpty(getDisplayFields()) || Collections.contains(getDisplayFields(), sorter.getColumn())) {
				if (Strings.isEmpty(sorter.getDirection())) {
					sorter.setDirection(Sorter.ASC);
				}
				dq.orderBy(sorter.getColumn(), sorter.getDirection());
			}
		}
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
