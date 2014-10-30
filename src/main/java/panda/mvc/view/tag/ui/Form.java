package panda.mvc.view.tag.ui;

import org.apache.struts2.components.UrlRenderer;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.MvcConstants;

/**
 * <!-- START SNIPPET: javadoc -->
 * <p/>
 * Renders HTML an input form.
 * <p/>
 * <p/>
 * The remote form allows the form to be submitted without the page being refreshed. The results
 * from the form can be inserted into any HTML element on the page.
 * <p/>
 * <p/>
 * NOTE:
 * <p/>
 * The order / logic in determining the posting url of the generated HTML form is as follows:-
 * <ol>
 * <li>If the action attribute is not specified, then the current request will be used to determine
 * the posting url</li>
 * <li>If the action is given, Struts will try to obtain an ActionConfig. This will be successfull
 * if the action attribute is a valid action alias defined struts.xml.</li>
 * <li>If the action is given and is not an action alias defined in struts.xml, Struts will used the
 * action attribute as if it is the posting url, separting the namespace from it and using UrlHelper
 * to generate the final url.</li>
 * </ol>
 * <p/>
 * <!-- END SNIPPET: javadoc -->
 * <p/>
 * <p/>
 * <b>Examples</b>
 * <p/>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;p/&gt;
 * &lt;s:form ... /&gt;
 * &lt;p/&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class Form extends UIBean {
	private int sequence = 0;

	protected String onsubmit;
	protected String onreset;
	protected String action;
	protected String target;
	protected String enctype;
	protected String method;
	protected String acceptcharset;

	protected String focusElement;
	
	protected Boolean hooked;
	protected Boolean loadmask;
	
	@IocInject
	protected UrlRenderer urlRenderer;

	/*
	 * Revised for Portlet actionURL as form action, and add wwAction as hidden field. Refer to
	 * template.simple/form.vm
	 */
	@Override
	protected void evaluateParams() {
		super.evaluateParams();

		if (name == null) {
			// make the name the same as the id
			if (Strings.isNotEmpty(id)) {
				name = id;
			}
		}
	}

	/**
	 * Get a incrementing sequence unique to this <code>Form</code> component. It is used by
	 * <code>Form</code> component's child that might need a sequence to make them unique.
	 * 
	 * @return int
	 */
	protected int getSequence() {
		return sequence++;
	}

	/**
	 * @return the loadmask
	 */
	public Boolean getLoadmask() {
		return loadmask;
	}

	/**
	 * @return the onsubmit
	 */
	public String getOnsubmit() {
		return onsubmit;
	}

	/**
	 * @return the onreset
	 */
	public String getOnreset() {
		return onreset;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the enctype
	 */
	public String getEnctype() {
		return enctype;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @return the acceptcharset
	 */
	public String getAcceptcharset() {
		return acceptcharset;
	}

	/**
	 * @return the focusElement
	 */
	public String getFocusElement() {
		return focusElement;
	}

	/**
	 * @return the hooked
	 */
	public Boolean getHooked() {
		return hooked;
	}

	/**
	 * @param hooked the hooked to set
	 */
	public void setHooked(Boolean hooked) {
		this.hooked = hooked;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public void setOnsubmit(String onsubmit) {
		this.onsubmit = onsubmit;
	}

	public void setOnreset(String onreset) {
		this.onreset = onreset;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setAcceptcharset(String acceptcharset) {
		this.acceptcharset = acceptcharset;
	}

	public void setFocusElement(String focusElement) {
		this.focusElement = focusElement;
	}

	/**
	 * @param loadmask the loadmask to set
	 */
	@IocInject(value = MvcConstants.UI_FORM_LOADMASK, required = false)
	public void setLoadmask(Boolean loadmask) {
		this.loadmask = loadmask;
	}
	
	
}
