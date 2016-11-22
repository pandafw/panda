package panda.wing.action.tool;

import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.lang.crypto.Digests;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

import java.util.ArrayList;
import java.util.List;

@At("${super_context}/pwdhash")
@Auth(AUTH.SUPER)
@Ok(View.SFTL)
public class PasswordHashAction extends AbstractAction {

	@At("")
	@Ok(View.SFTL)
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
