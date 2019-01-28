package panda.mvc.view.tag.ui.theme.simple;

import java.util.HashMap;
import java.util.Map;

import panda.lang.Classes;
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
import panda.mvc.view.tag.ui.HtmlEditor;
import panda.mvc.view.tag.ui.Icon;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.Password;
import panda.mvc.view.tag.ui.Queryer;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.Reset;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.Submit;
import panda.mvc.view.tag.ui.TextArea;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.Token;
import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.UIBean;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.ViewField;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.mvc.view.tag.ui.theme.TagRenderer;
import panda.mvc.view.tag.ui.theme.Theme;

public class SimpleTheme extends Theme {
	private Map<Class<? extends UIBean>, Class<? extends TagRenderer>> rfs;

	public SimpleTheme() {
		setName("simple");

		rfs = new HashMap<Class<? extends UIBean>, Class<? extends TagRenderer>>();

		// panda class
		rfs.put(Anchor.class, AnchorRenderer.class);
		rfs.put(ActionConfirm.class, ActionConfirmRenderer.class);
		rfs.put(ActionError.class, ActionErrorRenderer.class);
		rfs.put(ActionMessage.class, ActionMessageRenderer.class);
		rfs.put(ActionWarning.class, ActionWarningRenderer.class);
		rfs.put(Button.class, ButtonRenderer.class);
		rfs.put(Checkbox.class, CheckboxRenderer.class);
		rfs.put(CheckboxList.class, CheckboxListRenderer.class);
		rfs.put(DatePicker.class, DatePickerRenderer.class);
		rfs.put(DateTimePicker.class, DateTimePickerRenderer.class);
		rfs.put(Div.class, DivRenderer.class);
		rfs.put(FieldError.class, FieldErrorRenderer.class);
		rfs.put(File.class, FileRenderer.class);
		rfs.put(Form.class, FormRenderer.class);
		rfs.put(Hidden.class, HiddenRenderer.class);
		rfs.put(HtmlEditor.class, HtmlEditorRenderer.class);
		rfs.put(Icon.class, IconRenderer.class);
		rfs.put(Link.class, LinkRenderer.class);
		rfs.put(ListView.class, ListViewRenderer.class);
		rfs.put(Pager.class, PagerRenderer.class);
		rfs.put(Password.class, PasswordRenderer.class);
		rfs.put(Queryer.class, QueryerRenderer.class);
		rfs.put(Radio.class, RadioRenderer.class);
		rfs.put(Reset.class, ResetRenderer.class);
		rfs.put(Select.class, SelectRenderer.class);
		rfs.put(Submit.class, SubmitRenderer.class);
		rfs.put(TextField.class, TextFieldRenderer.class);
		rfs.put(TextArea.class, TextAreaRenderer.class);
		rfs.put(TimePicker.class, TimePickerRenderer.class);
		rfs.put(Token.class, TokenRenderer.class);
		rfs.put(TriggerField.class, TriggerFieldRenderer.class);
		rfs.put(Uploader.class, UploaderRenderer.class);
		rfs.put(ViewField.class, ViewFieldRenderer.class);
	}

	@Override
	public TagRenderer createTagRenderer(RenderingContext rctx) {
		Class<? extends TagRenderer> rendererClass = rfs.get(rctx.getTag().getClass());
		return (TagRenderer)Classes.born(rendererClass, rctx, RenderingContext.class);
	}
}
