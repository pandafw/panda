package panda.wing.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.cast.Castors;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.GenericQuery;
import panda.lang.Collections;
import panda.lang.mutable.MutableInt;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.constant.RC;


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
	 */
	protected Object bdelete(Map<String, String[]> args) {
		return doResult(doBulkDeleteSelect(args));
	}

	/**
	 * bdelete_execute
	 */
	protected Object bdelete_execute(Map<String, String[]> args) {
		return doResult(doBulkDeleteExecute(args));
	}

	/**
	 * bupdate
	 */
	protected Object bupdate(Map<String, String[]> args) {
		return doResult(doBulkUpdateSelect(args));
	}

	/**
	 * bupdate_execute
	 */
	protected Object bupdate_execute(Map<String, String[]> args) {
		return doResult(doBulkUpdateExecute(args));
	}
	
	//------------------------------------------------------------
	// bulk delete methods
	//------------------------------------------------------------
	/**
	 * doBulkDeleteSelect
	 */
	protected Object doBulkDeleteSelect(Map<String, String[]> args) {
		List<T> keys = convertArgsToList(args);
		final List<T> dataList = selectDataList(keys);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return dataList;
		}
		if (checkOnBulkDelete(dataList)) {
			addActionConfirm(getScenarioMessage(RC.ACTION_CONFIRM_PREFIX, String.valueOf(dataList.size())));
		}
		return dataList;
	}

	/**
	 * doBulkDeleteExecute
	 */
	protected Object doBulkDeleteExecute(Map<String, String[]> args) {
		List<T> keys = convertArgsToList(args);
		final List<T> dataList = selectDataList(keys);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			setScenarioResult();
			return dataList;
		}

		if (!checkOnBulkDelete(dataList)) {
			setScenarioResult();
			return dataList;
		}
		
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
			addActionError(getScenarioMessage(RC.ACTION_FAILED_PREFIX, e.getMessage()));
			setScenarioResult();
			return dataList;
		}
		finally {
			finalBulkDelete(dataList);
		}
		
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			setScenarioResult();
			return dataList;
		}

		addActionMessage(getScenarioMessage(RC.ACTION_SUCCESS_PREFIX, String.valueOf(dataList.size())));
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
	 * delete data list
	 * @param dataList data list
	 * @return deleted count
	 */
	protected int deleteDataList(List<T> dataList) {
		if (Collections.isEmpty(dataList)) {
			return 0;
		}

		int cnt = 0;
		for (int i = dataList.size() - 1; i >= 0; i--) {
			int c = daoDelete(dataList.get(i));
			if (c <= 0) {
				dataList.remove(i);
			}
			cnt += c;
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
	// bulk update methods
	//------------------------------------------------------------
	/**
	 * doBulkUpdateSelect
	 */
	protected Object doBulkUpdateSelect(Map<String, String[]> args) {
		List<T> keys = convertArgsToList(args);
		final List<T> dataList = selectDataList(keys);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return dataList;
		}
		if (checkOnBulkUpdate(dataList)) {
			addActionConfirm(getScenarioMessage(RC.ACTION_CONFIRM_PREFIX, String.valueOf(dataList.size())));
		}
		return dataList;
	}

	/**
	 * doBulkUpdateExecute
	 */
	protected Object doBulkUpdateExecute(Map<String, String[]> args) {
		List<T> keys = convertArgsToList(args);
		final List<T> dataList = selectDataList(keys);
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			setScenarioResult();
			return dataList;
		}

		if (!checkOnBulkUpdate(dataList)) {
			setScenarioResult();
			return dataList;
		}
		
		final MutableInt count = new MutableInt(0);
		try {
			startBulkUpdate(dataList);
			getEntityDao().exec(new Runnable() {
				public void run() {
					count.setValue(updateDataList(dataList));
				}
			});
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			addActionError(getScenarioMessage(RC.ACTION_FAILED_PREFIX, e.getMessage()));
			setScenarioResult();
			return dataList;
		}
		finally {
			finalBulkUpdate(dataList);
		}
		
		if (Collections.isEmpty(dataList)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			setScenarioResult();
			return dataList;
		}

		addActionMessage(getScenarioMessage(RC.ACTION_SUCCESS_PREFIX, String.valueOf(dataList.size())));
		return dataList;
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

	protected List<T> convertArgsToList(Map<String, String[]> args) {
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
			T d = Castors.scast(o, type);
			ds.add(d);
		}

		return ds;
	}
	
	/**
	 * selectDataList
	 * @param dataList dataList
	 * @return dataList
	 */
	protected List<T> selectDataList(List<T> dataList) {
		Collections.removeNull(dataList);
		if (Collections.isNotEmpty(dataList)) {
			GenericQuery<T> q = new GenericQuery<T>(getEntity());

			int count = addKeyListToQuery(q, dataList, false);
			if (count > 0) {
				dataList = daoSelect(q);
			}
			else {
				dataList = null;
			}
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
	 * @param gq generic query
	 * @return sample data
	 */
	protected T getBulkUpdateSample(List<T> dataList, GenericQuery<T> gq) {
		return null;
	}
	
	/**
	 * update data list
	 * @param dataList data list
	 * @return updated count
	 */
	protected int updateDataList(List<T> dataList) {
		if (Collections.isEmpty(dataList)) {
			return 0;
		}

		GenericQuery<T> q = new GenericQuery<T>(getEntity());
		addKeyListToQuery(q, dataList, true);

		T sample = getBulkUpdateSample(dataList, q);
		if (sample == null) {
			return 0;
		}

		int cnt = daoUpdates(sample, q);
		List<T> newList = selectDataList(dataList);

		dataList.clear();
		dataList.addAll(newList);
		
		return cnt;
	}


	/**
	 * finalBulkUpdate
	 * @param dataList data list
	 */
	protected void finalBulkUpdate(List<T> dataList) {
	}

}
