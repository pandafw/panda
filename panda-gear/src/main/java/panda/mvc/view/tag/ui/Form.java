package panda.mvc.view.tag.ui;

import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.util.MvcURLBuilder;

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

	protected String focusme;
	
	protected Boolean hooked;
	protected Boolean loadmask;
	
	protected MvcURLBuilder urlbuilder;

	protected String labelClass;
	protected String labelSeparator = ":";
	protected String labelPosition;
	protected String inputClass;
	protected Boolean hideDescrip;
	protected Boolean token;

	@Override
	protected void evaluateParams() {
		super.evaluateParams();

		if (Strings.isNotEmpty(name)) {
			// make the name the same as the id
			if (Strings.isNotEmpty(id)) {
				name = id;
			}
		}
		if (Strings.isNotEmpty(action)) {
			action = urlbuilder.build();
		}
		
		if (theme == null) {
			theme = (String)context.getReq().get("theme");
			if (Strings.isEmpty(theme)) {
				theme = context.getText().getText("form-theme", "bs3h");
			}
		}
		if (loadmask == null) {
			loadmask = context.getText().getTextAsBoolean("form-loadmask");
		}
		if (focusme == null) {
			focusme = context.getText().getText("form-focusme");
		}
		
		if (labelClass == null) {
			labelClass = context.getText().getText("form-label-class", "col-sm-3");
		}
		if (inputClass == null) {
			inputClass = context.getText().getText("form-input-class", "col-sm-9");
		}
	}

	/**
	 * @param urlbuilder the urlbuilder to set
	 */
	@IocInject
	protected void setUrlbuilder(MvcURLBuilder urlbuilder) {
		this.urlbuilder = urlbuilder;
		urlbuilder.setEscapeAmp(true);
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
	 * @return the focusme
	 */
	public String getFocusme() {
		return focusme;
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

	public void setFocusme(String focusElement) {
		this.focusme = focusElement;
	}

	/**
	 * @param loadmask the loadmask to set
	 */
	public void setLoadmask(Boolean loadmask) {
		this.loadmask = loadmask;
	}

	/**
	 * @return the labelSeparator
	 */
	public String getLabelSeparator() {
		return labelSeparator;
	}

	/**
	 * @param labelSeparator the labelSeparator to set
	 */
	public void setLabelSeparator(String labelSeparator) {
		this.labelSeparator = labelSeparator;
	}

	/**
	 * @return the labelClass
	 */
	public String getLabelClass() {
		return labelClass;
	}

	/**
	 * @param labelClass the labelClass to set
	 */
	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
	}

	/**
	 * @return the labelPosition
	 */
	public String getLabelPosition() {
		return labelPosition;
	}

	/**
	 * @param labelPosition the labelPosition to set
	 */
	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**
	 * @return the inputClass
	 */
	public String getInputClass() {
		return inputClass;
	}

	/**
	 * @param inputClass the inputClass to set
	 */
	public void setInputClass(String inputClass) {
		this.inputClass = inputClass;
	}

	/**
	 * @return the hideDescrip
	 */
	public Boolean getHideDescrip() {
		return hideDescrip;
	}

	/**
	 * @param hideDescrip the hideDescrip to set
	 */
	public void setHideDescrip(Boolean hideDescrip) {
		this.hideDescrip = hideDescrip;
	}

	/**
	 * @return the token
	 */
	public Boolean getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Boolean token) {
		this.token = token;
	}

	//----------------------------------------------------------
	public void setScheme(String scheme) {
		urlbuilder.setScheme(scheme);
	}
	
	public void setPort(int port) {
		urlbuilder.setPort(port);
	}

	public void setAction(String action) {
		this.action = action;
		urlbuilder.setAction(action);
	}

	public void setQuery(String query) {
		urlbuilder.setQuery(query);
	}
	
	public void setParams(Map params) {
		urlbuilder.setParams(params);
	}

	public void setIncludeParams(String includeParams) {
		urlbuilder.setIncludeParams(includeParams);
	}

	public void setIncludeContext(boolean includeContext) {
		urlbuilder.setIncludeContext(includeContext);
	}

	public void setAnchor(String anchor) {
		urlbuilder.setAnchor(anchor);
	}

	public void setEscapeAmp(boolean escapeAmp) {
		urlbuilder.setEscapeAmp(escapeAmp);
	}

	public void setSuppressParam(String suppress) {
		urlbuilder.setSuppressParam(suppress);
	}

	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort) {
		urlbuilder.setForceAddSchemeHostAndPort(forceAddSchemeHostAndPort);
	}
}
