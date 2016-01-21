package panda.wing.action;

import java.util.Collection;
import java.util.List;

import panda.cast.Castors;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityIndex;
import panda.dao.query.GenericQuery;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.view.tag.Property;
import panda.wing.constant.RC;
import panda.wing.entity.Bean;
import panda.wing.entity.IUpdate;


/**
 * @param <T> data type
 */
public abstract class GenericEditAction<T> extends GenericBaseAction<T> {
	private static final Log log = Logs.getLog(GenericEditAction.class);
	
	//------------------------------------------------------------
	// config properties
	//------------------------------------------------------------
	private boolean checkAbortOnError = false;
	private boolean updateSelective = false;

	/**
	 * Constructor 
	 */
	public GenericEditAction() {
	}

	//------------------------------------------------------------
	// protected getter & setter
	//------------------------------------------------------------
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
	 * insert
	 */
	protected Object insert() {
		return doResult(doInsertInit());
	}

	/**
	 * insert_input
	 */
	protected Object insert_input(T data) {
		return doResult(doInsertInput(data));
	}

	/**
	 * insert_confirm
	 */
	protected Object insert_confirm(T data) {
		return doResult(doInsertConfirm(data));
	}

	/**
	 * insert_execute
	 */
	protected Object insert_execute(T data) {
		return doResult(doInsertExecute(data));
	}

	/**
	 * update
	 */
	protected Object update(T key) {
		return doResult(doUpdateSelect(key));
	}

	/**
	 * update_input
	 */
	protected Object update_input(T data) {
		return doResult(doUpdateInput(data));
	}

	/**
	 * update_confirm
	 */
	public Object update_confirm(T data) {
		return doResult(doUpdateConfirm(data));
	}

	/**
	 * update_execute
	 */
	protected Object update_execute(T data) {
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
			getEntityDao().exec(new Runnable() {
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
			getEntityDao().exec(new Runnable() {
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
			getEntityDao().exec(new Runnable() {
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
	protected void trimData(T d) {
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
		if (!hasPrimaryKeyValues(key)) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return null;
		}
		
		T d = daoFetch(key);
		if (d == null) {
			addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
			return null;
		}

		trimData(d);
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
		assist().initCommonFields(data);
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

			addFieldError(ef.getName(), getMessage(RC.ERROR_FIELDVALUE_DUPLICATE));
		}

		addActionError(getMessage(RC.ERROR_DATA_DUPLICATE, sb.toString()));
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
		if (hasPrimaryKeyValues(data)) {
			return true;
		}

		addActionError(getMessage(RC.ERROR_DATA_NOTFOUND));
		return false;
	}

	/**
	 * @param data data
	 * @return true if check successfully
	 */
	protected boolean hasPrimaryKeyValues(T data) {
		for (EntityField ef : getEntity().getPrimaryKeys()) {
			Object dv = ef.getValue(data);
			if (dv == null) {
				return false;
			}
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
						addFieldError(getFullDataFieldName(ef.getName()), getMessage(RC.ERROR_FIELDVALUE_INCORRECT));
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
			addActionConfirm(getMessage(RC.CONFIRM_DATA_OVERWRITE));
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
			addActionConfirm(getScenarioMessage(RC.ACTION_CONFIRM_PREFIX));
			setScenarioResult();
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
		if (data instanceof IUpdate) {
			IUpdate cb = (IUpdate)data;
			IUpdate sb = (IUpdate)srcData;
			if (Bean.isUpdated(cb, sb)) {
				cb.setUusid(sb.getUusid());
				cb.setUtime(sb.getUtime());
				
				addActionWarning(getMessage(RC.WARN_DATA_UPDATED, DateTimes.datetimeFormat().format(sb.getUtime())));
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
			EntityField eid = entity.getIdentity(); 
			if (eid == null) {
				clearPrimaryKeyValues(data);
			}
			else {
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
	 * @param pn property name
	 * @return dataName + "." + propertyName
	 */
	protected String getFullDataFieldName(String pn) {
		return "p." + pn;
	}
}
