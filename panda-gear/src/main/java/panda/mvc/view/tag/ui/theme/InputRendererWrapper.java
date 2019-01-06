package panda.mvc.view.tag.ui.theme;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.InputUIBean;

public abstract class InputRendererWrapper<T extends InputUIBean> extends RendererWrapper<T> {
	/**
	 * @param rc context
	 */
	public InputRendererWrapper(RenderingContext rc) {
		super(rc);
	}

	protected void writeRequired() throws IOException {
		write("<span class=\"p-required\">");
		write(defs(tag.getRequiredString(), "*"));
		write("</span>");
	}

	protected void writeLeftRequired() throws IOException {
		if (tag.isRequired() && "left".equals(defs(tag.getRequiredPosition(), "left"))) {
			writeRequired();
		}
	}

	protected void writeSideRequired() throws IOException {
		if (tag.isRequired() && "side".equals(tag.getRequiredPosition())) {
			writeRequired();
		}
	}

	protected void writeSeparator() throws IOException {
		write(tag.getLabelSeparator());
	}

	protected void writeDescrip() throws IOException {
		if (Strings.isNotEmpty(tag.getDescription())) {
			Form form = tag.findForm();
			if (form != null && !Boolean.FALSE.equals(form.getHideDescrip())) {
				write("<span class=\"p-descrip\">");
				write(tag.getDescription());
				write("</span>");
			}
		}
	}

	protected String getLabelClass() {
		return null;
	}
	
	protected void writeInputLabel() throws IOException {
		Attributes attr = new Attributes();
		attr.forId(tag.getId()).cssClass(getLabelClass());
		stag("label", attr);

		if (Strings.isNotEmpty(tag.getLabel())) {
			writeLeftRequired();

			write(phtml(tag.getLabel()));

			if (tag.isReadonly() && "right".equals(tag.getRequiredPosition())) {
				writeRequired();
			}
			writeSeparator();
		}
		etag("label");
	}

	protected void writeFieldErrors() throws IOException {
		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		if (Collections.isNotEmpty(fieldErrors)) {
			for (String fn : getGroupTags()) {
				List<String> fes = fieldErrors.get(fn);
				if (Collections.isNotEmpty(fes)) {
					boolean ul = false;
					Attributes as = new Attributes();
					for (String fe : fes) {
						if (Strings.isNotEmpty(fe)) {
							if (!ul) {
								as.clear().add("errorFor", fn).cssClass("fa-ul p-field-errors");
								stag("ul", as);
								ul = true;
							}
							write("<li class=\"text-danger\">");
							write("<i class=\"fa-li fa fa-exclamation-circle\"></i>");
							write("<span>");
							write(phtml(fe));
							write("</span>");
							write("</li>");
						}
					}
					if (ul) {
						etag("ul");
					}
				}
			}
		}
	}
}
