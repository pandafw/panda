package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;

@IocBean
public class VoidView implements View {
	@Override
	public void setArgument(String arg) {
	}

	@Override
	public void render(ActionContext ac) {
	}
}
