package panda.wing.action.tool;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import panda.io.Streams;
import panda.lang.Strings;
import panda.lang.codec.binary.Hex;
import panda.lang.crypto.Digests;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.vfs.FileItem;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;


@At("${super_context}/hash")
@Auth(AUTH.SUPER)
@To(View.SFTL)
public class HashAction extends AbstractAction {
	/**
	 * execute
	 * @return Hash result map
	 * @throws Exception if an error occurs
	 */
	@At("")
	public Object execute(@Param("s") String s, @Param("f") FileItem f) throws Exception {
		if (Strings.isNotEmpty(s)) {
			return hashString(s);
		}
		
		if (f != null && f.isExists()) {
			return hashFile(f);
		}
		
		return null;
	}
	
	private Object hashString(String s) throws Exception {
		Map<String, String> m = new LinkedHashMap<String, String>();
		
		Set<String> as = Digests.getAvailableAlgorithms();
		for (String a : as) {
			m.put(a, Hex.encodeHexString(Digests.getDigest(a).digest(Strings.getBytesUtf8(s))));
		}

		return m;
	}
	
	private Object hashFile(FileItem f) throws Exception {
		Map<String, String> m = new LinkedHashMap<String, String>();
		
		Set<String> as = Digests.getAvailableAlgorithms();
		for (String a : as) {
			InputStream fis = f.getInputStream();
			try {
				m.put(a, Hex.encodeHexString(Digests.digest(Digests.getDigest(a), fis)));
			}
			finally {
				Streams.safeClose(fis);
			}
		}

		return m;
	}
}

