package panda.mvc.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionContext;
import panda.net.http.HttpMethod;

/**
 * 根据 HTTP 请求的方法 (GET|POST|PUT|DELETE) 来调用响应的动作链
 */
public class ActionInvoker {

	private static final Log log = Logs.get();

	private ActionChain defaultChain;

	private Map<HttpMethod, ActionChain> chainMap;

	public ActionInvoker() {
		chainMap = new HashMap<HttpMethod, ActionChain>();
	}

	/**
	 * 增加 ActionChain
	 * 
	 * @param method HTTP 的请求方法 (GET|POST|PUT|DELETE),如果为空，则会抛错
	 * @param chain 动作链
	 */
	public void addChain(HttpMethod method, ActionChain chain) {
		chainMap.put(method, chain);
	}

	public void setDefaultChain(ActionChain defaultChain) {
		this.defaultChain = defaultChain;
	}

	/**
	 * 根据动作链上下文对象，调用一个相应的动作链
	 * 
	 * @param ac 动作链上下文
	 * @return true- 成功的找到一个动作链并执行。 false- 没有找到动作链
	 */
	public boolean invoke(ActionContext ac) {
		ActionChain chain = getActionChain(ac);
		if (chain == null) {
			if (log.isDebugEnabled()) {
				log.debugf("Not chain for req (path=%s, method=%s)", ac.getPath(), ac.getRequest().getMethod());
			}
			return false;
		}
		chain.doChain(ac);
		return true;
	}

	public ActionChain getActionChain(ActionContext ac) {
		HttpServletRequest req = ac.getRequest();
		HttpMethod hm = HttpMethod.parse(req.getMethod());
		ActionChain chain = chainMap.get(hm);
		if (null != chain) {
			return chain;
		}
		
		if (null != defaultChain) {
			return defaultChain;
		}
		
		return null;
	}

}
