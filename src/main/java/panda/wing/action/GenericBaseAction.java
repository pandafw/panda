package panda.wing.action;

import java.util.List;

import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.query.GenericQuery;
import panda.lang.Strings;
import panda.mvc.view.SitemeshFreemarkerView;


/**
 * @param <T> data type
 */
public abstract class GenericBaseAction<T> extends AbstractAction {
	/**
	 * RESULT_CONFIRM = "confirm";
	 */
	protected final static String RESULT_CONFIRM = "confirm";
	
	/**
	 * METHOD_SEPARATOR = "_";
	 */
	protected final static String METHOD_SEPARATOR = "_";

	//------------------------------------------------------------
	// scenario & result
	//------------------------------------------------------------
	protected Object result;
	protected String actionScenario;

	//------------------------------------------------------------
	// entity properties
	//------------------------------------------------------------
	protected Class<T> type;
	protected Entity<T> entity;
	protected EntityDao<T> entityDao;

	/**
	 * Constructor 
	 */
	public GenericBaseAction() {
	}

	//------------------------------------------------------------
	// entity
	//------------------------------------------------------------
	/**
	 * public for getText(...)
	 * @return the type
	 */
	public Class<T> getT() {
		return type;
	}

	/**
	 * public for getText(...)
	 * @return the type
	 */
	public Class<T> getType() {
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
	// scenario
	//------------------------------------------------------------
	/**
	 * @return the actionScenario
	 */
	protected String getActionScenario() {
		if (actionScenario == null) {
			actionScenario = Strings.substringBefore(context.getMethodName(), METHOD_SEPARATOR);
		}
		return actionScenario;
	}

	/**
	 * @param actionScenario the actionScenario to set
	 */
	protected void setActionScenario(String actionScenario) {
		this.actionScenario = actionScenario;
	}

	/**
	 * set result by action scenario
	 */
	protected void setScenarioResult() {
		result = new SitemeshFreemarkerView("~" + getActionScenario());
	}

	/**
	 * set result by action scenario
	 * @param step scenario step
	 */
	protected void setScenarioResult(String step) {
		result = new SitemeshFreemarkerView("~" + getActionScenario() + "_" + step);
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
	// result
	//------------------------------------------------------------
	protected Object doResult(Object r) {
		if (result == null) {
			return r;
		}
		getContext().setResult(r);
		return result;
	}

	//------------------------------------------------------------
	// message
	//------------------------------------------------------------
	/**
	 * getMessage
	 * @param msg msg id
	 * @return message string
	 */
	protected String getMessage(String msg) {
		return getText(msg, msg);
	}
	
	/**
	 * getMessage
	 * @param msg msg id
	 * @param param parameters
	 * @return message string
	 */
	protected String getMessage(String msg, String param) {
		return getText(msg, msg, param);
	}

	/**
	 * getScenarioMessage
	 * @param prefix message id prefix
	 * @return message string
	 */
	protected String getScenarioMessage(String prefix) {
		String msg = prefix + getActionScenario();
		return getText(msg, msg);
	}
	
	/**
	 * getMessage
	 * @param prefix message id prefix
	 * @param param parameters
	 * @return message string
	 */
	protected String getScenarioMessage(String prefix, String param) {
		String msg = prefix + getActionScenario();
		return getText(msg, msg, param);
	}
}
