package panda.gems.users.action.admin;

import java.util.ArrayList;
import java.util.List;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.users.util.PasswordHelper;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/pwhash")
@Auth(AUTH.SUPER)
@To(Views.SFTL)
public class PasswordHashAction extends BaseAction {
	@IocInject
	private PasswordHelper pwHelper;

	@At("")
	public void input() {
		
	}
	
	@At
	@To(Views.SJSON)
	public Object hash(@Param("txt") String txt) {
		if (Strings.isEmpty(txt)) {
			return null;
		}
		
		List<KeyValue<String, String>> hash = new ArrayList<KeyValue<String, String>>();
		String[] pws = Strings.split(txt);
		for (String pw : pws) {
			String h = pwHelper.hashPassword(pw);
			KeyValue<String, String> kv = new KeyValue<String, String>(pw, h);
			hash.add(kv);
		}
		return hash;
	}
}
