package panda.gems.admin.action;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.codec.binary.Hex;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.util.crypto.Digests;
import panda.vfs.FileItem;
import panda.vfs.FileStores;


@At("${!!super_path|||'/super'}/hash")
@Auth(AUTH.SUPER)
@To(Views.SFTL)
public class HashAction extends BaseAction {
	/**
	 * execute
	 * @param s the string to hash
	 * @param f the file to hash
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
	
	private Object hashString(String s) {
		Map<String, String> m = new TreeMap<String, String>();
		
		Set<String> as = Digests.getAvailableAlgorithms();
		for (String a : as) {
			String h;
			try {
				h = Hex.encodeHexString(Digests.getDigest(a).digest(Strings.getBytesUtf8(s)));
			}
			catch (Exception e) {
				h = e.getMessage();
			}
			m.put(a, h);
		}

		return m;
	}
	
	private Object hashFile(FileItem f) {
		byte[] bs;
		try {
			bs = FileStores.toByteArray(f);
		}
		catch (IOException e) {
			addActionError(e.getMessage());
			return null;
		}
		finally {
			FileStores.safeDelete(f);
		}

		Map<String, String> m = new TreeMap<String, String>();
		
		Set<String> as = Digests.getAvailableAlgorithms();
		for (String a : as) {
			String h;
			try {
				h = Hex.encodeHexString(Digests.getDigest(a).digest(bs));
			}
			catch (Exception e) {
				h = e.getMessage();
			}
			m.put(a, h);
		}

		return m;
	}
}

