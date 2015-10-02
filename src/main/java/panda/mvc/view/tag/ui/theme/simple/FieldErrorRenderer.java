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
		Attributes lia = new Attributes();
		lia.cssClass("text-danger");

		// iterate over field error names
		for (String fieldErrorFieldName : fieldErrorFieldNames) {
			List<String> fieldErrors = errors.get(fieldErrorFieldName);
			if (fieldErrors == null) {
				continue;
			}
			
			boolean ul = false;

			String label = null;
			if (tag.isLabel()) {
				label = getText("p." + fieldErrorFieldName)
					+ tag.getLabelSeparator()
					+ ' ';
			}
			
			for (String fieldError : fieldErrors) {
				if (Strings.isNotEmpty(fieldError)) {
					if (!ul) {
						// wrapping ul
						ula.clear()
							.add("errorFor", fieldErrorFieldName)
							.cssClass(tag, "fa-ul p-field-errors")
							.cssStyle(tag);
						stag("ul", ula);
						ul = true;
					}
					
					stag("li", lia);
					write("<i class=\"fa-li fa fa-exclamation-circle\"></i>");
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
