package panda.mvc.adaptor.ejector;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.lang.time.DateTimes;
import panda.net.URLHelper;
import panda.vfs.FileItem;

@IocBean(singleton=false)
public class StreamParamEjector extends AbstractParamEjector {
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

				String fn = "noname-" + DateTimes.timestampLogFormat().format(DateTimes.getDate());
				FileItem fi = getActionContext().getFilePool().saveFile(fn, req.getInputStream());
				params.put(ALL, fi);
			}
			catch (Exception e) {
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
