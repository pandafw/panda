package panda.wing.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.cast.Castors;
import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
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
import panda.lang.mutable.MutableInt;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.view.tag.Property;
import panda.wing.constant.RC;
import panda.wing.entity.Bean;
import panda.wing.entity.IUpdate;


/**
 * @param <T> data type
 */
public abstract class GenericEditAction<T> extends AbstractAction {
	private static final Log log = Logs.getLog(GenericEditAction.class);
	
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
	private String[] viewScenarios = { "view", "print", "delete" };

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
	public GenericEditAction() {
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
	
	//------------------------------------------------------------
	// public properties getter
	//------------------------------------------------------------
	/**
	 * @return the actionScenario
	 */
	public String getActionScenario() {
		if (actionScenario == null) {
			actionScenario = Strings.substringBefore(context.getMethodName(), METHOD_SEPARATOR);
		}
		return actionScenario;
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
	// endpoint methods
	//------------------------------------------------------------
	//--------------------------------------------------------------------------
	/**
	 * bdelete
	 */
	protected Object bdelete() {
		doBulkDeleteSelect();
		return null;
	}

	/**
	 * bdelete_execute
	 */
	protected Object bdelete_execute() {
		doBulkDeleteExecute();
		return null;
	}

	/**
	 * bupdate
	 */
	protected Object bupdate() {
		doBulkUpdateSelect();
		return null;
	}

	/**
	 * bupdate_execute
	 */
	protected Object bupdate_execute() {
		doBulkUpdateExecute();
		return null;
	}
	
//	/**
//	 * bedit
//	 * @return SUCCESS
//	 */
//	protected String bedit() {
//		return doBulkEditInput();
//	}
//
//	/**
//	 * bedit_input
//	 * @return SUCCESS
//	 */
//	protected String bedit_input() {
//		return doBulkEditInput();
//	}
//
//	/**
//	 * bedit_confirm
//	 * @return SUCCESS
//	 */
//	protected String bedit_confirm() {
//		return doBulkEditConfirm();
//	}
//
//	/**
//	 * bedit_execute
//	 * @return SUCCESS
//	 */
//	protected String bedit_execute() {
//		return doBulkEditExecute();
//	}

	//--------------------------------------------------------------------------
	/**
	 * view
	 */
	protected Object view() {
		doViewSelect();
		return null;
	}
	
	/**
	 * view_input
	 */
	protected Object view_input() {
		doViewInput();
		return null;
	}

	/**
	 * print
	 */
	protected Object print() {
		doViewSelect();
		return null;
	}

	/**
	 * print_input
	 */
	protected Object print_input() {
		doViewInput();
		return null;
	}

	/**
	 * copy
	 */
	protected Object copy() {
		doInsertSelect();
		return null;
	}

	/**
	 * copy_input
	 */
	protected Object copy_input() {
		doInsertInput();
		return null;
	}

	/**
	 * copy_confirm
	 */
	protected Object copy_confirm() {
		doInsertConfirm();
		return null;
	}

	/**
	 * copy_execute
	 */
	public Object copy_execute() {
		doInsertExecute();
		return null;
	}

	/**
	 * insert
	 */
	protected Object insert() {
		doInsertClear();
		return null;
	}

	/**
	 * insert_input
	 */
	protected Object insert_input() {
		doInsertInput();
		return null;
	}

	/**
	 * insert_confirm
	 */
	protected Object insert_confirm() {
		doInsertConfirm();
		return null;
	}

	/**
	 * insert_execute
	 */
	protected Object insert_execute() {
		doInsertExecute();
		return null;
	}

	/**
	 * update
	 */
	protected Object update() {
		doUpdateSelect();
		return null;
	}

	/**
	 * update_input
	 */
	protected Object update_input() {
		doUpdateInput();
		return null;
	}

	/**
	 * update_confirm
	 */
	public Object update_confirm() {
		doUpdateConfirm();
		return null;
	}

	/**
	 * update_execute
	 */
	protected Object update_execute() {
		doUpdateExecute();
		return null;
	}

	/**
	 * delete
	 */
	protected Object delete() {
		doDeleteSelect();
		return null;
	}

	/**
	 * delete_execute
	 */
	protected Object delete_execute() {
		doDeleteExecute();
		return null;
	}

	//------------------------------------------------------------
	// do method
	//------------------------------------------------------------
	/**
	 * doViewInput 
	 */
	protected void doViewInput() {
//		setMethodResult(RESULT_DEFAULT);
	}

	/**
	 * doViewSelect
	 */
	protected void doViewSelect() {
//		setMethodResult(RESULT_DEFAULT);
		T d = selectData(data);
		if (d != null) {
			data = d;
		}
	}

	/**
	 * doInsertClear
	 */
	protected void doInsertClear() {
//		setMethodResult(RESULT_DEFAULT);
		data = prepareDefaultData(null);
	}

	/**
	 * doInsertSelect
	 */
	protected void doInsertSelect() {
//		setMethodResult(RESULT_DEFAULT);
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
//		setMethodResult(RESULT_DEFAULT);
		clearOnCopy(data);
		data = prepareDefaultData(data);
	}

	/**
	 * doInsertConfirm
	 */
	protected void doInsertConfirm() {
//		setMethodResult(RESULT_DEFAULT);
		data = prepareDefaultData(data);
		if (checkOnInsert(data)) {
			addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario()));
//			setMethodResult(RESULT_CONFIRM);
		}
		else {
			if (!hasActionErrors() && !hasFieldErrors()) {
//				setMethodResult(RESULT_DEFAULT);
//				setMethodResult(RESULT_CONFIRM);
			}
		}
	}

	/**
	 * doInsertExecute
	 */
	protected void doInsertExecute() {
//		setMethodResult(RESULT_DEFAULT);

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
//			setMethodResult(RESULT_SUCCESS);
		}
		else {
			if (!hasActionErrors() && !hasFieldErrors() 
					&& getTextAsBoolean(ActionRC.UI_INPUT_CONFIRM, false)) {
//				setMethodResult(RESULT_CONFIRM);
			}
		}
	}

	/**
	 * doUpdateInput
	 */
	protected void doUpdateInput() {
//		setMethodResult(RESULT_DEFAULT);
	}

	/**
	 * doUpdateSelect
	 */
	protected void doUpdateSelect() {
//		setMethodResult(RESULT_DEFAULT);
		T d = selectData(data);
		if (d != null) {
			data = d;
		}
	}

	/**
	 * doUpdateConfirm
	 */
	protected void doUpdateConfirm() {
//		setMethodResult(RESULT_DEFAULT);

		sourceData = selectData(data);
		if (sourceData != null) {
			if (checkOnUpdate(data, sourceData)) {
				addActionConfirm(getMessage(ACTION_CONFIRM_PREFIX + getActionScenario()));
//				setMethodResult(RESULT_CONFIRM);
			}
			else {
				if (!hasActionErrors() && !hasFieldErrors()) {
//					setMethodResult(RESULT_CONFIRM);
				}
			}
		}
	}

	/**
	 * doUpdateExecute
	 */
	protected void doUpdateExecute() {
//		setMethodResult(RESULT_DEFAULT);

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
//				setMethodResult(RESULT_SUCCESS);
			}
			else {
				if (!hasActionErrors() && !hasFieldErrors()
						&& getTextAsBoolean(ActionRC.UI_INPUT_CONFIRM, false)) {
//					setMethodResult(RESULT_CONFIRM);
				}
			}
		}
	}

	/**
	 * doDeleteSelect
	 */
	protected void doDeleteSelect() {
//		setMethodResult(RESULT_DEFAULT);
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
//		setMethodResult(RESULT_DEFAULT);

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
//				setMethodResult(RESULT_SUCCESS);
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
//		setMethodResult(RESULT_DEFAULT);
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
//		setMethodResult(RESULT_DEFAULT);

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
//			setMethodResult(RESULT_SUCCESS);
		}
	}

	/**
	 * doBulkDeleteSelect
	 */
	protected void doBulkDeleteSelect() {
//		setMethodResult(RESULT_DEFAULT);
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
//		setMethodResult(RESULT_DEFAULT);

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
//			setMethodResult(RESULT_SUCCESS);
		}
	}

	//------------------------------------------------------------
	// overridable methods
	//------------------------------------------------------------
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

	protected void trimDataList(List<T> ds) {
	}
	
	protected void trimData(T d) {
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
		if (data instanceof IUpdate) {
			IUpdate cb = (IUpdate)data;
			IUpdate sb = (IUpdate)srcData;
			if (Bean.isUpdated(cb, sb)) {
				cb.setUusid(sb.getUusid());
				cb.setUtime(sb.getUtime());
				addActionWarning(getText(RC.ERROR_DATA_IS_UPDATED, RC.ERROR_DATA_IS_UPDATED, sb));
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
