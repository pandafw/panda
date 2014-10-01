package panda.mvc.validator;

import panda.bean.Beans;
import panda.mvc.ActionContext;


/**
 * Base class for Validator.
 */
public abstract class AbstractFieldValidator implements FieldValidator {
	protected FieldValidator parent;
	
	protected String name;
	
	protected String message;
	
	protected String msgId;

	protected boolean shortCircuit;

	/**
	 * @return the parent
	 */
	public FieldValidator getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(FieldValidator parent) {
		this.parent = parent;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the shortCircuit
	 */
	public boolean isShortCircuit() {
		return shortCircuit;
	}

	/**
	 * @param shortCircuit the shortCircuit to set
	 */
	public void setShortCircuit(boolean shortCircuit) {
		this.shortCircuit = shortCircuit;
	}

	/**
	 * 
	 * @param object object
	 * @param name field name
	 * @return field value
	 */
	protected Object getFieldValue(Object object, String name) {
		return Beans.getBean(object, name);
	}
	
	protected String getFullFieldName(String name) {
		if (parent != null) {
			StringBuilder sb = new StringBuilder(name);
			FieldValidator p = parent;
			while (p != null) {
				sb.insert(0, '.');
				sb.insert(0, p.getName());
			}
			return sb.toString();
		}
		return name;
	}
	
	protected String evalMessage(Object value) {
		//TODO
		return null;
	}
	
	protected void addFieldError(ActionContext ac, String name, Object value) {
		String msg = evalMessage(value);
		String fn = getFullFieldName(name);
		ac.getParamErrors().addError(fn, msg);
	}
}
