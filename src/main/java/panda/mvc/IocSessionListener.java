package panda.mvc;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ioc.SessionIocContext;

/**
 * 如果你的应用，在 Session 中保存了一些需要注销的对象，比如你在 Ioc 容器中将一个 DataSource 对象的范围设成 "session"，那么请启用本的监听器，它会在一个
 * session 注销时，关闭 DataSource
 * <p>
 * 启用的方法是在 web.xml 中，添加下面的代码：
 * 
 * <pre>
 * &lt;listener&gt;
 * &lt;listerner-class&gt;panda.mvc.IocSessionListener&lt;/listerner-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 */
public final class IocSessionListener implements HttpSessionListener {

	private static final Log log = Logs.getLog(IocSessionListener.class);

	/**
	 * 如果你在 web.xml 配置了这个监听器，那么我们理解你的意图就是要自动创建 session <br>
	 * 否则你就不需要自动创建 session。<br>
	 * 在默认的 ModuleProcessor 里，会根据这个变量来决定是否自动创建session的
	 */
	public static boolean isSessionScopeEnable = false;

	public IocSessionListener() {
		isSessionScopeEnable = true;
		log.info("Ioc SessionScope is enabled.");
	}

	public void sessionCreated(HttpSessionEvent se) {
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		SessionIocContext.depose(se.getSession());
	}

}
