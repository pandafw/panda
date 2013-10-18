package panda.mock.web;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Streams;
import panda.io.resource.FileSystemResourceLoader;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpResponse;
import panda.servlet.HttpServletUtils;

/**
 */
public class ServletMockSupport {
	protected static final Log log = Logs.getLog(ServletMockSupport.class);

	private static MockServletContext mockServletContext;
	private static Filter filterDispatcher;
	
	private MockHttpSession mockSession;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;

	/**
	 * Constructor
	 */
	public ServletMockSupport() {
		super();
	}

	public synchronized static void initContext(String contextRoot) {
		if (mockServletContext == null) {
			mockServletContext = new MockServletContext(contextRoot, new FileSystemResourceLoader());
		}
	}

	public synchronized static void initDispatcher(String contextRoot) {
		if (filterDispatcher == null) {
			try {
				initContext(contextRoot);
				
				//MockFilterConfig filterConfig = new MockFilterConfig(mockServletContext, "nuts");

				//TODO: filterDispatcher = new StrutsMockFilter();
				//filterDispatcher.init(filterConfig);
			}
			catch (Exception e) {
				log.error("init", e);
				throw new RuntimeException(e);
			}
		}
	}
	
	public void initMockRequest(String uri, Map<String, String> params) {
//		if (Strings.isNotEmpty(getActionExtension())) {
//			uri += "." + getActionExtension();
//		}

		mockRequest = new MockHttpServletRequest();
		mockRequest.setRequestURI(uri);
		mockRequest.setServletPath(uri);
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				mockRequest.addParameter(param.getKey(), param.getValue());
			}
		}
		if (mockSession != null) {
			mockRequest.setSession(mockSession);
		}
	}

	public void initMockResponse() {
		mockResponse = new MockHttpServletResponse();
	}
	
	public void initConsoleResponse() {
		mockResponse = new MockHttpServletResponse();
		mockResponse.setOutputStream(new ConsoleServletOutputStream());
	}
	
	public void doRequest() throws Exception {
		doRequest(mockRequest, mockResponse);
	}

	public void doRequest(String uri) throws Exception {
		doRequest(uri, null);
	}

	public void doRequest(String uri, Map<String, String> params) throws Exception {
		initMockRequest(uri, params);
		initMockResponse();
		doRequest();
	}

	public void doRequest(ServletRequest req, ServletResponse res) throws Exception {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(request.getRequestURI()).append(" - start ... ");

			if (!request.getParameterMap().isEmpty()) {
				sb.append(HttpServletUtils.dumpRequestParameters(request));
			}
			
			log.debug(sb.toString());
		}
		
		MockFilterChain chain = new MockFilterChain();
		try {
			filterDispatcher.doFilter(request, response, chain);
		}
		catch (Exception ex) {
			log.error("doRequest", ex);
			throw ex;
		}
		
		Throwable ex = (Throwable)request.getAttribute("javax.servlet.error.exception");
		if (ex != null) {
			log.error("doRequest", ex);
		}

		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(request.getRequestURI()).append(" - end.");
			sb.append(Streams.LINE_SEPARATOR);
			sb.append("  Response Status: ");
			sb.append(getMockResponse().getStatus());
			sb.append(" - ");
			sb.append(HttpResponse.getStatusReason(getMockResponse().getStatus()));
			String rh = dumpMockResponseHeader("\t");
			if (Strings.isNotEmpty(rh)) {
				sb.append(Streams.LINE_SEPARATOR);
				sb.append("  Response Head: ");
				sb.append(Streams.LINE_SEPARATOR);
				sb.append(rh);
			}
			String rb = getMockResponse().getContentAsString();
			if (Strings.isNotEmpty(rb)) {
				sb.append(Streams.LINE_SEPARATOR);
				sb.append("  Response Body: ");
				sb.append(rb);
			}
			log.debug(sb.toString());
		}
	}

	public String dumpMockResponseHeader(String prefix) {
		StringBuilder sb = new StringBuilder();
		Set<String> hns = getMockResponse().getHeaderNames();
		for (String hn : hns) {
			if (sb.length() > 0) {
				sb.append(Streams.LINE_SEPARATOR);
			}
			sb.append(prefix);
			sb.append(hn);
			sb.append(": ");
			sb.append(Strings.join(getMockResponse().getHeaders(hn)));
		}
		return sb.toString();
	}
	
	public void cleanUp() {
		if (mockRequest != null) {
//TODO:			filterDispatcher.cleanUp(mockRequest);
		}
	}

	public String getResponseContentAsString() throws UnsupportedEncodingException {
		return getMockResponse().getContentAsString();
	}

	public byte[] getResponseContentAsByteArray() {
		return getMockResponse().getContentAsByteArray();
	}

	// setter & getter
	/**
	 * @return the mockServletContext
	 */
	public static MockServletContext getMockServletContext() {
		return mockServletContext;
	}

	/**
	 * @param mockServletContext the mockServletContext to set
	 */
	public static void setMockServletContext(MockServletContext mockServletContext) {
		ServletMockSupport.mockServletContext = mockServletContext;
	}

	/**
	 * @return the mockSession
	 */
	public MockHttpSession getMockSession() {
		return mockSession;
	}

	/**
	 * @param mockSession the mockSession to set
	 */
	public void setMockSession(MockHttpSession mockSession) {
		this.mockSession = mockSession;
	}

	/**
	 * @return the mockRequest
	 */
	public MockHttpServletRequest getMockRequest() {
		return mockRequest;
	}

	/**
	 * @param mockRequest the mockRequest to set
	 */
	public void setMockRequest(MockHttpServletRequest mockRequest) {
		this.mockRequest = mockRequest;
	}

	/**
	 * @return the mockResponse
	 */
	public MockHttpServletResponse getMockResponse() {
		return mockResponse;
	}

	/**
	 * @param mockResponse the mockResponse to set
	 */
	public void setMockResponse(MockHttpServletResponse mockResponse) {
		this.mockResponse = mockResponse;
	}
}