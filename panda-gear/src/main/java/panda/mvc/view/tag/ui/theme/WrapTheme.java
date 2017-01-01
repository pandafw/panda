package panda.mvc.view.tag.ui.theme;

import java.util.HashMap;
import java.util.Map;

import panda.mvc.view.tag.ui.UIBean;

public class WrapTheme extends Theme {
	private Theme defaultTheme;
	private Map<Class<? extends UIBean>, RendererWrapperFactory> wrappers;

	protected void addWrapper(Class<? extends UIBean> key, RendererWrapperFactory twf) {
		wrappers.put(key, twf);
	}
	
	public WrapTheme(Theme defaultTheme) {
		this.defaultTheme = defaultTheme;
		this.wrappers = new HashMap<Class<? extends UIBean>, RendererWrapperFactory>();
	}

	@Override
	public TagRenderer createTagRenderer(RenderingContext rctx) {
		RendererWrapperFactory twf = wrappers.get(rctx.getTag().getClass());
		if (twf != null) {
			return twf.create(rctx);
		}
		return defaultTheme.createTagRenderer(rctx);
	}
}
