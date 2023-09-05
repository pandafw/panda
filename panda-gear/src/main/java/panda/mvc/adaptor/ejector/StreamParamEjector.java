package panda.mvc.adaptor.ejector;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.Randoms;
import panda.mvc.MvcConstants;
import panda.net.URLHelper;
import panda.vfs.FileItem;
import panda.vfs.FileStore;

@IocBean(singleton = false)
public class StreamParamEjector extends AbstractParamEjector {
	/**
	 * the temporary directory of upload file.
	 */
	@IocInject(value = MvcConstants.FILE_UPLOAD_TMPDIR, required = false)
	private String tmpdir = MultiPartParamEjector.DEFAULT_TMPDIR;

	private Map<String, Object> params;

	public StreamParamEjector() {
	}

	@Override
	protected Map<String, Object> getParams() {
		if (params == null) {
			try {
				HttpServletRequest req = ac.getRequest();

				String qs = req.getQueryString();
				params = URLHelper.parseQueryString(qs);

				String path = tmpdir + '/' + Randoms.randUUID32() + "/noname";

				FileStore fs = getActionContext().getFileStore();

				FileItem fi = fs.getFile(path);
				fi.save(req.getInputStream());

				params.put(ALL, fi);
			} catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return params;
	}

	@Override
	public Object eject() {
		return getParams().get(ALL);
	}
}
