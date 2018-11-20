package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.List;

import panda.lang.Collections;
import panda.lang.collection.KeyValue;
import panda.mvc.view.tag.ui.ListUIBean;
import panda.mvc.view.tag.ui.OptGroup;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class SelectRenderer extends AbstractEndRenderer<Select> {
	public SelectRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes a = new Attributes();

		Object value = tag.getValue();

		a.id(tag)
			.name(tag)
			.css(this, "p-select")
			.size(tag)
			.disabled(tag)
			.readonly(tag)
			.multiple(tag)
			.tabindex(tag)
			.tooltip(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);

		if (tag.isEditable()) {
			a.data("spy", "editable-select");
		}

		stag("select", a);

		// options

		// header
		String headerKey = tag.getHeaderKey();
		String headerValue = tag.getHeaderValue();
		if (headerKey != null && headerValue != null) {
			boolean selected = tag.contains(value, headerKey);
			writeOption(headerKey, headerValue, selected);
		}

		// emptyoption
		if (tag.isEmptyOption()) {
			boolean selected = tag.contains(value, "") || (value == null);
			writeOption("", "", selected);
		}

		if (tag.isNotEmptyList()) {
			for (KeyValue kv : tag.asIterable()) {
				boolean selected = tag.contains(value, kv.getKey());
				writeOption(kv.getKey(), kv.getValue(), selected);
			}
		}

		// opt group
		List<OptGroup> optGroups = tag.getOptGroups();
		if (Collections.isNotEmpty(optGroups)) {
			for (OptGroup og : optGroups) {
				writeOptionGroup(og, value);
			}
		}

		etag("select");
	}

	protected void writeOption(Object value, Object text, boolean selected) throws IOException {
		Attributes attrs = new Attributes();
		attrs.addIfExists("value", value).addIfTrue("selected", selected);

		stag("option", attrs);
		body(text == null ? null : text.toString());
		etag("option");
	}

	protected void writeOptionGroup(ListUIBean tag, Object value) throws IOException {
		Attributes a = new Attributes();
		a.addIfExists("label", tag.getLabel()).disabled(tag);
		stag("optgroup", a);

		if (tag.isNotEmptyList()) {
			for (KeyValue kv : tag.asIterable()) {
				boolean selected = tag.contains(value, kv.getKey());
				writeOption(kv.getKey(), kv.getValue(), selected);
			}
		}
		etag("optgroup");
	}
}
