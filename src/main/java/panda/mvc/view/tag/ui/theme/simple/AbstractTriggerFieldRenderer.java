package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public abstract class AbstractTriggerFieldRenderer<T extends TriggerField> extends AbstractEndRenderer<T> {
	public AbstractTriggerFieldRenderer(RenderingContext context) {
		super(context);
	}

	protected String getName() {
		return "triggerfield";
	}

	@Override
	protected void render() throws IOException {
		renderHeader();
		
		renderLeftTrigger();

		renderInput();
		
		renderRightTrigger();
		
		renderFooter();
	}

	protected void renderHeader() throws IOException {
		write("<div class=\"input-group p-" + getName() + "\">");
	}
	
	protected void renderFooter() throws IOException {
		write("</div>");
	}
	
	protected void renderInput() throws IOException {
		Attributes attr = new Attributes();
		attr.type("text")
			.id(tag)
			.name(tag)
			.css(this)
			.size(tag)
			.maxlength(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.formatValue(this, tag)
			.title(tag)
			.placeholder(tag)
			.mask(tag)
			.datas(getDatas())
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		xtag("input", attr);
	}

	protected Map<String, String> getDatas() {
		return null;
	}
	
	protected void renderLeftTrigger() throws IOException {
		if (tag.hasLeftTrigger()) {
			Attributes attr = new Attributes();
			attr.cssClass("input-group-addon p-" + getName() + "-licon")
				.onclick(tag.getOnlclick());
			stag("div", attr);
			write(tag.getLtext());
			if (Strings.isNotEmpty(tag.getLicon())) {
				write(xicon(tag.getLicon()));
			}
			etag("div");
		}
	}
	
	protected void renderRightTrigger() throws IOException {
		if (tag.hasRightTrigger()) {
			Attributes attr = new Attributes();
			attr.cssClass("input-group-addon p-" + getName() + "-ricon")
				.onclick(tag.getOnrclick());
			stag("div", attr);
			write(tag.getRtext());
			if (Strings.isNotEmpty(tag.getRicon())) {
				write(xicon(tag.getRicon()));
			}
			etag("div");
		}
	}
}
