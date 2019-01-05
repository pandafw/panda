package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.view.tag.ui.Checkbox;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class CheckboxRenderer extends AbstractEndRenderer<Checkbox> {
	public CheckboxRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attrs = new Attributes();
		attrs.css(this, "checkbox-inline");
		stag("label", attrs);
		
		attrs.clear()
			.add("type", "checkbox")
			.id(tag)
			.name(tag)
			.css(this, "p-checkbox")
			.add("value", tag.getFieldValue())
			.addIfTrue("checked", isChecked())
			.readonly(tag)
			.disabled(tag)
			.tabindex(tag)
			.tooltip(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		xtag("input", attrs);

		if (Strings.isNotEmpty(tag.getFieldLabel())) {
			body(tag.getFieldLabel());
		}
		write("&nbsp;</label>");
	}
	
	protected boolean isChecked() {
		Object v = tag.getValue();
		if (v == null) {
			return false;
		}
		
		if (v instanceof Boolean) {
			return ((Boolean)v).booleanValue();
		}
		
		if (Strings.isEmpty(tag.getFieldValue())) {
			boolean b = Mvcs.castValue(context, v, boolean.class);
			return b;
		}
		
		String s = Mvcs.castString(context, v);
		return tag.getFieldValue().equals(s);
	}
}
