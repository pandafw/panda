package panda.mvc.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import panda.dao.DaoClient;
import panda.io.Settings;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;
import panda.mvc.alert.ActionAlert;
import panda.mvc.alert.ParamAlert;

/**
 * ActionSupport
 */
public class ActionSupport {
	@IocInject
	protected ActionContext context;

	/**
	 * @return the context
	 */
	public ActionContext getContext() {
		return context;
	}

	/**
	 * @return the ioc
	 */
	public Ioc getIoc() {
		return context.getIoc();
	}
	
	/**
	 * @return the daoClient
	 */
	protected DaoClient getDaoClient() {
		return context.getDaoClient();
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return context.getSettings();
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return context.getLocale();
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
	 * @return the servlet
	 */
	public ServletContext getServlet() {
		return context.getServlet();
	}
	
	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return context.getSession();
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
	public Map<String, Object> getSes() {
		return context.getSes();
	}

	/**
	 * @return request attribute map
	 */
	public Map<String, Object> getReq() {
		return context.getReq();
	}

	/**
	 * @return request parameter map
	 */
	public Map<String, Object> getReqParams() {
		return context.getReqParams();
	}

	/**
	 * @return request header map
	 */
	public Map<String, Object> getReqHeader() {
		return context.getReqHeader();
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
	 * @return the action alert
	 */
	public ActionAlert getActionAlert() {
		return context.getActionAlert();
	}

	/**
	 * @return the param alert
	 */
	public ParamAlert getParamAlert() {
		return context.getParamAlert();
	}

	/**
	 * @return the assist
	 */
	public ActionAssist getAssist() {
		return context.getAssist();
	}

	/**
	 * @return the consts
	 */
	public ActionConsts getConsts() {
		return context.getConsts();
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
	// Alert
	//
	public boolean hasFieldErrors() {
		return getParamAlert().hasErrors();
	}

	public boolean hasActionErrors() {
		return getActionAlert().hasErrors();
	}

	public void addFieldError(String field, String msg) {
		getParamAlert().addError(field, msg);
	}
	
	public void addActionError(String msg) {
		getActionAlert().addError(msg);
	}

	public void addActionWarning(String msg) {
		getActionAlert().addWarning(msg);
	}

	public void addActionConfirm(String msg) {
		getActionAlert().addConfirm(msg);
	}

	public void addActionMessage(String msg) {
		getActionAlert().addMessage(msg);
	}
}
