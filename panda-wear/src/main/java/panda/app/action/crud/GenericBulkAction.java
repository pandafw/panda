package panda.app.action.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.app.BusinessRuntimeException;
import panda.app.constant.RES;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.DataQuery;
import panda.lang.Collections;
import panda.lang.mutable.MutableInt;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;


/**
 * @param <T> data type
 */
public abstract class GenericBulkAction<T> extends GenericBaseAction<T> {
	private static final Log log = Logs.getLog(GenericBulkAction.class);
	
	/**
	 * Constructor 
	 */
	public GenericBulkAction() {
	}

	//------------------------------------------------------------
	// endpoint methods
	//------------------------------------------------------------
	/**
	 * bdelete
	 * @param args arguments
	 * @return data list
	 */
	protected Object bdelete(Map<String, String[]> args) {
		return doBulkDeleteSelect(args);
	}

	/**
	 * bdelete_execute
	 * @param args arguments
	 * @return data list
	 */
	protected Object bdelete_execute(Map<String, String[]> args) {
		return doBulkDeleteExecute(args);
	}

	/**
	 * bupdate
	 * @param args arguments
	 * @return data list
	 */
	protected Object bupdate(Map<String, String[]> args) {
		return doBulkUpdateSelect(args);
	}

	/**
	 * bupdate_execute
	 * @param args arguments
	 * @return data list
	 */
	protected Object bupdate_execute(Map<String, String[]> args) {
		return doBulkUpdateExecute(args);
	}
	
	//------------------------------------------------------------
	// bulk delete methods
	//------------------------------------------------------------
	/**
	 * doBulkDeleteSelect
	 * @param args arguments
	 * @return data list
	 */
	protected Object doBulkDeleteSelect(Map<String, String[]> args) {
		final List<T> dataList = selectDataList(args, true);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			return dataList;
		}
		if (checkOnBulkDelete(dataList)) {
			addActionConfirm(getScenarioMessage(RES.ACTION_CONFIRM_PREFIX, String.valueOf(dataList.size())));
		}
		return dataList;
	}

	/**
	 * doBulkDeleteExecute
	 * @param args arguments
	 * @return data list
	 */
	protected Object doBulkDeleteExecute(Map<String, String[]> args) {
		final List<T> dataList = selectDataList(args, true);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			setScenarioView();
			return dataList;
		}

		if (!checkOnBulkDelete(dataList)) {
			setScenarioView();
			return dataList;
		}
		
		final MutableInt count = new MutableInt(0);
		try {
			startBulkDelete(dataList);
			execBulkDelete(dataList, count);
			afterBulkDelete(dataList);
		}
		catch (Exception e) {
			if (e instanceof BusinessRuntimeException) {
				log.warn(e.getMessage(), e);
			}
			else {
				log.error(e.getMessage(), e);
			}
			addSystemError(RES.ACTION_FAILED_PREFIX, e);
			setScenarioView();
			return dataList;
		}
		finally {
			finalBulkDelete(dataList);
		}
		
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			setScenarioView();
			return dataList;
		}

		addActionMessage(getScenarioMessage(RES.ACTION_SUCCESS_PREFIX, count.toString()));
		return dataList;
	}

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
	 * afterBulkDelete
	 * @param dataList data list
	 */
	protected void afterBulkDelete(List<T> dataList) {
	}
	
	/**
	 * execBulkDelete
	 * @param dataList data list
	 * @param count deleted count
	 */
	protected void execBulkDelete(final List<T> dataList, final MutableInt count) {
		if (Collections.isEmpty(dataList)) {
			return;
		}

		getDao().exec(new Runnable() {
			public void run() {
				deleteDataList(dataList, count);
			}
		});
	}

	/**
	 * delete data list
	 * @param dataList data list
	 * @param count count for delete
	 */
	protected void deleteDataList(List<T> dataList, MutableInt count) {
		for (int i = dataList.size() - 1; i >= 0; i--) {
			int c = deleteData(dataList.get(i));
			if (c <= 0) {
				dataList.remove(i);
			}
			else {
				count.add(c);
			}
		}
	}

	/**
	 * delete data
	 * @param data data to be delete
	 * @return deleted count
	 */
	protected int deleteData(T data) {
		return getDao().delete(data);
	}

	/**
	 * finalBulkDelete
	 * @param dataList data list
	 */
	protected void finalBulkDelete(List<T> dataList) {
	}

	//------------------------------------------------------------
	// bulk update methods
	//------------------------------------------------------------
	/**
	 * doBulkUpdateSelect
	 * @param args arguments
	 * @return data list
	 */
	protected Object doBulkUpdateSelect(Map<String, String[]> args) {
		final List<T> dataList = selectDataList(args, true);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			return dataList;
		}
		if (checkOnBulkUpdate(dataList)) {
			addActionConfirm(getScenarioMessage(RES.ACTION_CONFIRM_PREFIX, String.valueOf(dataList.size())));
		}
		return dataList;
	}

	/**
	 * doBulkUpdateExecute
	 * @param args arguments
	 * @return data list
	 */
	protected Object doBulkUpdateExecute(Map<String, String[]> args) {
		final List<T> dataList = selectDataList(args, true);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			setScenarioView();
			return dataList;
		}

		if (!checkOnBulkUpdate(dataList)) {
			setScenarioView();
			return dataList;
		}
		
		final MutableInt count = new MutableInt();
		try {
			startBulkUpdate(dataList);
			execBulkUpdate(dataList, count);
			afterBulkUpdate(dataList);
		}
		catch (Exception e) {
			if (e instanceof BusinessRuntimeException) {
				log.warn(e.getMessage(), e);
			}
			else {
				log.error(e.getMessage(), e);
			}
			addSystemError(RES.ACTION_FAILED_PREFIX, e);
			setScenarioView();
			return dataList;
		}
		finally {
			finalBulkUpdate(dataList);
		}
		
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			setScenarioView();
			return dataList;
		}

		addActionMessage(getScenarioMessage(RES.ACTION_SUCCESS_PREFIX, count.toString()));
		return dataList;
	}

	//------------------------------------------------------------
	// bulk methods
	//------------------------------------------------------------
	/**
	 * @param dq query
	 * @param dataList data list
	 * @param raiseError raise error if invalid item 
	 * @return count
	 */
	protected int addKeyListToQuery(DataQuery<T> dq, List<T> dataList, boolean raiseError) {
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

			dq.in(pk.getName(), vs);
			return vs.size();
		}
		
		if (keys.size() > 1) {
			int count = 0;

			dq.or();
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
					dq.and();
					for (int i = 0; i < keys.size(); i++) {
						dq.equalTo(keys.get(i).getName(), vs[i]);
					}
					dq.end();
					count++;
				}
			}
			dq.end();
			return count;
		}

		return 0;
	}

	protected List<T> convertArgsToList(Map<String, String[]> args, Class<? extends T> type) {
		List<T> ds = new ArrayList<T>();
		if (Collections.isEmpty(args)) {
			return ds;
		}

		Set<String> ks = args.keySet();
		
		int sz = 0;
		for (String[] v : args.values()) {
			if (v != null && v.length > sz) {
				sz = v.length;
			}
		}

		Map<String, String> o = new HashMap<String, String>();
		for (int i = 0; i < sz; i++) {
			for (String k : ks) {
				String[] vs = args.get(k);
				String v = (vs == null || i >= vs.length) ? null : vs[i];
				o.put(k, v);
			}
			T d = Mvcs.castValue(getContext(), o, type);
			ds.add(d);
		}

		return ds;
	}
	
	protected void addQueryColumns(DataQuery<T> dq) {
		Set<String> cs = getDisplayFields();
		if (Collections.isNotEmpty(cs)) {
			dq.excludeAll();
			dq.includePrimayKeys();
			dq.include(cs);
		}
	}
	
	protected void addQueryJoins(DataQuery<T> dq) {
	}
	
	protected void addQueryFilters(DataQuery<T> dq) {
	}
	
	/**
	 * selectDataList
	 * @param args   arguments
	 * @param filter true to call addQueryFilters()
	 * @return dataList
	 */
	protected List<T> selectDataList(Map<String, String[]> args, boolean filter) {
		List<T> keys = convertArgsToList(args, getType());
		return selectDataList(keys, true);
	}

	/**
	 * selectDataList
	 * @param dataList dataList
	 * @param filter true to call addQueryFilters()
	 * @return dataList
	 */
	protected List<T> selectDataList(List<T> dataList, boolean filter) {
		Collections.removeNull(dataList);
		if (Collections.isNotEmpty(dataList)) {
			DataQuery<T> dq = getDataQuery();

			int count = addKeyListToQuery(dq, dataList, false);
			if (count > 0) {
				addQueryColumns(dq);
				addQueryJoins(dq);
				if (filter) {
					addQueryFilters(dq);
				}
				dataList = getDao().select(dq);
				dataList = trimDataList(dataList);
			}
			else {
				dataList = null;
			}
		}
		return dataList;
	}

	protected List<T> trimDataList(List<T> ds) {
		return ds;
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
	 * afterBulkUpdate
	 * @param dataList data list
	 */
	protected void afterBulkUpdate(List<T> dataList) {
	}
	
	/**
	 * execBulkUpdate
	 * @param dataList data list
	 * @param count updated count
	 */
	protected void execBulkUpdate(final List<T> dataList, final MutableInt count) {
		if (Collections.isEmpty(dataList)) {
			return;
		}

		getDao().exec(new Runnable() {
			public void run() {
				updateDataList(dataList, count);
			}
		});
	}
	
	/**
	 * getBulkUpdateSample
	 * @param dataList data list
	 * @param dq data query
	 * @return sample data
	 */
	protected T getBulkUpdateSample(List<T> dataList, DataQuery<T> dq) {
		return null;
	}
	
	/**
	 * update data list
	 * @param dataList data list
	 * @param count updated count
	 */
	protected void updateDataList(List<T> dataList, MutableInt count) {
		DataQuery<T> dq = getDataQuery();
		addKeyListToQuery(dq, dataList, true);
		addQueryFilters(dq);

		T sample = getBulkUpdateSample(dataList, dq);
		if (sample == null) {
			return;
		}

		count.add(getDao().updates(sample, dq));

		List<T> newList = selectDataList(dataList, false);

		dataList.clear();
		if (Collections.isNotEmpty(newList)) {
			dataList.addAll(newList);
		}
	}


	/**
	 * finalBulkUpdate
	 * @param dataList data list
	 */
	protected void finalBulkUpdate(List<T> dataList) {
	}

}
