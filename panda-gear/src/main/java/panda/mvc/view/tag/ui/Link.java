package panda.mvc.view.tag.ui;

import java.util.List;
import java.util.Locale;

import panda.ioc.annotation.IocBean;
import panda.mvc.Mvcs;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Write link.
 *
 * <p/>
 *
 * Configurable attributes are :-
 * <ul>
 *    <li>js</li>
 *    <li>css</li>
 *    <li>cdn</li>
 *    <li>debug</li>
 *    <li>version</li>
 *    <li>jquery</li>
 *    <li>jquery1</li>
 *    <li>jquery2</li>
 *    <li>jquery3</li>
 *    <li>jqplugins</li>
 *    <li>jqextras</li>
 *    <li>bootstrap</li>
 *    <li>bootstrap3</li>
 *    <li>bootstrap4</li>
 *    <li>fontawesome</li>
 *    <li>flagiconcss</li>
 *    <li>corejs</li>
 *    <li>panda</li>
 *    <li>notifyjs</li>
 *    <li>respondjs</li>
 *    <li>stylesheets</li>
 *    <li>jscripts</li>
 * </ul>
 *
 * <p></p>
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <p></p> <b>Examples</b>
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
	protected String version;
	protected Boolean cdn;
	protected Boolean debug;

	protected Locale locale;

	protected boolean js;
	protected boolean css;
	protected boolean jquery;
	protected boolean jquery1;
	protected boolean jquery2;
	protected boolean jquery3;
	protected boolean jqplugins;
	protected boolean bootstrap;
	protected boolean bootstrap3;
	protected boolean bootstrap4;
	protected boolean fontawesome;
	protected boolean flagiconcss;
	protected boolean corejs;
	protected boolean panda;
	protected boolean notifyjs;
	protected boolean respondjs;
	protected List<String> stylesheets;
	protected List<String> jscripts;

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		if (locale == null) {
			locale = context.getLocale();
		}

		if (bootstrap3 || bootstrap4) {
			bootstrap = true;
		}
		
		if (jquery1 || jquery2 || jquery3 || notifyjs) {
			jquery = true;
		}
		
		if (bootstrap || jqplugins) {
			jquery = true;
		}
		if (panda) {
			jquery = true;
			jqplugins = true;
			bootstrap = true;
			fontawesome = true;
			corejs = true;
		}
		
		if (cdn == null) {
			cdn = Mvcs.isUseCdn(context);
		}
		if (debug == null) {
			debug = context.isAppDebug();
		}
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
	 * @return the cdn
	 */
	public boolean useCdn() {
		return cdn;
	}

	/**
	 * @return the cdn
	 */
	public Boolean getCdn() {
		return cdn;
	}

	/**
	 * @param cdn the cdn to set
	 */
	public void setCdn(Boolean cdn) {
		this.cdn = cdn;
	}

	/**
	 * @return the debug
	 */
	public boolean useDebug() {
		return debug;
	}

	/**
	 * @return the debug
	 */
	public Boolean getDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(Boolean debug) {
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
	 * @return the jquery1
	 */
	public boolean isJquery1() {
		return jquery1;
	}

	/**
	 * @param jquery1 the jquery1 to set
	 */
	public void setJquery1(boolean jquery1) {
		this.jquery1 = jquery1;
	}

	/**
	 * @return the jquery2
	 */
	public boolean isJquery2() {
		return jquery2;
	}

	/**
	 * @param jquery2 the jquery2 to set
	 */
	public void setJquery2(boolean jquery2) {
		this.jquery2 = jquery2;
	}

	/**
	 * @return the jquery3
	 */
	public boolean isJquery3() {
		return jquery3;
	}

	/**
	 * @param jquery3 the jquery3 to set
	 */
	public void setJquery3(boolean jquery3) {
		this.jquery3 = jquery3;
	}

	/**
	 * @return the jqplugins
	 */
	public boolean isJqplugins() {
		return jqplugins;
	}

	/**
	 * @param jqplugins the jqplugins to set
	 */
	public void setJqplugins(boolean jqplugins) {
		this.jqplugins = jqplugins;
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
	 * @return the bootstrap3
	 */
	public boolean isBootstrap3() {
		return bootstrap3;
	}

	/**
	 * @param bootstrap3 the bootstrap3 to set
	 */
	public void setBootstrap3(boolean bootstrap3) {
		this.bootstrap3 = bootstrap3;
	}

	/**
	 * @return the bootstrap4
	 */
	public boolean isBootstrap4() {
		return bootstrap4;
	}

	/**
	 * @param bootstrap4 the bootstrap4 to set
	 */
	public void setBootstrap4(boolean bootstrap4) {
		this.bootstrap4 = bootstrap4;
	}

	/**
	 * @return the fontawesome
	 */
	public boolean isFontawesome() {
		return fontawesome;
	}

	/**
	 * @param fontawesome the fontawesome to set
	 */
	public void setFontawesome(boolean fontawesome) {
		this.fontawesome = fontawesome;
	}

	/**
	 * @return the flagiconcss
	 */
	public boolean isFlagiconcss() {
		return flagiconcss;
	}

	/**
	 * @param flagiconcss the flagiconcss to set
	 */
	public void setFlagiconcss(boolean flagiconcss) {
		this.flagiconcss = flagiconcss;
	}

	/**
	 * @return the corejs
	 */
	public boolean isCorejs() {
		return corejs;
	}

	/**
	 * @param corejs the corejs to set
	 */
	public void setCorejs(boolean corejs) {
		this.corejs = corejs;
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

	/**
	 * @return the notifyjs
	 */
	public boolean isNotifyjs() {
		return notifyjs;
	}

	/**
	 * @param notifyjs the notifyjs to set
	 */
	public void setNotifyjs(boolean notifyjs) {
		this.notifyjs = notifyjs;
	}

	/**
	 * @return the respondjs
	 */
	public boolean isRespondjs() {
		return respondjs;
	}

	/**
	 * @param respondjs the respondjs to set
	 */
	public void setRespondjs(boolean respondjs) {
		this.respondjs = respondjs;
	}

	/**
	 * @return the stylesheets
	 */
	public List<String> getStylesheets() {
		return stylesheets;
	}

	/**
	 * @param stylesheets the stylesheets to set
	 */
	public void setStylesheets(List<String> stylesheets) {
		this.stylesheets = stylesheets;
	}

	/**
	 * @return the jscripts
	 */
	public List<String> getJscripts() {
		return jscripts;
	}

	/**
	 * @param jscripts the jscripts to set
	 */
	public void setJscripts(List<String> jscripts) {
		this.jscripts = jscripts;
	}
}

