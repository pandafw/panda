package panda.mvc.view.tag.ui.theme.xhtml;

import panda.mvc.view.tag.ui.Checkbox;
import panda.mvc.view.tag.ui.CheckboxList;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.File;
import panda.mvc.view.tag.ui.Form;
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
import panda.mvc.view.tag.ui.theme.RendererWrapperFactory;
import panda.mvc.view.tag.ui.theme.Theme;
import panda.mvc.view.tag.ui.theme.WrapTheme;
import panda.mvc.view.tag.ui.theme.simple.CheckboxListRenderer;
import panda.mvc.view.tag.ui.theme.simple.CheckboxRenderer;
import panda.mvc.view.tag.ui.theme.simple.DatePickerRenderer;
import panda.mvc.view.tag.ui.theme.simple.DateTimePickerRenderer;
import panda.mvc.view.tag.ui.theme.simple.DivRenderer;
import panda.mvc.view.tag.ui.theme.simple.FileRenderer;
import panda.mvc.view.tag.ui.theme.simple.FormRenderer;
import panda.mvc.view.tag.ui.theme.simple.PasswordRenderer;
import panda.mvc.view.tag.ui.theme.simple.RadioRenderer;
import panda.mvc.view.tag.ui.theme.simple.ResetRenderer;
import panda.mvc.view.tag.ui.theme.simple.SelectRenderer;
import panda.mvc.view.tag.ui.theme.simple.SubmitRenderer;
import panda.mvc.view.tag.ui.theme.simple.TextAreaRenderer;
import panda.mvc.view.tag.ui.theme.simple.TextFieldRenderer;
import panda.mvc.view.tag.ui.theme.simple.TimePickerRenderer;
import panda.mvc.view.tag.ui.theme.simple.TriggerFieldRenderer;
import panda.mvc.view.tag.ui.theme.simple.UploaderRenderer;
import panda.mvc.view.tag.ui.theme.simple.ViewFieldRenderer;

public class XhtmlTheme extends WrapTheme {
	public XhtmlTheme(Theme defaultTheme) {
		super(defaultTheme);

		setName("xhtml");

		addWrapper(Form.class, new RendererWrapperFactory(XFormWrapper.class, FormRenderer.class));

		addWrapper(Div.class, new RendererWrapperFactory(XInputWrapper.GroupWrapper.class, DivRenderer.class));

		addWrapper(Checkbox.class, new RendererWrapperFactory(XInputWrapper.NormalWrapper.class, CheckboxRenderer.class));
		addWrapper(CheckboxList.class, new RendererWrapperFactory(XInputWrapper.NormalWrapper.class, CheckboxListRenderer.class));
		addWrapper(DatePicker.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, DatePickerRenderer.class));
		addWrapper(DateTimePicker.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, DateTimePickerRenderer.class));
		addWrapper(File.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, FileRenderer.class));
		addWrapper(Password.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, PasswordRenderer.class));
		addWrapper(Radio.class, new RendererWrapperFactory(XInputWrapper.NormalWrapper.class, RadioRenderer.class));
		addWrapper(Select.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, SelectRenderer.class));
		addWrapper(TextField.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, TextFieldRenderer.class));
		addWrapper(TextArea.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, TextAreaRenderer.class));
		addWrapper(TimePicker.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, TimePickerRenderer.class));
		addWrapper(TriggerField.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, TriggerFieldRenderer.class));
		addWrapper(Uploader.class, new RendererWrapperFactory(XInputWrapper.ControlWrapper.class, UploaderRenderer.class));

		addWrapper(ViewField.class, new RendererWrapperFactory(XInputWrapper.StaticWrapper.class, ViewFieldRenderer.class));

		addWrapper(Reset.class, new RendererWrapperFactory(XInputWrapper.NormalWrapper.class, ResetRenderer.class));
		addWrapper(Submit.class, new RendererWrapperFactory(XInputWrapper.NormalWrapper.class, SubmitRenderer.class));
	}
}
