package panda.mvc.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;
import panda.mvc.aware.ActionAware;
import panda.mvc.aware.ApplicationAware;
import panda.mvc.aware.ParamAware;
import panda.mvc.aware.SessionAware;

/**
 * ActionSupport
 */
public class ActionSupport {
	/*------------------------------------------------------------
	 * bean
	 *------------------------------------------------------------*/
	@IocInject
	protected ActionContext context;

	/**
	 * @return the context
	 */
	public ActionContext getContext() {
		return context;
	}

	/**
	 * @return the action
	 */
	public Object getAction() {
		return context.getAction();
	}

	/**
	 * @return method name
	 */
	public String getMethodName() {
		return context.getMethodName();
	}
	
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return context.getRequest();
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return context.getResponse();
	}

	/**
	 * @return application map
	 */
	public Map<String, Object> getApp() {
		return context.getApp();
	}

	/**
	 * @return session map
	 */
	public Map<String, Object> getSession() {
		return context.getSes();
	}

	/**
	 * @return request map
	 */
	public Map<String, Object> getReq() {
		return context.getReq();
	}
	
	/**
	 * @return the text
	 */
	public TextProvider getText() {
		return context.getText();
	}

	/**
	 * @return the state
	 */
	public StateProvider getState() {
		return context.getState();
	}

	/**
	 * @return the actionAware
	 */
	public ActionAware getActionAware() {
		return context.getActionAware();
	}

	/**
	 * @return the param Aware
	 */
	public ParamAware getParamAware() {
		return context.getParamAware();
	}

	/**
	 * @return the application Aware
	 */
	public ApplicationAware getApplicationAware() {
		return context.getApplicationAware();
	}

	/**
	 * @return the session Aware
	 */
	public SessionAware getSessionAware() {
		return context.getSessionAware();
	}

	//-----------------------------------------------------
	// TextProvider methods
	//
	/**
	 * Gets a message based on a message name or if no message is found the provided name is returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return the message as found in the resource bundle, or the provided name if none is found.
	 */
	public String getText(String key) {
		return getText().getText(key);
	}

	/**
	 * Gets a message based on a name, or, if the message is not found, a supplied default value is
	 * returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def the default value which will be returned if no message is found
	 * @return the message as found in the resource bundle, or def if none is found
	 */
	public String getText(String key, String def) {
		return getText().getText(key, def);
	}

	/**
	 * Gets a message based on a name using the supplied arg, or, if the message is not found, a
	 * supplied default value is returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def the default value which will be returned if no message is found
	 * @param arg object to be used in a EL expression such as "${top}"
	 * @return the message as found in the resource bundle, or def if none is found
	 */
	public String getText(String key, String def, Object arg) {
		return getText().getText(key, def, arg);
	}

	/**
	 * getTextAsBoolean
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return boolean value
	 */
	public Boolean getTextAsBoolean(String key) {
		return getText().getTextAsBoolean(key);
	}

	/**
	 * getTextAsBoolean
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return boolean value
	 */
	public Boolean getTextAsBoolean(String key, Boolean def) {
		return getText().getTextAsBoolean(key, def);
	}

	/**
	 * getTextAsInt
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return integer value
	 */
	public Integer getTextAsInt(String key) {
		return getText().getTextAsInt(key);
	}

	/**
	 * getTextAsInt
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return integer value
	 */
	public Integer getTextAsInt(String key, Integer def) {
		return getText().getTextAsInt(key, def);
	}

	/**
	 * getTextAsLong
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return long value
	 */
	public Long getTextAsLong(String key) {
		return getText().getTextAsLong(key);
	}

	/**
	 * getTextAsLong
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return long value
	 */
	public Long getTextAsLong(String key, Long def) {
		return getText().getTextAsLong(key, def);
	}

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return list value
	 */
	public List getTextAsList(String key) {
		return getText().getTextAsList(key);
	}

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return list value
	 */
	public List getTextAsList(String key, List def) {
		return getText().getTextAsList(key, def);
	}

	/**
	 * getTextAsMap
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return map value
	 */
	public Map getTextAsMap(String key) {
		return getText().getTextAsMap(key);
	}

	/**
	 * getTextAsMap
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return map value
	 */
	public Map getTextAsMap(String key, Map def) {
		return getText().getTextAsMap(key, def);
	}
	
	//----------------------------------------------------------------------
	// Aware
	//
	public void addFieldError(String field, String msg) {
		getParamAware().addError(field, msg);
	}

	public void addActionError(String msg) {
		getActionAware().addError(msg);
	}

	public void addActionWarning(String msg) {
		getActionAware().addWarning(msg);
	}

	public void addActionConfirm(String msg) {
		getActionAware().addConfirm(msg);
	}

	public void addActionMessage(String msg) {
		getActionAware().addMessage(msg);
	}

	public void addApplicationError(String msg) {
		getApplicationAware().addError(msg);
	}

	public void addApplicationWarning(String msg) {
		getApplicationAware().addWarning(msg);
	}

	public void addApplicationConfirm(String msg) {
		getApplicationAware().addConfirm(msg);
	}

	public void addApplicationMessage(String msg) {
		getApplicationAware().addMessage(msg);
	}

	public void addSessionError(String msg) {
		getSessionAware().addError(msg);
	}

	public void addSessionWarning(String msg) {
		getSessionAware().addWarning(msg);
	}

	public void addSessionConfirm(String msg) {
		getSessionAware().addConfirm(msg);
	}

	public void addSessionMessage(String msg) {
		getSessionAware().addMessage(msg);
	}

}
