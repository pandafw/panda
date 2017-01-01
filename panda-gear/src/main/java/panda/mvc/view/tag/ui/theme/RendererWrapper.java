package panda.mvc.view.tag.ui.theme;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.UIBean;

public abstract class RendererWrapper<T extends UIBean> extends AbstractTagRenderer<T> {
	private static final String GROUP_KEY = "form_group";
	private static final String GROUP_TAG = "form_group_tags";
	
	private TagRenderer simpleRenderer;
	
	/**
	 * @param context context
	 */
	public RendererWrapper(RenderingContext context) {
		super(context);
	}

	/**
	 * @return the simpleRenderer
	 */
	public TagRenderer getSimpleRenderer() {
		return simpleRenderer;
	}

	/**
	 * @param simpleRenderer the simpleRenderer to set
	 */
	public void setSimpleRenderer(TagRenderer simpleRenderer) {
		this.simpleRenderer = simpleRenderer;
	}

	protected void writeBefore() throws IOException {
		String before = (String)tag.getParameter("before");
		write(before);
	}

	protected void writeAfter() throws IOException {
		String after = (String)tag.getParameter("after");
		write(after);
	}
	
	protected boolean isInGroup() {
		Object in = findInContext(GROUP_KEY);
		return Boolean.TRUE.equals(in);
	}
	
	protected void setInGroup(boolean in) {
		putInContext(GROUP_KEY, in);
		putInContext(GROUP_TAG, "");
	}
	
	protected void addToGroup() {
		String ts = (String)findInContext(GROUP_TAG);
		if (ts != null && Strings.isNotEmpty(tag.getName())) {
			ts = ' ' + tag.getName();
			putInContext(GROUP_TAG, ts);
		}
	}
	
	protected String[] getGroupTags() {
		String ts = (String)findInContext(GROUP_TAG);
		if (Strings.isNotEmpty(ts)) {
			return Strings.split(ts);
		}
		
		if (Strings.isNotEmpty(tag.getName())) {
			return new String[] { tag.getName() };
		}
		
		return Strings.EMPTY_ARRAY;
	}
	
	@Override
	public void renderStart() throws Exception {
		simpleRenderer.renderStart();
	}

	@Override
	public void renderEnd() throws Exception {
		simpleRenderer.renderEnd();
	}
}
