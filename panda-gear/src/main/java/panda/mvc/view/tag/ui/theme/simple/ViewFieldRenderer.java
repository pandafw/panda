package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.StringEscapes;
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
		Attributes a = new Attributes();

		Object value = tag.getValue();
		if (Iterators.isIterable(value)) {
			for (Object item : Iterators.asIterable(value)) {
				a.clear()
				 .type("hidden")
				 .id(tag)
				 .name(tag)
				 .disabled(tag)
				 .value(tag.castString(item, tag.getFormat()));
				xtag("input", a);
			}
		}
		else {
			a.clear()
			 .type("hidden")
			 .id(tag)
			 .name(tag)
			 .disabled(tag)
			 .value(hiddenValue(tag));
			xtag("input", a);
		}
		
		a.clear()
		 .css(this, "p-viewfield")
		 .tooltip(tag)
		 .commons(tag)
		 .data("format", tag.getFormat())
		 .dynamics(tag);
		stag("div", a);

		StringBuilder body = new StringBuilder();
		if (Objects.isNotEmpty(value)) {
			String icon = tag.getIcon();
			if (icon != null) {
				body.append(xicon(icon + " p-vf-icon"));
			}

			if (tag.isNotEmptyList()) {
				String sp = tag.isListBreak() ? "<br>" : " ";
				if (tag.isListOrder()) {
					if (tag.getList() instanceof Map) {
						Map m = (Map)tag.getList();
						for (Object v : Iterators.asIterable(value)) {
							Object label = m.get(v);
							if (label == null && !(v instanceof String)) {
								label = m.get(v.toString());
							}

							if (label != null) {
								String sl = label.toString();
								if (Strings.isNotEmpty(sl)) {
									body.append(StringEscapes.escapeHTML(sl)).append(sp);
								}
							}
						}
					}
					else {
						for (Object v : Iterators.asIterable(value)) {
							if (v != null) {
								String sl = v.toString();
								if (Strings.isNotEmpty(sl)) {
									body.append(StringEscapes.escapeHTML(sl)).append(sp);
								}
							}
						}
					}
				}
				else {
					for (KeyValue kv : tag.asIterable()) {
						boolean selected = tag.contains(value, kv.getKey());
	
						if (selected && kv.getValue() != null) {
							body.append(StringEscapes.escapeHTML(kv.getValue().toString())).append(sp);
						}
					}
				}
			}
			else if ("html".equalsIgnoreCase(tag.getFormat())) {
				body.append(tag.getValue());
			}
			else {
				body.append(formatValue(tag.getValue(), tag.getFormat(), tag.getEscape()));
			}
		}
		
		if (body.length() > 0) {
			write(body.toString());
		}
		else {
			write("&nbsp;");
		}
		etag("div");
	}
	
	private String hiddenValue(ViewField tag) {
		if (Strings.isNotEmpty(tag.getFieldValue())) {
			return Boolean.TRUE.equals(tag.getValue()) ? tag.getFieldValue() : "";
		}

		return tag.castString(tag.getValue(), tag.getFormat());
	}
}
