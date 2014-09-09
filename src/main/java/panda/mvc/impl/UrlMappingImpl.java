package panda.mvc.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
import panda.mvc.config.AtMap;
import panda.net.http.HttpMethod;

public class UrlMappingImpl implements UrlMapping {

	private static final Log log = Logs.getLog(UrlMappingImpl.class);

	private MappingNode<ActionInvoker> root;

	public UrlMappingImpl() {
		this.root = new MappingNode<ActionInvoker>();
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

		Map<String, ActionInvoker> map = new HashMap<String, ActionInvoker>();
		AtMap atmap = config.getAtMap();
		
		ActionChain chain = maker.eval(config, ai);
		for (String path : ai.getPaths()) {
			// 尝试获取，看看有没有创建过这个 URL 调用者
			ActionInvoker invoker = map.get(path);

			// 如果没有增加过这个 URL 的调用者，为其创建备忘记录，并加入索引
			if (invoker == null) {
				invoker = new ActionInvoker();
				map.put(path, invoker);
				root.add(path, invoker);
				// 记录一下方法与 url 的映射
				atmap.addMethod(path, ai.getMethod());
			}

			// 将动作链，根据特殊的 HTTP 方法，保存到调用者内部
			if (ai.isForSpecialHttpMethod()) {
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

		// TODO 下面个IF要不要转换到NutLoading中去呢?
		// 记录一个 @At.key
		if (Strings.isNotBlank(ai.getPathKey())) {
			atmap.add(ai.getPathKey(), ai.getPaths()[0]);
		}
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
				for (int i = 1; i < paths.length; i++)
					sb.append(", '").append(paths[i]).append("'");
			}
			else {
				throw Exceptions.impossible();
			}
			// 打印方法名
			Method method = ai.getMethod();
			String str;
			if (null != method)
				str = String.format("%-30s : %-10s", method.toString(), method.getReturnType()
					.getSimpleName());
			else
				throw Exceptions.impossible();

			log.debugf("%s >> %s | @Ok(%-5s) @Fail(%-5s) | (I:%s/O:%s)",
				Strings.rightPad(sb, 30), str, ai.getOkView(), ai.getFailView(),
				ai.getInputEncoding(), ai.getOutputEncoding());
		}
	}
}
