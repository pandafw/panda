package panda.mvc.util;

import java.io.IOException;

import javax.servlet.ServletContext;

import panda.io.RuntimeSettings;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.mvc.SetConstants;

@IocBean(type=Settings.class, create="initialize")
public class MvcSettings extends RuntimeSettings {

	@IocInject(required=false)
	private ServletContext servlet;

	public void initialize() throws IOException {
		String dir = Systems.getUserDir().getAbsolutePath();
		if (servlet != null) {
			dir = servlet.getRealPath("/");
			if (Strings.endsWithChars(dir, "\\/")) {
				dir = dir.substring(0, dir.length() - 1);
			}
		}
		super.put(SetConstants.WEB_DIR, dir, MvcSettings.class.getName());
	}
}
