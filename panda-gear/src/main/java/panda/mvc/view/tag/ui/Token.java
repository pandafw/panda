package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.processor.TokenProcessor;


@IocBean(singleton=false)
public class Token extends InputUIBean {
	@IocInject
	private TokenProcessor tokener;

	private String token;

	@Override
	protected void evaluateParams() {
		if (name == null) {
			name = tokener.getParameterName();
		}
		token = tokener.getTokenString(context);

		super.evaluateParams();
	}

	public String getToken() {
		return token;
	}
}
