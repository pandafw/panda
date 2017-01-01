package panda.mvc.view.tag.ui.theme;

import java.io.Writer;

import panda.mvc.ActionContext;
import panda.mvc.view.tag.ui.UIBean;

/**
 * Context used when rendering templates.
 */
public class RenderingContext {
	String theme;
	ActionContext context;
	UIBean tag;
	Writer writer;

	/**
	 * Constructor
	 * 
	 * @param theme the theme.
	 * @param writer the writer.
	 * @param context action context.
	 * @param tag the tag UI component.
	 */
	public RenderingContext(String theme, Writer writer, ActionContext context, UIBean tag) {
		this.theme = theme;
		this.writer = writer;
		this.context = context;
		this.tag = tag;
	}

	public String getTheme() {
		return theme;
	}

	public ActionContext getActionContext() {
		return context;
	}

	public UIBean getTag() {
		return tag;
	}

	public Writer getWriter() {
		return writer;
	}
}
