package panda.mvc.view.tag.ui.theme.xhtml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.InputUIBean;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;

/**
 */
public class XInputWrapper<T extends InputUIBean> extends RendererWrapper<T> {
	/**
	 * @param context context
	 */
	public XInputWrapper(RenderingContext context) {
		super(context);
	}

	@Override
	public void renderStart() throws Exception {
		if (!isInGroup()) {
			renderHeader();
		}
		writeBefore();

		super.renderStart();
	}
	
	@Override
	public void renderEnd() throws Exception {
		super.renderEnd();
		
		writeAfter();

		if (!isInGroup()) {
			renderFooter();
		}
	}
	
	protected void writeRequired() throws IOException {
		write("<span class=\"p-required\">");
		write(defs(tag.getRequiredString(), "*"));
		write("</span>");
	}

	protected void renderHeader() throws Exception {
		String name = tag.getName();

		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();

		boolean hasFieldErrors = (name != null
			&& Collections.isNotEmpty(fieldErrors)
			&& fieldErrors.get(name) != null);

		boolean required = tag.isRequired();
		String requiredposition = tag.getRequiredPosition();

		write("<tr class=\"p-tr-input\"><td class=\"p-td-label\">");

		String label = tag.getLabel();
		if (Strings.isNotEmpty(label)) {
			Attributes al = new Attributes();
			al.addIfExists("for", tag.getId())
			  .add("class", hasFieldErrors ? "p-label-error" : "p-label");
			stag("label", al);
			
			if (required && "left".equals(defs(requiredposition, "left"))) {
				writeRequired();
			}

			write(html(label));

			if (required && "right".equals(requiredposition)) {
				write("<span class=\"p-required\">*</span>");
			}
			write(defs(tag.getLabelSeparator(), ":"));
			etag("label");
		}
		etag("td");
		
		Attributes a = new Attributes();
		a.add("class", "p-td-input");
		if (Strings.isEmpty(tag.getTooltip())) {
			a.add("colspan", "2");
		}
		stag("td", a);
	}
	
	protected void renderFooter() throws Exception {
		boolean required = tag.isRequired();
		String requiredposition = tag.getRequiredString();
		
		if (required && "side".equals(requiredposition)) {
			writeRequired();
		}
		
		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		if (Collections.isNotEmpty(fieldErrors)) {
			for (String fn : getGroupTags()) {
				List<String> fes = fieldErrors.get(fn);
				if (Strings.isNotEmpty(fn) && Collections.isNotEmpty(fes)) {
					boolean ul = false;
					for (String fe : fes) {
						if (Strings.isNotEmpty(fe)) {
							if (!ul) {
								Attributes aul = new Attributes();
								aul.add("errorFor", fn)
									.add("class", "p-field-errors");
								stag("ul", aul);
								ul = true;
							}
							write("<li class=\"p-field-error\">");
							write(icon("p-icon p-icon-error p-field-error"));
							write(phtml(fe));
							write("</li>");
						}
					}
					if (ul) {
						etag("ul");
					}
				}
			}
			etag("td");
		}

		String tooltip = tag.getTooltip();
		if (Strings.isNotEmpty(tooltip)) {
			write("<td class=\"p-td-tooltip\">");
			write("<div class=\"p-tooltip\">");
			write(Strings.isEmpty(tooltip) ? "&nbsp;" : tooltip);
			write("</div>");
			etag("td");
		}

		etag("tr");
	}
}
