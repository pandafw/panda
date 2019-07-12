package panda.app.action.crud;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import panda.app.BusinessRuntimeException;
import panda.app.constant.RES;
import panda.app.entity.Bean;
import panda.app.entity.ICreatedBy;
import panda.app.entity.IUpdatedBy;
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
	 * @param key the input key
	 * @return data object
	 */
	protected Object view(T key) {
		return doViewSelect(key);
	}
	
	/**
	 * view_input
	 * @param data the input data
	 * @return data object
	 */
	protected Object view_input(T data) {
		return doViewInput(data);
	}

	/**
	 * print
	 * @param key the input key
	 * @return data object
	 */
	protected Object print(T key) {
		return doViewSelect(key);
	}

	/**
	 * print_input
	 * @param data the input data
	 * @return data object
	 */
	protected Object print_input(T data) {
		return doViewInput(data);
	}

	/**
	 * copy
	 * @param key the input key
	 * @return data object
	 */
	protected Object copy(T key) {
		return doCopySelect(key);
	}

	/**
	 * copy_input
	 * @param data the input data
	 * @return data object
	 */
	protected Object copy_input(T data) {
		return doCopyInput(data);
	}

	/**
	 * copy_confirm
	 * @param data the input data
	 * @return data object
	 */
	protected Object copy_confirm(T data) {
		return doInsertConfirm(data);
	}

	/**
	 * copy_execute
	 * @param data the input data
	 * @return data object
	 */
	public Object copy_execute(T data) {
		return doInsertExecute(data);
	}

	/**
	 * add
	 * @return data object
	 */
	protected Object add() {
		return doInsertInit();
	}

	/**
	 * add_input
	 * @param data the input data
	 * @return data object
	 */
	protected Object add_input(T data) {
		return doInsertInput(data);
	}

	/**
	 * add_confirm
	 * @param data the input data
	 * @return data object
	 */
	protected Object add_confirm(T data) {
		return doInsertConfirm(data);
	}

	/**
	 * add_execute
	 * @param data the input data
	 * @return data object
	 */
	protected Object add_execute(T data) {
		return doInsertExecute(data);
	}

	/**
	 * add_execute with apimode
	 * @param data the input data
	 * @param apimode the apimode
	 * @return data object
	 */
	protected Object add_execute(T data, boolean apimode) {
		setApimode(apimode);
		return doInsertExecute(data);
	}

	/**
	 * edit
	 * @param key the input key
	 * @return data object
	 */
	protected Object edit(T key) {
		return doUpdateSelect(key);
	}

	/**
	 * edit_input
	 * @param data the input data
	 * @return data object
	 */
	protected Object edit_input(T data) {
		return doUpdateInput(data);
	}

	/**
	 * edit_confirm
	 * @param data the input data
	 * @return data object
	 */
	public Object edit_confirm(T data) {
		return doUpdateConfirm(data);
	}

	/**
	 * edit_check only, commonly used by apimode
	 * @param data the input data
	 * @return data object
	 */
	public Object edit_check(T data) {
		return doUpdateCheck(data);
	}

	/**
	 * edit_execute
	 * @param data the input data
	 * @return data object
	 */
	protected Object edit_execute(T data) {
		return doUpdateExecute(data);
	}

	/**
	 * edit_execute with apimode
	 * @param data the input data
	 * @param apimode the apimode
	 * @return data object
	 */
	protected Object edit_execute(T data, boolean apimode) {
		setApimode(apimode);
		return doUpdateExecute(data);
	}

	/**
	 * delete
	 * @param key the input key
	 * @return data object
	 */
	protected Object delete(T key) {
		return doDeleteSelect(key);
	}

	/**
	 * delete_execute
	 * @param key the input key
	 * @return data object
	 */
	protected Object delete_execute(T key) {
		return doDeleteExecute(key);
	}

	/**
	 * delete_execute with apimode
	 * @param key the input key
	 * @param apimode the apimode
	 * @return data object
	 */
	protected Object delete_execute(T key, boolean apimode) {
		setApimode(apimode);
		return doDeleteExecute(key);
	}

	//------------------------------------------------------------
	// do method
	//------------------------------------------------------------
	/**
	 * doViewInput 
	 * @param data the input data
	 * @return data object
	 */
	protected Object doViewInput(T data) {
		return prepareData(data);
	}

	/**
	 * doViewSelect
	 * @param key the input key
	 * @return data object
	 */
	protected Object doViewSelect(T key) {
		T pkey = prepareKey(key);
		T sdat = selectData(pkey);
		return sdat;
	}

	/**
	 * doCopySelect
	 * @param key the input key
	 * @return data object
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
	 * @param data the input data
	 * @return data object
	 */
	protected Object doCopyInput(T data) {
		return prepareData(data);
	}

	/**
	 * doInsertInit
	 * @return data object
	 */
	protected Object doInsertInit() {
		T pdat = prepareData(null);
		getContext().setParams(pdat);
		return pdat;
	}

	/**
	 * doInsertInput
	 * @param data the input data
	 * @return data object
	 */
	protected Object doInsertInput(T data) {
		return prepareData(data);
	}

	/**
	 * doInsertConfirm
	 * @param data the input data
	 * @return data object
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
	 * @param data the input data
	 * @return data object
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
	 * @param data the input data
	 * @return data object
	 */
	protected Object doUpdateInput(T data) {
		return prepareData(data);
	}

	/**
	 * doUpdateSelect
	 * @param key the input key
	 * @return data object
	 */
	protected Object doUpdateSelect(T key) {
		T pk = prepareKey(key);
		return selectData(pk);
	}

	/**
	 * doUpdateCheck (used for apimode)
	 * @param data the input data
	 * @return data object
	 */
	protected Object doUpdateCheck(T data) {
		T pdat = prepareData(data);
		T sdat = selectData(pdat);
		if (sdat == null) {
			return null;
		}
		
		checkOnUpdate(pdat, sdat);
		return pdat;
	}

	/**
	 * doUpdateConfirm
	 * @param data the input data
	 * @return data object
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
	 * @param data the input data
	 * @return data object
	 */
	protected Object doUpdateExecute(T data) {
		final T pdat = prepareData(data);
		final T sdat = selectData(pdat);
		if (sdat == null) {
			return null;
		}

		if (!checkOnUpdate(pdat, sdat)) {
			setResultOnExecCheckError();
			return pdat;
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
	 * @param key the input key
	 * @return data object
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
	 * @param key the input key
	 * @return data object
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
	 * @param data the input data
	 * @return data object
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
	 * @param data the input data
	 * @return data
	 */
	protected T prepareData(T data) {
		if (data == null) {
			data = Classes.born(getType());
			assist().setCreatedByFields(data);
		}
		return data;
	}

	//------------------------------------------------------------
	// select methods
	//------------------------------------------------------------
	/**
	 * selectData
	 * @param key the input key
	 * @return data the data found from Dao
	 */
	protected T selectData(T key) {
		if (!EntityHelper.hasPrimaryKeyValues(getEntity(), key)) {
			addActionError(getMessage(RES.ERROR_DATA_NOTFOUND));
			return null;
		}
		
		DataQuery<T> gq = getDataQuery();
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

	/**
	 * add query fields
	 * @param gq query
	 */
	protected void addQueryFields(DataQuery<T> gq) {
		Collection<String> ufs = getDisplayFields();
		if (Collections.isNotEmpty(ufs)) {
			gq.excludeAll();
			gq.includePrimayKeys();
			gq.include(ufs);
		}
	}

	/**
	 * add query joins
	 * @param gq query
	 */
	protected void addQueryJoins(DataQuery<T> gq) {
	}

	/**
	 * add query filters
	 * @param gq query
	 * @param key the input key
	 */
	protected void addQueryFilters(DataQuery<T> gq, T key) {
		gq.equalToPrimaryKeys(key);
	}

	/**
	 * checkCommon
	 * @param data the input data
	 * @param sdat the source data (null on insert)
	 * @return true if do something success
	 */
	protected boolean checkCommon(T data, T sdat) {
		Asserts.notNull(data, "data is null");
		return true;
	}

	/**
	 * checkOnInput (Insert & Update)
	 * @param data the input data
	 * @param sdat the source data (null on insert)
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
	 * @param data the input data
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
	 * @param data the input data
	 * @return data object
	 */
	protected T startInsert(T data) {
		assist().setCreatedByFields(data);
		return data;
	}

	/**
	 * insert data
	 * @param data the input data
	 */
	protected void insertData(T data) {
		getDao().insert(data);
	}

	/**
	 * afterInsert
	 * @param data the input data
	 */
	protected void afterInsert(T data) {
	}

	/**
	 * finalInsert
	 * @param data the input data
	 */
	protected void finalInsert(T data) {
	}

	//------------------------------------------------------------
	// update methods
	//------------------------------------------------------------
	/**
	 * @param data the input data
	 * @param sdat the source data
	 * @return the update fields
	 */
	protected Set<String> getUpdateFields(T data, T sdat) {
		Set<String> fs = getDisplayFields();
		if (Collections.isEmpty(fs)) {
			// empty means update all fields
			return fs;
		}
		
		if (data instanceof ICreatedBy || data instanceof IUpdatedBy) {
			fs = new LinkedHashSet<String>(fs);
			if (data instanceof ICreatedBy) {
				fs.remove(ICreatedBy.CREATED_AT);
				fs.remove(ICreatedBy.CREATED_BY);
			}
			if (data instanceof IUpdatedBy) {
				fs.add(IUpdatedBy.UPDATED_AT);
				fs.add(IUpdatedBy.UPDATED_BY);
			}
		}
		return fs;
	}

	/**
	 * checkOnUpdate
	 * @param data the input data
	 * @param sdat the source data
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
	 * @param data the input data
	 * @param sdat the source data
	 * @return data object
	 */
	protected T startUpdate(T data, T sdat) {
		assist().setUpdatedByFields(data, sdat);
		return data;
	}

	/**
	 * update data
	 * @param udat update data
	 * @param sdat the source data
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
	 * @param data the input data
	 * @param sdat the source data
	 */
	protected void afterUpdate(T data, T sdat) {
	}

	/**
	 * finalUpdate
	 * @param data the input data
	 * @param sdat the source data
	 */
	protected void finalUpdate(T data, T sdat) {
	}

	//------------------------------------------------------------
	// delete methods
	//------------------------------------------------------------
	/**
	 * checkOnDelete
	 * @param data the input data
	 * @param sdat the source data
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
	 * @param data the input data
	 */
	protected void startDelete(T data) {
	}

	/**
	 * delete data
	 * @param data the input data
	 */
	protected void deleteData(T data) {
		int cnt = getDao().delete(data);
		if (cnt != 1) {
			throw new RuntimeException("The deleted data count (" + cnt + ") does not equals 1.");
		}
	}

	/**
	 * afterDelete
	 * @param data the input data
	 */
	protected void afterDelete(T data) {
	}

	/**
	 * finalDelete
	 * @param data the input data
	 */
	protected void finalDelete(T data) {
	}

	//------------------------------------------------------------
	// error message methods
	//------------------------------------------------------------
	/**
	 * add data field errors
	 * @param data the input data
	 * @param efs entity fields
	 * @param itemErrMsg item error message
	 * @param dataErrMsg data error message
	 */
	protected void addDataFieldErrors(T data, Collection<EntityField> efs, String itemErrMsg, String dataErrMsg) {
		StringBuilder sb = new StringBuilder();
		for (EntityField ef : efs) {
			EntityField eff = mappedEntityField(ef);
			if (!displayField(eff.getName())) {
				continue;
			}

			addFieldError(eff.getName(), getMessage(itemErrMsg));

			sb.append(Streams.EOL);

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

	/**
	 * add data duplicate error
	 * @param data the input data
	 * @param efs entity fields
	 */
	protected void addDataDuplicateError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RES.ERROR_ITEM_DUPLICATE, RES.ERROR_DATA_DUPLICATE);
	}

	/**
	 * add data incorrect error
	 * @param data the input data
	 * @param efs entity fields
	 */
	protected void addDataIncorrectError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RES.ERROR_ITEM_INCORRECT, RES.ERROR_DATA_INCORRECT);
	}

	/**
	 * add data required error
	 * @param data the input data
	 * @param efs entity fields
	 */
	protected void addDataRequiredError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RES.ERROR_ITEM_REQUIRED, RES.ERROR_ITEM_REQUIRED);
	}
	
	//------------------------------------------------------------
	// check methods
	//------------------------------------------------------------
	/**
	 * checkNotNulls
	 * 
	 * @param data the input data
	 * @return true if check successfully
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
	 * @param data the input data
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
		
		if (getDao().exists(getEntity(), data)) {
			addDataDuplicateError(data, getEntity().getPrimaryKeys());
			return false;
		}
		
		return true;
	}

	/**
	 * @param data the input data
	 * @param sdat the source data
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
	 * @param data the input data
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
	 * @param data the input data
	 * @param sdat the source data
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
	 * @param data the input data
	 * @param sdat the source data
	 * @return true if check successfully
	 */
	protected boolean checkDataChangedOnUpdate(T data, T sdat) {
		return checkDataChanged(data, sdat, RES.WARN_DATA_CHANGED_PREFIX);
	}

	/**
	 * check data changed on delete
	 * @param data the input data
	 * @param sdat the source data
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
	 * @param data the input data
	 * @param sdat the source data
	 * @param msg warn message id
	 * @return true if check successfully
	 */
	protected boolean checkDataChanged(T data, T sdat, String msg) {
		if (data instanceof IUpdatedBy) {
			IUpdatedBy cb = (IUpdatedBy)data;
			IUpdatedBy sb = (IUpdatedBy)sdat;
			if (Bean.isChanged(cb, sb)) {
				cb.setUpdatedBy(sb.getUpdatedBy());
				cb.setUpdatedAt(sb.getUpdatedAt());
				
				addActionWarning(getScenarioMessage(msg, DateTimes.isoDatetimeNotFormat().format(sb.getUpdatedAt())));
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
	 * @param data the input data
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

			assist().setCreatedByFields(data);
		}
	}
}
