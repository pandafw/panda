package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.util.MvcURLBuilder;

/**
 * FormButton.
 */
public abstract class FormButton extends InputUIBean {
	static final String BUTTONTYPE_INPUT = "input";
	static final String BUTTONTYPE_BUTTON = "button";
	static final String BUTTONTYPE_IMAGE = "image";

	protected String align;
	protected String type;

	protected String src;
	protected String icon;
	protected String sicon;
	protected String btype;

	protected String action;
	protected MvcURLBuilder urlbuilder;

	protected abstract String getDefaultValue();

	@Override
	public boolean usesBody() {
		return true;
	}

	public void evaluateParams() {
		if ((key == null) && (value == null)) {
			value = getDefaultValue();
		}

		if (((key != null)) && (value == null)) {
			value = context.getText().getText(key, key);
		}

		if (align == null) {
			align = "right";
		}

		String submitType = BUTTONTYPE_INPUT;
		if (type != null
				&& (BUTTONTYPE_BUTTON.equalsIgnoreCase(type) || (supportsImageType() && BUTTONTYPE_IMAGE
					.equalsIgnoreCase(type)))) {
			submitType = type;
		}

		type = submitType;

		if (!BUTTONTYPE_INPUT.equals(submitType) && (label == null) && value != null) {
			label = value.toString();
		}

		super.evaluateParams();
	}

	protected boolean supportsImageType() {
		return true;
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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the sicon
	 */
	public String getSicon() {
		return sicon;
	}

	/**
	 * @return the btype
	 */
	public String getBtype() {
		return btype;
	}

	public void setAction(String action) {
		action = Strings.stripToNull(action);
		if (Strings.isNotEmpty(action)) {
			urlbuilder.setAction(action);
			this.action = urlbuilder.build();
		}
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param src the image src to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * @param btype the btype to set
	 */
	public void setBtype(String btype) {
		this.btype = btype;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @param sicon the sicon to set
	 */
	public void setSicon(String sicon) {
		this.sicon = sicon;
	}
}
