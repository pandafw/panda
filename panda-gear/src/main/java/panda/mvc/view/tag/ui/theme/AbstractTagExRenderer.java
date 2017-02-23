package panda.mvc.view.tag.ui.theme;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import panda.mvc.view.tag.ui.CheckboxList;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.UIBean;

public abstract class AbstractTagExRenderer<T extends UIBean> extends AbstractTagRenderer<T> {
	private CheckboxList checkboxlist;
	private DatePicker datepicker;
	private DateTimePicker datetimepicker;
	private TimePicker timepicker;
	private Radio radio;
	private Select select;
	private TextField textField;
	
	public AbstractTagExRenderer(RenderingContext rc) {
		super(rc);
	}

	protected void writeTextField(String cssClass, String name, String id, Object value, boolean disabled) throws IOException {
		if (textField == null) {
			textField = newTag(TextField.class);
		}

		textField.setCssClass(cssClass);
		textField.setName(name);
		textField.setId(id);
		textField.setValue(value);
		textField.setDisabled(disabled);
		
		textField.start(writer);
		textField.end(writer, "");
	}

	protected void writeSelect(String cssClass, String name, String id, Object list, String value) throws IOException {
		writeSelect(cssClass, name, id, list, value, false);
	}

	protected void writeSelect(String cssClass,
			String name, String id, Object list, String value,
			Boolean emptyOption) throws IOException {
		writeSelect(cssClass, name, id, list, value, emptyOption, null);
	}
	
	protected void writeSelect(String cssClass,
			String name, String id, Object list, String value,
			boolean emptyOption, String onchange) throws IOException {
		
		if (select == null) {
			select = newTag(Select.class);
		}

		select.setCssClass(cssClass);
		select.setName(name);
		select.setId(id);
		select.setList(list);
		select.setValue(value);
		select.setEmptyOption(emptyOption);
		select.setOnchange(onchange);

		select.start(writer);
		select.end(writer, "");
	}
	
	protected void writeCheckboxList(String cssClass, String name, String id, Object list, List<String> value)
			throws IOException {
		if (checkboxlist == null) {
			checkboxlist = newTag(CheckboxList.class);
		}

		checkboxlist.setCssClass(cssClass);
		checkboxlist.setName(name);
		checkboxlist.setId(id);
		checkboxlist.setList(list);
		checkboxlist.setValue(value);

		checkboxlist.start(writer);
		checkboxlist.end(writer, "");
	}
	
	protected void writeRadio(String cssClass, String name, String id, Object list, String value) throws IOException {
		if (radio == null) {
			radio = newTag(Radio.class);
		}

		radio.setCssClass(cssClass);
		radio.setName(name);
		radio.setId(id);
		radio.setList(list);
		radio.setValue(value);

		radio.start(writer);
		radio.end(writer, "");
	}
	
	protected void writeDatePicker(String cssClass,
			String name, String id, String format,
			Date value, Boolean disabled) throws IOException {

		if (datepicker == null) {
			datepicker = newTag(DatePicker.class);
		}

		datepicker.setCssClass(cssClass);
		datepicker.setName(name);
		datepicker.setId(id);
		datepicker.setFormat(format);
		datepicker.setValue(value);
		datepicker.setDisabled(disabled);
		datepicker.setMaxlength(10);
		
		datepicker.start(writer);
		datepicker.end(writer, "");
	}
	
	protected void writeDateTimePicker(String cssClass, String name, String id, String format, Date value, Boolean disabled)
			throws IOException {

		if (datetimepicker == null) {
			datetimepicker = newTag(DateTimePicker.class);
		}

		datetimepicker.setCssClass(cssClass);
		datetimepicker.setName(name);
		datetimepicker.setId(id);
		datetimepicker.setFormat(format);
		datetimepicker.setValue(value);
		datetimepicker.setDisabled(disabled);
		datetimepicker.setMaxlength(20);
		
		datetimepicker.start(writer);
		datetimepicker.end(writer, "");
	}
	
	protected void writeTimePicker(String cssClass, String name, String id, String format, Date value, Boolean disabled)
			throws IOException {
		if (timepicker == null) {
			timepicker = newTag(TimePicker.class);
		}

		timepicker.setCssClass(cssClass);
		timepicker.setName(name);
		timepicker.setId(id);
		timepicker.setFormat(format);
		timepicker.setValue(value);
		timepicker.setDisabled(disabled);
		timepicker.setMaxlength(8);
		
		timepicker.start(writer);
		timepicker.end(writer, "");
	}
}
