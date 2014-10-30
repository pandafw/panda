package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.collection.KeyValue;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class RadioRenderer extends AbstractEndRenderer<Radio> {
	public RadioRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		String id = tag.getId();
		String name = tag.getName();
		Object value = tag.getValue();

		write("<div id=\"" + html(id) + "\" class=\"p-radiomap\">");

		Attributes attrs = new Attributes();
		attrs.add("type", "radio")
			.name(tag)
			.cssClass(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.title(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);

		// header
		String headerKey = tag.getHeaderKey();
		String headerValue = tag.getHeaderValue();
		if (headerKey != null && headerValue != null) {
			boolean checked = tag.contains(value, headerKey);
			writeItem(attrs, name, id, 0, headerKey, headerValue, checked);
		}

		if (tag.isNotEmptyList()) {
			int itemCount = 0;
			for (KeyValue kv : tag.asIterable()) {
				itemCount++;

				boolean checked = tag.contains(value, kv.getKey());

				writeItem(attrs, name, id, itemCount, kv.getKey().toString(), kv.getValue().toString(), checked);
			}
		}
		else {
			write(" &nbsp;");
		}

		write("</div>");
	}

	protected void writeItem(Attributes attrs, String name, String id, int itemCount, String itemKeyStr, String itemValueStr,
			boolean checked) throws IOException {
		write("<label class=\"radio-inline\">");
		attrs.add("id", id + "_" + itemCount)
			.addIfExists("value", itemKeyStr)
			.addIfTrue("checked", checked);
		xtag("input", attrs);

		body(itemValueStr);
		write("</label>");
	}
}
