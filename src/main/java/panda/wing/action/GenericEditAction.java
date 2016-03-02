package panda.wing.action;

import java.util.Collection;

import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityHelper;
import panda.dao.entity.EntityIndex;
import panda.dao.query.GenericQuery;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.wing.constant.RC;
import panda.wing.entity.Bean;
import panda.wing.entity.IUpdate;


/**
 * @param <T> data type
 */
public abstract class GenericEditAction<T> extends GenericBaseAction<T> {
	private static final Log log = Logs.getLog(GenericEditAction.class);
	
	/**
	 * RESULT_CONFIRM = "confirm";
	 */
	protected final static String RESULT_CONFIRM = "confirm";
	
	//------------------------------------------------------------
	// config properties
	//------------------------------------------------------------
	private boolean checkAbortOnError = false;

	/**
	 * Constructor 
	 */
	public GenericEditAction() {
	}

	//------------------------------------------------------------
	// protected getter & setter
	//------------------------------------------------------------
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

	//------------------------------------------------------------
	// result
	//------------------------------------------------------------
	/**
	 * set result on ?_execute check error occurs  
	 */
	protected void setResultOnExecCheckError() {
		if (hasActionErrors() || hasFieldErrors() || !isInputConfirm()) {
			setScenarioResult();
		}
		else {
			setScenarioResult(RESULT_CONFIRM);
		}
	}

	//------------------------------------------------------------
	// endpoint methods
	//------------------------------------------------------------
	//--------------------------------------------------------------------------
	/**
	 * view
	 */
	protected Object view(T key) {
		return doResult(doViewSelect(key));
	}
	
	/**
	 * view_input
	 */
	protected Object view_input(T data) {
		return doResult(doViewInput(data));
	}

	/**
	 * print
	 */
	protected Object print(T key) {
		return doResult(doViewSelect(key));
	}

	/**
	 * print_input
	 */
	protected Object print_input(T data) {
		return doResult(doViewInput(data));
	}

	/**
	 * copy
	 */
	protected Object copy(T key) {
		return doResult(doCopySelect(key));
	}

	/**
	 * copy_input
	 */
	protected Object copy_input(T data) {
		return doResult(doCopyInput(data));
	}

	/**
	 * copy_confirm
	 */
	protected Object copy_confirm(T data) {
		return doResult(doInsertConfirm(data));
	}

	/**
	 * copy_execute
	 */
	public Object copy_execute(T data) {
		return doResult(doInsertExecute(data));
	}

	/**
	 * add
	 */
	protected Object add() {
		return doResult(doInsertInit());
	}

	/**
	 * add_input
	 */
	protected Object add_input(T data) {
		return doResult(doInsertInput(data));
	}

	/**
	 * add_confirm
	 */
	protected Object add_confirm(T data) {
		return doResult(doInsertConfirm(data));
	}

	/**
	 * add_execute
	 */
	protected Object add_execute(T data) {
		return doResult(doInsertExecute(data));
	}

	/**
	 * edit
	 */
	protected Object edit(T key) {
		return doResult(doUpdateSelect(key));
	}

	/**
	 * edit_input
	 */
	protected Object edit_input(T data) {
		return doResult(doUpdateInput(data));
	}

	/**
	 * edit_confirm
	 */
	public Object edit_confirm(T data) {
		return doResult(doUpdateConfirm(data));
	}

	/**
	 * edit_execute
	 */
	protected Object edit_execute(T data) {
		return doResult(doUpdateExecute(data));
	}

	/**
	 * delete
	 */
	protected Object delete(T key) {
		return doResult(doDeleteSelect(key));
	}

	/**
	 * delete_execute
	 */
	protected Object delete_execute(T key) {
		return doResult(doDeleteExecute(key));
	}

	//------------------------------------------------------------
	// do method
	//------------------------------------------------------------
	/**
	 * doViewInput 
	 */
	protected Object doViewInput(T data) {
		return data;
	}

	/**
	 * doViewSelect
	 */
	protected Object doViewSelect(T key) {
		return selectData(key);
	}

	/**
	 * doCopySelect
	 */
	protected Object doCopySelect(T key) {
		T sd = selectData(key);
		if (sd != null) {
			clearOnCopy(sd);
			clearOnCopy(key);
		}
		return sd;
	}

	/**
	 * doCopyInput
	 */
	protected Object doCopyInput(T data) {
		return prepareDefaultData(data);
	}

	/**
	 * doInsertInit
	 */
	protected Object doInsertInit() {
		T data = prepareDefaultData(null);
		getContext().setParams(data);
		return data;
	}

	/**
	 * doInsertInput
	 */
	protected Object doInsertInput(T data) {
		return prepareDefaultData(data);
	}

	/**
	 * doInsertConfirm
	 */
	protected Object doInsertConfirm(T data) {
		data = prepareDefaultData(data);
		if (checkOnInsert(data)) {
			addActionConfirm(getScenarioMessage(RC.ACTION_CONFIRM_PREFIX));
		}
		else {
			if (hasActionErrors() || hasFieldErrors()) {
				setScenarioResult();
			}
		}
		return data;
	}

	/**
	 * doInsertExecute
	 */
	protected Object doInsertExecute(T data) {
		data = prepareDefaultData(data);
		if (!checkOnInsert(data)) {
			setResultOnExecCheckError();
			return data;
		}

		try {
			startInsert(data);
			final T d = data;
			getDao().exec(new Runnable() {
				public void run() {
					insertData(d);
				}
			});
		}
		catch (Throwable e) {
			log.error(e.getMessage(), e);
			addActionError(getScenarioMessage(RC.ACTION_FAILED_PREFIX, e.getMessage()));
			setScenarioResult();
			return data;
		}
		finally {
			finalInsert(data);
		}

		addActionMessage(getScenarioMessage(RC.ACTION_SUCCESS_PREFIX));
		return data;
	}

	/**
	 * doUpdateInput
	 */
	protected Object doUpdateInput(T data) {
		return data;
	}

	/**
	 * doUpdateSelect
	 */
	protected Object doUpdateSelect(T key) {
		return selectData(key);
	}

	/**
	 * doUpdateConfirm
	 */
	protected Object doUpdateConfirm(T data) {
		T sd = selectData(data);
		if (sd == null) {
			return null;
		}
		
		if (checkOnUpdate(data, sd)) {
			addActionConfirm(getScenarioMessage(RC.ACTION_CONFIRM_PREFIX));
		}
		else {
			if (hasActionErrors() || hasFieldErrors()) {
				setScenarioResult();
			}
		}
		return data;
	}

	public boolean isInputConfirm() {
		return getTextAsBoolean(RC.UI_INPUT_CONFIRM, false);
	}
	
	/**
	 * doUpdateExecute
	 */
	protected Object doUpdateExecute(final T data) {
		final T sd = selectData(data);
		if (sd == null) {
			return null;
		}

		if (!checkOnUpdate(data, sd)) {
			setResultOnExecCheckError();
			return data;
		}

		try {
			startUpdate(data, sd);
			getDao().exec(new Runnable() {
				public void run() {
					updateData(data, sd);
				}
			});
		}
		catch (Throwable e) {
			log.error(e.getMessage(), e);
			addActionError(getScenarioMessage(RC.ACTION_FAILED_PREFIX, e.getMessage()));
			setScenarioResult();
			return data;
		}
		finally {
			finalUpdate(data, sd);
		}

		addActionMessage(getScenarioMessage(RC.ACTION_SUCCESS_PREFIX));
		return data;
	}

	/**
	 * doDeleteSelect
	 */
	protected Object doDeleteSelect(T key) {
		T sd = selectData(key);
		if (sd == null) {
			return null;
		}
		
		if (checkOnDelete(key, sd)) {
			addActionConfirm(getScenarioMessage(RC.ACTION_CONFIRM_PREFIX));
		}
		return sd;
	}

	/**
	 * doDeleteExecute
	 */
	protected Object doDeleteExecute(final T key) {
		final T sd = selectData(key);
		if (sd == null) {
			return null;
		}

		if (!checkOnDelete(key, sd)) {
			return sd;
		}
		
		try {
			startDelete(sd);
			getDao().exec(new Runnable() {
				public void run() {
					deleteData(sd);
				}
			});
		}
		catch (Throwable e) {
			log.error(e.getMessage(), e);
			addActionError(getScenarioMessage(RC.ACTION_FAILED_PREFIX, e.getMessage()));
			setScenarioResult();
			return sd;
		}
		finally {
			finalDelete(sd);
		}
		
		addActionMessage(getScenarioMessage(RC.ACTION_SUCCESS_PREFIX));
		return sd;
	}

	//------------------------------------------------------------
	// data methods
	//------------------------------------------------------------
	/**
	 * trim data
	 * @param d data object
	 */
	protected T trimData(T d) {
		return d;
	}
	
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

	//------------------------------------------------------------
	// select methods
	//------------------------------------------------------------
	/**
	 * selectData
	 * @param key key
	 * @return data data found
	 */
	protected T selectData(T key) {
		if (!EntityHelper.hasPrimaryKeyValues(getEntity(), key)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return null;
		}
		
		GenericQuery<T> gq = new GenericQuery<T>(getEntity());
		addQueryFields(gq);
		addQueryJoins(gq);
		addQueryFilters(gq, key);
		
		T d = getDao().fetch(gq);
		if (d == null) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return null;
		}

		d = trimData(d);
		if (d == null) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return null;
		}
		return d;
	}

	protected void addQueryFields(GenericQuery<T> gq) {
		Collection<String> ufs = getDisplayFields();
		if (Collections.isNotEmpty(ufs)) {
			gq.excludeAll();
			gq.includePrimayKeys();
			gq.include(ufs);
		}
	}

	protected void addQueryJoins(GenericQuery<T> gq) {
	}
	
	protected void addQueryFilters(GenericQuery<T> gq, T key) {
		gq.equalToPrimaryKeys(key);
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

	/**
	 * checkOnInput (Insert & Update)
	 * @param data data
	 * @param srcData source data (null on insert)
	 * @return true if do something success
	 */
	protected boolean checkOnInput(T data, T srcData) {
		Asserts.notNull(data, "data is null");
		return true;
	}

	//------------------------------------------------------------
	// insert methods
	//------------------------------------------------------------
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
		
		if (!checkOnInput(data, null)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkPrimaryKeysOnInsert(data)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkUniqueKeysOnInsert(data)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkForeignKeys(data)) {
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
		assist().initCommonFields(data);
	}

	/**
	 * insert data
	 * @param data data
	 */
	protected void insertData(T data) {
		getDao().insert(data);
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
		int cnt = getDao().update(data, getDisplayFields());
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
		
		if (!checkOnInput(data, null)) {
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
		if (!checkPrimaryKeysOnUpdate(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkUniqueKeysOnUpdate(data, srcData)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkForeignKeys(data)) {
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
		assist().initUpdateFields(data, srcData);
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
		int cnt = getDao().delete(data);
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
	// error message methods
	//------------------------------------------------------------
	protected void addDataFieldErrors(T data, Collection<EntityField> efs, String itemErrMsg, String dataErrMsg) {
		StringBuilder sb = new StringBuilder();
		for (EntityField ef : efs) {
			if (!displayField(ef.getName())) {
				continue;
			}
			
			EntityField eff = mappedEntityField(ef);

			addFieldError(eff.getName(), getMessage(itemErrMsg));

			sb.append(Streams.LINE_SEPARATOR);

			String label = getFieldLabel(eff.getName());
			sb.append(label);
			sb.append(": ");
			
			Object fv = eff.getValue(data);
			if (fv != null) {
				sb.append(Mvcs.castString(context, fv));
			}
		}

		addActionError(getMessage(dataErrMsg, sb.toString()));
	}
	
	protected void addDataDuplicateError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RC.ERROR_ITEM_DUPLICATE, RC.ERROR_DATA_DUPLICATE);
	}

	protected void addDataIncorrectError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RC.ERROR_ITEM_INCORRECT, RC.ERROR_DATA_INCORRECT);
	}
	
	//------------------------------------------------------------
	// check methods
	//------------------------------------------------------------
	/**
	 * checkPrimaryKeyOnInsert
	 * @param data data
	 * @return true if check successfully
	 */
	protected boolean checkPrimaryKeysOnInsert(T data) {
		if (EntityHelper.checkPrimaryKeys(getDao(), getEntity(), data)) {
			return true;
		}

		addDataDuplicateError(data, getEntity().getPrimaryKeys());
		return false;
	}

	/**
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkPrimaryKeysOnUpdate(T data, T srcData) {
		if (EntityHelper.hasPrimaryKeyValues(getEntity(), data)) {
			return true;
		}

		addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
		return false;
	}

	protected boolean checkUniqueIndex(T data, EntityIndex ei) {
		if (EntityHelper.checkUniqueIndex(getDao(), getEntity(), data, ei)) {
			return true;
		}

		addDataDuplicateError(data, ei.getFields());
		return false;
	}

	/**
	 * checkUniqueKeysOnInsert
	 * @return true if check successfully
	 */
	protected boolean checkUniqueKeysOnInsert(T data) {
		Collection<EntityIndex> eis = getEntity().getIndexes();
		if (Collections.isEmpty(eis)) {
			return true;
		}
		
		for (EntityIndex ei : eis) {
			if (!checkUniqueIndex(data, ei)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checkUniqueKeysOnUpdate
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkUniqueKeysOnUpdate(T data, T srcData) {
		Collection<EntityIndex> eis = getEntity().getIndexes();
		if (Collections.isEmpty(eis)) {
			return true;
		}
		
		for (EntityIndex ei : eis) {
			if (!ei.isUnique()) {
				continue;
			}

			if (EntityHelper.isDifferent(ei.getFields(), data, srcData)) {
				if (!checkUniqueIndex(data, ei)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * checkForeignKeys
	 * @param data
	 * @return true if check successfully
	 */
	protected boolean checkForeignKeys(T data) {
		Collection<EntityFKey> efks = getEntity().getForeignKeys();
		if (Collections.isEmpty(efks)) {
			return true;
		}
		
		for (EntityFKey efk : efks) {
			if (!EntityHelper.checkForeignKey(getDao(), getEntity(), data, efk)) {
				addDataIncorrectError(data, efk.getFields());
				return false;
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
		return checkUpdated(data, srcData, RC.WARN_DATA_CHANGED_PREFIX);
	}

	/**
	 * checkUpdatedOnDelete
	 * @param data data
	 * @param srcData srcData
	 * @return true if check successfully
	 */
	protected boolean checkUpdatedOnDelete(T data, T srcData) {
		if (checkUpdated(data, srcData, RC.WARN_DATA_CHANGED_PREFIX)) {
			return true;
		}

		setScenarioResult();
		return false;
	}

	/**
	 * checkUpdated
	 * @param data data
	 * @param srcData source data
	 * @param msg warn message id
	 * @return true if check successfully
	 */
	protected boolean checkUpdated(T data, T srcData, String msg) {
		if (data instanceof IUpdate) {
			IUpdate cb = (IUpdate)data;
			IUpdate sb = (IUpdate)srcData;
			if (Bean.isUpdated(cb, sb)) {
				cb.setUusid(sb.getUusid());
				cb.setUtime(sb.getUtime());
				
				addActionWarning(getScenarioMessage(msg, DateTimes.datetimeFormat().format(sb.getUtime())));
				return false;
			}
		}
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
			EntityField eid = getEntity().getIdentity(); 
			if (eid == null) {
				EntityHelper.clearPrimaryKeyValues(getEntity(), data);
			}
			else {
				EntityHelper.clearIdentityValue(getEntity(), data);
			}
		}
	}
}
