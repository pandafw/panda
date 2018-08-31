package panda.app.task;

import javax.servlet.ServletContext;

import panda.app.auth.UserAuthenticator;
import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.app.util.AppSettings;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpClient;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;

@IocBean(singleton=false)
public class ActionTaskSubmitter implements Runnable {
	private static Log log = Logs.getLog(ActionTaskSubmitter.class);

	@IocInject
	protected AppSettings settings;
	
	@IocInject(required=false)
	protected ServletContext servlet;

	@IocInject(required=false)
	protected UserAuthenticator authenticator;

	@IocInject(value=MVC.TASK_ACTION_SCHEME, required=false)
	protected String scheme = "http://localhost:8080";

	@IocInject(value=MVC.TASK_ERROR_LIMIT, required=false)
	protected int defaultErrorLimit = 3;

	private ActionTask task;
	private int errors;

	/**
	 * @param task the task to set
	 */
	public void setTask(ActionTask task) {
		this.task = task;
	}

	@Override
	public void run() {
		scheme = settings.getProperty(SET.TASK_ACTION_SCHEME, scheme);

		String url = task.getAction();
		if (servlet != null && url.startsWith("/")) {
			url = scheme + servlet.getContextPath() + url;
		}

		int errorLimit = task.getErrorLimit() == null ? defaultErrorLimit : task.getErrorLimit();
		
		if (log.isInfoEnabled()) {
			log.info("Start action task: " + url);
		}

		HttpResponse hres = null;
		try {
			HttpClient hc = new HttpClient();
			
			hc.setAutoRedirect(false);
			HttpRequest hreq = HttpRequest.create(url, task.getMethod());
			if (task.getParams() != null) {
				hreq.getParams().putAll(task.getParams());
			}
			if (task.getHeaders() != null) {
				hreq.getHeader().putAll(task.getHeaders());
			}
			if (task.isToken() && authenticator != null) {
				hreq.setParam(authenticator.getTokenName(), authenticator.getTokenValue());
			}
			hc.setRequest(hreq);
			
			if (log.isDebugEnabled()) {
				log.debug("Task> " + url);
			}

			StopWatch sw = new StopWatch();
			hres = hc.send();

			if (hres.isOK()) {
				if (log.isDebugEnabled()) {
					log.debug("Task> " + url + " : " + hres.getStatusLine() 
						+ Streams.LINE_SEPARATOR
						+ hres.getContentText());
					log.debug("Task> " + url + " : " + hres.getStatusLine() + " (" + sw + ")");
				}
				else if (log.isInfoEnabled()) {
					hres.safeDrain();
					log.info("Task> " + url + " : " + hres.getStatusLine() + " (" + sw + ")");
				}
				else {
					hres.safeDrain();
				}
				errors = 0;
			}
			else {
				errors++;
				String msg = "Failed to " + task.getMethod() + " " + url + " : " + hres.getStatusLine() 
						+ Streams.LINE_SEPARATOR
						+ hres.getHeader().toString()
						+ Streams.LINE_SEPARATOR
						+ hres.getContentText();
				if (errors < errorLimit) {
					log.warn(msg);
				}
				else {
					log.error(msg);
				}
			}
		}
		catch (Throwable e) {
			log.error("Failed to " + task.getMethod() + " " + url, e);
		}
		finally {
			Streams.safeClose(hres);
		}
	}
}
