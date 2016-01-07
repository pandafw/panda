package panda.mvc.view.tag.io.jsp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.tag.CBoolean;
import panda.mvc.view.tag.CDate;
import panda.mvc.view.tag.CLog;
import panda.mvc.view.tag.CNumber;
import panda.mvc.view.tag.CSet;
import panda.mvc.view.tag.Component;
import panda.mvc.view.tag.Csv;
import panda.mvc.view.tag.Head;
import panda.mvc.view.tag.Param;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.Text;
import panda.mvc.view.tag.ui.ActionConfirm;
import panda.mvc.view.tag.ui.ActionError;
import panda.mvc.view.tag.ui.ActionMessage;
import panda.mvc.view.tag.ui.ActionWarning;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.Button;
import panda.mvc.view.tag.ui.Checkbox;
import panda.mvc.view.tag.ui.CheckboxList;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.FieldError;
import panda.mvc.view.tag.ui.File;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.Hidden;
import panda.mvc.view.tag.ui.Icon;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.Password;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.Reset;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.Submit;
import panda.mvc.view.tag.ui.TextArea;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.ViewField;

/**
 */
@SuppressWarnings("serial")
public abstract class JspTag extends BodyTagSupport implements DynamicAttributes {
	protected Map<String, Object> parameters;
	protected Component component;

	protected Component getBean(ActionContext ac, Class<? extends Component> cls) {
		return ac.getIoc().get(cls);
	}
	
	protected abstract Component getBean(ActionContext ac);
	
	protected ActionContext getActionContext() {
		return (ActionContext)pageContext.getRequest().getAttribute(ActionContext.class.getName());
	}
	
	public Component getComponent() {
		return component;
	}

	protected String getBody() {
		if (bodyContent == null) {
			return "";
		}
		return bodyContent.getString().trim();
	}

	public int doEndTag() throws JspException {
		component.end(pageContext.getOut(), getBody());
		component = null;
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		component = getBean(getActionContext());

		populateParams();
		boolean evalBody = component.start(pageContext.getOut());

		if (evalBody) {
			return component.usesBody() ? EVAL_BODY_BUFFERED : EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}

	protected void populateParams() {
		if (Strings.isNotEmpty(getId())) {
			parameters.put("id", getId());
		}
		component.setParameters(parameters);
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		parameters.put(localName, value);
	}
	
	
	//-----------------------------------------------------------------------------
	public static class BooleanTag extends JspTag {
		protected Component getBean(ActionContext context) {
			return getBean(context, CBoolean.class);
		}
	}
	
	public static class CsvTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Csv.class);
		}
	}

	public static class DateTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, CDate.class);
		}
	}

	public static class HeadTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Head.class);
		}
	}

	public static class LogTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, CLog.class);
		}
	}

	public static class NumberTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, CNumber.class);
		}
	}

	public static class PropertyTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Property.class);
		}
	}

	public static class SetTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, CSet.class);
		}
	}

	public static class TextTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Text.class);
		}
	}

	public static class ParamTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Param.class);
		}
	}
	
	//-----------------------------------------------------
	// UI tags
	//
	public static class ActionConfirmTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, ActionConfirm.class);
		}
	}

	public static class ActionErrorTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, ActionError.class);
		}
	}

	public static class ActionMessageTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, ActionMessage.class);
		}
	}

	public static class ActionWarningTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, ActionWarning.class);
		}
	}

	public static class AnchorTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Anchor.class);
		}
	}

	public static class ButtonTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Button.class);
		}
	}

	public static class CheckboxTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Checkbox.class);
		}

	}

	public static class CheckboxListTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, CheckboxList.class);
		}
	}

	public static class DatePickerTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, DatePicker.class);
		}
	}

	public static class DateTimePickerTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, DateTimePicker.class);
		}
	}

	public static class DivTag extends JspTag {
		@Override
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Div.class);
		}
	}

	public static class FieldErrorTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, FieldError.class);
		}
	}

	public static class FileTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, File.class);
		}
	}

	public static class FormTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Form.class);
		}
	}

	public static class HiddenTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Hidden.class);
		}
	}

	public static class IconTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Icon.class);
		}
	}

	public static class LinkTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Link.class);
		}
	}

	public static class ListViewTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, ListView.class);
		}
	}

	public static class PagerTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Pager.class);
		}
	}

	public static class PasswordTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Password.class);
		}
	}

	public static class RadioTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Radio.class);
		}
	}

	public static class ResetTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Reset.class);
		}
	}

	public static class SelectTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Select.class);
		}
	}

	public static class SubmitTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Submit.class);
		}
	}

	public static class TextAreaTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, TextArea.class);
		}
	}

	public static class TextFieldTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, TextField.class);
		}
	}

	public static class TimePickerTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, TimePicker.class);
		}
	}

	public static class TriggerFieldTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, TriggerField.class);
		}
	}

	public static class UploaderTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, Uploader.class);
		}
	}

	public static class ViewFieldTag extends JspTag {
		protected Component getBean(ActionContext ac) {
			return getBean(ac, ViewField.class);
		}
	}
}

