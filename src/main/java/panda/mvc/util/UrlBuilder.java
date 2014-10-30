package panda.mvc.util;

import java.util.Map;

/**
 * Implemntations of this interface can be used to build a URL
 */
public interface UrlBuilder {
	/**
	 * The includeParams attribute may have the value 'none', 'get' or 'all'. 
	 * none - include no parameters in the URL 
	 * get - include only GET parameters in the URL (default) 
	 * all - include both GET and POST parameters in the URL
	 */
	public static final String NONE = "none";
	public static final String GET = "get";
	public static final String ALL = "all";

	/**
	 * @return the scheme
	 */
	public String getScheme();

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme);


	/**
	 * @return the port
	 */
	public int getPort();


	/**
	 * @param port the port to set
	 */
	public void setPort(int port);


	/**
	 * @return the action
	 */
	public String getAction();


	/**
	 * @param action the action to set
	 */
	public void setAction(String action);


	/**
	 * @return the query
	 */
	public String getQuery();


	/**
	 * @param query the query to set
	 */
	public void setQuery(String query);


	/**
	 * @return the params
	 */
	public Map getParams();


	/**
	 * @param params the params to set
	 */
	public void setParams(Map params);


	/**
	 * @return the includeParams
	 */
	public boolean isIncludeParams();


	/**
	 * @param includeParams the includeParams to set
	 */
	public void setIncludeParams(boolean includeParams);


	/**
	 * @return the includeContext
	 */
	public boolean isIncludeContext();


	/**
	 * @param includeContext the includeContext to set
	 */
	public void setIncludeContext(boolean includeContext);


	/**
	 * @return the anchor
	 */
	public String getAnchor();


	/**
	 * @param anchor the anchor to set
	 */
	public void setAnchor(String anchor);


	/**
	 * @return the escapeAmp
	 */
	public boolean isEscapeAmp();


	/**
	 * @param escapeAmp the escapeAmp to set
	 */
	public void setEscapeAmp(boolean escapeAmp);


	/**
	 * @return the encoding
	 */
	public String getEncoding();


	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding);


	/**
	 * @return the forceAddSchemeHostAndPort
	 */
	public boolean isForceAddSchemeHostAndPort();


	/**
	 * @param forceAddSchemeHostAndPort the forceAddSchemeHostAndPort to set
	 */
	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort);


	String build();
}
