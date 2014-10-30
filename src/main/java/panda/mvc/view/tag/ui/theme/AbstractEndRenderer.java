package panda.mvc.view.tag.ui.theme;

import panda.mvc.view.tag.ui.UIBean;


public abstract class AbstractEndRenderer<T extends UIBean> extends AbstractTagRenderer<T> {
	public AbstractEndRenderer(RenderingContext rctx) {
		super(rctx);
	}

	@Override
	public void renderStart() throws Exception {
	}

	@Override
	public void renderEnd() throws Exception {
		render();
	}

	protected abstract void render() throws Exception;
}
