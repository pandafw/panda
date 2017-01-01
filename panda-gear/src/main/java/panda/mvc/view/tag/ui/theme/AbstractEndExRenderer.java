package panda.mvc.view.tag.ui.theme;

import panda.mvc.view.tag.ui.UIBean;


public abstract class AbstractEndExRenderer<T extends UIBean> extends AbstractTagExRenderer<T> {
	public AbstractEndExRenderer(RenderingContext rctx) {
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
