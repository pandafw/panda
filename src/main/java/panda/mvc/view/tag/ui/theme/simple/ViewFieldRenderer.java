package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.mvc.view.tag.ui.ViewField;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ViewFieldRenderer extends AbstractEndRenderer<ViewField> {
	public ViewFieldRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes a = null;

		Object value = tag.getValue();
		if (Iterators.isIterable(value)) {
			for (Object item : Iterators.asIterable(value)) {
				a = new Attributes();
				a.add("type", "hidden")
				 .id(tag)
				 .name(tag)
				 .disabled(tag)
				 .add("value", formatValue(item, tag.getFormat()), true);
				xtag("input", a);
			}
		}
		else {
			a = new Attributes();
			a.add("type", "hidden")
			 .id(tag)
			 .name(tag)
			 .disabled(tag)
			 .value(hiddenValue(tag));
			xtag("input", a);
		}
		
		a = new Attributes();
		a.css(this, "p-viewfield")
		 .title(tag)
		 .commons(tag)
		 .dynamics(tag);
		stag("p", a);

		StringBuilder body = new StringBuilder();
		if (Objects.isNotEmpty(value)) {
			String icon = tag.getIcon();
			if (icon != null) {
				body.append(xicon(icon + " p-vf-icon"));
			}

			if (tag.isNotEmptyList()) {
				for (KeyValue kv : tag.asIterable()) {
					boolean selected = tag.contains(value, kv.getKey());

					if (selected && kv.getValue() != null) {
						body.append(kv.getValue().toString());
						body.append(' ');
					}
				}
			}
			else {
				body.append(formatValue(tag));
			}
		}
		
		if (body.length() > 0) {
			write(body.toString());
		}
		else {
			write("&nbsp;");
		}
		etag("p");
	}
	
	private String hiddenValue(ViewField tag) {
		if (Strings.isNotEmpty(tag.getFieldValue())) {
			return tag.getFieldValue();
		}

		return formatValue(tag);
	}
}
