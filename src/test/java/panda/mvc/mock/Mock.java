package panda.mvc.mock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.lang.Exceptions;
import panda.mock.web.MockHttpServletRequest;
import panda.mock.web.MockHttpServletResponse;
import panda.mock.web.MockHttpSession;
import panda.mock.web.MockServletConfig;
import panda.mock.web.MockServletContext;
import panda.mvc.ActionContext;

/**
 * 一些方面的静态方法
 */
public abstract class Mock {

	public static class MockActionContext extends ActionContext {
		private static final long serialVersionUID = 1L;

		public MockHttpServletRequest getMockRequest() {
			return (MockHttpServletRequest)super.getRequest();
		}

		public MockHttpServletResponse getMockResponse() {
			return (MockHttpServletResponse)super.getResponse();
		}

		public MockServletContext getMockServlet() {
			return (MockServletContext)super.getServlet();
		}
	}
	
	public static MockActionContext actionContext() {
		MockActionContext ac = new MockActionContext();
		ac.setRequest(servlet.fullRequest());
		return ac;
	}

	public static class servlet {
		public static MockServletContext context() {
			return new MockServletContext();
		}

		public static MockServletConfig config(String s) {
			return new MockServletConfig(context(), s);
		}

		public static MockHttpServletRequest request() {
			return new MockHttpServletRequest();
		}
		
		public static MockHttpServletRequest fullRequest() {
			MockHttpServletRequest req = request();
			req.setSession(session(context()));
			return req;
		}

		public static MockHttpSession session(MockServletContext context) {
			return new MockHttpSession(context);
		}

		public static ServletInputStream ins(final InputStream ins) {
			return new ServletInputStream() {
				public int read() throws IOException {
					return ins.read();
				}

				public int available() throws IOException {
					return super.available();
				}

				public void close() throws IOException {
					ins.close();
				}

				public synchronized void mark(int readlimit) {
					ins.mark(readlimit);
				}

				public boolean markSupported() {
					return ins.markSupported();
				}

				public int read(byte[] b, int off, int len) throws IOException {
					return ins.read(b, off, len);
				}

				public int read(byte[] b) throws IOException {
					return ins.read(b);
				}

				public synchronized void reset() throws IOException {
					ins.reset();
				}

				public long skip(long n) throws IOException {
					return ins.skip(n);
				}
			};
		}

		public static ServletInputStream ins(String path) throws IOException {
			return ins(new FileInputStream(path));
		}
	}

	public static final InvocationHandler EmtryInvocationHandler = new InvocationHandler() {
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
			throw Exceptions.unsupported();
		};
	};

	public static final HttpServletRequest EmtryHttpServletRequest = (HttpServletRequest)Proxy.newProxyInstance(
		Mock.class.getClassLoader(), new Class[] { HttpServletRequest.class }, EmtryInvocationHandler);

	public static final HttpServletResponse EmtryHttpServletResponse = (HttpServletResponse)Proxy.newProxyInstance(
		Mock.class.getClassLoader(), new Class[] { HttpServletResponse.class }, EmtryInvocationHandler);

}
