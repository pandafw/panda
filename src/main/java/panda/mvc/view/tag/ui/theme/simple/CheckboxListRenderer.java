package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.mvc.view.tag.ui.CheckboxList;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class CheckboxListRenderer extends AbstractEndRenderer<CheckboxList> {
	public CheckboxListRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		String id = tag.getId();

		write("<div");
		if (Strings.isNotEmpty(id)) {
			write(" id=\"" + html(id) + "\"");
		}
		write(" class=\"p-checkboxlist\">");
		
		Boolean disabled = tag.getDisabled();
		Object value = tag.getValue();

		Attributes attrs = new Attributes();
		attrs.add("type", "checkbox")
			.name(tag)
			.readonly(tag)
			.title(tag)
			.cssClass(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);

		if (tag.isNotEmptyList()) {
			int itemCount = 0;
			for (KeyValue kv : tag.asIterable()) {
				itemCount++;

				boolean checked = tag.contains(value, kv.getKey());

				write("<label class=\"checkbox-inline\">");
				attrs.add("id", id + "_" + itemCount)
					.addIfExists("value", kv.getKey().toString())
					.addIfTrue("checked", checked)
					.addIfTrue("disabled", disabled);
				xtag("input", attrs);
				
				body(kv.getValue().toString());
				write("</label>");
			}
		}
		else {
			write(" &nbsp;");
		}

		write("</div>");
	}
}
