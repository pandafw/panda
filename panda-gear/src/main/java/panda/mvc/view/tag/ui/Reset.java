package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc -->
 * Render a reset button. The reset tag is used together with the form tag to provide form resetting.
 * The reset can have two different types of rendering:
 * <ul>
 * <li>input: renders as html &lt;input type="reset"...&gt;</li>
 * <li>button: renders as html &lt;button type="reset"...&gt;</li>
 * </ul>
 * Please note that the button type has advantages by adding the possibility to seperate the submitted value from the
 * text shown on the button face, but has issues with Microsoft Internet Explorer at least up to 6.0
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;s:reset value="Reset" /&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * <pre>
 * <!-- START SNIPPET: example2 -->
 * Render an button reset:
 * &lt;s:reset type="button" key="reset"/&gt;
 * <!-- END SNIPPET: example2 -->
 * </pre>
 *
 */
@IocBean(singleton=false)
public class Reset extends FormButton {
	protected String getDefaultValue() {
		return "Reset";
	}
	
	/**
	 * Indicate whether the concrete button supports the type "image".
	 *
	 * @return <tt>false</tt> to indicate type image is supported.
	 */
	protected boolean supportsImageType() {
		return false;
	}
}
