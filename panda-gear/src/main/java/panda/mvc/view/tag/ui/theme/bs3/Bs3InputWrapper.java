package panda.mvc.view.tag.ui.theme.bs3;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.InputUIBean;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.InputRendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public abstract class Bs3InputWrapper<T extends InputUIBean> extends InputRendererWrapper<T> {
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

		writeSideRequired();
		writeFieldErrors();
		writeDescrip();
		writeAfter();

		if (!isInGroup()) {
			renderFooter();
		}
		else {
			write(Strings.SPACE);
		}
	}

	protected void renderHeader() throws Exception {
		String name = tag.getName();

		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		boolean hasFieldErrors = (name != null
			&& Collections.isNotEmpty(fieldErrors)
			&& fieldErrors.get(name) != null);

		Attributes attr = new Attributes();
		attr.cssClass(join(tag.getCssClass(), "form-group", (hasFieldErrors ? "has-error" : null)));
		stag("div", attr);

		writeInputLabel();

		renderInputDivBegin();
	}

	protected void renderInputDivBegin() throws IOException {
	}

	protected void renderInputDivEnd() throws IOException {
	}

	protected void renderFooter() throws Exception {
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
