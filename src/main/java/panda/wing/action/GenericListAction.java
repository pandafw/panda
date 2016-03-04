package panda.wing.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.GenericQuery;
import panda.dao.query.Join;
import panda.dao.query.Query;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.Sorter;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.SessionStateProvider;
import panda.mvc.util.StateProvider;
import panda.mvc.view.VoidView;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLHelper;
import panda.wing.constant.RC;
import panda.wing.constant.VC;


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
	 */
	protected Object list(Queryer qr) {
		set_save(true);
		return doList(qr, VC.DEFAULT_LIST_PAGE_ITEMS, VC.DEFAULT_LIST_MAX_ITEMS);
	}
	
	/**
	 * list_json
	 */
	protected Object list_json(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VC.DEFAULT_JSON_PAGE_ITEMS, VC.DEFAULT_JSON_MAX_ITEMS);
	}
	
	/**
	 * list_xml
	 */
	protected Object list_xml(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VC.DEFAULT_XML_PAGE_ITEMS, VC.DEFAULT_XML_MAX_ITEMS);
	}
	
	/**
	 * list_csv
	 */
	protected Object list_csv(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VC.DEFAULT_CSV_PAGE_ITEMS, VC.DEFAULT_CSV_MAX_ITEMS);
	}
	
	/**
	 * list_print
	 */
	protected Object list_print(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VC.DEFAULT_LIST_PAGE_ITEMS, VC.DEFAULT_LIST_MAX_ITEMS);
	}
	
	/**
	 * list_popup
	 */
	protected Object list_popup(Queryer qr) {
		set_load(false);
		set_save(false);
		return doList(qr, VC.DEFAULT_POPUP_PAGE_ITEMS, VC.DEFAULT_LIST_MAX_ITEMS);
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
				if (e.getKey().toString().startsWith("_") 
						|| Objects.isEmpty(e.getValue())) {
					keys.add(e.getKey());
				}
			}
		}
		for (Object key : keys) {
			params.remove(key);
		}
	}

	/**
	 * 
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
	 * @return null - send redirect url
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
	 * @return null - send redirect url
	 */
	protected Queryer loadListParameters() {
		StateProvider sp = getState();
		if (sp == null) {
			return new Queryer();
		}
		
		if (sp instanceof SessionStateProvider) {
			Queryer qr = (Queryer)sp.loadState(STATE_LIST);
			return qr == null ? new Queryer() : qr;
		}
		
		if (sp instanceof CookieStateProvider) {
			String qs = (String)sp.loadState(STATE_LIST);
			if (Strings.isNotBlank(qs)) {
				Map params = ServletURLHelper.parseQueryString(qs);
				removeRedundantParams(params);
				if (Collections.isNotEmpty(params)) {
					HttpServletRequest request = getRequest();
					HttpServletResponse response = getResponse();
					String url = ServletURLHelper.buildURL(request, params);
					HttpServlets.sendRedirect(response, url);
					return null;
				}
			}
		}

		return new Queryer();
	}
	
	/**
	 * save list parameters
	 */
	protected void saveListParameters(Queryer qr) {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
		
		if (sp instanceof SessionStateProvider) {
			sp.saveState(STATE_LIST, qr);
		}
		else if (sp instanceof CookieStateProvider) {
			sp.saveState(STATE_LIST, getListParametersString(qr));
		}
	}

	/**
	 * @return list parameters string
	 */
	protected String getListParametersString(Queryer qr) {
		Map<String, Object> params = getListParameters(qr);
		return URLHelper.buildQueryString(params);
	}
	
	/**
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

	protected Object doList(Queryer qr, long defLimit, long maxLimit) {
		if (isNeedLoadListParameters(qr)) {
			qr = loadListParameters(qr);
			if (qr == null) {
				return VoidView.INSTANCE;
			}
		}

		if (Boolean.TRUE.equals(_save)) {
			saveListParameters(qr);
		}

		queryList(qr, defLimit, maxLimit);
		
		return dataList;
	}

	/**
	 * queryList
	 */
	protected void queryList(final Queryer qr, long defLimit, long maxLimit) {
		final GenericQuery<T> gq = new GenericQuery<T>(getEntity());

		addQueryColumns(gq);
		addQueryJoins(gq);
		addQueryFilters(gq, qr);
		addQueryOrders(gq, qr.getSorter());
		addLimitToPager(qr.getPager(), defLimit, maxLimit);
		
		if (listCountable == null) {
			listCountable = getTextAsBoolean(RC.UI_LIST_COUNTABLE, true);
		}

		final Dao dao = getDao();
		dao.exec(new Runnable() {
			public void run() {
				if (listCountable) {
					qr.getPager().setTotal(getDao().count(gq));
					qr.getPager().normalize();
					if (qr.getPager().getTotal() > 0) {
						gq.setStart(qr.getPager().getStart());
						gq.setLimit(qr.getPager().getLimit());
						dataList = getDao().select(gq);
						dataList = trimDataList(dataList);
					}
					else {
						dataList = new ArrayList<T>();
					}
				}
				else {
					gq.setStart(qr.getPager().getStart());
					gq.setLimit(qr.getPager().getLimit());
					dataList = getDao().select(gq);
					dataList = trimDataList(dataList);
				}
			}
		});
	}

	//-------------------------------------------------------------
	// call by view
	//-------------------------------------------------------------
	public boolean isActionsAlignLeft() {
		String a = getText("listview-actions-align");
		if (Strings.isEmpty(a)) {
			return true;
		}
		return a.charAt(0) == 'l';
	}
	
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
	 * @param gq query
	 */
	protected void addQueryColumns(GenericQuery<T> gq) {
		Set<String> fs = getDisplayFields();
		if (Collections.isNotEmpty(fs)) {
			gq.excludeAll();
			gq.includePrimayKeys();
			gq.include(fs);
		}
	}

	/**
	 * @param gq query
	 */
	protected void addQueryJoins(GenericQuery<T> gq) {
	}
	
	/**
	 * @param gq query
	 */
	@SuppressWarnings("unchecked")
	protected void addQueryFilters(GenericQuery<T> gq, Queryer qr) {
		qr.normalize();
		if (Collections.isEmpty(qr.getFilters())) {
			return;
		}
		
		if (qr.isOr()) {
			gq.or();
		}
		else {
			gq.and();
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
				log.warn("Invalid filter field [" + name + "] of entity " + entity);
				continue;
			}
			if (!ef.isPersistent()) {
				if (qr.isOr()) {
					log.debug("SKIP filter(OR) of non persistent field [" + name + "] of entity " + entity);
					continue;
				}
				
				if (ef.isJoinField()) {
					Join join = gq.getJoins().get(ef.getJoinName());
					if (join == null) {
						log.debug("SKIP filter of join field [" + name + "] of entity " + entity);
						continue;
					}
					
					join.setType(Join.INNER);
					Query<?> jq = join.getQuery();
					GenericQuery jgq;
					if (jq instanceof GenericQuery) {
						jgq = (GenericQuery)jq;
					}
					else {
						jgq = new GenericQuery(jq);
						join.setQuery(jgq);
					}

					addQueryFilter(jgq, f, ef.getJoinField(), value, values);
					continue;
				}

				log.debug("SKIP filter of non persistent field [" + name + "] of entity " + entity);
				continue;
			}
			
			addQueryFilter(gq, f, name, value, values);
		}

		gq.end();
	}

	protected void addQueryFilter(GenericQuery<T> gq, Filter f, String name, Object value, List<?> values) {
		if (Filter.EQUAL.equals(f.getComparator())) {
			gq.equalTo(name, value);
		}
		else if (Filter.NOT_EQUAL.equals(f.getComparator())) {
			gq.notEqualTo(name, value);
		}
		else if (Filter.GREATER_THAN.equals(f.getComparator())) {
			gq.greaterThan(name, value);
		}
		else if (Filter.GREATER_EQUAL.equals(f.getComparator())) {
			gq.greaterThanOrEqualTo(name, value);
		}
		else if (Filter.LESS_THAN.equals(f.getComparator())) {
			gq.lessThan(name, value);
		}
		else if (Filter.LESS_EQUAL.equals(f.getComparator())) {
			gq.lessThanOrEqualTo(name, value);
		}
		else if (Filter.LIKE.equals(f.getComparator())) {
			gq.like(name, value.toString());
		}
		else if (Filter.NOT_LIKE.equals(f.getComparator())) {
			gq.notLike(name, value.toString());
		}
		else if (Filter.MATCH.equals(f.getComparator())) {
			gq.match(name, value.toString());
		}
		else if (Filter.NOT_MATCH.equals(f.getComparator())) {
			gq.notMatch(name, value.toString());
		}
		else if (Filter.LEFT_MATCH.equals(f.getComparator())) {
			gq.leftMatch(name, value.toString());
		}
		else if (Filter.NOT_LEFT_MATCH.equals(f.getComparator())) {
			gq.notLeftMatch(name, value.toString());
		}
		else if (Filter.RIGHT_MATCH.equals(f.getComparator())) {
			gq.rightMatch(name, value.toString());
		}
		else if (Filter.NOT_RIGHT_MATCH.equals(f.getComparator())) {
			gq.notRightMatch(name, value.toString());
		}
		else if (Filter.IN.equals(f.getComparator())) {
			gq.in(name, values);
		}
		else if (Filter.NOT_IN.equals(f.getComparator())) {
			gq.notIn(name, values);
		}
		else if (Filter.BETWEEN.equals(f.getComparator())) {
			Object v1 = values.get(0);
			Object v2 = values.size() > 1 ? values.get(1) : null;

			if (v1 == null && v2 == null) {
			}
			else if (v1 == null) {
				gq.lessThanOrEqualTo(name, v2);
			}
			else if (v2 == null) {
				gq.greaterThanOrEqualTo(name, v1);
			}
			else {
				gq.between(name, v1, v2);
			}
		}
	}


	/**
	 * @param gq query
	 */
	protected void addQueryOrders(GenericQuery<T> gq, Sorter sorter) {
		if (Strings.isEmpty(sorter.getColumn())) {
			String sc = getText(getMethodName() + RC.SORTER_SUFFIX, (String)null);
			assist().castToSorter(sorter, sc);
		}
		if (Strings.isNotEmpty(sorter.getColumn())) {
			if (Collections.isEmpty(getDisplayFields()) || Collections.contains(getDisplayFields(), sorter.getColumn())) {
				if (Strings.isEmpty(sorter.getDirection())) {
					sorter.setDirection(Sorter.ASC);
				}
				gq.orderBy(sorter.getColumn(), sorter.getDirection());
			}
		}
	}

	protected void addLimitToPager(Pager pager, long def, long max) {
		assist().setLimitToPager(pager, def, max);
	}

}
