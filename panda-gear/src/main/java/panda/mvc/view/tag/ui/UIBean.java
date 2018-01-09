package panda.mvc.view.tag.ui;

import java.io.Writer;

import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.mvc.view.tag.TagBean;
import panda.mvc.view.tag.ui.theme.RendererEngine;
import panda.mvc.view.tag.ui.theme.RenderingContext;


public abstract class UIBean extends TagBean {
	private static final Log log = Logs.getLog(UIBean.class);

	protected String body;
	protected String theme;

	protected String id;
	protected String name;
	protected String cssClass;
	protected String cssStyle;
	protected String cssErrorClass;
	protected String cssErrorStyle;
	protected boolean disabled;
	protected Integer tabindex;
	protected String title;

	// HTML scripting events attributes
	protected String onclick;
	protected String ondblclick;
	protected String onmousedown;
	protected String onmouseup;
	protected String onmouseover;
	protected String onmousemove;
	protected String onmouseout;
	protected String onfocus;
	protected String onblur;
	protected String onkeypress;
	protected String onkeydown;
	protected String onkeyup;
	protected String onselect;
	protected String onchange;

	// common html attributes
	protected String accesskey;

	// javascript tooltip attribute
	protected String tooltip;

	@IocInject(value=MvcConstants.UI_THEME, required=false)
	protected String defaultUITheme = "simple";

	@IocInject
	protected RendererEngine rendererEngine;

	public boolean start(Writer writer) {
		boolean result = super.start(writer);
		try {
			evaluateStartParams();

			startRender(writer);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}

		return result;
	}

	public boolean end(Writer writer, String body) {
		try {
			this.body = body;

			evaluateEndParams();
			
			endRender(writer);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			popComponentStack();
		}

		return false;
	}

	protected void startRender(Writer writer) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start rendering tag: " + getTheme() + "/" + getClass().getName());
		}

		final RenderingContext trc = new RenderingContext(getTheme(), writer, context, this);
		rendererEngine.start(trc);
	}

	protected void endRender(Writer writer) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("End rendering tag: " + getTheme() + "/" + getClass().getName());
		}

		final RenderingContext trc = new RenderingContext(getTheme(), writer, context, this);
		rendererEngine.end(trc);
	}

	public String getTheme() {
		if (Strings.isEmpty(theme)) {
			Form form = (Form)findAncestor(Form.class);
			if (form != null) {
				theme = form.getTheme();
			}
		}

		// If theme set is not explicitly given,
		// try to find attribute which states the theme set to use
		if (Strings.isEmpty(theme)) {
			theme = (String)context.getReq().get("theme");
		}

		// Default theme set
		if (Strings.isEmpty(theme)) {
			theme = defaultUITheme;
		}

		return theme;
	}

	protected void evaluateStartParams() {
		evaluateParams();
	}

	protected void evaluateEndParams() {
	}

	protected void evaluateParams() {
	}

	protected String escape(String name) {
		// escape any possible values that can make the ID painful to work with in JavaScript
		if (name != null) {
			return Strings.replaceChars(name, "\\/.[]", '_');
		}
		return null;
	}

	/**
	 * Get's the id for referencing element.
	 * 
	 * @return the id for referencing element.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the defaultUITheme
	 */
	public String getDefaultUITheme() {
		return defaultUITheme;
	}

	/**
	 * @param defaultUITheme the defaultUITheme to set
	 */
	public void setDefaultUITheme(String defaultUITheme) {
		this.defaultUITheme = defaultUITheme;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @return the cssStyle
	 */
	public String getCssStyle() {
		return cssStyle;
	}

	/**
	 * @return the cssErrorClass
	 */
	public String getCssErrorClass() {
		return cssErrorClass;
	}

	/**
	 * @return the cssErrorStyle
	 */
	public String getCssErrorStyle() {
		return cssErrorStyle;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the tabindex
	 */
	public Integer getTabindex() {
		return tabindex;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the onclick
	 */
	public String getOnclick() {
		return onclick;
	}

	/**
	 * @return the ondblclick
	 */
	public String getOndblclick() {
		return ondblclick;
	}

	/**
	 * @return the onmousedown
	 */
	public String getOnmousedown() {
		return onmousedown;
	}

	/**
	 * @return the onmouseup
	 */
	public String getOnmouseup() {
		return onmouseup;
	}

	/**
	 * @return the onmouseover
	 */
	public String getOnmouseover() {
		return onmouseover;
	}

	/**
	 * @return the onmousemove
	 */
	public String getOnmousemove() {
		return onmousemove;
	}

	/**
	 * @return the onmouseout
	 */
	public String getOnmouseout() {
		return onmouseout;
	}

	/**
	 * @return the onfocus
	 */
	public String getOnfocus() {
		return onfocus;
	}

	/**
	 * @return the onblur
	 */
	public String getOnblur() {
		return onblur;
	}

	/**
	 * @return the onkeypress
	 */
	public String getOnkeypress() {
		return onkeypress;
	}

	/**
	 * @return the onkeydown
	 */
	public String getOnkeydown() {
		return onkeydown;
	}

	/**
	 * @return the onkeyup
	 */
	public String getOnkeyup() {
		return onkeyup;
	}

	/**
	 * @return the onselect
	 */
	public String getOnselect() {
		return onselect;
	}

	/**
	 * @return the onchange
	 */
	public String getOnchange() {
		return onchange;
	}

	/**
	 * @return the accesskey
	 */
	public String getAccesskey() {
		return accesskey;
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param id HTML id attribute
	 */
	public void setId(String id) {
		this.id = escape(id);
	}

	/**
	 * @param theme The theme (other than default) to use for rendering the element 
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @param cssClass The css class to use for element
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/** 
	 * @param cssStyle The css style definitions for element to use
	 */
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	/**
	 * @param cssErrorClass The css error class to use for element
	 */
	public void setCssErrorClass(String cssErrorClass) {
		this.cssErrorClass = cssErrorClass;
	}

	/**
	 * @param cssErrorStyle The css error style definitions for element to use
	 */
	public void setCssErrorStyle(String cssErrorStyle) {
		this.cssErrorStyle = cssErrorStyle;
	}

	/**
	 * @param title Set the html title attribute on rendered html element
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param disabled Set the html disabled attribute on rendered html element
	 */
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @param name The name to set for element
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param tabindex Set the html tabindex attribute on rendered html element
	 */
	public void setTabindex(Integer tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * @param onclick Set the html onclick attribute on rendered html element
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	/**
	 * @param ondblclick Set the html ondblclick attribute on rendered html element
	 */
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	/**
	 * @param onmousedown Set the html onmousedown attribute on rendered html element
	 */
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	/**
	 * @param onmouseup Set the html onmouseup attribute on rendered html element
	 */
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	/**
	 * @param onmouseover Set the html onmouseover attribute on rendered html element
	 */
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	/**
	 * @param onmousemove Set the html onmousemove attribute on rendered html element
	 */
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	/**
	 * @param onmouseout Set the html onmouseout attribute on rendered html element
	 */
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	/**
	 * @param onfocus Set the html onfocus attribute on rendered html element
	 */
	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	/**
	 * @param onblur Set the html onblur attribute on rendered html element
	 */
	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	/**
	 * @param onkeypress Set the html onkeypress attribute on rendered html element
	 */
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	/**
	 * @param onkeydown Set the html onkeydown attribute on rendered html element
	 */
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	/**
	 * @param onkeyup Set the html onkeyup attribute on rendered html element
	 */
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	/**
	 * @param onselect Set the html onselect attribute on rendered html element
	 */
	public void setOnselect(String onselect) {
		this.onselect = onselect;
	}

	/**
	 * @param onchange Set the html onchange attribute on rendered html element
	 */
	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	/**
	 * @param accesskey Set the html accesskey attribute on rendered html element
	 */
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	/**
	 * @param tooltip Set the tooltip of this particular component
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
}
