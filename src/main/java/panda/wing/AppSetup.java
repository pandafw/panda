package panda.wing;

import panda.bean.Beans;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.Setup;


@IocBean(type=Setup.class)
public class AppSetup implements Setup {
	@IocInject(required=false)
	protected Beans beans = Beans.i();
	
	/**
	 * initialize
	 */
	public void initialize() {
	}

	/**
	 * destroy
	 */
	public void destroy() {
		beans.clear();
	}
}
