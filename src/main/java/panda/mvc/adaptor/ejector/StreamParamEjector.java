package panda.mvc.adaptor.ejector;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.filepool.FileItem;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.net.http.URLHelper;

public class StreamParamEjector extends AbstractParamEjector {
	private final static String NAME = "file";
	
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
				
				FileItem fi = getActionContext().getFilePool().saveFile(NAME, req.getInputStream(), true);
				params.put(NAME, fi);
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return params;
	}
}
