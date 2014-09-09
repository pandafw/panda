package panda.mvc.testapp;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;

import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;

/**
 * 需要Jetty 7.3.1 的jar包
 * 
 * @author wendal
 */
public abstract class BaseWebappTest {

	protected Server server;

	protected HttpResponse resp;

	private boolean isRunInMaven = false;

	private String serverURL = "http://localhost:8888";

	{
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			if (ste.getClassName().startsWith("org.apache.maven.surefire")) {
				isRunInMaven = true;
				serverURL = "http://nutztest.herokuapp.com";
				break;
			}
		}
	}

	@Before
	public void startServer() throws Throwable {
		if (!isRunInMaven) {
			try {
				URL url = getClass().getClassLoader().getResource("org/nutz/mvc/testapp/Root/FLAG");
				String path = url.toExternalForm();
				System.err.println(url);
				server = new Server(8888);
				String warUrlString = path.substring(0, path.length() - 4);
				server.setHandler(new WebAppContext(warUrlString, getContextPath()));
				server.start();
			}
			catch (Throwable e) {
				if (server != null)
					server.stop();
				throw e;
			}
		}
	}

	@After
	public void shutdownServer() throws Throwable {
		if (!isRunInMaven) {
			if (server != null)
				server.stop();
		}
	}

	public String getContextPath() {
		return "/nutztest";
	}

	public String getBaseURL() {
		return serverURL + getContextPath();
	}

	public HttpResponse get(String path) {
		try {
			resp = HttpClient.get(getBaseURL() + path);
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
		try {
			resp = HttpClient.post(getBaseURL() + path, Strings.getBytes(data, Charsets.UTF_8));
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
