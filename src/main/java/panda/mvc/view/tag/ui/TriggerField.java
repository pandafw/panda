package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;


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
	protected String ltext;
	protected String licon;
	protected String onlclick;
	protected String rtext;
	protected String ricon = "icon-caret-down";
	protected String onrclick;

	/**
	 * @return the ltext
	 */
	public String getLtext() {
		return ltext;
	}
	/**
	 * @param ltext the ltext to set
	 */
	public void setLtext(String ltext) {
		this.ltext = ltext;
	}
	/**
	 * @return the licon
	 */
	public String getLicon() {
		return licon;
	}
	/**
	 * @param licon the licon to set
	 */
	public void setLicon(String licon) {
		this.licon = licon;
	}
	/**
	 * @return the onlclick
	 */
	public String getOnlclick() {
		return onlclick;
	}
	/**
	 * @param onlclick the onlclick to set
	 */
	public void setOnlclick(String onlclick) {
		this.onlclick = onlclick;
	}
	/**
	 * @return the rtext
	 */
	public String getRtext() {
		return rtext;
	}
	/**
	 * @param rtext the rtext to set
	 */
	public void setRtext(String rtext) {
		this.rtext = rtext;
	}
	/**
	 * @return the ricon
	 */
	public String getRicon() {
		return ricon;
	}
	/**
	 * @param ricon the ricon to set
	 */
	public void setRicon(String ricon) {
		this.ricon = ricon;
	}
	/**
	 * @return the onrclick
	 */
	public String getOnrclick() {
		return onrclick;
	}
	/**
	 * @param onrclick the onrclick to set
	 */
	public void setOnrclick(String onrclick) {
		this.onrclick = onrclick;
	}
	
	
	public boolean hasLeftTrigger() {
		return Strings.isNotEmpty(licon) || Strings.isNotEmpty(ltext) || Strings.isNotEmpty(onlclick);
	}
	
	public boolean hasRightTrigger() {
		return Strings.isNotEmpty(ricon) || Strings.isNotEmpty(rtext) || Strings.isNotEmpty(onrclick);
	}
}
