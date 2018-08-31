package panda.app.task;

import java.util.HashMap;
import java.util.Map;

import panda.lang.Objects;
import panda.lang.Strings;
import panda.net.http.HttpMethod;

public class ActionTask {
	private String action;
	private String method = HttpMethod.GET;
	private Map<String, String> headers;
	private Map<String, String> params;
	private boolean token;

	private String description;
	private Integer errorLimit;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = Strings.upperCase(method);
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * @return the token
	 */
	public boolean isToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(boolean token) {
		this.token = token;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the errorLimit
	 */
	public Integer getErrorLimit() {
		return errorLimit;
	}

	/**
	 * @param errorLimit the errorLimit to set
	 */
	public void setErrorLimit(Integer errorLimit) {
		this.errorLimit = errorLimit;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("action", action)
				.append("method", method)
				.append("headers", headers)
				.append("params", params)
				.append("token", token)
				.append("description", description)
				.append("errorLimit", errorLimit)
				.toString();
	}
}
