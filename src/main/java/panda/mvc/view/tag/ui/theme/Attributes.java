package panda.mvc.view.tag.ui.theme;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.lang.Collections;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.File;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.FormButton;
import panda.mvc.view.tag.ui.InputUIBean;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.TextArea;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.UIBean;
import panda.mvc.view.tag.ui.Uploader;
import panda.net.URLHelper;

/**
 * Map of tag attributes, used for rendering the tags
 */
public class Attributes {

	private Map<String, String> attributes = new LinkedHashMap<String, String>();

	public Map<String, String> attrs() {
		return attributes;
	}
	
	public Attributes clear() {
		attributes.clear();
		return this;
	}
	
	public Set<Entry<String, String>> entrySet() {
		return attributes.entrySet();
	}
	
	public String get(Object key) {
		return attributes.get(key);
	}
	
	public Attributes add(String key, Object value) {
		return add(key, value, true);
	}

	public Attributes add(String key, Object value, boolean encode) {
		String sv = value == null ? "" : value.toString();
		if (encode) {
			sv = StringEscapes.escapeHtml(sv);
		}
		attributes.put(key, sv);
		return this;
	}

	/**
	 * Add a key/value pair to the attributes only if the value is not null.
	 * Value is html encoded
	 * 
	 * @param attrName attribute name
	 * @param paramValue value of attribute
	 * @return this
	 */
	public Attributes addIfExists(String attrName, Object paramValue) {
		return addIfExists(attrName, paramValue, true);
	}

	/**
	 * Add a key/value pair to the attributes only if the value is not null.
	 * 
	 * @param attrName attribute name
	 * @param paramValue value of attribute
	 * @param encode html encode the value
	 * @return this
	 */
	public Attributes addIfExists(String attrName, Object paramValue, boolean encode) {
		if (paramValue != null) {
			String val = paramValue.toString();
			if (Strings.isNotEmpty(val)) {
				if (encode) {
					val = StringEscapes.escapeHtml(val);
				}
				attributes.put(attrName, val);
				return this;
			}
		}
		
		attributes.remove(attrName);
		return this;
	}

	/**
	 * Add a key/value pair to the attributes only if the value is not null and
	 * is a boolean with a value of 'true'. Value is html encoded
	 * 
	 * @param attrName attribute name
	 * @param paramValue value of attribute
	 * @return this
	 */
	public Attributes addIfTrue(String attrName, Object paramValue) {
		if (isTrue(paramValue)) {
			attributes.put(attrName, Boolean.TRUE.toString());
		}
		else {
			attributes.remove(attrName);
		}
		return this;
	}

	public static boolean isTrue(Object paramValue) {
		return isTrue(paramValue, false);
	}

	public static boolean isTrue(Object paramValue, boolean def) {
		if (paramValue != null) {
			return ((paramValue instanceof Boolean && ((Boolean)paramValue).booleanValue())
					|| (Boolean.valueOf(paramValue.toString()).booleanValue()));
		}
		return def;
	}

	public Attributes commons(UIBean tag) {
		addIfExists("accesskey", tag.getAccesskey());
		return this;
	}

	public Attributes data(String key, String value) {
		if (Strings.isNotEmpty(value)) {
			add("data-" + Texts.uncamelWord(key, '-'), value);
		}
		return this;
	}
	
	public Attributes dynamic(String key, Object value) {
		if (value != null) {
			String val = value.toString();
			if (Strings.isNotEmpty(val)) {
				if (Strings.startsWith(key, "data")) {
					key = Texts.uncamelWord(key, '-');
				}
				add(key, val);
			}
		}
		return this;
	}
	
	public Attributes dynamics(UIBean tag) {
		if (Collections.isNotEmpty(tag.getParameters())) {
			for (Entry<String, Object> e : tag.getParameters().entrySet()) {
				dynamic(e.getKey(), e.getValue());
			}
		}
		return this;
	}

	public Attributes events(UIBean tag) {
		addIfExists("onclick", tag.getOnclick());
		addIfExists("ondblclick", tag.getOndblclick());
		addIfExists("onmousedown", tag.getOnmousedown());
		addIfExists("onmouseup", tag.getOnmouseup());
		addIfExists("onmouseover", tag.getOnmouseover());
		addIfExists("onmousemove", tag.getOnmousemove());
		addIfExists("onmouseout", tag.getOnmouseout());
		addIfExists("onfocus", tag.getOnfocus());
		addIfExists("onblur", tag.getOnblur());
		addIfExists("onkeypress", tag.getOnkeypress());
		addIfExists("onkeydown", tag.getOnkeydown());
		addIfExists("onkeyup", tag.getOnkeyup());
		addIfExists("onselect", tag.getOnselect());
		addIfExists("onchange", tag.getOnchange());
		return this;
	}

	public Attributes name(UIBean bean) {
		return name(bean.getName());
	}

	public Attributes name(String value) {
		addIfExists("name", value);
		return this;
	}

	public Attributes id(Object id) {
		addIfExists("id", id);
		return this;
	}

	public Attributes id(UIBean bean) {
		return id(bean.getId());
	}

	public Attributes type(String type) {
		addIfExists("type", type);
		return this;
	}

	public Attributes forId(UIBean tag) {
		addIfExists("for", tag.getId());
		return this;
	}

	public Attributes css(AbstractTagRenderer tr) {
		return css(tr, null);
	}
	
	public Attributes css(AbstractTagRenderer tr, String basic) {
		UIBean tag = tr.tag;

		String name = tag.getName();
		String cssClass = tag.getCssClass();
		String cssErrorClass = tag.getCssErrorClass();
		String cssStyle = tag.getCssStyle();
		String cssErrorStyle = tag.getCssErrorStyle();
		
		Map<String, List<String>> errors = tr.context.getParamAlert().getErrors();
		
		boolean hasFieldErrors = (name != null && errors != null && errors.get(name) != null);
		
		if (cssClass != null && !(hasFieldErrors && cssErrorClass != null)) {
			cssClass(cssClass, basic);
		}
		else if (cssClass != null && hasFieldErrors && cssErrorClass != null) {
			cssClass(cssClass + ' ' + cssErrorClass, basic);
		}
		else if (cssClass == null && hasFieldErrors && cssErrorClass != null) {
			cssClass(cssErrorClass, basic);
		}
		
		if (cssStyle != null && !(hasFieldErrors && cssErrorStyle != null)) {
			cssStyle(cssStyle);
		}
		else if (cssStyle != null && hasFieldErrors && cssErrorStyle != null) {
			cssStyle(cssStyle + ' ' + cssErrorStyle);
		}
		else if (cssStyle == null && hasFieldErrors && cssErrorStyle != null) {
			cssStyle(cssErrorStyle);
		}
		
		return this;
	}

	public Attributes cssClass(String cssClass) {
		addIfExists("class", cssClass);
		return this;
	}

	public Attributes cssClass(String cssClass, String basic) {
		StringBuilder sb = new StringBuilder();
		if (Strings.isNotEmpty(basic)) {
			sb.append(basic);
		}
		if (Strings.isNotEmpty(cssClass)) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(cssClass);
		}
		addIfExists("class", sb.toString());
		return this;
	}

	public Attributes cssClass(UIBean tag) {
		addIfExists("class", tag.getCssClass());
		return this;
	}

	public Attributes cssClass(UIBean tag, String basic) {
		return cssClass(tag.getCssClass(), basic);
	}

	public Attributes cssStyle(String cssStyle) {
		addIfExists("style", cssStyle);
		return this;
	}

	public Attributes cssStyle(UIBean tag) {
		addIfExists("style", tag.getCssStyle());
		return this;
	}

	public Attributes href(Anchor tag) {
		addIfExists("href", tag.getHref(), false);
		return this;
	}

	public Attributes title(UIBean tag) {
		if (Strings.isEmpty(tag.getTitle())) {
			addIfExists("title", tag.getTooltip());
		}
		else {
			add("title", tag.getTitle());
		}
		return this;
	}

	public Attributes placeholder(TextField tf) {
		addIfExists("placeholder", tf.getPlaceholder());
		return this;
	}

	public Attributes mask(TextField tf) {
		addIfExists("mask", tf.getMask());
		return this;
	}

	public Attributes tabindex(UIBean tag) {
		addIfExists("tabindex", tag.getTabindex());
		return this;
	}

	public Attributes size(Select tag) {
		addIfExists("size", tag.getSize());
		return this;
	}

	public Attributes size(Uploader tag) {
		addIfExists("size", tag.getSize());
		return this;
	}

	public Attributes size(File tag) {
		addIfExists("size", tag.getSize());
		return this;
	}

	public Attributes size(TextField tf) {
		addIfExists("size", tf.getSize());
		return this;
	}

	public Attributes value(Object value) {
		addIfExists("value", value);
		return this;
	}

	public Attributes value(InputUIBean tag) {
		addIfExists("value", tag.getValue());
		return this;
	}

	public Attributes formatValue(AbstractTagRenderer tr, TextField tf) {
		add("value", tr.formatValue(tf.getValue(), tf.getFormat()), false);
		return this;
	}

	public Attributes accept(Uploader tag) {
		addIfExists("accept", tag.getAccept());
		return this;
	}

	public Attributes accept(File tag) {
		addIfExists("accept", tag.getAccept());
		return this;
	}

	public Attributes readonly(InputUIBean tag) {
		addIfTrue("readonly", tag.getReadonly());
		return this;
	}

	public Attributes disabled(boolean disabled) {
		addIfTrue("disabled", disabled);
		return this;
	}

	public Attributes disabled(UIBean tag) {
		addIfTrue("disabled", tag.getDisabled());
		return this;
	}

	public Attributes onsubmit(Form tag) {
		addIfExists("onsubmit", tag.getOnsubmit());
		return this;
	}

	public Attributes onreset(Form tag) {
		addIfExists("onreset", tag.getOnreset());
		return this;
	}

	public Attributes action(Form tag) {
		addIfExists("action", tag.getAction());
		return this;
	}

	public Attributes target(Anchor tag) {
		addIfExists("target", tag.getTarget());
		return this;
	}

	public Attributes target(Form tag) {
		addIfExists("target", tag.getTarget());
		return this;
	}

	public Attributes enctype(Form tag) {
		addIfExists("enctype", tag.getEnctype());
		return this;
	}

	public Attributes acceptcharset(Form tag) {
		addIfExists("accept-charset", tag.getAcceptcharset());
		return this;
	}

	public Attributes labelFor(Map<String, Object> params) {
		addIfExists("for", params.get("for"));
		return this;
	}

	public Attributes maxlength(TextField tf) {
		addIfExists("maxlength", tf.getMaxlength());
		return this;
	}

	public Attributes src(FormButton tag) {
		addIfExists("src", URLHelper.encodeURL(tag.getSrc()), false);
		return this;
	}

	public Attributes multiple(Select tag) {
		addIfTrue("multiple", tag.isMultiple());
		return this;
	}

	public Attributes cols(TextArea ta) {
		addIfExists("cols", ta.getCols(), false);
		return this;
	}

	public Attributes rows(TextArea ta) {
		addIfExists("rows", ta.getRows(), false);
		return this;
	}

	public Attributes wrap(TextArea ta) {
		addIfExists("wrap", ta.getWrap(), false);
		return this;
	}
}
