package panda.app;

import java.util.ArrayList;

import panda.app.auth.UserAuthenticateProcessor;
import panda.ioc.annotation.IocBean;
import panda.ioc.meta.IocValue;
import panda.mvc.ActionChainCreator;
import panda.mvc.impl.DefaultActionChainCreator;
import panda.mvc.processor.AdaptProcessor;

@IocBean(type=ActionChainCreator.class, create="initialize")
public class AppActionChainCreator extends DefaultActionChainCreator {
	@Override
	protected ArrayList<String> defaultChain() {
		ArrayList<String> chain = super.defaultChain();

		int i = chain.indexOf(IocValue.TYPE_REF + AdaptProcessor.class.getName());
		if (i >= 0) {
			chain.add(i, IocValue.TYPE_REF + UserAuthenticateProcessor.class.getName());
		}
		
		return chain;
	}
	
	@Override
	public void initialize() {
		super.initialize();
	}

}
