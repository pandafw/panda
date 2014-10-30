package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc -->
 * <p/>
 * A tag that creates a HTML &lt;button &gt;.This tag supports the same attributes as the "url" tag,
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
 * &lt;s:button id="link1" theme="ajax" href="/DoIt.action"&gt;
 *     &lt;img border="none" src="&lt;%=request.getContextPath()%&gt;/images/delete.gif"/&gt;
 *     &lt;s:param name="id" value="1"/&gt;
 * &lt;/s:button&gt;
 * <!-- END SNIPPET: example1 -->
 * </pre>
 */
@IocBean(singleton=false)
public class Button extends UIBean {
	protected String btype;
	protected String icon;
	protected String sicon;

	public boolean usesBody() {
		return true;
	}

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
	 * @param btype the btype to set
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
}
