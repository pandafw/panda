package panda.mvc.init.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;

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
