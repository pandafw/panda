package panda.mvc.init.module;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;

@At("/base")
@To(View.SJSON)
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
