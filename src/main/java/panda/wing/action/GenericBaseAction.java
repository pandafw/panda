package panda.wing.action;

import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.lang.Strings;
import panda.mvc.View;
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
	protected View resultView;
	protected String actionScenario;

	//------------------------------------------------------------
	// entity properties
	//------------------------------------------------------------
	protected Class<T> type;
	protected Entity<T> entity;
	protected Dao dao;
	
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
	
	protected Dao getDao() {
		if (dao == null) {
			dao = getDaoClient().getDao();
		}
		return dao;
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

	protected void setResultView(View result) {
		this.resultView = result;
	}

	/**
	 * set result by action scenario
	 */
	protected void setScenarioResult() {
		resultView = new SitemeshFreemarkerView("~" + getActionScenario());
	}

	/**
	 * set result by action scenario
	 * @param step scenario step
	 */
	protected void setScenarioResult(String step) {
		resultView = new SitemeshFreemarkerView("~" + getActionScenario() + "_" + step);
	}

	//------------------------------------------------------------
	// result
	//------------------------------------------------------------
	protected Object doResult(Object r) {
		if (resultView == null) {
			return r;
		}
		getContext().setResult(r);
		return resultView;
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
