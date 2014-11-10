package panda.mvc;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.io.Streams;
import panda.ioc.Ioc;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.config.ServletMvcConfig;
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
	protected ActionHandler handler;

	public MvcConsole(Class<?> mainModule) {
		this(mainModule, null);
	}
	
	public MvcConsole(Class<?> mainModule, String base) {
		servlet = new MockServletContext(base);
		config = new MockServletConfig(servlet);
		config.addInitParameter("modules", mainModule.getName());
		
		ServletMvcConfig smc = new ServletMvcConfig(config);
		handler = new ActionHandler(smc);
	}

	public void destroy() {
		if (handler != null) {
			handler.depose();
		}
	}

	public Ioc getIoc() {
		return handler.getConfig().getIoc();
	}
	
	public DaoClient getDaoClient() {
		return getIoc().get(DaoClient.class);
	}
	
	public Dao getDao() {
		return getDaoClient().getDao();
	}
	
	public void doGet(String uri) throws Exception {
		doRequest(uri, null, HttpMethod.GET.toString());
	}

	public void doGet(String uri, Map<String, Object> params) throws Exception {
		doRequest(uri, params, HttpMethod.GET.toString());
	}

	public void doPost(String uri) throws Exception {
		doRequest(uri, null, HttpMethod.POST.toString());
	}

	public void doPost(String uri, Map<String, Object> params) throws Exception {
		doRequest(uri, params, HttpMethod.GET.toString());
	}
	
	public void doRequest(String uri) throws Exception {
		doRequest(uri, null);
	}

	public void doRequest(String uri, Map<String, Object> params) throws Exception {
		doRequest(uri, params, HttpMethod.GET.toString());
	}
	
	public void doRequest(String uri, Map<String, Object> params, String method) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI(uri);
		request.setServletPath(uri);
		request.setMethod(method);
		if (params != null) {
			request.addParameters(params);
		}

		MockHttpServletResponse response = new MockHttpServletResponse();
		//response.setOutputStream(new ConsoleServletOutputStream());
		
		IocRequestListener irl = new IocRequestListener();
		ServletRequestEvent sre = new ServletRequestEvent(servlet, request);
		try {
			irl.requestInitialized(sre);
			service(request, response);
		}
		finally {
			irl.requestDestroyed(sre);
		}
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
			response.sendError(404);
		}
		
		Throwable ex = HttpServlets.getServletException(request);
		if (ex != null) {
			log.error("service", ex);
		}

		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(request.getRequestURI()).append(" - end.");
			sb.append(Streams.LINE_SEPARATOR);
			sb.append("  Response Status: ");
			sb.append(response.getStatus());
			sb.append(" - ");
			sb.append(HttpStatus.getStatusReason(response.getStatus()));
			String rh = dumpMockResponseHeader(response, "\t");
			if (Strings.isNotEmpty(rh)) {
				sb.append(Streams.LINE_SEPARATOR);
				sb.append("  Response Head: ");
				sb.append(Streams.LINE_SEPARATOR);
				sb.append(rh);
			}
			if (response.getContentType().indexOf("text") >= 0) {
				String rb = response.getContentAsString();
				if (Strings.isNotEmpty(rb)) {
					sb.append(Streams.LINE_SEPARATOR);
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
				sb.append(Streams.LINE_SEPARATOR);
			}
			sb.append(indent);
			sb.append(hn);
			sb.append(": ");
			sb.append(Strings.join(response.getHeaders(hn)));
		}
		return sb.toString();
	}
}
