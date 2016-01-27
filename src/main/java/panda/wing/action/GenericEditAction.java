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
	// update fields
	//------------------------------------------------------------
	/**
	 * @return fields to be update
	 */
	protected String[] getUpdateFields() {
		return null;
	}

	/**
	 * used by view
	 * @param field field name
	 * @return true if the field should be display
	 */
	public boolean displayField(String field) {
		return true;
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
	 * update data
	 * 
	 * @param data data
	 * @param fields fields to update
	 * @return count of updated records
	 */ 
	protected int daoUpdate(T data, String... fields) {
		return getEntityDao().update(data, fields);
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
		int cnt = daoUpdate(data, getUpdateFields());
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
	// error message methods
	//------------------------------------------------------------
	protected String getFieldValueErrorText(T data, EntityField ef) {
		StringBuilder sb = new StringBuilder();

		String fn = getTextDataFieldName(ef.getName());
		sb.append(getText(fn));
		sb.append(": ");
		
		Object fv = ef.getValue(data);
		if (fv != null) {
			Property ptag = context.getIoc().get(Property.class);
			ptag.setValue(fv);
			ptag.setEscape(null);
			sb.append(ptag.formatValue());
		}
		
		return sb.toString();
	}
	
	protected void addDataFieldErrors(T data, Collection<EntityField> efs, String fieldErrMsg, String dataErrMsg) {
		StringBuilder sb = new StringBuilder();
		for (EntityField ef :efs) {
			if (!displayField(ef.getName())) {
				continue;
			}
			
			sb.append(getFieldValueErrorText(data, ef));
			sb.append(Streams.LINE_SEPARATOR);
			addFieldError(ef.getName(), getMessage(fieldErrMsg));
		}

		addActionError(getMessage(dataErrMsg, sb.toString()));
	}
	
	protected void addDataDuplicateError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RC.ERROR_FIELDVALUE_DUPLICATE, RC.ERROR_DATA_DUPLICATE);
	}

	protected void addDataIncorrectError(T data, Collection<EntityField> efs) {
		addDataFieldErrors(data, efs, RC.ERROR_FIELDVALUE_INCORRECT, RC.ERROR_DATA_INCORRECT);
	}
	
	//------------------------------------------------------------
	// check methods
	//------------------------------------------------------------
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
					addDataDuplicateError(data, pks);
					return false;
				}
			}
		}
		else {
			Object id = eid.getValue(data);
			if (getEntityDao().isValidIdentity(id) && daoExists(data)) {
				addDataDuplicateError(data, entity.getPrimaryKeys());
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
				addDataDuplicateError(data, ei.getFields());
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
					addDataIncorrectError(data, efk.getFields());
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
	protected String getTextDataFieldName(String pn) {
		return "t." + pn;
	}
}
