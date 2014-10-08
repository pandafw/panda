package panda.mvc.validation.validator;

import panda.mvc.ActionContext;


public interface Validator {
	Validator getParent();
	
	void setParent(Validator parent);
	
	String getName();
	
	void setName(String name);
	
	String getMessage();
	
	void setMessage(String message);
	
	String getMsgId();
	
	void setMsgId(String msgId);
	
	boolean isShortCircuit();

	void setShortCircuit(boolean shortCircuit);
	
	boolean validate(ActionContext ac, Object obj);
}
