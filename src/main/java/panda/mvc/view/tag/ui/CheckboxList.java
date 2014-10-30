package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;

/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Creates a series of checkboxes from a list. Setup is like &lt;s:select /&gt; or &lt;s:radio /&gt;, but creates checkbox tags.
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;s:checkboxlist name="foo" list="bar"/&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 */
@IocBean(singleton=false)
public class CheckboxList extends ListUIBean {
}
