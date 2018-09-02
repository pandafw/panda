package panda.mvc;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.entity.EntityDao;
import panda.io.Streams;
import panda.ioc.Ioc;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ioc.IocRequestListener;
import panda.net.http.HttpMethod;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServlets;
import panda.servlet.mock.MockHttpServletRequest;
import panda.servlet.mock.MockHttpServletResponse;
import panda.servlet.mock.MockServletConfig;
import panda.servlet.mock.MockServletContext;

public class MvcConsole {
	private static final Log log = Logs.getLog(MvcConsole.class);
	
	protected MockServletContext servlet;
	protected MockServletConfig config;
	protected MvcHandler handler;

	public MvcConsole(Class<?> mainModule) {
		this(mainModule, null);
	}
	
	public MvcConsole(Class<?> mainModule, String base) {
		servlet = new MockServletContext(base);
		config = new MockServletConfig(servlet);
		config.addInitParameter("modules", mainModule.getName());

		MvcConfig mcfg = new MvcConfig(config.getServletContext(), config.getInitParameter("modules"));
		MvcLoader loader = new MvcLoader(mcfg);
		handler = loader.getActionHandler();
		Mvcs.setActionHandler(mcfg.getServletContext(), handler);
	}

	public void destroy() {
		if (handler != null) {
			handler.depose();
		}
	}

	public Ioc getIoc() {
		return handler.getIoc();
	}
	
	public DaoClient getDaoClient() {
		return getIoc().get(DaoClient.class);
	}
	/**
	 * @param type record type
	 * @return the entity
	 */
	public <T> EntityDao<T> getEntityDao(Class<T> type) {
		return getDao().getEntityDao(type);
	}

	/**
	 * @param type record type
	 * @param param argument used for dynamic table
	 * @return the entity
	 */
	public <T> EntityDao<T> getEntityDao(Class<T> type, Object param) {
		return getDao().getEntityDao(type, param);
	}

	
	public Dao getDao() {
		return getDaoClient().getDao();
	}
	
	public MockHttpServletResponse doGet(String uri) throws Exception {
		return doRequest(uri, HttpMethod.GET, null);
	}

	public MockHttpServletResponse doGet(String uri, Map<String, Object> params) throws Exception {
		return doRequest(uri, HttpMethod.GET, params);
	}

	public MockHttpServletResponse doPost(String uri) throws Exception {
		return doRequest(uri, HttpMethod.POST, null);
	}

	public MockHttpServletResponse doPost(String uri, Map<String, Object> params) throws Exception {
		return doRequest(uri, HttpMethod.GET, params);
	}
	
	public MockHttpServletResponse doRequest(String uri) throws Exception {
		return doRequest(uri, null);
	}

	public MockHttpServletResponse doRequest(String uri, Map<String, Object> params) throws Exception {
		return doRequest(uri, HttpMethod.GET, params);
	}
	
	public MockHttpServletRequest initRequest(String uri, String method, Map<String, Object> params) {
		MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
		request.setServletPath(uri);
		if (params != null) {
			request.addParameters(params);
		}
		return request;
	}

	public MockHttpServletResponse doRequest(MockHttpServletRequest request) throws ServletException, IOException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		//response.setOutputStream(new ConsoleServletOutputStream());
		
		IocRequestListener irl = new IocRequestListener();
		ServletRequestEvent sre = new ServletRequestEvent(servlet, request);
		try {
			irl.requestInitialized(sre);
			service(request, response);
			return response;
		}
		finally {
			irl.requestDestroyed(sre);
		}
	}
	
	public MockHttpServletResponse doRequest(String uri, String method, Map<String, Object> params) throws ServletException, IOException {
		MockHttpServletRequest request = initRequest(uri, method, params);
		return doRequest(request);
	}

	public void service(MockHttpServletRequest request, MockHttpServletResponse response) throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(request.getRequestURI()).append(" - start ... ");

			if (!request.getParameterMap().isEmpty()) {
				sb.append(HttpServlets.dumpRequestParameters(request));
			}
			
			log.debug(sb.toString());
		}
		
		if (!handler.handle(request, response)) {
			response.sendError(HttpStatus.SC_NOT_FOUND);
		}
		
		Throwable ex = HttpServlets.getServletException(request);
		if (ex != null) {
			log.error("service", ex);
		}

		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(request.getRequestURI()).append(" - end.");
			sb.append(Streams.EOL);
			sb.append("  Response Status: ");
			sb.append(response.getStatus());
			sb.append(" - ");
			sb.append(HttpStatus.getStatusReason(response.getStatus()));
			String rh = dumpMockResponseHeader(response, "\t");
			if (Strings.isNotEmpty(rh)) {
				sb.append(Streams.EOL);
				sb.append("  Response Head: ");
				sb.append(Streams.EOL);
				sb.append(rh);
			}
			if (response.getContentType().indexOf("text") >= 0) {
				String rb = response.getContentAsString();
				if (Strings.isNotEmpty(rb)) {
					sb.append(Streams.EOL);
					sb.append("  Response Body: ");
					sb.append(rb);
				}
			}
			log.debug(sb.toString());
		}
	}

	private String dumpMockResponseHeader(MockHttpServletResponse response, String indent) {
		StringBuilder sb = new StringBuilder();
		Set<String> hns = response.getHeaderNames();
		for (String hn : hns) {
			if (sb.length() > 0) {
				sb.append(Streams.EOL);
			}
			sb.append(indent);
			sb.append(hn);
			sb.append(": ");
			sb.append(Strings.join(response.getHeaders(hn)));
		}
		return sb.toString();
	}
}
