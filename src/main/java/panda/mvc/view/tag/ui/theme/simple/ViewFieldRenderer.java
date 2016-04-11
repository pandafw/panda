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
		Attributes a = null;

		Object value = tag.getValue();
		if (Iterators.isIterable(value)) {
			for (Object item : Iterators.asIterable(value)) {
				a = new Attributes();
				a.type("hidden")
				 .id(tag)
				 .name(tag)
				 .disabled(tag)
				 .value(tag.castString(item, tag.getFormat()));
				xtag("input", a);
			}
		}
		else {
			a = new Attributes();
			a.type("hidden")
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
									body.append(StringEscapes.escapeHtml(sl)).append(sp);
								}
							}
						}
					}
					else {
						for (Object v : Iterators.asIterable(value)) {
							if (v != null) {
								String sl = v.toString();
								if (Strings.isNotEmpty(sl)) {
									body.append(StringEscapes.escapeHtml(sl)).append(sp);
								}
							}
						}
					}
				}
				else {
					for (KeyValue kv : tag.asIterable()) {
						boolean selected = tag.contains(value, kv.getKey());
	
						if (selected && kv.getValue() != null) {
							body.append(StringEscapes.escapeHtml(kv.getValue().toString())).append(sp);
						}
					}
				}
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
		etag("p");
	}
	
	private String hiddenValue(ViewField tag) {
		if (Strings.isNotEmpty(tag.getFieldValue())) {
			return Boolean.TRUE.equals(tag.getValue()) ? tag.getFieldValue() : "";
		}

		return tag.castString(tag.getValue(), tag.getFormat());
	}
}
