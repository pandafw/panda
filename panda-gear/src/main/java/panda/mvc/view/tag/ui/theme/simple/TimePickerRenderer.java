package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Arrays;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TimePickerRenderer extends AbstractTextFieldRenderer<TimePicker> {
	public TimePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "timepicker";
	}
	
	@Override
	protected Map<String, String> getDatas() {
		return Arrays.toMap("format", tag.getPattern());
	}

	@Override
	protected void renderHeader() throws IOException {
		write("<div class=\"input-group p-timepicker\" data-spy=\"datetimepicker\" data-pick-date=\"false\">");
	}
}
