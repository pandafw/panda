package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Ok;

@IocBean
@At("/base")
@Ok("json")
public class BaseModule {

	private String nameX;

	@At
	public boolean login() {
		return getNameX() != null;
	}

	public void setNameX(String nameX) {
		this.nameX = nameX;
	}

	public String getNameX() {
		return nameX;
	}
}
