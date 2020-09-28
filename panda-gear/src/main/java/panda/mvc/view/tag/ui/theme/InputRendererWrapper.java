package panda.mvc.view.tag.ui.theme;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.alert.ParamAlert;
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

			write(html(tag.getLabel()));

			if (tag.isReadonly() && "right".equals(tag.getRequiredPosition())) {
				writeRequired();
			}
			writeSeparator();
		}
		etag("label");
	}

	protected void writeFieldErrors() throws IOException {
		ParamAlert pa = context.getParamAlert();
		if (!pa.hasErrors()) {
			return;
		}

		for (String fn : getGroupTags()) {
			if (pa.hasErrors(fn)) {
				boolean ul = false;
				Attributes as = new Attributes();
				for (String fe : pa.getErrors(fn)) {
					if (Strings.isEmpty(fe)) {
						continue;
					}
					
					if (!ul) {
						as.clear().add("errorFor", fn).cssClass("fa-ul p-field-errors");
						stag("ul", as);
						ul = true;
					}
					write("<li class=\"text-danger\">");
					write("<i class=\"fa-li fa fa-exclamation-circle\"></i>");
					write("<span>");
					write(html(fe));
					write("</span>");
					write("</li>");
				}
				if (ul) {
					etag("ul");
				}
			}
		}
	}
}
