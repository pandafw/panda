package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Render action confirms if they exists the specific layout of the
 * rendering depends on the theme itself. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * 
 *    &lt;r:actionconfirm /&gt;
 *    &lt;r:form .... &gt;
 *       ....
 *    &lt;/r:form&gt;
 * 
 * <!-- END SNIPPET: example -->
 * </pre>
 */
@IocBean(singleton=false)
public class ActionConfirm extends EscapeUIBean {
}
