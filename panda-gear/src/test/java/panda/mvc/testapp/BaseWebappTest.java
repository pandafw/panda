package panda.mvc.testapp;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Before;

import panda.io.MimeType;
import panda.lang.Exceptions;
import panda.net.http.HttpClient;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;

/**
 * 需要Jetty 7.3.1 的jar包
 */
public abstract class BaseWebappTest {

	protected static Server server;

	protected static HttpResponse resp;

	private static final String serverURL = "http://localhost:9999";

	@Before
	public void startServer() throws Throwable {
		try {
			if (server == null) {
				String xml = "WEB-INF/web.xml";
				URL url = BaseWebappTest.class.getResource(xml);
				String path = url.toExternalForm();
				System.err.println(url);

				server = new Server(9999);
				
				String war = path.substring(0, path.length() - xml.length());
				WebAppContext wac = new WebAppContext();
				wac.setResourceBase(war);
				wac.setContextPath(getContextPath());

				//wac.setAttribute("javax.servlet.context.tempdir", war + "WEB-INF/tmp");

				server.setHandler(wac);
				server.start();
			}
		}
		catch (Throwable e) {
			if (server != null) {
				server.stop();
				server = null;
			}
			throw e;
		}
	}

	public void shutdownServer() throws Throwable {
		if (server != null) {
			server.stop();
		}
	}

	public String getContextPath() {
		return "/mvctest";
	}

	public String getBaseURL() {
		return serverURL + getContextPath();
	}

	public HttpResponse get(String path) {
		return get(path, true);
	}

	public HttpResponse get(String path, boolean redirect) {
		try {
			HttpRequest hr = HttpRequest.create(getBaseURL() + path, HttpMethod.GET);
			hr.getHeader().setDefaultAgentPC();
			
			HttpClient hc = new HttpClient(hr);
			hc.setAutoRedirect(redirect);
			resp = hc.send();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		assertNotNull(resp);
		return resp;
	}

	public HttpResponse post(String path, Map<String, Object> params) {
		try {
			resp = HttpClient.post(getBaseURL() + path, params);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		assertNotNull(resp);
		return resp;
	}

	public HttpResponse post(String path, String data) {
		return post(path, data, MimeType.APP_STREAM);
	}
	
	public HttpResponse post(String path, String data, String contentType) {
		try {
			HttpRequest hr = HttpRequest.create(getBaseURL() + path, HttpMethod.POST);
			hr.getHeader().setDefaultAgentPC();
			hr.setBody(data);
			if (contentType != null) {
				hr.getHeader().add(HttpHeader.CONTENT_TYPE, contentType);
			}
			HttpClient hc = new HttpClient(hr);

			resp = hc.send();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		assertNotNull(resp);
		return resp;
	}

	public HttpResponse post(String path, byte[] data) {
		try {
			resp = HttpClient.post(getBaseURL() + path, data);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		assertNotNull(resp);
		return resp;
	}
}
