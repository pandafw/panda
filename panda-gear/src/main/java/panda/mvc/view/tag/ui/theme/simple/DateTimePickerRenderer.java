package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Arrays;
import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class DateTimePickerRenderer extends AbstractTextFieldRenderer<DateTimePicker> {
	public DateTimePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "datetimepicker";
	}
	
	@Override
	protected Map<String, String> getDatas() {
		return Arrays.toMap("format", tag.getPattern());
	}

	@Override
	protected void renderHeader() throws IOException {
		write("<div class=\"input-group p-datetimepicker\" data-spy=\"datetimepicker\"");
		if (Boolean.FALSE.equals(tag.getPickSeconds())) {
			write(" data-pick-seconds=\"false\"");
		}
		write(">");
	}
}
