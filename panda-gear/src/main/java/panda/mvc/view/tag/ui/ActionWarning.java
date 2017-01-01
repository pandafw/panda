package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Render action errors if they exists the specific layout of the
 * rendering depends on the theme itself. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * 
 *    &lt;r:actionwarning /&gt;
 *    &lt;r:form .... &gt;
 *       ....
 *    &lt;/r:form&gt;
 * 
 * <!-- END SNIPPET: example -->
 * </pre>
 */
@IocBean(singleton=false)
public class ActionWarning extends EscapeUIBean {
}
