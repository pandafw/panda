package panda.mvc.testapp;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.junit.Before;

import panda.io.MimeTypes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.Threads;
import panda.net.http.HttpClient;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;

/**
 * webapp-runner required
 */
public abstract class BaseWebappTest {

	private static final String SERVER_URL = "http://localhost:9999";
	private static final String CONTEXT = "/mvctest";

	private static Thread server;
	private static Throwable error;
	
	protected static HttpResponse resp;

	@Before
	public void startServer() throws Throwable {
		if (server != null) {
			return;
		}
		
		if (error != null) {
			throw error;
		}
		
		try {
			server = Threads.start(new Runnable() {
				@Override
				public void run() {
					String xml = "WEB-INF/web.xml";
					URL url = BaseWebappTest.class.getResource(xml);
					String path = url.toExternalForm();
					String war = path.substring(0, path.length() - xml.length());
					war = Strings.removeStart(war, "file:");
					try {
						webapp.runner.launch.Main.main(new String[] { 
								"--port", "9999", 
								"--path", CONTEXT, 
								"--temp-directory", "out/tomcat",
								war });
					}
					catch (Exception e) {
						error = e;
					}
				}
			});
			Threads.safeSleep(10000);
		}
		catch (Throwable e) {
			server = null;
			error = e;
		}

		if (error != null) {
			throw error;
		}
	}

	public String getContextPath() {
		return CONTEXT;
	}

	public String getServerURL() {
		return SERVER_URL;
	}
	
	public String getBaseURL() {
		return SERVER_URL + getContextPath();
	}

	public HttpResponse get(String path) {
		return get(path, true);
	}

	public HttpResponse get(String path, boolean redirect) {
		try {
			HttpRequest hr = HttpRequest.create(getBaseURL() + path, HttpMethod.GET);
			hr.asWindowsChrome();
			
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
		return post(path, data, MimeTypes.APP_STREAM);
	}
	
	public HttpResponse post(String path, String data, String contentType) {
		try {
			HttpRequest hr = HttpRequest.create(getBaseURL() + path, HttpMethod.POST);
			hr.asWindowsChrome();
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
