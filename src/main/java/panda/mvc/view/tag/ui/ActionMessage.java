package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Render action messages if they exists, specific rendering layout
 * depends on the theme itself. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 *    &lt;r:actionmessage /&gt;
 *    &lt;r:form .... &gt;
 *       ....
 *    &lt;/s:form&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 */
@IocBean(singleton=false)
public class ActionMessage extends EscapeUIBean {
}
