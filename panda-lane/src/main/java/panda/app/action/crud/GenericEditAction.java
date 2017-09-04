package panda.app.action.crud;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import panda.app.BusinessRuntimeException;
import panda.app.constant.RES;
import panda.app.entity.Bean;
import panda.app.entity.IUpdate;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityHelper;
import panda.dao.entity.EntityIndex;
import panda.dao.query.DataQuery;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;


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
	// setting
	//------------------------------------------------------------
	public boolean isInputConfirm() {
		return getTextAsBoolean(RES.UI_INPUT_CONFIRM, false);
	}
	
	//------------------------------------------------------------
	// result
	//------------------------------------------------------------
	/**
	 * set result on ?_execute check error occurs  
	 */
	protected void setResultOnExecCheckError() {
		if (hasActionErrors() || hasFieldErrors() || !isInputConfirm()) {
			setScenarioView();
		}
		else {
			setScenarioView(RESULT_CONFIRM);
		}
	}

	//------------------------------------------------------------
	// end point methods
	//------------------------------------------------------------
	/**
	 * view
	 */
	protected Object view(T key) {
		return doViewSelect(key);
	}
	
	/**
	 * view_input
	 */
	protected Object view_input(T data) {
		return doViewInput(data);
	}

	/**
	 * print
	 */
	protected Object print(T key) {
		return doViewSelect(key);
	}

	/**
	 * print_input
	 */
	protected Object print_input(T data) {
		return doViewInput(data);
	}

	/**
	 * copy
	 */
	protected Object copy(T key) {
		return doCopySelect(key);
	}

	/**
	 * copy_input
	 */
	protected Object copy_input(T data) {
		return doCopyInput(data);
	}

	/**
	 * copy_confirm
	 */
	protected Object copy_confirm(T data) {
		return doInsertConfirm(data);
	}

	/**
	 * copy_execute
	 */
	public Object copy_execute(T data) {
		return doInsertExecute(data);
	}

	/**
	 * add
	 */
	protected Object add() {
		return doInsertInit();
	}

	/**
	 * add_input
	 */
	protected Object add_input(T data) {
		return doInsertInput(data);
	}

	/**
	 * add_confirm
	 */
	protected Object add_confirm(T data) {
		return doInsertConfirm(data);
	}

	/**
	 * add_execute
	 */
	protected Object add_execute(T data) {
		return doInsertExecute(data);
	}

	/**
	 * edit
	 */
	protected Object edit(T key) {
		return doUpdateSelect(key);
	}

	/**
	 * edit_input
	 */
	protected Object edit_input(T data) {
		return doUpdateInput(data);
	}

	/**
	 * edit_confirm
	 */
	public Object edit_confirm(T data) {
		return doUpdateConfirm(data);
	}

	/**
	 * edit_execute
	 */
	protected Object edit_execute(T data) {
		return doUpdateExecute(data);
	}

	/**
	 * delete
	 */
	protected Object delete(T key) {
		return doDeleteSelect(key);
	}

	/**
	 * delete_execute
	 */
	protected Object delete_execute(T key) {
		return doDeleteExecute(key);
	}

	//------------------------------------------------------------
	// do method
	//------------------------------------------------------------
	/**
	 * doViewInput 
	 */
	protected Object doViewInput(T data) {
		return prepareData(data);
	}

	/**
	 * doViewSelect
	 */
	protected Object doViewSelect(T key) {
		T pkey = prepareKey(key);
		T sdat = selectData(pkey);
		return sdat;
	}

	/**
	 * doCopySelect
	 */
	protected Object doCopySelect(T key) {
		T pkey = prepareKey(key);
		T sdat = selectData(pkey);
		if (sdat != null) {
			clearOnCopy(sdat);
			clearOnCopy(key);
		}
		return sdat;
	}

	/**
	 * doCopyInput
	 */
	protected Object doCopyInput(T data) {
		return prepareData(data);
	}

	/**
	 * doInsertInit
	 */
	protected Object doInsertInit() {
		T pdat = prepareData(null);
		getContext().setParams(pdat);
		return pdat;
	}

	/**
	 * doInsertInput
	 */
	protected Object doInsertInput(T data) {
		return prepareData(data);
	}

	/**
	 * doInsertConfirm
	 */
	protected Object doInsertConfirm(T data) {
		T pdat = prepareData(data);
		if (checkOnInsert(pdat)) {
			addActionConfirm(getScenarioMessage(RES.ACTION_CONFIRM_PREFIX));
		}
		else {
			if (hasActionErrors() || hasFieldErrors()) {
				setScenarioView();
			}
		}
		return pdat;
	}

	/**
	 * doInsertExecute
	 */
	protected Object doInsertExecute(T data) {
		final T pdat = prepareData(data);
		if (!checkOnInsert(pdat)) {
			setResultOnExecCheckError();
			return pdat;
		}

		try {
			final T id = startInsert(pdat);
			// Required fields are check by validators,
			// but some fields are initialed by program,
			// we check these not null fields to prevent SQL error
			if (!checkNotNulls(id)) {
				setResultOnExecCheckError();
				return pdat;
			}
			getDao().exec(new Runnable() {
				public void run() {
					insertData(id);
					EntityHelper.copyIdentityValue(getEntity(), id, pdat);
				}
			});
			afterInsert(pdat);
		}
		catch (Throwable e) {
			if (e instanceof BusinessRuntimeException) {
				log.warn(e.getMessage(), e);
			}
			else {
				log.error(e.getMessage(), e);
			}
			addSystemError(RES.ACTION_FAILED_PREFIX, e);
			setScenarioView();
			return data;
		}
		finally {
			finalInsert(pdat);
		}

		addActionMessage(getScenarioMessage(RES.ACTION_SUCCESS_PREFIX));
		return pdat;
	}
	
	/**
	 * doUpdateInput
	 */
	protected Object doUpdateInput(T data) {
		return prepareData(data);
	}

	/**
	 * doUpdateSelect
	 */
	protected Object doUpdateSelect(T key) {
		T pk = prepareKey(key);
		return selectData(pk);
	}

	/**
	 * doUpdateConfirm
	 */
	protected Object doUpdateConfirm(T data) {
		T pdat = prepareData(data);
		T sdat = selectData(pdat);
		if (sdat == null) {
			return null;
		}
		
		if (checkOnUpdate(pdat, sdat)) {
			addActionConfirm(getScenarioMessage(RES.ACTION_CONFIRM_PREFIX));
		}
		else {
			if (hasActionErrors() || hasFieldErrors()) {
				setScenarioView();
			}
		}
		return pdat;
	}
	
	/**
	 * doUpdateExecute
	 */
	protected Object doUpdateExecute(T data) {
		final T pdat = prepareData(data);
		final T sdat = selectData(pdat);
		if (sdat == null) {
			return null;
		}

		if (!checkOnUpdate(pdat, sdat)) {
			setResultOnExecCheckError();
			return data;
		}

		try {
			final T udat = startUpdate(pdat, sdat);
			getDao().exec(new Runnable() {
				public void run() {
					updateData(udat, sdat);
				}
			});
			afterUpdate(pdat, sdat);
		}
		catch (Throwable e) {
			if (e instanceof BusinessRuntimeException) {
				log.warn(e.getMessage(), e);
			}
			else {
				log.error(e.getMessage(), e);
			}
			addSystemError(RES.ACTION_FAILED_PREFIX, e);
			setScenarioView();
			return pdat;
		}
		finally {
			finalUpdate(pdat, sdat);
		}

		addActionMessage(getScenarioMessage(RES.ACTION_SUCCESS_PREFIX));
		return pdat;
	}

	/**
	 * doDeleteSelect
	 */
	protected Object doDeleteSelect(T key) {
		final T pkey = prepareKey(key);
		final T sdat = selectData(pkey);
		if (sdat == null) {
			return null;
		}
		
		if (checkOnDelete(pkey, sdat)) {
			addActionConfirm(getScenarioMessage(RES.ACTION_CONFIRM_PREFIX));
		}
		return sdat;
	}

	/**
	 * doDeleteExecute
	 */
	protected Object doDeleteExecute(final T key) {
		final T pkey = prepareKey(key);
		final T sdat = selectData(pkey);
		if (sdat == null) {
			return null;
		}

		if (!checkOnDelete(pkey, sdat)) {
			return sdat;
		}
		
		try {
			startDelete(sdat);
			getDao().exec(new Runnable() {
				public void run() {
					deleteData(sdat);
				}
			});
			afterDelete(sdat);
		}
		catch (Throwable e) {
			if (e instanceof BusinessRuntimeException) {
				log.warn(e.getMessage(), e);
			}
			else {
				log.error(e.getMessage(), e);
			}
			addSystemError(RES.ACTION_FAILED_PREFIX, e);
			setScenarioView();
			return sdat;
		}
		finally {
			finalDelete(sdat);
		}
		
		addActionMessage(getScenarioMessage(RES.ACTION_SUCCESS_PREFIX));
		return sdat;
	}

	//------------------------------------------------------------
	// data methods
	//------------------------------------------------------------
	/**
	 * trim data
	 * @param data data object
	 */
	protected T trimData(T data) {
		return data;
	}
	
	/**
	 * prepare key for select data from store
	 * @param key key
	 * @return key
	 */
	protected T prepareKey(T key) {
		return key;
	}
	
	/**
	 * prepare default data
	 * @param data data
	 * @return data
	 */
	protected T prepareData(T data) {
		if (data == null) {
			data = Classes.born(getType());
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
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			return null;
		}
		
		DataQuery<T> gq = new DataQuery<T>(getEntity());
		addQueryFields(gq);
		addQueryJoins(gq);
		addQueryFilters(gq, key);
		
		T d = getDao().fetch(gq);
		if (d == null) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			return null;
		}

		d = trimData(d);
		if (d == null) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			return null;
		}
		return d;
	}

	protected void addQueryFields(DataQuery<T> gq) {
		Collection<String> ufs = getDisplayFields();
		if (Collections.isNotEmpty(ufs)) {
			gq.excludeAll();
			gq.includePrimayKeys();
			gq.include(ufs);
		}
	}

	protected void addQueryJoins(DataQuery<T> gq) {
	}
	
	protected void addQueryFilters(DataQuery<T> gq, T key) {
		gq.equalToPrimaryKeys(key);
	}

	/**
	 * checkCommon
	 * @param data data
	 * @param sdat source data (null on insert)
	 * @return true if do something success
	 */
	protected boolean checkCommon(T data, T sdat) {
		Asserts.notNull(data, "data is null");
		return true;
	}

	/**
	 * checkOnInput (Insert & Update)
	 * @param data data
	 * @param sdat source data (null on insert)
	 * @return true if do something success
	 */
	protected boolean checkOnInput(T data, T sdat) {
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

		// !IMPORTANT: do not change check order
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
		if (!checkUniqueIndexesOnInsert(data)) {
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
	protected T startInsert(T data) {
		assist().initCommonFields(data);
		return data;
	}

	/**
	 * insert data
	 * @param data data
	 */
	protected void insertData(T data) {
		getDao().insert(data);
	}

	/**
	 * afterInsert
	 * @param data data
	 */
	protected void afterInsert(T data) {
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
	 * @return the update fields
	 */
	protected Set<String> getUpdateFields(T data, T sdat) {
		return getDisplayFields();
	}

	/**
	 * checkOnUpdate
	 * @param data data
	 * @param sdat source data
	 * @return true if check success
	 */
	protected boolean checkOnUpdate(T data, T sdat) {
		boolean c = true;

		// !IMPORTANT: do not change check order
		if (!checkCommon(data, sdat)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkOnInput(data, sdat)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkDataChangedOnUpdate(data, sdat)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		// primary key can not be modified or null
		if (!checkPrimaryKeysOnUpdate(data, sdat)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		if (!checkUniqueIndexesOnUpdate(data, sdat)) {
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
	 * @param data input data
	 * @param sdat source data
	 */
	protected T startUpdate(T data, T sdat) {
		assist().initUpdateFields(data, sdat);
		return data;
	}

	/**
	 * update data
	 * @param udat update data
	 * @param sdat source data
	 * @return update count
	 */
	protected int updateData(T udat, T sdat) {
		int cnt = getDao().update(udat, getUpdateFields(udat, sdat));
		if (cnt != 1) {
			throw new RuntimeException("The update data count (" + cnt + ") does not equals 1.");
		}
		return cnt;
	}

	/**
	 * afterUpdate
	 * @param data data
	 * @param sdat source data
	 */
	protected void afterUpdate(T data, T sdat) {
	}

	/**
	 * finalUpdate
	 * @param data data
	 * @param sdat source data
	 */
	protected void finalUpdate(T data, T sdat) {
	}

	//------------------------------------------------------------
	// delete methods
	//------------------------------------------------------------
	/**
	 * checkOnDelete
	 * @param data data
	 * @param sdat source data
	 * @return true if check success
	 */
	protected boolean checkOnDelete(T data, T sdat) {
		boolean c = true;
		
		if (!checkCommon(data, sdat)) {
			c = false;
			if (checkAbortOnError) {
				return false;
			}
		}
		
		if (!checkDataChangedOnDelete(data, sdat)) {
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
	 * afterDelete
	 * @param data data
	 */
	protected void afterDelete(T data) {
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
			EntityField eff = mappedEntityField(ef);
			if (!displayField(eff.getName())) {
				continue;
			}

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
		addDataFieldErrors(data, efs, RES.ERROR_ITEM_DUPLICATE, RES.ERROR_DATA_DUPLICATE);
	}

	protected void addDataIncorrectError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RES.ERROR_ITEM_INCORRECT, RES.ERROR_DATA_INCORRECT);
	}

	protected void addDataRequiredError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RES.ERROR_ITEM_REQUIRED, RES.ERROR_ITEM_REQUIRED);
	}
	
	//------------------------------------------------------------
	// check methods
	//------------------------------------------------------------
	/**
	 * checkNotNulls
	 * 
	 * @param data data
	 */
	protected boolean checkNotNulls(T data) {
		List<EntityField> efs = EntityHelper.checkNotNulls(getEntity(), data);
		if (Collections.isNotEmpty(efs)) {
			addDataRequiredError(data, efs);
			return false;
		}
		return true;
	}
	
	/**
	 * checkPrimaryKeyOnInsert
	 * @param data data
	 * @return true if check successfully
	 */
	protected boolean checkPrimaryKeysOnInsert(T data) {
		EntityField eid = getEntity().getIdentity(); 
		if (eid == null) {
			if (!EntityHelper.hasPrimaryKeyValues(getEntity(), data)) {
				addDataIncorrectError(data, getEntity().getPrimaryKeys());
				return false;
			}
		}
		else {
			Object id = eid.getValue(data);
			if (!getDao().isValidIdentity(id)) {
				return true;
			}
		}
		
		if (!getDao().exists(getEntity(), data)) {
			addDataDuplicateError(data, getEntity().getPrimaryKeys());
			return false;
		}
		
		return true;
	}

	/**
	 * @param data data
	 * @param sdat source data
	 * @return true if check successfully
	 */
	protected boolean checkPrimaryKeysOnUpdate(T data, T sdat) {
		if (EntityHelper.hasPrimaryKeyValues(getEntity(), data)) {
			return true;
		}

		addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
		return false;
	}

	/**
	 * checkUniqueIndexesOnInsert
	 * @return true if check successfully
	 */
	protected boolean checkUniqueIndexesOnInsert(T data) {
		EntityIndex ei = EntityHelper.findDuplicateUniqueIndex(getDao(), getEntity(), data, null);
		if (ei == null) {
			return true;
		}
		
		addDataDuplicateError(data, ei.getFields());
		return false;
	}

	/**
	 * checkUniqueIndexesOnUpdate
	 * @param data data
	 * @param sdat source data
	 * @return true if check successfully
	 */
	protected boolean checkUniqueIndexesOnUpdate(T data, T sdat) {
		EntityIndex ei = EntityHelper.findDuplicateUniqueIndex(getDao(), getEntity(), data, sdat);
		if (ei == null) {
			return true;
		}
		
		addDataDuplicateError(data, ei.getFields());
		return false;
	}

	/**
	 * checkForeignKeys
	 * @param data
	 * @return true if check successfully
	 */
	protected boolean checkForeignKeys(T data) {
		EntityFKey efk = EntityHelper.findIncorrectForeignKey(getDao(), getEntity(), data);
		if (efk == null) {
			return true;
		}
		
		addDataIncorrectError(data, efk.getFields());
		return false;
	}

	/**
	 * check data is changed on update
	 * @param data data
	 * @param sdat source data
	 * @return true if check successfully
	 */
	protected boolean checkDataChangedOnUpdate(T data, T sdat) {
		return checkDataChanged(data, sdat, RES.WARN_DATA_CHANGED_PREFIX);
	}

	/**
	 * check data changed on delete
	 * @param data data
	 * @param sdat source data
	 * @return true if check successfully
	 */
	protected boolean checkDataChangedOnDelete(T data, T sdat) {
		if (checkDataChanged(data, sdat, RES.WARN_DATA_CHANGED_PREFIX)) {
			return true;
		}

		setScenarioView();
		return false;
	}

	/**
	 * check data is changed or not
	 * @param data data
	 * @param sdat source data
	 * @param msg warn message id
	 * @return true if check successfully
	 */
	protected boolean checkDataChanged(T data, T sdat, String msg) {
		if (data instanceof IUpdate) {
			IUpdate cb = (IUpdate)data;
			IUpdate sb = (IUpdate)sdat;
			if (Bean.isChanged(cb, sb)) {
				cb.setUusid(sb.getUusid());
				cb.setUtime(sb.getUtime());
				
				addActionWarning(getScenarioMessage(msg, DateTimes.isoDatetimeNotFormat().format(sb.getUtime())));
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
