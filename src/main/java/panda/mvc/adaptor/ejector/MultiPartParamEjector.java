package panda.mvc.adaptor.ejector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.filepool.FileItem;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.RequestPath;
import panda.mvc.adaptor.multipart.FileItemIterator;
import panda.mvc.adaptor.multipart.FileItemStream;
import panda.mvc.adaptor.multipart.FileUploadException;
import panda.mvc.adaptor.multipart.FileUploader;
import panda.net.http.URLHelper;
import panda.servlet.HttpServlets;

public class MultiPartParamEjector extends AbstractParamEjector {
	private static final Log log = Logs.getLog(MultiPartParamEjector.class);

	private Map<String, Object> params;

	public MultiPartParamEjector(ActionContext ac) {
		super(ac);
	}

	@Override
	protected Map<String, Object> getParams() {
		if (params == null) {
			try {
				HttpServletRequest req = ac.getRequest();
				if (log.isDebugEnabled()) {
					log.debug("parse: " + RequestPath.getRequestPath(req));
				}

				String qs = req.getQueryString();
				params = URLHelper.parseQueryString(qs);

				parse(req);
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return params;
	}

	protected void parse(HttpServletRequest req) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Parse: " + RequestPath.getRequestPath(req));
		}
		
		String encoding = HttpServlets.getEncoding(req);
		
		try {
			FileUploader fu = new FileUploader();
			FileItemIterator iter = fu.getItemIterator(req);
			while (iter.hasNext()) {
				final FileItemStream item = iter.next();
				Object val = null;
				if (item.isFormField()) {
					val = saveUploadFile(item);
				}
				else {
					val = Streams.toString(item.openStream(), encoding);
				}
				addParam(item.getFieldName(), val);
			}
		}
		catch (FileUploadException e) {
			throw e;
		}
		catch (IOException e) {
			throw new FileUploadException(e.getMessage(), e);
		}
	}

	protected FileItem saveUploadFile(FileItemStream item) throws IOException {
		return this.getActionContext().getFilePool().saveFile(item, true);
	}

	@SuppressWarnings("unchecked")
	protected void addParam(String name, Object value) {
		Object v = params.get(name);
		if (v == null) {
			params.put(name, value);
		}
		else if (v instanceof List) {
			((List)v).add(value);
		}
		else {
			List l = new ArrayList();
			l.add(v);
			l.add(value);
		}
	}
}