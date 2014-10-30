package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Collection;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.EscapeUIBean;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

/**
 * Base class for ActionError and ActionMessage
 */
public abstract class AbstractMessageListRenderer<T extends EscapeUIBean> extends AbstractEndRenderer<T> {
	
	public AbstractMessageListRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Collection<String> msgs = getMessages();

		if (msgs != null) {
			boolean ul = false;
			Attributes lia = new Attributes();

			for (String msg : msgs) {
				if (Strings.isNotEmpty(msg)) {
					if (!ul) {
						Attributes ula = new Attributes();
						ula.id(tag)
							.cssStyle(tag)
							.cssClass(tag, getDefaultULClass());
						stag("ul", ula);
						ul = true;
					}

					// li for each error
					lia.cssClass(getDefaultLIClass());
					stag("li", lia);

					// img for error
					write(icon(getDefaultIconClass()));

					if (tag.isEscape()) {
						write(phtml(msg));
					}
					else {
						write(msg);
					}

					etag("li");
				}
			}
			if (ul) {
				etag("ul");
			}
		}
	}

	/*
	 * messages
	 */
	protected abstract Collection<String> getMessages();

	/*
	 * default class for UL element
	 */
	protected abstract String getDefaultULClass();

	/*
	 * default class for LI element
	 */
	protected abstract String getDefaultLIClass();

	/*
	 * default class for Icon element
	 */
	protected abstract String getDefaultIconClass();
}
