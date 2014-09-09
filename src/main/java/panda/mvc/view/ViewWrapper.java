package panda.mvc.view;

import panda.mvc.ActionContext;
import panda.mvc.View;

/**
 * 组合一个视图以及其渲染对象
 */
public class ViewWrapper implements View {

	public ViewWrapper(View view, Object data) {
		this.view = view;
		this.data = data;
	}

	private View view;

	private Object data;

	public void render(ActionContext ac, Object obj) throws Throwable {
		view.render(ac, data);
	}

	public Object getData() {
		return data;
	}
}
