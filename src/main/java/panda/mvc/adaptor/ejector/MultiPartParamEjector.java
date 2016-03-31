package panda.mvc.adaptor.ejector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.adaptor.multipart.FileItemIterator;
import panda.mvc.adaptor.multipart.FileItemStream;
import panda.mvc.adaptor.multipart.FileUploadException;
import panda.mvc.adaptor.multipart.FileUploader;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;
import panda.vfs.FileItem;
import panda.vfs.FilePool;

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
					log.debug("parse: " + ac.getPath());
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
		String encoding = HttpServlets.getEncoding(req);
		
		try {
			FileUploader fu = new FileUploader();
			FileItemIterator iter = fu.getItemIterator(req);
			while (iter.hasNext()) {
				final FileItemStream item = iter.next();
				Object val = null;
				if (item.isFormField()) {
					val = Streams.toString(item.openStream(), encoding);
				}
				else {
					val = saveUploadFile(item);
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
		FilePool fp = getActionContext().getFilePool();
		String name = FileNames.getName(item.getName());
		return fp.saveFile(name, item.openStream(), true);
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
