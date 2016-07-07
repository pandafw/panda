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
		
		Collection<String> fieldErrorFieldNames = tag.getFieldNames();

		if (Collections.isEmpty(fieldErrorFieldNames)) {
			fieldErrorFieldNames = errors.keySet();
		}

		if (Collections.isEmpty(fieldErrorFieldNames)) {
			return;
		}

		boolean escape = tag.isEscape();
		
		Attributes ula = new Attributes();

		// iterate over field error names
		for (String fieldErrorFieldName : fieldErrorFieldNames) {
			List<String> fieldErrors = errors.get(fieldErrorFieldName);
			if (fieldErrors == null) {
				continue;
			}
			
			boolean ul = false;

			boolean hidden = false;
			String label = null;
			if (tag.isShowLabel()) {
				String key = "p." + fieldErrorFieldName;
				label = getText(key, null);
				if (Strings.isEmpty(label)) {
					label = key;
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
							.add("errorFor", fieldErrorFieldName)
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
					write(label);
					if (escape) {
						write(phtml(fieldError));
					}
					else {
						write(fieldError);
					}
					etag("li");
				}
			}
			if (ul) {
				etag("ul");
			}
		}
	}
}
