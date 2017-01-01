package panda.mvc.view.tag.ui;

import java.util.Locale;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Write link.
 *
 * <p/>
 *
 * Configurable attributes are :-
 * <ul>
 *    <li>cdn</li>
 *    <li>debug</li>
 *    <li>version</li>
 *    <li>jquery</li>
 *    <li>jqueryUi</li>
 *    <li>jqueryPlugins</li>
 *    <li>bootstrap</li>
 *    <li>bootstrapPlugins</li>
 *    <li>panda</li>
 *    <li>loading</li>
 * </ul>
 *
 * <p/>
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 * <pre>
 *  <!-- START SNIPPET: example -->
 *  &lt;r:link /&gt;
 *  <!-- END SNIPPET: example -->
 * </pre>
 *
 */
@IocBean(singleton=false)
public class Link extends UIBean {
	// attributes
	protected boolean cdn;
	protected String statics;
	protected String version;
	protected boolean debug;
	protected boolean js;
	protected boolean css;
	protected Locale locale;
	protected boolean jquery;
	protected boolean jqueryPlugins;
	protected boolean bootstrap;
	protected boolean bootstrapPlugins;
	protected boolean i18n;
	protected boolean panda;
	

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		if (locale == null) {
			locale = context.getLocale();
		}

		if (jqueryPlugins || bootstrap) {
			jquery = true;
		}
		if (bootstrapPlugins) {
			jquery = true;
			bootstrap = true;
		}
		if (panda) {
			jquery = true;
			jqueryPlugins = true;
			bootstrap = true;
			bootstrapPlugins = true;
		}
	}

	/**
	 * @return the cdn
	 */
	public boolean isCdn() {
		return cdn;
	}

	/**
	 * @param cdn the cdn to set
	 */
	public void setCdn(boolean cdn) {
		this.cdn = cdn;
	}

	/**
	 * @return the statics
	 */
	public String getStatics() {
		return statics;
	}

	/**
	 * @param statics the statics to set
	 */
	public void setStatics(String statics) {
		this.statics = statics;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return the js
	 */
	public boolean isJs() {
		return js;
	}

	/**
	 * @param js the js to set
	 */
	public void setJs(boolean js) {
		this.js = js;
	}

	/**
	 * @return the css
	 */
	public boolean isCss() {
		return css;
	}

	/**
	 * @param css the css to set
	 */
	public void setCss(boolean css) {
		this.css = css;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the jquery
	 */
	public boolean isJquery() {
		return jquery;
	}

	/**
	 * @param jquery the jquery to set
	 */
	public void setJquery(boolean jquery) {
		this.jquery = jquery;
	}

	/**
	 * @return the jqueryPlugins
	 */
	public boolean isJqueryPlugins() {
		return jqueryPlugins;
	}

	/**
	 * @param jqueryPlugins the jqueryPlugins to set
	 */
	public void setJqueryPlugins(boolean jqueryPlugins) {
		this.jqueryPlugins = jqueryPlugins;
	}

	/**
	 * @return the bootstrap
	 */
	public boolean isBootstrap() {
		return bootstrap;
	}

	/**
	 * @param bootstrap the bootstrap to set
	 */
	public void setBootstrap(boolean bootstrap) {
		this.bootstrap = bootstrap;
	}

	/**
	 * @return the bootstrapPlugins
	 */
	public boolean isBootstrapPlugins() {
		return bootstrapPlugins;
	}

	/**
	 * @param bootstrapPlugins the bootstrapPlugins to set
	 */
	public void setBootstrapPlugins(boolean bootstrapPlugins) {
		this.bootstrapPlugins = bootstrapPlugins;
	}

	/**
	 * @return the i18n
	 */
	public boolean isI18n() {
		return i18n;
	}

	/**
	 * @param i18n the i18n to set
	 */
	public void setI18n(boolean i18n) {
		this.i18n = i18n;
	}

	/**
	 * @return the panda
	 */
	public boolean isPanda() {
		return panda;
	}

	/**
	 * @param panda the panda to set
	 */
	public void setPanda(boolean panda) {
		this.panda = panda;
	}

}

