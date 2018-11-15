package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.mvc.Mvcs;


/**
 * <!-- START SNIPPET: javadoc --> Render HTML textarea tag.</p> <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;p:htmleditor label=&quot;Comments&quot; name=&quot;comments&quot; cols=&quot;30&quot; rows=&quot;8&quot;/&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class HtmlEditor extends TextArea {
	protected Boolean cdn;
	protected Boolean debug;

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		if (cdn == null) {
			cdn = Mvcs.isUseCdn(context);
		}
		if (debug == null) {
			debug = context.isAppDebug();
		}
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
}
