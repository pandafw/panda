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
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.mvc.adaptor.multipart.FileItemIterator;
import panda.mvc.adaptor.multipart.FileItemStream;
import panda.mvc.adaptor.multipart.FileUploader;
import panda.net.URLHelper;
import panda.servlet.FilteredHttpServletRequestWrapper;
import panda.servlet.HttpServlets;
import panda.vfs.FileItem;
import panda.vfs.FileStore;

@IocBean(singleton=false)
public class MultiPartParamEjector extends AbstractParamEjector {
	private static final Log log = Logs.getLog(MultiPartParamEjector.class);

	public static final String DEFAULT_TMPDIR = "uploads";
	
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
	@IocInject(value=MvcConstants.MULTIPART_BODY_MAXSIZE, required=false)
	private long bodySizeMax = 2097152;

	/**
	 * The maximum size permitted for a single uploaded file, as opposed to {@link #bodySizeMax}. A
	 * value of -1 indicates no maximum.
	 * default: 2M
	 */
	@IocInject(value=MvcConstants.MULTIPART_FILE_MAXSIZE, required=false)
	private long fileSizeMax = 2097152;

	/**
	 * The timeout of drain a request.
	 * default: 20s
	 */
	@IocInject(value=MvcConstants.MULTIPART_DRAIN_TIMEOUT, required=false)
	private long drainTimeout = 20 * DateTimes.MS_SECOND;

	/**
	 * the temporary directory of upload file.
	 */
	@IocInject(value=MvcConstants.FILE_UPLOAD_TMPDIR, required=false)
	private String tmpdir = DEFAULT_TMPDIR;

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
		fu.setDrainTimeout(drainTimeout);
		fu.setHeaderEncoding(encoding);
		FileItemIterator iter = fu.getItemIterator(req);
		while (iter.hasNext()) {
			final FileItemStream item = iter.next();
			if (item.isFormField()) {
				String s = Streams.toString(item.openStream(), encoding);
				addStringParam(req, item.getFieldName(), s);
			}
			else {
				FileItem i = saveUploadFile(item);
				addFileItemParam(item.getFieldName(), i);
			}
		}
	}

	protected FileItem saveUploadFile(FileItemStream item) throws IOException {
		String pref = tmpdir + '/' + Randoms.randUUID32() + '/';

		String name = FileNames.getName(item.getName());
		name = FileNames.trimFileName(name, FileNames.MAX_FILENAME_LENGTH - pref.length());
		
		if (Strings.isEmpty(name)) {
			// ignore empty file name
			Streams.drain(item.openStream());
			return null;
		}

		String path = pref + name;

		FileStore fs = getActionContext().getFileStore();
		FileItem fi = fs.getFile(path);
		fi.save(item.openStream());
		return fi;
	}

	protected void addStringParam(HttpServletRequest req, String name, String value) {
		if (Strings.isNotEmpty(value)) {
			addParam(name, value);

			// add to FilteredHttpServletRequestWrapper
			if (req instanceof FilteredHttpServletRequestWrapper) {
				((FilteredHttpServletRequestWrapper)req).addParam(name, value);
			}
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
