package panda.wing.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.cast.Castors;
import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityIndex;
import panda.dao.query.GenericQuery;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.mutable.MutableInt;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.bean.CompositeQuery;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.SessionStateProvider;
import panda.mvc.util.StateProvider;
import panda.mvc.view.VoidView;
import panda.mvc.view.tag.Property;
import panda.net.http.URLHelper;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLHelper;
import panda.wing.mvc.AbstractDaoAction;
import panda.wing.mvc.ActionRC;


/**
 * @param <T> data type
 */
public abstract class AbstractEntityDaoAction<T> extends AbstractDaoAction {
	private static final Log log = Logs.getLog(AbstractEntityDaoAction.class);
	
	/**
	 * DEFAULT_DATA_NAME = "d";
	 */
	public final static String DEFAULT_DATA_FIELD_NAME = "d";

	/**
	 * DEFAULT_DATA_LIST_NAME = "d";
	 */
	public final static String DEFAULT_DATA_LIST_FIELD_NAME = "ds";

	/**
	 * RESULT_DEFAULT = "";
	 */
	public final static String RESULT_DEFAULT = "";
	
	/**
	 * RESULT_CONFIRM = "confirm";
	 */
	public final static String RESULT_CONFIRM = "confirm";
	
	/**
	 * RESULT_SUCCESS = "success";
	 */
	public final static String RESULT_SUCCESS = "success";

	/**
	 * METHOD_SEPARATOR = "_";
	 */
	public final static String METHOD_SEPARATOR = "_";

	/**
	 * STATE_LIST = "list";
	 */
	public final static String STATE_LIST = "list";
	
	//------------------------------------------------------------
	// ACTION MESSAGE PREFIX
	//------------------------------------------------------------
	/**
	 * ACTION_SUCCESS_PREFIX = "action-success-";
	 */
	public final static String ACTION_SUCCESS_PREFIX = "action-success-";
	
	/**
	 * ACTION_CONFIRM_PREFIX = "action-confirm-";
	 */
	public final static String ACTION_CONFIRM_PREFIX = "action-confirm-";
	
	/**
	 * ACTION_FAILED_PREFIX = "action-failed-";
	 */
	public final static String ACTION_FAILED_PREFIX = "action-failed-";
	
	//------------------------------------------------------------
	// scenario & result
	//------------------------------------------------------------
	private String actionScenario;
	private String methodResult;
	private String[] viewScenarios = { "view", "print", "delete" };

	//------------------------------------------------------------
	// parameters
	//------------------------------------------------------------
	private Pager pager = new Pager();
	private Sorter sorter = new Sorter();
	private CompositeQuery query = new CompositeQuery(CompositeQuery.AND);

	private Boolean _load;
	private Boolean _save;

	//------------------------------------------------------------
	// config properties
	//------------------------------------------------------------
	private String dataFieldName = DEFAULT_DATA_FIELD_NAME;
	private String dataListFieldName = DEFAULT_DATA_LIST_FIELD_NAME;
	private boolean checkAbortOnError = false;
	private boolean updateSelective = false;
	private boolean clearPrimarys = true;
	private boolean clearIdentity = true;
	private Boolean listCountable;

	//------------------------------------------------------------
	// entity properties
	//------------------------------------------------------------
	private Class<T> type;
	private Entity<T> entity;
	private EntityDao<T> entityDao;
	private T sourceData;
	private T data;
	private List<T> dataList;

	/**
	 * Constructor 
	 */
	public AbstractEntityDaoAction() {
	}

	/**
	 * @return the type
	 */
	protected Class<T> getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(Class<T> type) {
		this.type = type;
	}

	protected Entity<T> getEntity() {
		if (entity == null) {
			entity = getDaoClient().getEntity(type);
		}
		return entity;
	}
	
	protected EntityDao<T> getEntityDao() {
		if (entityDao == null) {
			entityDao = getDaoClient().getEntityDao(type);
		}
		return entityDao;
	}
	
	protected Dao getDao() {
		return getEntityDao().getDao();
	}
	
	protected <X> EntityDao<X> getEntityDao(Class<X> type) {
		return getEntityDao().getDao().getEntityDao(type);
	}
	
	//--------------------------------------------------------
	// public parameters setter & getter shortcuts
	//--------------------------------------------------------
	/**
	 * @return pager
	 */
	public Pager getPg() {
		return getPager();
	}

	/**
	 * @param pager the pager to set
	 */
	public void setPg(Pager pager) {
		setPager(pager);
	}

	/**
	 * @return sorter
	 */
	public Sorter getSo() {
		return getSorter();
	}

	/**
	 * @param sorter the sorter to set
	 */
	public void setSo(Sorter sorter) {
		setSorter(sorter);
	}

	/**
	 * @return the query
	 */
	public CompositeQuery getQu() {
		return getQuery();
	}

	/**
	 * @param query the query to set
	 */
	public void setQu(CompositeQuery query) {
		setQuery(query);
	}

	/**
	 * @return query.filters
	 */
	public Map<String, Filter> getQf() {
		return getQueryFilters();
	}

	/**
	 * @param filters the query.filters to set
	 */
	public void setQf(Map<String, Filter> filters) {
		setQueryFilters(filters);
	}

	/**
	 * @return query.method
	 */
	public String getQm() {
		return getQueryMethod();
	}

	/**
	 * @param method the method to set
	 */
	public void setQm(String method) {
		setQueryMethod(method);
	}

	//------------------------------------------------------------
	// public parameter setter & getter
	//------------------------------------------------------------
	/**
	 * @return the pager
	 */
	public Pager getPager() {
		return pager;
	}

	/**
	 * @param pager the pager to set
	 */
	public void setPager(Pager pager) {
		this.pager = pager;
	}

	/**
	 * @return the sorter
	 */
	public Sorter getSorter() {
		return sorter;
	}

	/**
	 * @param sorter the sorter to set
	 */
	public void setSorter(Sorter sorter) {
		this.sorter = sorter;
	}

	/**
	 * @return the query
	 */
	public CompositeQuery getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(CompositeQuery query) {
		this.query = query;
	}

	/**
	 * @return the query.filters
	 */
	public Map<String, Filter> getQueryFilters() {
		return query.getFilters();
	}

	/**
	 * @param filters the filters to set
	 */
	public void setQueryFilters(Map<String, Filter> filters) {
		query.setFilters(filters);
	}

	/**
	 * @return the query.method
	 */
	public String getQueryMethod() {
		return query.getMethod();
	}

	/**
	 * @param method the query method to set
	 */
	public void setQueryMethod(String method) {
		query.setMethod(method);
	}

	/**
	 * @return the load
	 */
	public Boolean get_load() {
		return _load;
	}

	/**
	 * @param load the load to set
	 */
	public void set_load(Boolean load) {
		this._load = load;
	}

	/**
	 * @return the save
	 */
	public Boolean get_save() {
		return _save;
	}

	/**
	 * @param save the save to set
	 */
	public void set_save(Boolean save) {
		this._save = save;
	}

	//------------------------------------------------------------
	// public properties getter
	//------------------------------------------------------------
	/**
	 * @return true if the result is input view
	 */
	public boolean isInputResult() {
		return isInputMethodResult() && !Arrays.contains(viewScenarios, getActionScenario());
	}

	/**
	 * @return true if methodResult is Input
	 */
	public boolean isInputMethodResult() {
		return Strings.isEmpty(methodResult);
	}
	
	/**
	 * @return true if methodResult is Confirm
	 */
	public boolean isConfirmMethodResult() {
		return RESULT_CONFIRM.equals(methodResult);
	}
	
	/**
	 * @return true if methodResult is Success
	 */
	public boolean isSuccessMethodResult() {
		return RESULT_SUCCESS.equals(methodResult);
	}
	
	/**
	 * @return the methodResult
	 */
	public String getMethodResult() {
		if (methodResult == null) {
			String m = context.getMethodName();
			int i = m.indexOf(METHOD_SEPARATOR);
			methodResult = i < 0 ? RESULT_DEFAULT : m.substring(i + 1);
		}
		return methodResult;
	}

	/**
	 * @return the actionScenario
	 */
	public String getActionScenario() {
		if (actionScenario == null) {
			actionScenario = Strings.substringBefore(context.getMethodName(), METHOD_SEPARATOR);
		}
		return actionScenario;
	}

	/**
	 * @return the action result
	 */
	public String getActionResult() {
		if (Strings.isEmpty(getMethodResult())) {
			return getActionScenario();
		}
		else {
			return getActionScenario() + METHOD_SEPARATOR + getMethodResult();
		}
	}

	//------------------------------------------------------------
	// protected getter & setter
	//------------------------------------------------------------
	/**
	 * @return the sourceData
	 */
	protected T getSourceData() {
		return sourceData;
	}

	/**
	 * @param sourceData the sourceData to set
	 */
	protected void setSourceData(T sourceData) {
		this.sourceData = sourceData;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the dataList
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * @return dataName
	 */
	public String getDataFieldName() {
		return dataFieldName;
	}

	/**
	 * @param dataName the dataName to set
	 */
	protected void setDataFieldName(String dataName) {
		this.dataFieldName = dataName;
	}

	/**
	 * @return the dataListFieldName
	 */
	protected String getDataListFieldName() {
		return dataListFieldName;
	}

	/**
	 * @param dataListFieldName the dataListFieldName to set
	 */
	protected void setDataListFieldName(String dataListFieldName) {
		this.dataListFieldName = dataListFieldName;
	}

	/**
	 * @return the updateSelective
	 */
	protected boolean isUpdateSelective() {
		return updateSelective;
	}

	/**
	 * @param updateSelective the updateSelective to set
	 */
	protected void setUpdateSelective(boolean updateSelective) {
		this.updateSelective = updateSelective;
	}

	/**
	 * @return the clearPrimarys
	 */
	public boolean isClearPrimarys() {
		return clearPrimarys;
	}

	/**
	 * @param clearPrimarys the clearPrimarys to set
	 */
	public void setClearPrimarys(boolean clearPrimarys) {
		this.clearPrimarys = clearPrimarys;
	}

	/**
	 * @return the clearIdentity
	 */
	protected boolean isClearIdentity() {
		return clearIdentity;
	}

	/**
	 * @param clearIdentity the clearIdentity to set
	 */
	protected void setClearIdentity(boolean clearIdentity) {
		this.clearIdentity = clearIdentity;
	}

	/**
	 * @return the checkAbortOnError
	 */
	protected boolean isCheckAbortOnError() {
		return checkAbortOnError;
	}

	/**
	 * @param checkAbortOnError the checkAbortOnError to set
	 */
	protected void setCheckAbortOnError(boolean checkAbortOnError) {
		this.checkAbortOnError = checkAbortOnError;
	}

	/**
	 * @return the listCountable
	 */
	protected Boolean getListCountable() {
		return listCountable;
	}

	protected void setListCountable(Boolean listCountable) {
		this.listCountable = listCountable;
	}

	/**
	 * @param methodResult the methodResult to set
	 */
	protected void setMethodResult(String methodResult) {
		this.methodResult = methodResult;
	}

	/**
	 * @return the viewScenarios
	 */
	protected String[] getViewScenarios() {
		return viewScenarios;
	}

	/**
	 * @param viewScenarios the viewScenarios to set
	 */
	protected void setViewScenarios(String[] viewScenarios) {
		this.viewScenarios = viewScenarios;
	}

	/**
	 * @param actionScenario the actionScenario to set
	 */
	protected void setActionScenario(String actionScenario) {
		this.actionScenario = actionScenario;
	}

	//------------------------------------------------------------
	// dao methods
	//------------------------------------------------------------
	/**
	 * Count records by query
	 * 
	 * @param q query
	 * @return count
	 */ 
	protected long daoCount(GenericQuery<?> q) {
		return getDao().count(q);
	}

	/**
	 * daoExists
	 * 
	 * @param key T
	 * @return T
	 */ 
	protected boolean daoExists(T key) {
		return getEntityDao().exists(key);
	}

	/**
	 * fetch data by primary key
	 * 
	 * @param key primary key
	 * @return data
	 */ 
	protected T daoFetch(T key) {
		return getEntityDao().fetch(key);
	}

	/**
	 * select by query
	 * 
	 * @param q query
	 * @return data list
	 */ 
	protected List<T> daoSelect(GenericQuery<T> q) {
		return getEntityDao().select(q);
	}

	/**
	 * daoInsert
	 * 
	 * @param data data
	 */ 
	protected void daoInsert(T data) {
		getEntityDao().insert(data);
	}

	/**
	 * delete record
	 * 
	 * @param key key
	 * @return count of deleted records
	 */ 
	protected int daoDelete(T key) {
		return getEntityDao().delete(key);
	}

	/**
	 * delete records by query
	 * 
	 * @param q query
	 * @return count of deleted records
	 */ 
	protected int daoDeletes(GenericQuery<T> q) {
		return getEntityDao().deletes(q);
	}

	/**
	 * update data (ignore null properties)
	 * 
	 * @param data data
	 * @return count of updated records
	 */ 
	protected int daoUpdate(T data) {
		return getEntityDao().update(data);
	}

	/**
	 * update data (ignore null properties)
	 * 
	 * @param data data
	 * @return count of updated records
	 */ 
	protected int daoUpdateIgnoreNull(T data) {
		return getEntityDao().updateIgnoreNull(data);
	}

	/**
	 * use sample data to update record by query
	 * 
	 * @param sample sample data
	 * @param q query
	 * @return count of updated records
	 */ 
	protected int daoUpdates(T sample, GenericQuery<T> q) {
		return getEntityDao().updates(sample, q);
	}

	/**
	 * use sample data to update record by query (ignore null properties)
	 * 
	 * @param sample sample data
	 * @param q query
	 * @return count of updated records
	 */ 
	protected int daoUpdatesIgnoreNull(T sample, GenericQuery<T> q) {
		return getEntityDao().updatesIgnoreNull(sample, q);
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
	 * load list parameters
	 * @return false - send redirect url
	 */
	@SuppressWarnings("unchecked")
	protected boolean loadListParameters() {
		StateProvider sp = getState();
		if (sp == null) {
			return true;
		}
		
		if (sp instanceof SessionStateProvider) {
			Map<String, Object> pm = (Map<String, Object>)sp.loadState(STATE_LIST);
			if (pm != null) {
				pager = (Pager)pm.get("pager");
				sorter = (Sorter)pm.get("sorter");
				query = (CompositeQuery)pm.get("query");
			}
		}
		else if (sp instanceof CookieStateProvider) {
			String qs = (String)sp.loadState(STATE_LIST);
			if (Strings.isNotBlank(qs)) {
				Map params = ServletURLHelper.parseQueryString(qs);
				removeRedundantParams(params);
				if (Collections.isNotEmpty(params)) {
					HttpServletRequest request = getRequest();
					HttpServletResponse response = getResponse();
					String url = ServletURLHelper.buildURL(request, params);
					HttpServlets.sendRedirect(response, url);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * save list parameters
	 */
	protected void saveListParameters() {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
		
		if (sp instanceof SessionStateProvider) {
			Map<String, Object> pm = new HashMap<String, Object>();
			pm.put("pager", pager);
			pm.put("sorter", sorter);
			pm.put("query", query);
			sp.saveState(STATE_LIST, pm);
		}
		else if (sp instanceof CookieStateProvider) {
			sp.saveState(STATE_LIST, getListParametersString());
		}
	}

	/**
	 * @return list parameters string
	 */
	public String getListParametersString() {
		Map<String, Object> params = getListParameters();
		return URLHelper.buildQueryString(params);
	}
	
	/**
	 * @return list parameters string
	 */
	public Map<String, Object> getListParameters() {
		Map<String, Object> params = new HashMap<String, Object>();
		if (pager.getStart() != null) {
			params.put("pg.s", pager.getStart());
		}
		if (pager.getLimit() != null) {
			params.put("pg.l", pager.getLimit());
		}
		
		if (Strings.isNotBlank(sorter.getColumn())) {
			params.put("so.c", sorter.getColumn());
		}
		if (Strings.isNotBlank(sorter.getDirection())) {
			params.put("so.d", sorter.getDirection());
		}

		if (Strings.isNotEmpty(query.getMethod())) {
			params.put("q.m", query.getMethod());
		}
		if (Collections.isNotEmpty(query.getFilters())) {
			for (Entry<String, Filter> e : query.getFilters().entrySet()) {
				Filter f = e.getValue();
				if (f != null && Collections.isNotEmpty(f.getValues())) {
					String prex = "q.f." + e.getKey() + "."; 
					params.put(prex + "c", f.getComparator());
					
					List<?> vs = f.getValues();
					params.put(prex + f.getType() + "vs", vs);
				}
			}
		}

		return params;
	}

	/**
	 * getMessage
	 * @param msg msg id
	 * @return message string
	 */
	protected String getMessage(String msg) {
		return getText(msg);
	}
	
	/**
	 * getMessage
	 * @param msg msg id
	 * @param params parameters
	 * @return message string
	 */
	protected String getMessage(String msg, String[] params) {
		return getText(msg, msg, params);
	}

	//------------------------------------------------------------
	// do method
	//------------------------------------------------------------
	/**
	 * doViewInput 
	 */
	protected void doViewInput() {
		setMethodResult(RESULT_DEFAULT);
	}

	/**
	 * doViewSelect
	 */
	protected void doViewSelect() {
		setMethodResult(RESULT_DEFAULT);
		T d = selectData(data);
		if (d != null) {
			data = d;
		}
	}

	/**
	 * doInsertClear
	 */
	protected void doInsertClear() {
		setMethodResult(RESULT_DEFAULT);
		data = prepareDefaultData(null);
	}

	/**
	 * doInsertSelect
	 */
	protected void doInsertSelect() {
		setMethodResult(RESULT_DEFAULT);
		T d = selectData(data);
		if (d != null) {
			data = d;
			clearOnCopy(d);
		}
	}

	/**
	 * doInsertInput
	 */
	protected void doInsertInput() {
		setMethodResult(RESULT_DEFAULT);
		clearOnCopy(data);
		data = prepareDefaultData(data);
	}

	/**
	 * doInsertConfirm
	 */
	protected void doInsertConfirm() {
		setMethodResult(RESULT_DEFAULT);
		data = prepareDefaultData(data);
		if (checkOnInsert(data)) {
			addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario()));
			setMethodResult(RESULT_CONFIRM);
		}
		else {
			if (!hasActionErrors() && !hasFieldErrors()) {
				setMethodResult(RESULT_DEFAULT);
				setMethodResult(RESULT_CONFIRM);
			}
		}
	}

	/**
	 * doInsertExecute
	 */
	protected void doInsertExecute() {
		setMethodResult(RESULT_DEFAULT);

		data = prepareDefaultData(data);
		if (checkOnInsert(data)) {
			try {
				startInsert(data);
				getEntityDao().exec(new Runnable() {
					public void run() {
						insertData(data);
					}
				});
			}
			catch (Throwable e) {
				log.error(e.getMessage(), e);
				addActionError(getMessage(ACTION_FAILED_PREFIX + getActionScenario(), 
					new String[] { e.getMessage() }));
				return;
			}
			finally {
				finalInsert(data);
			}

			addActionMessage(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario()));
			setMethodResult(RESULT_SUCCESS);
		}
		else {
			if (!hasActionErrors() && !hasFieldErrors() 
					&& getTextAsBoolean(ActionRC.UI_INPUT_CONFIRM, false)) {
				setMethodResult(RESULT_CONFIRM);
			}
		}
	}

	/**
	 * doUpdateInput
	 */
	protected void doUpdateInput() {
		setMethodResult(RESULT_DEFAULT);
	}

	/**
	 * doUpdateSelect
	 */
	protected void doUpdateSelect() {
		setMethodResult(RESULT_DEFAULT);
		T d = selectData(data);
		if (d != null) {
			data = d;
		}
	}

	/**
	 * doUpdateConfirm
	 */
	protected void doUpdateConfirm() {
		setMethodResult(RESULT_DEFAULT);

		sourceData = selectData(data);
		if (sourceData != null) {
			if (checkOnUpdate(data, sourceData)) {
				addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario()));
				setMethodResult(RESULT_CONFIRM);
			}
			else {
				if (!hasActionErrors() && !hasFieldErrors()) {
					setMethodResult(RESULT_CONFIRM);
				}
			}
		}
	}

	/**
	 * doUpdateExecute
	 */
	protected void doUpdateExecute() {
		setMethodResult(RESULT_DEFAULT);

		sourceData = selectData(data);
		if (sourceData != null) {
			if (checkOnUpdate(data, sourceData)) {
				try {
					startUpdate(data, sourceData);
					getEntityDao().exec(new Runnable() {
						public void run() {
							updateData(data, sourceData);
						}
					});
				}
				catch (Throwable e) {
					log.error(e.getMessage(), e);
					addActionError(getMessage(ACTION_FAILED_PREFIX + getActionScenario(), 
						new String[] { e.getMessage() }));
					return;
				}
				finally {
					finalUpdate(data, sourceData);
				}

				addActionMessage(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario()));
				setMethodResult(RESULT_SUCCESS);
			}
			else {
				if (!hasActionErrors() && !hasFieldErrors()
						&& getTextAsBoolean(ActionRC.UI_INPUT_CONFIRM, false)) {
					setMethodResult(RESULT_CONFIRM);
				}
			}
		}
	}

	/**
	 * doDeleteSelect
	 */
	protected void doDeleteSelect() {
		setMethodResult(RESULT_DEFAULT);
		sourceData = selectData(data);
		if (sourceData != null) {
			if (checkOnDelete(data, sourceData)) {
				addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario()));
			}
			data = sourceData;
		}
	}

	/**
	 * doDeleteExecute
	 */
	protected void doDeleteExecute() {
		setMethodResult(RESULT_DEFAULT);

		sourceData = selectData(data);
		if (sourceData != null) {
			if (checkOnDelete(data, sourceData)) {
				data = sourceData;
				try {
					startDelete(data);
					getEntityDao().exec(new Runnable() {
						public void run() {
							deleteData(data);
						}
					});
				}
				catch (Throwable e) {
					log.error(e.getMessage(), e);
					addActionError(getMessage(ACTION_FAILED_PREFIX + getActionScenario(), 
						new String[] { e.getMessage() }));
					return;
				}
				finally {
					finalDelete(data);
				}
				
				addActionMessage(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario()));
				setMethodResult(RESULT_SUCCESS);
			}
			else {
				data = sourceData;
			}
		}
	}

	/**
	 * doBulkUpdateSelect
	 */
	protected void doBulkUpdateSelect() {
		setMethodResult(RESULT_DEFAULT);
		dataList = selectDataList(dataList);
		if (Collections.isNotEmpty(dataList)) {
			if (checkOnBulkUpdate(dataList)) {
				addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario(), 
						new String[] { String.valueOf(dataList.size()) }));
			}
		}
	}

	/**
	 * doBulkUpdateExecute
	 */
	protected void doBulkUpdateExecute() {
		setMethodResult(RESULT_DEFAULT);

		dataList = selectDataList(dataList);
		if (Collections.isNotEmpty(dataList) && checkOnBulkUpdate(dataList)) {
			final MutableInt count = new MutableInt(0);
			try {
				startBulkUpdate(dataList);
				getEntityDao().exec(new Runnable() {
					public void run() {
						count.setValue(updateDataList(dataList));
					}
				});
			}
			catch (Throwable e) {
				log.error(e.getMessage(), e);
				addActionError(getMessage(ACTION_FAILED_PREFIX + getActionScenario(), 
					new String[] { e.getMessage() }));
				return;
			}
			finally {
				finalBulkUpdate(dataList);
			}

			if (count.getValue() == dataList.size()) {
				addActionMessage(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario(), 
						new String[] { count.toString() }));
			}
			else {
				addActionWarning(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario(), 
						new String[] { count.toString() }));
			}
			setMethodResult(RESULT_SUCCESS);
		}
	}

	/**
	 * doBulkDeleteSelect
	 */
	protected void doBulkDeleteSelect() {
		setMethodResult(RESULT_DEFAULT);
		dataList = selectDataList(dataList);
		if (Collections.isNotEmpty(dataList)) {
			if (checkOnBulkDelete(dataList)) {
				addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario(), 
					new String[] { String.valueOf(dataList.size()) }));
			}
		}
	}

	/**
	 * doBulkDeleteExecute
	 */
	protected void doBulkDeleteExecute() {
		setMethodResult(RESULT_DEFAULT);

		dataList = selectDataList(dataList);
		if (Collections.isNotEmpty(dataList) && checkOnBulkDelete(dataList)) {
			final MutableInt count = new MutableInt(0);
			try {
				startBulkDelete(dataList);
				getEntityDao().exec(new Runnable() {
					public void run() {
						count.setValue(deleteDataList(dataList));
					}
				});
			}
			catch (Exception e) {
				log.error(e.getMessage(), e);
				addActionError(getMessage(ACTION_FAILED_PREFIX + getActionScenario(), 
					new String[] { e.getMessage() }));
				return;
			}
			finally {
				finalBulkDelete(dataList);
			}
			
			if (count.getValue() == dataList.size()) {
				addActionMessage(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario(), 
						new String[] { count.toString() }));
			}
			else {
				addActionWarning(getMessage(ACTION_SUCCESS_PREFIX + getActionScenario(), 
						new String[] { count.toString() }));
			}
			setMethodResult(RESULT_SUCCESS);
		}
	}

	/**
	 * 
	 * @return true - if need load list parameters
	 */
	protected boolean isNeedLoadListParameters() {
		if (_load == null) {
			return Strings.isEmpty(HttpServlets.getRequestQuery(getRequest()));
		}
		return _load;
	}
	
	/**
	 * doList
	 * @return SUCCESS
	 */
	protected Object doList() {
		if (isNeedLoadListParameters()) {
			if (!loadListParameters()) {
				return VoidView.INSTANCE;
			}
		}

		queryList();
		
		if (Boolean.TRUE.equals(_save)) {
			saveListParameters();
		}
		return null;
	}

	//------------------------------------------------------------
	// list methods
	//------------------------------------------------------------
	/**
	 * queryList
	 */
	protected void queryList() {
		GenericQuery<T> q = new GenericQuery<T>(getEntity());

		addCompositeQuery(q);
		addOrder(q);

		queryList(q);
	}

	/**
	 * @param q query
	 */
	protected void queryList(final GenericQuery<T> q) {
		addLimitToPager(ActionRC.DEFAULT_LIST_PAGER_LIMIT);
		
		final EntityDao<T> dao = getEntityDao();
		dao.exec(new Runnable() {
			public void run() {
				if (listCountable == null) {
					listCountable = getTextAsBoolean(ActionRC.UI_LIST_COUNTABLE, true);
				}
				if (listCountable) {
					pager.setTotal(daoCount(q));
					pager.normalize();
					if (pager.getTotal() > 0) {
						q.setStart(pager.getStart());
						q.setLimit(pager.getLimit());
						dataList = daoSelect(q);
					}
					else {
						dataList = new ArrayList<T>();
					}
				}
				else {
					q.setStart(pager.getStart());
					q.setLimit(pager.getLimit());
					dataList = daoSelect(q);
				}
			}
		});
	}

	/**
	 */
	protected void addLimitToPager() {
		addLimitToPager(ActionRC.DEFAULT_POPUP_PAGER_LIMIT);
	}
	
	/**
	 * @param def default limit
	 */
	protected void addLimitToPager(long def) {
		if (pager.getLimit() == null || pager.getLimit() < 1) {
			String tx = context.getMethodName() + ActionRC.PAGER_LIMIT_SUFFIX;
			Long l = getTextAsLong(tx);
			if (l == null && !ActionRC.LIST_PAGER_LIMIT.equals(tx)) {
				l = getTextAsLong(ActionRC.LIST_PAGER_LIMIT);
			}
			if (l == null) {
				l = def;
			}
			pager.setLimit(l);
		}

		long maxLimit = getTextAsLong(ActionRC.PAGER_MAX_LIMIT, 100L);
		if (pager.getLimit() == null 
				|| pager.getLimit() < 1 
				|| pager.getLimit() > maxLimit) {
			pager.setLimit(maxLimit);
		}
	}

	/**
	 * @param gq query
	 */
	protected void addCompositeQuery(GenericQuery<T> gq) {
		query.normalize();
		if (Collections.isNotEmpty(query.getFilters())) {
			if (query.isOr()) {
				gq.or();
			}
			else {
				gq.and();
			}
			for (Entry<String, Filter> e : query.getFilters().entrySet()) {
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

				String name = Strings.isEmpty(f.getName()) ? e.getKey() : f.getName();
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
			gq.end();
		}
	}

	/**
	 * @param q query
	 */
	protected void addOrder(GenericQuery q) {
		if (Strings.isEmpty(sorter.getColumn())) {
			String tx = context.getMethodName() + ActionRC.SORTER_COLUMN_SUFFIX;
			String sc = getText(tx, (String)null);
			if (sc == null && !ActionRC.LIST_SORTER_COLUMN.equals(tx)) {
				sc = getText(ActionRC.LIST_SORTER_COLUMN, (String)null);
			}
			if (Strings.isNotEmpty(sc)) {
				sorter.setColumn(sc);
			}
		}
		if (Strings.isEmpty(sorter.getDirection())) {
			String tx = context.getMethodName() + ActionRC.SORTER_DIRECTION_SUFFIX;
			String sd = getText(tx, (String)null);
			if (sd == null && !ActionRC.LIST_SORTER_DIRECTION.equals(tx)) {
				sd = getText(ActionRC.LIST_SORTER_DIRECTION, (String)null);
			}
			if (Strings.isNotEmpty(sd)) {
				sorter.setDirection(sd);
			}
		}
		if (!Strings.isEmpty(sorter.getColumn())) {
			if (Strings.isEmpty(sorter.getDirection())) {
				sorter.setDirection(Sorter.ASC);
			}
			q.orderBy(sorter.getColumn(), sorter.getDirection());
		}
	}

	//------------------------------------------------------------
	// select methods
	//------------------------------------------------------------
	/**
	 * selectData
	 * @param data data
	 * @return data data found
	 */
	protected T selectData(T data) {
		T d = daoFetch(data);
		if (d == null) {
			addActionError(getMessage(ActionRC.ERROR_DATA_NOTFOUND));
		}
		return d;
	}

	/**
	 * checkCommon
	 * @param data data
	 * @param srcData source data (null on insert)
	 * @return true if do something success
	 */
	protected boolean checkCommon(T data, T srcData) {
		Asserts.notNull(data, "data is null");
		return true;
	}

	//------------------------------------------------------------
	// insert methods
	//------------------------------------------------------------
	/**
	 * prepareDefaultData
	 * @param data data
	 * @return data
	 */
	protected T prepareDefaultData(T data) {
		if (data == null) {
			data = Classes.born(type);
		}
		return data;
	}

	/**
	 * checkOnInsert
	 * @param data data
	 * @return true if check success
	 */
	protected boolean checkOnInsert(T data) {
		boolean c = true;

		if (!checkCommon(data, null)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkPrimaryKeyOnInsert(data)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkUniqueKeyOnInsert(data)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkForeignKey(data)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		return c;
	}

	/**
	 * startInsert
	 * @param data data
	 */
	protected void startInsert(T data) {
	}

	/**
	 * insert data
	 * @param data data
	 */
	protected void insertData(T data) {
		daoInsert(data);
	}

	/**
	 * finalInsert
	 * @param data data
	 */
	protected void finalInsert(T data) {
	}

	//------------------------------------------------------------
	// update methods
	//------------------------------------------------------------
	/**
	 * update data
	 * @param data data
	 * @param srcData source data
	 * @return update count
	 */
	protected int updateData(T data, T srcData) {
		int cnt;
		if (updateSelective) {
			cnt = daoUpdateIgnoreNull(data);
		}
		else {
			cnt = daoUpdate(data);
		}
		if (cnt != 1) {
			throw new RuntimeException("The update data count (" + cnt + ") does not equals 1.");
		}
		return cnt;
	}

	/**
	 * checkOnUpdate
	 * @param data data
	 * @param srcData srcData
	 * @return true if check success
	 */
	protected boolean checkOnUpdate(T data, T srcData) {
		boolean c = true;

		if (!checkCommon(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkUpdatedOnUpdate(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		// primary key can not be modified or null
		if (!checkPrimaryKeyOnUpdate(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkUniqueKeyOnUpdate(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkForeignKey(data)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		return c;
	}

	/**
	 * startUpdate
	 * @param data data
	 * @param srcData srcData
	 */
	protected void startUpdate(T data, T srcData) {
	}

	/**
	 * finalUpdate
	 * @param data data
	 * @param srcData srcData
	 */
	protected void finalUpdate(T data, T srcData) {
	}

	//------------------------------------------------------------
	// delete methods
	//------------------------------------------------------------
	/**
	 * checkOnDelete
	 * @param data data
	 * @param srcData srcData
	 * @return true if check success
	 */
	protected boolean checkOnDelete(T data, T srcData) {
		boolean c = true;
		
		if (!checkCommon(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkUpdatedOnDelete(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}

		return c;
	}

	/**
	 * startDelete(T)
	 * @param data data
	 */
	protected void startDelete(T data) {
	}

	/**
	 * delete data
	 * @param data data
	 */
	protected void deleteData(T data) {
		int cnt = daoDelete(data);
		if (cnt != 1) {
			throw new RuntimeException("The deleted data count (" + cnt + ") does not equals 1.");
		}
	}

	/**
	 * finalDelete
	 * @param data data
	 */
	protected void finalDelete(T data) {
		
	}

	//------------------------------------------------------------
	// bulk methods
	//------------------------------------------------------------
	/**
	 * @param gq query
	 * @param dataList data list
	 * @return count
	 */
	protected int addKeyListToQuery(GenericQuery<T> gq, List<T> dataList, boolean raiseError) {
		Entity<T> entity = getEntity();
		List<EntityField> keys = entity.getPrimaryKeys();

		if (keys.size() == 1) {
			EntityField pk = keys.get(0);
			List<Object> vs = new ArrayList<Object>();
			for (int n = 0; n < dataList.size(); n++) {
				T d = dataList.get(n);
				Object v = pk.getValue(d);
				if (v != null) {
					vs.add(v);
				}
				else {
					if (raiseError) {
						throw new RuntimeException("The item[" + n + "] has empty primary key value. (" + d + ")");
					}
				}
			}

			gq.in(pk.getName(), vs);
			return vs.size();
		}
		else if (keys.size() > 1) {
			int count = 0;

			gq.or();
			Object[] vs = new Object[keys.size()]; 
			for (int n = 0; n < dataList.size(); n++) {
				T d = dataList.get(n);
				for (int i = 0; i < keys.size(); i++) {
					EntityField ef = keys.get(i);

					Object v = ef.getValue(d);
					if (v != null) {
						vs[i] = v;
					}
					else {
						if (raiseError) {
							throw new RuntimeException("The item[" + n + "] has empty primary key value. (" + d + ")");
						}
						vs[0] = null;
						break;
					}
				}
				if (vs[0] != null) {
					gq.and();
					for (int i = 0; i < keys.size(); i++) {
						gq.equalTo(keys.get(i).getName(), vs[i]);
					}
					gq.end();
					count++;
				}
			}
			gq.end();
			return count;
		}
		else {
			return 0;
		}
	}

	/**
	 * selectDataList
	 * @param dataList dataList
	 * @return dataList
	 */
	protected List<T> selectDataList(List<T> dataList) {
		Collections.removeNull(dataList);
		if (dataList != null && dataList.size() > 0) {
			GenericQuery<T> q = new GenericQuery<T>(getEntity());

			int count = addKeyListToQuery(q, dataList, false);
			if (count > 0) {
				dataList = daoSelect(q);
			}
			else {
				dataList = null;
			}
		}
		if (dataList == null || dataList.size() < 1) {
			addActionError(getMessage(ActionRC.ERROR_DATA_LIST_EMPTY));
		}
		return dataList;
	}
	
	//------------------------------------------------------------
	// bulk update methods
	//------------------------------------------------------------
	/**
	 * checkOnBulkUpdate
	 * @param dataList data list
	 * @return true to continue update
	 */
	protected boolean checkOnBulkUpdate(List<T> dataList) {
		return true;
	}

	/**
	 * startBulkUpdate
	 * @param dataList data list
	 */
	protected void startBulkUpdate(List<T> dataList) {
	}
	
	/**
	 * getBulkUpdateSample
	 * @param dataList data list
	 * @return sample data
	 */
	protected T getBulkUpdateSample(List<T> dataList) {
		return null;
	}
	
	/**
	 * update data list
	 * @param dataList data list
	 * @return updated count
	 */
	protected int updateDataList(List<T> dataList) {
		T sample = getBulkUpdateSample(dataList);

		int cnt = 0;
		if (sample != null && Collections.isNotEmpty(dataList)) {
			GenericQuery<T> q = new GenericQuery<T>(getEntity());

			addKeyListToQuery(q, dataList, true);
			cnt = daoUpdatesIgnoreNull(sample, q);
//			if (cnt != dataList.size()) {
//				throw new RuntimeException("The updated data count (" + cnt + ") does not equals dataList.size(" + dataList.size() + ").");
//			}

			dataList = selectDataList(dataList);
		}
		return cnt;
	}


	/**
	 * finalBulkUpdate
	 * @param dataList data list
	 */
	protected void finalBulkUpdate(List<T> dataList) {
	}

	//------------------------------------------------------------
	// bulk delete methods
	//------------------------------------------------------------
	/**
	 * checkOnBulkDelete
	 * @param dataList data list
	 * @return true to continue delete
	 */
	protected boolean checkOnBulkDelete(List<T> dataList) {
		return true;
	}
	
	/**
	 * startBulkDelete
	 * @param dataList data list
	 */
	protected void startBulkDelete(List<T> dataList) {
	}
	
	/**
	 * delete data list
	 * @param dataList data list
	 * @return deleted count
	 */
	protected int deleteDataList(List<T> dataList) {
		int cnt = 0;
		if (dataList != null && dataList.size() > 0) {
			GenericQuery<T> q = new GenericQuery<T>(getEntity());

			addKeyListToQuery(q, dataList, true);
			cnt = daoDeletes(q);
//			if (cnt != dataList.size()) {
//				throw new RuntimeException("The deleted data count (" + cnt + ") does not equals dataList.size(" + dataList.size() + ").");
//			}
		}
		return cnt;
	}

	/**
	 * finalBulkDelete
	 * @param dataList data list
	 */
	protected void finalBulkDelete(List<T> dataList) {
	}
	
	//------------------------------------------------------------
	// check methods
	//------------------------------------------------------------
	protected void addDataDuplateError(T data, Collection<EntityField> efs) {
		StringBuilder sb = new StringBuilder();
		for (EntityField ef :efs) {
			String fn = getFullDataFieldName(ef.getName());
			
			addFieldError(fn, Strings.EMPTY);

			sb.append(getText(fn));
			sb.append(": ");
			
			Property ptag = context.getIoc().get(Property.class);
			ptag.setValue(ef.getValue(data));
			ptag.setEscape(null);
			sb.append(ptag.formatValue());
			sb.append(Streams.LINE_SEPARATOR);
		}

		addActionError(getMessage(ActionRC.ERROR_DATA_DUPLICATE, new String[] { sb.toString() }));
	}

	/**
	 * checkPrimaryKeyOnInsert
	 * @param data data
	 * @return true if check successfully
	 */
	protected boolean checkPrimaryKeyOnInsert(T data) {
		Entity<T> entity = getEntity();
		EntityField eid = entity.getIdentity(); 
		if (eid == null) {
			List<EntityField> pks = entity.getPrimaryKeys();

			boolean hasNull = false;

			GenericQuery<T> q = new GenericQuery<T>(getEntity());
			for (EntityField pk : pks) {
				Object dv = pk.getValue(data);
				if (dv == null) {
					hasNull = true;
				}
				else {
					q.equalTo(pk.getName(), dv);
				}
			}

			if (!hasNull) {
				if (daoExists(data)) {
					addDataDuplateError(data, pks);
					return false;
				}
			}
		}
		else {
			Object id = eid.getValue(data);
			if (getEntityDao().isValidIdentity(id) && daoExists(data)) {
				addDataDuplateError(data, entity.getPrimaryKeys());
				return false;
			}
		}
		return true;
	}

	/**
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkPrimaryKeyOnUpdate(T data, T srcData) {
		boolean hasNull = false;
		for (EntityField ef : getEntity().getPrimaryKeys()) {
			Object dv = ef.getValue(data);
			if (dv == null) {
				hasNull = true;
				addFieldError(getFullDataFieldName(ef.getName()), getMessage(ActionRC.ERROR_FIELDVALUE_REQUIRED));
			}
		}
		if (hasNull) {
			return false;
		}

		return true;
	}

	protected boolean checkUniqueIndex(T data, EntityIndex ei) {
		boolean allNull = true;

		GenericQuery<T> q = new GenericQuery<T>(getEntity());
		for (EntityField ef : ei.getFields()) {
			Object dv = ef.getValue(data);
			if (dv == null) {
				q.isNull(ef.getName());
			}
			else {
				allNull = false;
				q.equalTo(ef.getName(), dv);
			}
		}

		if (!allNull) {
			if (daoCount(q) > 0) {
				addDataDuplateError(data, ei.getFields());
				return false;
			}
		}
		
		return true;
	}

	/**
	 * checkUniqueKeyOnInsert
	 * @param data data
	 * @return true if check successfully
	 */
	protected boolean checkUniqueKeyOnInsert(T data) {
		Collection<EntityIndex> eis = getEntity().getIndexes();
		if (Collections.isEmpty(eis)) {
			return true;
		}
		
		for (EntityIndex ei : eis) {
			if (!ei.isUnique()) {
				continue;
			}
			
			if (!checkUniqueIndex(data, ei)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checkUniqueKeyOnUpdate
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkUniqueKeyOnUpdate(T data, T srcData) {
		Collection<EntityIndex> eis = getEntity().getIndexes();
		if (Collections.isEmpty(eis)) {
			return true;
		}
		
		for (EntityIndex ei : eis) {
			if (!ei.isUnique()) {
				continue;
			}

			boolean dirty = false;
			for (EntityField ef : ei.getFields()) {
				Object ov = ef.getValue(srcData);
				Object dv = ef.getValue(data);
				if (!Objects.equals(ov, dv)) {
					dirty = true;
					break;
				}
			}

			if (dirty) {
				if (!checkUniqueIndex(data, ei)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * checkForeignKey
	 * @param data
	 * @return true if check successfully
	 */
	protected boolean checkForeignKey(T data) {
		Collection<EntityFKey> efks = getEntity().getForeignKeys();
		if (Collections.isEmpty(efks)) {
			return true;
		}
		
		for (EntityFKey efk : efks) {
			boolean allNull = true;
			
			@SuppressWarnings("unchecked")
			GenericQuery q = new GenericQuery(efk.getReference());

			int i = 0;
			for (EntityField rf : efk.getReference().getPrimaryKeys()) {
				EntityField ef = efk.getFields().get(i);
				Object dv = ef.getValue(data);
				if (dv == null) {
					q.isNull(rf.getName());
				}
				else {
					allNull = false;
					q.equalTo(rf.getName(), dv);
				}
				i++;
			}

			if (!allNull) {
				q.setLimit(1);
				if (daoCount(q) < 1) {
					for (EntityField ef : efk.getFields()) {
						addFieldError(getFullDataFieldName(ef.getName()), getMessage(ActionRC.ERROR_FIELDVALUE_INCORRECT));
					}
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * checkUpdatedOnUpdate
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkUpdatedOnUpdate(T data, T srcData) {
		if (!checkUpdated(data, srcData)) {
			addActionConfirm(getMessage(ActionRC.CONFIRM_DATA_OVERWRITE));
			return false;
		}
		return true;
	}

	/**
	 * checkUpdatedOnDelete
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkUpdatedOnDelete(T data, T srcData) {
		if (!checkUpdated(data, srcData)) {
			addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario()));
			return false;
		}
		return true;
	}

	/**
	 * checkUpdated
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkUpdated(T data, T srcData) {
		return true;
	}

	//------------------------------------------------------------
	// other methods
	//------------------------------------------------------------
	/**
	 * clear on copy
	 * @param data data
	 */
	protected void clearOnCopy(T data) {
		if (data != null) {
			if (clearPrimarys) {
				clearPrimaryKeyValues(data);
			}
			else if (clearIdentity) {
				clearIdentityValue(data);
			}
		}
	}
	
	/**
	 * clear primary key value of data
	 * @param data data
	 */
	protected void clearPrimaryKeyValues(T data) {
		if (data != null) {
			Castors c = getDaoClient().getCastors();
			List<EntityField> pks = getEntity().getPrimaryKeys();
			for (EntityField pk : pks) {
				Object value = c.cast(null, pk.getType());
				pk.setValue(data, value);
			}
		}
	}

	/**
	 * clear identity value of data
	 * @param data data
	 */
	protected void clearIdentityValue(T data) {
		if (data != null) {
			EntityField eid = getEntity().getIdentity();
			if (eid != null) {
				Castors c = getDaoClient().getCastors();
				Object value = c.cast(null, eid.getType());
				eid.setValue(data, value);
			}
		}
	}

	/**
	 * @param propertyName property name
	 * @return dataName + "." + propertyName
	 */
	protected String getFullDataFieldName(String propertyName) {
		return dataFieldName + "." + propertyName;
	}

	/**
	 * @param propertyName property name
	 * @param index index
	 * @return dataListName + "[index]." + propertyName
	 */
	protected String getDataListFieldName(String propertyName, int index) {
		return dataListFieldName + '[' + index + "]." + propertyName;
	}
	
}
