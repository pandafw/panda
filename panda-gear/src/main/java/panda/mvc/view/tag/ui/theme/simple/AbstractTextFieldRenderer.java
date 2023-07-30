package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Map;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public abstract class AbstractTextFieldRenderer<T extends TextField> extends AbstractEndRenderer<T> {
	public AbstractTextFieldRenderer(RenderingContext context) {
		super(context);
	}

	protected abstract String getName();

	@Override
	protected void render() throws IOException {
		if (tag.hasAddon()) {
			renderHeader();
			
			renderLeftAddon();
	
			renderInput();
			
			renderRightAddon();
			
			renderFooter();
		}
		else {
			renderInput();
		}
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
			.css(this, "p-" + getName())
			.size(tag)
			.maxlength(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.formatValue(this, tag)
			.tooltip(tag)
			.placeholder(tag)
			.mask(tag)
			.commons(tag)
			.events(tag)
			.datas(getDatas())
			.dynamics(tag);
		if (tag.isClearholder()) {
			attr.add("textclear");
//			attr.data("ptrigger", "true");
		}
		xtag("input", attr);
	}

	protected Map<String, String> getDatas() {
		return null;
	}
	
	protected void renderLeftAddon() throws IOException {
		if (tag.hasLeftAddon()) {
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
	
	protected void renderRightAddon() throws IOException {
		if (tag.hasRightAddon()) {
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
