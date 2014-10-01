package panda.mvc.validator;

import panda.mvc.ActionContext;


public interface FieldValidator {
	FieldValidator getParent();
	
	void setParent(FieldValidator parent);
	
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
