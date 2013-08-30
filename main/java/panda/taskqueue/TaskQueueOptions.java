package panda.taskqueue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yf.frank.wang@gmail.com
 */
public class TaskQueueOptions {
	private long etaMillis;
	private String url;
	private String method;
	private Map<String, String> headers;
	private Map<String, String> params;
	
	/**
	 * @return the etaMillis
	 */
	public long getEtaMillis() {
		return etaMillis;
	}

	/**
	 * @param etaMillis the etaMillis to set
	 */
	public void setEtaMillis(long etaMillis) {
		this.etaMillis = etaMillis;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
		this.method = method;
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
	 * Set the number of milliseconds delay before execution of the task.
	 */
	public void setCountdownMillis(long countdownMillis) {
		etaMillis = System.currentTimeMillis() + countdownMillis;
	}
}
