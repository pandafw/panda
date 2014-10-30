package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class FormRenderer extends AbstractTagRenderer {
	private Form form;
	
	public FormRenderer(RenderingContext context) {
		super(context);
		form = (Form)tag;
	}

	public void renderStart() throws IOException {
		Attributes attrs = new Attributes();

		attrs.id(tag)
			.name(tag)
			.cssClass(tag, "p-form")
			.action(form)
			.target(form)
			.add("method", defs(form.getMethod(), "post"))
			.enctype(form)
			.acceptcharset(form)
			.title(tag)
			.onsubmit(form)
			.onreset(form)
			.addIfExists("hooked", form.getHooked())
			.addIfExists("loadmask", form.getLoadmask())
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("form", attrs);
	}

	public void renderEnd() throws IOException {
		etag("form");
	}
}
