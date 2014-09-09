package panda.mvc;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ioc.RequestIocContext;

/**
 * 如果你的应用，在 Request 中保存了一些需要注销的对象，比如你在 Ioc 容器中将一个 DataSource 对象的范围设成 "request"，那么请启用本的监听器，它会在一个
 * request 注销时，关闭 DataSource
 * <p>
 * 启用的方法是在 web.xml 中，添加下面的代码：
 * 
 * <pre>
 * &lt;listener&gt;
 * &lt;listerner-class&gt;panda.mvc.IocRequestListener&lt;/listerner-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 */
public final class IocRequestListener implements ServletRequestListener {

	private static final Log log = Logs.getLog(IocRequestListener.class);

	/**
	 * 如果你在 web.xml 配置了这个监听器，那么我们理解你的意图就是要自动创建 request scope <br>
	 * 在默认的 ModuleProcessor 里，会根据这个变量来决定是否自动创建request scope
	 */
	public static boolean isRequestScopeEnable = false;

	public IocRequestListener() {
		isRequestScopeEnable = true;
		log.info("Ioc RequestScope is enabled.");
	}

	public void requestInitialized(ServletRequestEvent sre) {
		
	}

	public void requestDestroyed(ServletRequestEvent sre) {
		RequestIocContext.depose(sre.getServletRequest());
	}

}
