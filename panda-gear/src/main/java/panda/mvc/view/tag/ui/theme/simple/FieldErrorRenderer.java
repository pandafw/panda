package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.FieldError;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.mvc.view.util.Escapes;

public class FieldErrorRenderer extends AbstractEndRenderer<FieldError> {
	public static final String UL_CLASS = "fa-ul p-field-errors";
	public static final String LI_CLASS = "text-danger";
	public static final String ICON_CLASS = "fa-li fa fa-exclamation-circle";
	
	public FieldErrorRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Map<String, List<String>> errors = context.getParamAlert().getErrors();
		if (Collections.isEmpty(errors)) {
			return;
		}
		
		Collection<String> fns = tag.getFieldNames();
		if (Collections.isEmpty(fns)) {
			fns = errors.keySet();
		}
		if (Collections.isEmpty(fns)) {
			return;
		}

		Attributes ula = new Attributes();

		// iterate over field error names
		for (String fn : fns) {
			List<String> fieldErrors = errors.get(fn);
			if (fieldErrors == null) {
				continue;
			}
			
			boolean ul = false;

			boolean hidden = false;
			String label = null;
			if (tag.isShowLabel()) {
				label = getText("p." + fn, null);
				if (Strings.isEmpty(label)) {
					label = fn;
					if (tag.isHideEmptyLabel()) {
						hidden = true;
					}
				}
				label += tag.getLabelSeparator() + ' ';
			}
			
			for (String fieldError : fieldErrors) {
				if (Strings.isNotEmpty(fieldError)) {
					if (!ul) {
						// wrapping ul
						ula.clear()
							.add("errorFor", fn)
							.cssClass(tag, UL_CLASS)
							.cssStyle(tag);
						stag("ul", ula);
						ul = true;
					}
					
					write("<li class=\"");
					write(LI_CLASS);
					if (hidden) {
						write(" p-hidden");
					}
					write("\">");
					write(icon(ICON_CLASS));
					write("<span>");
					write(label);
					write(Escapes.escape(fieldError, tag.getEscape()));
					write("</span>");
					etag("li");
				}
			}
			if (ul) {
				etag("ul");
			}
		}
	}
}
