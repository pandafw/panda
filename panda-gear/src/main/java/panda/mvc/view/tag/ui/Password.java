package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;

/**
 * <!-- START SNIPPET: javadoc --> Render an HTML input tag of type password.</p> <!-- END SNIPPET:
 * javadoc -->
 * <p/>
 * <b>Examples</b>
 * <p/>
 * <!-- START SNIPPET: exdescription --> In this example, a password control is displayed. For the
 * label, we are calling ActionSupport's getText() to retrieve password label from a resource
 * bundle.
 * <p/>
 * <!-- END SNIPPET: exdescription -->
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;s:password label=&quot;%{text('password')}&quot; name=&quot;password&quot; size=&quot;10&quot; maxlength=&quot;15&quot; /&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class Password extends TextField {
	protected boolean showPassword;

	/**
	 * @return the showPassword
	 */
	public boolean isShowPassword() {
		return showPassword;
	}

	/**
	 * @param showPassword the showPassword to set
	 */
	public void setShowPassword(boolean showPassword) {
		this.showPassword = showPassword;
	}

}
