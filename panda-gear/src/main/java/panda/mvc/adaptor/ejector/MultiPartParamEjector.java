package panda.mvc.adaptor.ejector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.io.FileNames;
import panda.io.SizeLimitExceededException;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.mvc.adaptor.multipart.FileItemIterator;
import panda.mvc.adaptor.multipart.FileItemStream;
import panda.mvc.adaptor.multipart.FileUploader;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;
import panda.vfs.FileItem;
import panda.vfs.FilePool;

@IocBean(singleton=false)
public class MultiPartParamEjector extends AbstractParamEjector {
	private static final Log log = Logs.getLog(MultiPartParamEjector.class);

	@IocInject(value=MvcConstants.REQUEST_ENCODING, required=false)
	private String encoding = Charsets.UTF_8;
	
	@IocInject(value=MvcConstants.REQUEST_EMPTY_PARAMS, required=false)
	private boolean emptyParams = true;
	
	private Map<String, Object> params;

	/**
	 * The maximum size permitted for the complete request, as opposed to {@link #fileSizeMax}. A
	 * value of -1 indicates no maximum.
	 * default: 2M
	 */
	@IocInject(value=MvcConstants.MULTIPART_BODY_SIZE_MAX, required=false)
	private long bodySizeMax = 2097152;

	/**
	 * The maximum size permitted for a single uploaded file, as opposed to {@link #bodySizeMax}. A
	 * value of -1 indicates no maximum.
	 * default: 2M
	 */
	@IocInject(value=MvcConstants.MULTIPART_FILE_SIZE_MAX, required=false)
	private long fileSizeMax = 2097152;

	/**
	 * The timeout of drain a request.
	 * default: 60s
	 */
	@IocInject(value=MvcConstants.MULTIPART_DRAIN_TIMEOUT, required=false)
	private long drainTimeout = DateTimes.MS_MINUTE;

	public MultiPartParamEjector() {
	}

	@Override
	protected Map<String, Object> getParams() {
		if (params == null) {
			HttpServletRequest req = ac.getRequest();
			try {
				if (log.isDebugEnabled()) {
					log.debug("parse: " + ac.getPath());
				}

				String enc = HttpServlets.getEncoding(req, encoding);

				String qs = req.getQueryString();
				params = URLHelper.parseQueryString(qs, enc, !emptyParams);

				parse(req, enc);
			}
			catch (SizeLimitExceededException e) {
				HttpServlets.safeDrain(req, drainTimeout);
				throw Exceptions.wrapThrow(e);
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return params;
	}

	protected void parse(HttpServletRequest req, String encoding) throws IOException {
		FileUploader fu = new FileUploader();
		fu.setBodySizeMax(bodySizeMax);
		fu.setFileSizeMax(fileSizeMax);
		fu.setHeaderEncoding(encoding);
		FileItemIterator iter = fu.getItemIterator(req);
		while (iter.hasNext()) {
			final FileItemStream item = iter.next();
			if (item.isFormField()) {
				String s = Streams.toString(item.openStream(), encoding);
				addStringParam(item.getFieldName(), s);
			}
			else {
				FileItem i = saveUploadFile(item);
				addFileItemParam(item.getFieldName(), i);
			}
		}
	}

	protected FileItem saveUploadFile(FileItemStream item) throws IOException {
		FilePool fp = getActionContext().getFilePool();
		String name = FileNames.getName(item.getName());
		return fp.saveFile(name, item.openStream(), true);
	}

	protected void addStringParam(String name, String value) {
		if (Strings.isNotEmpty(value)) {
			addParam(name, value);
		}
	}
	
	protected void addFileItemParam(String name, FileItem value) {
		if (value != null) {
			addParam(name, value);
		}
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
			params.put(name, l);
		}
	}
}
