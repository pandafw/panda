package panda.mvc.adaptor.ejector;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.filepool.FileItem;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.net.http.URLHelper;

public class StreamParamEjector extends AbstractParamEjector {
	private Map<String, Object> params;

	public StreamParamEjector(ActionContext ac) {
		super(ac);
	}

	@Override
	protected Map<String, Object> getParams() {
		if (params == null) {
			try {
				HttpServletRequest req = ac.getRequest();
				
				String qs = req.getQueryString();
				params = URLHelper.parseQueryString(qs);
				
				FileItem fi = getActionContext().getFilePool().saveFile("noname", req.getInputStream(), true);
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
