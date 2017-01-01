package panda.mvc.view;

import panda.mvc.ActionContext;
import panda.mvc.View;

public class VoidView implements View {
	public static final VoidView INSTANCE = new VoidView();
	
	private VoidView() {
	}

	@Override
	public void render(ActionContext ac) {
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
