package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;

@IocBean
public class VoidView implements View {
	public static final VoidView INSTANCE = new VoidView();

	@Override
	public void setDescription(String desc) {
	}

	@Override
	public void render(ActionContext ac) {
	}
}
