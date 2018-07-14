package panda.mvc.init.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

@At("/base")
@To(Views.SJSON)
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
