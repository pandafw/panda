package panda.mvc.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.RequestPath;
import panda.mvc.UrlMapping;
import panda.net.http.HttpMethod;

@IocBean(type=UrlMapping.class)
public class DefaultUrlMapping implements UrlMapping {
	private static final Log log = Logs.getLog(DefaultUrlMapping.class);

	private MappingNode<ActionInvoker> root;
	private Map<String, ActionInvoker> map;
	
	public DefaultUrlMapping() {
		root = new MappingNode<ActionInvoker>();
		map = new HashMap<String, ActionInvoker>();
	}

	public void add(ActionChainMaker maker, ActionInfo ai, MvcConfig config) {
		// 检查所有的path
		String[] paths = ai.getPaths();
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (Strings.isBlank(path)) {
				throw new IllegalArgumentException(String.format("Can not support blank @At in %s.%s", ai.getActionType().getName(), ai.getMethod().getName()));
			}
			
			if (path.charAt(0) != '/') {
				paths[i] = '/' + path;
			}
		}

		ActionChain chain = maker.eval(config, ai);
		for (String path : ai.getPaths()) {
			// 尝试获取，看看有没有创建过这个 URL 调用者
			ActionInvoker invoker = map.get(path);

			// 如果没有增加过这个 URL 的调用者，为其创建备忘记录，并加入索引
			if (invoker == null) {
				invoker = new ActionInvoker();
				map.put(path, invoker);
				root.add(path, invoker);
			}

			// 将动作链，根据特殊的 HTTP 方法，保存到调用者内部
			if (ai.hasHttpMethod()) {
				for (HttpMethod httpMethod : ai.getHttpMethods()) {
					invoker.addChain(httpMethod, chain);
				}
			}
			// 否则，将其设置为默认动作链
			else {
				invoker.setDefaultChain(chain);
			}
		}

		printActionMapping(ai);
	}

	public ActionInvoker get(ActionContext ac) {
		String path = RequestPath.getRequestPath(ac.getRequest());
		ActionInvoker invoker = root.get(ac, path);
		if (invoker != null) {
			ActionChain chain = invoker.getActionChain(ac);
			if (chain != null) {
				if (log.isDebugEnabled()) {
					log.debugf("Found mapping for [%s] path=%s : %s", ac.getRequest().getMethod(), path, chain);
				}
				return invoker;
			}
		}
		if (log.isDebugEnabled()) {
			log.debugf("Search mapping for path=%s : No action match", path);
		}
		return null;
	}

	protected void printActionMapping(ActionInfo ai) {
		/*
		 * 打印基本调试信息
		 */
		if (log.isDebugEnabled()) {
			// 打印路径
			String[] paths = ai.getPaths();
			StringBuilder sb = new StringBuilder();
			if (null != paths && paths.length > 0) {
				sb.append("   '").append(paths[0]).append("'");
				for (String p : paths) {
					sb.append(", '").append(p).append("'");
				}
			}
			else {
				throw Exceptions.impossible();
			}

			// 打印方法名
			Method method = ai.getMethod();
			String str;
			if (null != method) {
				str = String.format("%-30s : %-10s", method.toString(), method.getReturnType().getSimpleName());
			}
			else {
				throw Exceptions.impossible();
			}

			log.debugf("%s >> %s | @Ok(%-5s) @Err(%-5s) @Fail(%-5s)",
				Strings.rightPad(sb, 30), str, ai.getOkView(), ai.getErrorView(), ai.getFatalView());
		}
	}
}
