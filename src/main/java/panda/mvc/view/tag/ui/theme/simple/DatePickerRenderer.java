package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Arrays;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class DatePickerRenderer extends AbstractTriggerFieldRenderer<DatePicker> {
	public DatePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "datepicker";
	}
	
	@Override
	protected Map<String, String> getDatas() {
		return Arrays.toMap("format", tag.getPattern());
	}
	
	@Override
	protected void renderHeader() throws IOException {
		write("<div class=\"input-group p-datepicker\" data-spy=\"datetimepicker\" data-pick-time=\"false\">");
	}
}
