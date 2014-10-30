package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.FormButton;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class SubmitRenderer<T extends FormButton> extends AbstractTagRenderer<T> {

	protected String button = "submit";
	
	public SubmitRenderer(RenderingContext context) {
		super(context);
	}

	public void renderStart() throws IOException {
		Attributes attrs = new Attributes();

		String type = defs(tag.getType(), "button");
		String icon = tag.getIcon();
		String sicon = tag.getSicon();
		String btype = defs(tag.getBtype(), "default");
		
		boolean isButton = Strings.isNotEmpty(icon) || Strings.isNotEmpty(sicon) || "button".equals(type);

		if (isButton) {
			attrs.add("type", button)
				.id(tag)
				.name(tag)
				.cssClass(tag, "btn btn-" + btype)
				.value(tag)
				.disabled(tag)
				.tabindex(tag)
				.title(tag)
				.cssStyle(tag)
				.commons(tag)
				.events(tag)
				.dynamics(tag);
			stag("button", attrs);

			if (Strings.isNotEmpty(icon)) {
				write(xicon(icon));
				write(Strings.SPACE);
			}
		}
		else if ("image".equals(type)) {
			attrs.add("type", "image")
				.src(tag)
				.addIfExists("alt", tag.getLabel());
			xtag("input", attrs);
		}
		else {
			attrs.add("type", button)
				.id(tag)
				.name(tag)
				.cssClass(tag, "btn btn-" + btype)
				.disabled(tag)
				.tabindex(tag)
				.value(tag)
				.cssStyle(tag)
				.title(tag)
				.commons(tag)
				.events(tag)
				.dynamics(tag);

			xtag("input", attrs);
		}
	}

	public void renderEnd() throws IOException {
		String type = defs(tag.getType(), "button");
		String icon = tag.getIcon();
		String sicon = tag.getSicon();
		
		boolean isButton = Strings.isNotEmpty(icon) || Strings.isNotEmpty(sicon) || "button".equals(type);

		if (isButton) {
			//button body
			if (Strings.isNotEmpty(tag.getBody())) {
				body(tag.getBody(), false);
			}
			else if (Strings.isNotEmpty(tag.getLabel())) {
				body(tag.getLabel(), false);
			}

			if (Strings.isNotEmpty(sicon)) {
				write(Strings.SPACE);
				write(xicon(sicon));
			}
			etag("button");
		}
		else {
			if (Strings.isNotEmpty(tag.getBody())) {
				body(tag.getBody(), false);
			}
		}
	}
}

