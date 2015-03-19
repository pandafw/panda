package panda.mvc.view.tag.ui.theme;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcException;

public abstract class Theme {
	protected static final Log log = Logs.getLog(Theme.class);

	protected String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract TagRenderer createTagRenderer(RenderingContext rctx);

	public void renderStart(RenderingContext rctx) {
		TagRenderer renderer = createTagRenderer(rctx);

		try {
			if (log.isTraceEnabled()) {
				log.trace("Start rendering [" + rctx.getTag().getClass() + "]");
			}
			renderer.renderStart();
		}
		catch (Exception ex) {
			throw new MvcException("Unable to render start: " + rctx.getTag().getClass(), ex);
		}
	}

	public void renderEnd(RenderingContext rctx) {
		TagRenderer renderer = createTagRenderer(rctx);

		try {
			if (log.isTraceEnabled()) {
				log.trace("End rendering [" + rctx.getTag().getClass() + "]");
			}
			renderer.renderEnd();
		}
		catch (Exception ex) {
			throw new MvcException("Unable to render end: " + rctx.getTag().getClass(), ex);
		}
	}
}
