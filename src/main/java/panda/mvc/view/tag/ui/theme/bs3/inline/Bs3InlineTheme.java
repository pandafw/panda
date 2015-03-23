package panda.mvc.view.tag.ui.theme.bs3.inline;

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

public class Bs3InlineTheme extends WrapTheme {
	public Bs3InlineTheme(Theme defaultTheme) {
		super(defaultTheme);
		setName("bs3i");

		addWrapper(Form.class, new RendererWrapperFactory(Bs3InlineFormWrapper.class, FormRenderer.class));

		addWrapper(Div.class, new RendererWrapperFactory(Bs3InlineInputWrapper.GroupWrapper.class, DivRenderer.class));

		addWrapper(Checkbox.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, CheckboxRenderer.class));
		addWrapper(CheckboxList.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, CheckboxListRenderer.class));
		addWrapper(DatePicker.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, DatePickerRenderer.class));
		addWrapper(DateTimePicker.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, DateTimePickerRenderer.class));
		addWrapper(File.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, FileRenderer.class));
		addWrapper(Password.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, PasswordRenderer.class));
		addWrapper(Radio.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, RadioRenderer.class));
		addWrapper(Select.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, SelectRenderer.class));
		addWrapper(TextField.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, TextFieldRenderer.class));
		addWrapper(TextArea.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, TextAreaRenderer.class));
		addWrapper(TimePicker.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, TimePickerRenderer.class));
		addWrapper(TriggerField.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, TriggerFieldRenderer.class));
		addWrapper(Uploader.class, new RendererWrapperFactory(Bs3InlineInputWrapper.ControlWrapper.class, UploaderRenderer.class));

		addWrapper(ViewField.class, new RendererWrapperFactory(Bs3InlineInputWrapper.StaticWrapper.class, ViewFieldRenderer.class));

		addWrapper(Reset.class, new RendererWrapperFactory(Bs3InlineInputWrapper.NormalWrapper.class, ResetRenderer.class));
		addWrapper(Submit.class, new RendererWrapperFactory(Bs3InlineInputWrapper.NormalWrapper.class, SubmitRenderer.class));
	}
}
