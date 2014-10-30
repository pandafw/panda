package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Render an HTML input field of type text</p> <!-- END SNIPPET:
 * javadoc -->
 * <p/>
 * <b>Examples</b>
 * <p/>
 * <!-- START SNIPPET: exdescription --> In this example, a text control for the "user" property is
 * rendered. The label is also retrieved from a ResourceBundle via the key attribute. <!-- END
 * SNIPPET: exdescription -->
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;r:triggerfield key="user" /&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 * 
 * <pre>
 * <!-- START SNIPPET: example2 -->
 * &lt;r:triggerfield name="user" label="User Name" /&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 */
@IocBean(singleton=false)
public class TriggerField extends TextField {
	protected String ontrigger;


	/**
	 * @return the ontrigger
	 */
	public String getOntrigger() {
		return ontrigger;
	}


	/**
	 * @param ontrigger the ontrigger to set
	 */
	public void setOntrigger(String ontrigger) {
		this.ontrigger = ontrigger;
	}
}
