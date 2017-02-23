package panda.mvc.view.tag.ui.theme.xhtml;

import java.util.List;
import java.util.Map;

import panda.lang.Collections;
import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.InputUIBean;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.InputRendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;


public abstract class XInputWrapper<T extends InputUIBean> extends InputRendererWrapper<T> {
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
		
		writeDescrip();
		writeAfter();

		if (!isInGroup()) {
			renderFooter();
		}
	}

	protected void renderHeader() throws Exception {
		String name = tag.getName();

		Map<String, List<String>> fieldErrors = context.getParamAlert().getErrors();
		boolean hasFieldErrors = (name != null
			&& Collections.isNotEmpty(fieldErrors)
			&& fieldErrors.get(name) != null);

		Attributes attr = new Attributes();
		if (hasFieldErrors) {
			attr.cssClass("has-error");
		}
		stag("tr", attr);

		if ("none".equals(tag.getLabelPosition())) {
			attr.clear().add("colspan", 2);
			stag("td", attr);
		}
		else {
			stag("th");
			writeInputLabel();
			etag("th");
			
			stag("td");
		}
	}
	
	protected void renderFooter() throws Exception {
		if (tag.isRequired() && "side".equals(tag.getRequiredPosition())) {
			writeRequired();
		}
		
		etag("td");

		etag("tr");
	}

	//-----------------------------------------------------------------
	public static class GroupWrapper extends XInputWrapper<Div> {
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

	public static class ControlWrapper extends XInputWrapper {
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

	public static class StaticWrapper extends XInputWrapper {
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

	public static class NormalWrapper extends XInputWrapper {
		/**
		 * @param context context
		 */
		public NormalWrapper(RenderingContext context) {
			super(context);
		}
	}
}
