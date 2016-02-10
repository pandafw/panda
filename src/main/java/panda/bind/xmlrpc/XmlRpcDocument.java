package panda.bind.xmlrpc;

import java.util.List;

import panda.lang.Objects;

public class XmlRpcDocument<T> {
	private String methodName;

	private XmlRpcFault fault;
	
	private List<T> params;

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the fault
	 */
	public XmlRpcFault getFault() {
		return fault;
	}

	/**
	 * @param fault the fault to set
	 */
	public void setFault(XmlRpcFault fault) {
		this.fault = fault;
	}

	/**
	 * @return the params
	 */
	public List<T> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<T> params) {
		this.params = params;
	}

	@Override
	public int hashCode() {
		return Objects.hashCodes(methodName, fault, params);
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

		XmlRpcDocument rhs = (XmlRpcDocument)obj;
		return Objects.equalsBuilder()
				.append(methodName, rhs.methodName)
				.append(fault, rhs.fault)
				.append(params == null ? null : params.toArray(), rhs.params == null ? null : rhs.params.toArray())
				.isEquals();
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("methodName", methodName)
				.append("fault", fault)
				.append("params", params)
				.toString();
	}

}
