package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.Token;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.net.http.HttpMethod;


public class FormRenderer extends AbstractTagRenderer<Form> {
	/**
	 * Token Protect Methods
	 */
	public static final Set<String> PROTECT_METHODS = Arrays.toSet(HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT);

	public FormRenderer(RenderingContext context) {
		super(context);
	}

	public void renderStart() throws IOException {
		Attributes attrs = new Attributes();

		String method = defs(tag.getMethod(), "post");
		
		attrs.id(tag)
			.name(tag)
			.cssClass(tag, "p-form")
			.action(tag)
			.target(tag)
			.add("method", method)
			.enctype(tag)
			.acceptcharset(tag)
			.tooltip(tag)
			.onsubmit(tag)
			.onreset(tag)
			.addIfExists("hooked", tag.getHooked())
			.addIfExists("loadmask", tag.getLoadmask())
			.addIfExists("focusme", tag.getFocusme())
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("form", attrs);

		Boolean token = tag.getToken();
		if (token == null) {
			token = isTokenProtectMethod(method);
		}
		if (token) {
			Token t = newTag(Token.class);
			t.start(writer);
			t.end(writer, "");
		}
	}

	public void renderEnd() throws IOException {
		etag("form");
	}
	
	protected boolean isTokenProtectMethod(String method) {
		method = Strings.upperCase(method);
		return PROTECT_METHODS.contains(method);
	}
}
