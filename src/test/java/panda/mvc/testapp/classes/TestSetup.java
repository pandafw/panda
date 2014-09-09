package panda.mvc.testapp.classes;

import panda.mvc.MvcConfig;
import panda.mvc.Setup;

public class TestSetup implements Setup {

	public void init(MvcConfig config) {
		System.out.println(config.getAtMap().size());
	}

	public void destroy(MvcConfig config) {

	}

}
