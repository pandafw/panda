package panda.mvc.view.tag.ui;

import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.util.UrlBuilder;

/**
 * <!-- START SNIPPET: javadoc -->
 * <p/>
 * A tag that creates a HTML &lt;a &gt;.This tag supports the same attributes as the "url" tag,
 * including nested parameters using the "param" tag.
 * <p/>
 * <!-- END SNIPPET: javadoc -->
 * <p/>
 * <p/>
 * <b>Examples</b>
 * <p/>
 * 
 * <pre>
 * <!-- START SNIPPET: example1 -->
 * &lt;s:a id="link1" theme="ajax" href="/DoIt.action"&gt;
 *     &lt;img border="none" src="&lt;%=request.getContextPath()%&gt;/images/delete.gif"/&gt;
 *     &lt;s:param name="id" value="1"/&gt;
 * &lt;/s:a&gt;
 * <!-- END SNIPPET: example1 -->
 * </pre>
 */
@IocBean(singleton=false)
public class Anchor extends UIBean {
	protected String btype;
	protected String icon;
	protected String sicon;
	protected String href;
	protected String target;
	
	protected UrlBuilder urlbuilder;

	public boolean usesBody() {
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void evaluateEndParams() {
		super.evaluateEndParams();
		
		if (href == null) {
			if (Collections.isNotEmpty(params)) {
				if (urlbuilder.getParams() == null) {
					urlbuilder.setParams(params);
				}
				else {
					urlbuilder.getParams().putAll(params);
				}
			}

			href = urlbuilder.build();
		}
	}

	/**
	 * @param urlbuilder the urlbuilder to set
	 */
	@IocInject
	protected void setUrlbuilder(UrlBuilder urlbuilder) {
		this.urlbuilder = urlbuilder;
		urlbuilder.setEscapeAmp(true);
	}

	//---------------------------------------------------------
	/**
	 * @return the btype
	 */
	public String getBtype() {
		return btype;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the sicon
	 */
	public String getSicon() {
		return sicon;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param btype the icon to set
	 */
	public void setBtype(String btype) {
		this.btype = btype;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @param sicon the sicon to set
	 */
	public void setSicon(String sicon) {
		this.sicon = sicon;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setHref(String href) {
		this.href = Strings.stripToNull(href);
	}

	//----------------------------------------------------------
	public void setScheme(String scheme) {
		urlbuilder.setScheme(scheme);
	}
	
	public void setPort(int port) {
		urlbuilder.setPort(port);
	}

	public void setAction(String action) {
		urlbuilder.setAction(action);
	}

	public void setQuery(String query) {
		urlbuilder.setQuery(query);
	}
	
	public void setParams(Map params) {
		urlbuilder.setParams(params);
	}

	public void setIncludeParams(String includeParams) {
		urlbuilder.setIncludeParams(includeParams);
	}

	public void setIncludeContext(boolean includeContext) {
		urlbuilder.setIncludeContext(includeContext);
	}

	public void setAnchor(String anchor) {
		urlbuilder.setAnchor(anchor);
	}

	public void setEscapeAmp(boolean escapeAmp) {
		urlbuilder.setEscapeAmp(escapeAmp);
	}

	public void setSuppressParam(String suppress) {
		urlbuilder.setSuppressParam(suppress);
	}

	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort) {
		urlbuilder.setForceAddSchemeHostAndPort(forceAddSchemeHostAndPort);
	}
}
