package panda.mvc.view.tag.ftl;

import panda.mvc.ActionContext;
import panda.mvc.view.tag.CBoolean;
import panda.mvc.view.tag.CDate;
import panda.mvc.view.tag.CLog;
import panda.mvc.view.tag.CNumber;
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
 * Provides @p.tag access for various tags.
 */
public class TagModels {
	protected ActionContext context;

	protected TagModel actionerror;
	protected TagModel actionwarning;
	protected TagModel actionconfirm;
	protected TagModel actionmessage;
	protected TagModel a;
	protected TagModel bool;
	protected TagModel b;
	protected TagModel checkbox;
	protected TagModel checkboxList;
	protected TagModel csv;
	protected TagModel date;
	protected TagModel datepicker;
	protected TagModel datetimepicker;
	protected TagModel div;
	protected TagModel fielderror;
	protected TagModel file;
	protected TagModel form;
	protected TagModel head;
	protected TagModel hidden;
	protected TagModel label;
	protected TagModel link;
	protected TagModel listview;
	protected TagModel log;
	protected TagModel number;
	protected TagModel pager;
	protected TagModel param;
	protected TagModel password;
	protected TagModel property;
	protected TagModel radio;
	protected TagModel reset;
	protected TagModel select;
	protected TagModel submit;
	protected TagModel text;
	protected TagModel textarea;
	protected TagModel textfield;
	protected TagModel timepicker;
	protected TagModel triggerfield;
	protected TagModel uploader;
	protected TagModel viewfield;

	/**
	 * Constructor
	 * 
	 * @param context action context
	 */
	public TagModels(ActionContext context) {
		this.context = context;
	}

	/**
	 * @return ActionErrorModel
	 */
	public TagModel getActionerror() {
		if (actionerror == null) {
			actionerror = new TagModel(context, ActionError.class);
		}
		return actionerror;
	}

	/**
	 * @return ActionWarningModel
	 */
	public TagModel getActionwarning() {
		if (actionwarning == null) {
			actionwarning = new TagModel(context, ActionWarning.class);
		}
		return actionwarning;
	}

	/**
	 * @return ActionConfirmModel
	 */
	public TagModel getActionconfirm() {
		if (actionconfirm == null) {
			actionconfirm = new TagModel(context, ActionConfirm.class);
		}
		return actionconfirm;
	}

	/**
	 * @return ActionMessageModel
	 */
	public TagModel getActionmessage() {
		if (actionmessage == null) {
			actionmessage = new TagModel(context, ActionMessage.class);
		}
		return actionmessage;
	}

	/**
	 * @return AnchorModel
	 */
	public TagModel getA() {
		if (a == null) {
			a = new TagModel(context, Anchor.class);
		}
		return a;
	}

	/**
	 * @return ButtonModel
	 */
	public TagModel getB() {
		if (b == null) {
			b = new TagModel(context, Button.class);
		}
		return b;
	}

	/**
	 * @return BooleanModel
	 */
	public TagModel getBoolean() {
		if (bool == null) {
			bool = new TagModel(context, CBoolean.class);
		}
		return bool;
	}

	/**
	 * @return CheckboxModel
	 */
	public TagModel getCheckbox() {
		if (checkbox == null) {
			checkbox = new TagModel(context, Checkbox.class);
		}
		return checkbox;
	}

	/**
	 * @return CheckboxListModel
	 */
	public TagModel getCheckboxlist() {
		if (checkboxList == null) {
			checkboxList = new TagModel(context, CheckboxList.class);
		}
		return checkboxList;
	}

	/**
	 * @return CsvModel
	 */
	public TagModel getCsv() {
		if (csv == null) {
			csv = new TagModel(context, Csv.class);
		}
		return csv;
	}

	/**
	 * @return DateModel
	 */
	public TagModel getDate() {
		if (date == null) {
			date = new TagModel(context, CDate.class);
		}
		return date;
	}

	/**
	 * @return DatePickerModel
	 */
	public TagModel getDatepicker() {
		if (datepicker == null) {
			datepicker = new TagModel(context, DatePicker.class);
		}
		return datepicker;
	}

	/**
	 * @return DateTimePickerModel
	 */
	public TagModel getDatetimepicker() {
		if (datetimepicker == null) {
			datetimepicker = new TagModel(context, DateTimePicker.class);
		}
		return datetimepicker;
	}

	/**
	 * @return Div
	 */
	public TagModel getDiv() {
		if (div == null) {
			div = new TagModel(context, Div.class);
		}
		return div;
	}

	/**
	 * @return FieldErrorModel
	 */
	public TagModel getFielderror() {
		if (fielderror == null) {
			fielderror = new TagModel(context, FieldError.class);
		}
		return fielderror;
	}

	/**
	 * @return FileModel
	 */
	public TagModel getFile() {
		if (file == null) {
			file = new TagModel(context, File.class);
		}
		return file;
	}

	/**
	 * @return FormModel
	 */
	public TagModel getForm() {
		if (form == null) {
			form = new TagModel(context, Form.class);
		}
		return form;
	}

	/**
	 * @return HeadModel
	 */
	public TagModel getHead() {
		if (head == null) {
			head = new TagModel(context, Head.class);
		}
		return head;
	}

	/**
	 * @return HiddenModel
	 */
	public TagModel getHidden() {
		if (hidden == null) {
			hidden = new TagModel(context, Hidden.class);
		}
		return hidden;
	}

	/**
	 * @return LabelModel
	 */
	public TagModel getLabel() {
		if (label == null) {
			label = new TagModel(context, Link.class);
		}
		return label;
	}

	/**
	 * @return LinkModel
	 */
	public TagModel getLink() {
		if (link == null) {
			link = new TagModel(context, Link.class);
		}
		return link;
	}

	/**
	 * @return ListViewModel
	 */
	public TagModel getListview() {
		if (listview == null) {
			listview = new TagModel(context, ListView.class);
		}
		return listview;
	}

	/**
	 * @return LogModel
	 */
	public TagModel getLog() {
		if (log == null) {
			log = new TagModel(context, CLog.class);
		}
		return log;
	}

	/**
	 * @return NumberModel
	 */
	public TagModel getNumber() {
		if (number == null) {
			number = new TagModel(context, CNumber.class);
		}
		return number;
	}

	/**
	 * @return PagerModel
	 */
	public TagModel getPager() {
		if (pager == null) {
			pager = new TagModel(context, Pager.class);
		}
		return pager;
	}

	/**
	 * @return PasswordModel
	 */
	public TagModel getPassword() {
		if (password == null) {
			password = new TagModel(context, Password.class);
		}
		return password;
	}

	/**
	 * @return ParamModel
	 */
	public TagModel getParam() {
		if (param == null) {
			param = new TagModel(context, Param.class);
		}
		return param;
	}

	/**
	 * @return PropertyModel
	 */
	public TagModel getProperty() {
		if (property == null) {
			property = new TagModel(context, Property.class);
		}
		return property;
	}

	/**
	 * @return RadioModel
	 */
	public TagModel getRadio() {
		if (radio == null) {
			radio = new TagModel(context, Radio.class);
		}
		return radio;
	}

	/**
	 * @return reset
	 */
	public TagModel getReset() {
		if (reset == null) {
			reset = new TagModel(context, Reset.class);
		}

		return reset;
	}

	/**
	 * @return SelectModel
	 */
	public TagModel getSelect() {
		if (select == null) {
			select = new TagModel(context, Select.class);
		}
		return select;
	}

	/**
	 * @return submit
	 */
	public TagModel getSubmit() {
		if (submit == null) {
			submit = new TagModel(context, Submit.class);
		}

		return submit;
	}

	/**
	 * @return TextModel
	 */
	public TagModel getText() {
		if (text == null) {
			text = new TagModel(context, Text.class);
		}
		return text;
	}

	/**
	 * @return TextAreaModel
	 */
	public TagModel getTextarea() {
		if (textarea == null) {
			textarea = new TagModel(context, TextArea.class);
		}
		return textarea;
	}

	/**
	 * @return TextFieldModel
	 */
	public TagModel getTextfield() {
		if (textfield == null) {
			textfield = new TagModel(context, TextField.class);
		}
		return textfield;
	}

	/**
	 * @return TimePickerModel
	 */
	public TagModel getTimepicker() {
		if (timepicker == null) {
			timepicker = new TagModel(context, TimePicker.class);
		}
		return timepicker;
	}

	/**
	 * @return TriggerFieldModel
	 */
	public TagModel getTriggerfield() {
		if (triggerfield == null) {
			triggerfield = new TagModel(context, TriggerField.class);
		}
		return triggerfield;
	}

	/**
	 * @return uploaderModel
	 */
	public TagModel getUploader() {
		if (uploader == null) {
			uploader = new TagModel(context, Uploader.class);
		}
		return uploader;
	}

	/**
	 * @return ViewFieldModel
	 */
	public TagModel getViewfield() {
		if (viewfield == null) {
			viewfield = new TagModel(context, ViewField.class);
		}
		return viewfield;
	}

}
