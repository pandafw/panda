package panda.net.xmlrpc;

import panda.bind.xmlrpc.XmlRpcFault;

/**
 * Exception thrown by the XML-RPC library in case of a fault response. The exception is thrown only
 * if the call was successfully made but the response contained a fault message. If a call could not
 * be made due to a local problem (if an argument could not be serialized or if there was a network
 * problem) an XmlRpcException is thrown instead.
 */
public class XmlRpcFaultException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The exception error code. See XML-RPC specification. */
	public final int faultCode;

	/**
	 * Creates a new exception with the supplied message and error code. The message and error code
	 * values are those returned from the remote XML-RPC service.
	 * 
	 * @param errorCode The error code.
	 * @param message The exception message.
	 */
	public XmlRpcFaultException(int errorCode, String message) {
		super(message);
		this.faultCode = errorCode;
	}

	/**
	 * Creates a new exception with the supplied message and error code. The message and error code
	 * values are those returned from the remote XML-RPC service.
	 * 
	 * @param fault The fault object.
	 */
	public XmlRpcFaultException(XmlRpcFault fault) {
		super(fault.getFaultString());
		this.faultCode = fault.getFaultCode();
	}

	/**
	 * Returns the error code reported by the remote XML-RPC service.
	 * 
	 * @return the error code reported by the XML-RPC service.
	 */
	public int getFaultCode() {
		return faultCode;
	}
	
	public String getFaultString() {
		return getMessage();
	}
}
