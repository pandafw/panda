package panda.bind.xmlrpc;

import panda.lang.Objects;

public class XmlRpcFault {
	private int faultCode;
	
	private String faultString;

	/**
	 * 
	 */
	public XmlRpcFault() {
	}

	/**
	 * @param faultCode XML-RPC Fault Code
	 * @param faultString XML-RPC Fault String
	 */
	public XmlRpcFault(int faultCode, String faultString) {
		this.faultCode = faultCode;
		this.faultString = faultString;
	}

	/**
	 * @return the faultCode
	 */
	public int getFaultCode() {
		return faultCode;
	}

	/**
	 * @param faultCode the faultCode to set
	 */
	public void setFaultCode(int faultCode) {
		this.faultCode = faultCode;
	}

	/**
	 * @return the faultString
	 */
	public String getFaultString() {
		return faultString;
	}

	/**
	 * @param faultString the faultString to set
	 */
	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}

	@Override
	public int hashCode() {
		return Objects.hash(faultCode, faultString);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		XmlRpcFault rhs = (XmlRpcFault)obj;
		return Objects.equalsBuilder()
				.append(faultCode, rhs.faultCode)
				.append(faultString, rhs.faultString)
				.isEquals();
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("faultCode", faultCode)
				.append("faultString", faultString)
				.toString();
	}
}
