package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;




/**
 * <!-- START SNIPPET: javadoc -->
 * Render a submit button. The submit tag is used together with the form tag to provide asynchronous form submissions.
 * The submit can have three different types of rendering:
 * <ul>
 * <li>input: renders as html &lt;input type="submit"...&gt;</li>
 * <li>image: renders as html &lt;input type="image"...&gt;</li>
 * <li>button: renders as html &lt;button type="submit"...&gt;</li>
 * </ul>
 * Please note that the button type has advantages by adding the possibility to seperate the submitted value from the
 * text shown on the button face, but has issues with Microsoft Internet Explorer at least up to 6.0
 * <!-- END SNIPPET: javadoc -->
 */
@IocBean(singleton=false)
public class Submit extends FormButton {
	protected String getDefaultValue() {
		return "Submit";
	}
	
	/**
	 * Indicate whether the concrete button supports the type "image".
	 *
	 * @return <tt>true</tt> to indicate type image is supported.
	 */
	protected boolean supportsImageType() {
		return true;
	}
}
