package panda.mvc.view.tag.ui.theme;

import panda.lang.Classes;


public class RendererWrapperFactory {
	private Class<? extends RendererWrapper> wrapRendererClass;
	private Class<? extends TagRenderer> simpleRendererClass;

	public RendererWrapperFactory(
			Class<? extends RendererWrapper> wrapRendererClass,
			Class<? extends TagRenderer> simpleRendererClass) {
		super();
		this.simpleRendererClass = simpleRendererClass;
		this.wrapRendererClass = wrapRendererClass;
	}

	public TagRenderer create(RenderingContext context) {
		RendererWrapper wrapRenderer = (RendererWrapper)Classes.born(wrapRendererClass, context, RenderingContext.class);
		TagRenderer simpleRenderer = (TagRenderer)Classes.born(simpleRendererClass, context, RenderingContext.class);
		wrapRenderer.setSimpleRenderer(simpleRenderer);
		return wrapRenderer;
	}
}
