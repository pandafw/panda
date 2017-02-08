package panda.wing.action.tool;

import java.util.ArrayList;
import java.util.List;

import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.lang.crypto.Digests;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_path}/pwdhash")
@Auth(AUTH.SUPER)
@To(View.SFTL)
public class PasswordHashAction extends AbstractAction {

	@At("")
	public Object exec(@Param("txt") String txt) {
		if (Strings.isEmpty(txt)) {
			return null;
		}
		
		List<KeyValue<String, String>> hash = new ArrayList<KeyValue<String, String>>();
		String[] pwds = Strings.split(txt);
		for (String p : pwds) {
			String h = hash(p);
			KeyValue<String, String> kv = new KeyValue<String, String>(p, h);
			hash.add(kv);
		}
		return hash;
	}
	
	protected String hash(String s) {
		return Digests.sha256Hex(s);
	}
}
