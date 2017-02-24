package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Iterators;
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

		String cssc = "p-checkboxlist";
		if (tag.isListBreak()) {
			cssc += " break";
		}
		if (tag.isListOrder()) {
			cssc += " order";
		}
		Attributes attrs = new Attributes();
		attrs.id(id)
			.tooltip(tag)
			.cssClass(tag, cssc)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("div", attrs);
		
		attrs.clear();
		attrs.add("type", "checkbox")
			.name(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag);

		Object value = tag.getValue();
		if (tag.isNotEmptyList()) {
			if (tag.isListOrder()) {
				if (tag.getList() instanceof Map) {
					writeOrderMapItems(id, attrs, value);
				}
				else {
					writeOrderListItems(id, attrs, value);
				}
			}
			else {
				writeItems(id, attrs, value);
			}
		}
		else {
			write(" &nbsp;");
		}

		etag("div");
	}
	
	private void writeOrderMapItems(String id, Attributes attrs, Object value) throws IOException {
		Map m = (Map)tag.getList();

		int itemCount = 0;
		if (value != null) {
			for (Object v : Iterators.asIterable(value)) {
				Object label = m.get(v);
				if (label == null && !(v instanceof String)) {
					label = m.get(v.toString());
				}
	
				if (label != null) {
					itemCount++;
					writeItem(id, itemCount, attrs, v.toString(), label.toString(), true);
				}
			}
		}
		write("<hr>");
		writeUncheckedItems(id, attrs, value, itemCount);
	}

	private void writeOrderListItems(String id, Attributes attrs, Object value) throws IOException {
		int itemCount = 0;
		if (value != null) {
			for (Object v : Iterators.asIterable(value)) {
				if (v == null) {
					continue;
				}
				if (tag.contains(tag.getList(), v)) {
					itemCount++;
					writeItem(id, itemCount, attrs, v.toString(), v.toString(), true);
				}
			}
		}
		write("<hr>");
		writeUncheckedItems(id, attrs, value, itemCount);
	}

	private void writeUncheckedItems(String id, Attributes attrs, Object value, int itemCount) throws IOException {
		for (KeyValue kv : tag.asIterable()) {
			boolean checked = tag.contains(value, kv.getKey());
			if (!checked) {
				itemCount++;
				writeItem(id, itemCount, attrs, kv.getKey().toString(), kv.getValue().toString(), checked);
			}
		}
	}

	private void writeItems(String id, Attributes attrs, Object value) throws IOException {
		int itemCount = 0;
		for (KeyValue kv : tag.asIterable()) {
			itemCount++;

			boolean checked = tag.contains(value, kv.getKey());
			writeItem(id, itemCount, attrs, kv.getKey().toString(), kv.getValue().toString(), checked);
		}
	}
	
	private void writeItem(String id, int itemCount, Attributes attrs, String key, String value, boolean checked) throws IOException {
		write("<label class=\"checkbox-inline\">");
		attrs.add("id", id + "_" + itemCount)
			.addIfExists("value", key)
			.addIfTrue("checked", checked);
		xtag("input", attrs);
		
		body(value);
		write("</label>");
	}

}
