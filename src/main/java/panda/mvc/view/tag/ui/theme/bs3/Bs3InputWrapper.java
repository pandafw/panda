package panda.mvc.view.tag.ui.theme.bs3;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.InputUIBean;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public abstract class Bs3InputWrapper<T extends InputUIBean> extends RendererWrapper<T> {
	/**
	 * @param rc context
	 */
	public Bs3InputWrapper(RenderingContext rc) {
		super(rc);
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

		boolean required = tag.isRequired();
		String requiredposition = tag.getRequiredPosition();

		if (required && "side".equals(requiredposition)) {
			writeRequired();
		}

		writeAfter();

		if (!isInGroup()) {
			renderFooter();
		}
		else {
			write(Strings.SPACE);
		}
	}

	protected void writeRequired() throws IOException {
		write("<span class=\"p-required\">");
		write(defs(tag.getRequiredString(), "*"));
		write("</span>");
	}

	protected void renderHeader() throws Exception {
		String name = tag.getName();

		Map<String, List<String>> fieldErrors = context.getParamAware().getErrors();
		boolean required = tag.isRequired();
		String requiredposition = tag.getRequiredPosition();

		boolean hasFieldErrors = (name != null
			&& Collections.isNotEmpty(fieldErrors)
			&& fieldErrors.get(name) != null);

		Attributes attr = new Attributes();
		attr.cssClass(join(tag.getCssClass(), "form-group", (hasFieldErrors ? "has-error" : null)));
		stag("div", attr);

		attr.clear().forId(tag).cssClass(getLabelClass());
		stag("label", attr);

		String label = tag.getLabel();
		if (Strings.isNotEmpty(label)) {
			if (required && "left".equals(defs(requiredposition, "left"))) {
				writeRequired();
			}

			write(html(label));

			if (required && "right".equals(requiredposition)) {
				writeRequired();
			}
			write(defs(tag.getLabelSeparator(), ":"));
		}
		etag("label");

		renderInputDivBegin();
	}

	protected String getLabelClass() {
		return null;
	}
	
	protected void renderInputDivBegin() throws IOException {
	}

	protected void renderInputDivEnd() throws IOException {
	}

	protected void renderFooter() throws Exception {
		Map<String, List<String>> fieldErrors = context.getParamAware().getErrors();
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
							write("<li class=\"text-danger\"><i class=\"fa-li fa fa-exclamation-circle\"></i>");
							write(phtml(fe));
							write("</li>");
						}
					}
					if (ul) {
						etag("ul");
					}
				}
			}
		}
		
		renderInputDivEnd();
		etag("div");
	}

	//-----------------------------------------------------------------
	public static class GroupWrapper extends Bs3InputWrapper<Div> {
		/**
		 * @param rc context
		 */
		public GroupWrapper(RenderingContext rc) {
			super(rc);
		}

		@Override
		public void renderStart() throws Exception {
			renderHeader();
			writeBefore();
			setInGroup(true);
		}

		@Override
		public void renderEnd() throws Exception {
			writeAfter();
			renderFooter();
			setInGroup(false);
		}
	}

	public static class ControlWrapper extends Bs3InputWrapper {
		/**
		 * @param context context
		 */
		public ControlWrapper(RenderingContext context) {
			super(context);
		}

		protected void renderHeader() throws Exception {
			super.renderHeader();
			addCss("form-control");
		}
	}

	public static class StaticWrapper extends Bs3InputWrapper {
		/**
		 * @param context context
		 */
		public StaticWrapper(RenderingContext context) {
			super(context);
		}

		protected void renderHeader() throws Exception {
			super.renderHeader();
			addCss("form-control-static");
		}
	}

	public static class NormalWrapper extends Bs3InputWrapper {
		/**
		 * @param context context
		 */
		public NormalWrapper(RenderingContext context) {
			super(context);
		}
	}
}
