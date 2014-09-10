package panda.mvc.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import panda.lang.Strings;
import panda.lang.Texts;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.servlet.HttpSessionMap;
import panda.servlet.ServletRequestMap;

public abstract class AbstractPathView implements View {

	private String dest;

	public AbstractPathView(String dest) {
		this.dest = dest;
	}

	protected String evalPath(ActionContext ac, Object obj) {
		if (Strings.isEmpty(dest)) {
			return null;
		}

		Object context = createContext(ac, obj);
		
		// TODO: 生成解析后的路径
		return Texts.translate(dest, context);
	}

	/**
	 * 为一次 HTTP 请求，创建一个可以被表达式引擎接受的上下文对象
	 * 
	 * @param ac ActionContext
	 * @param obj 入口函数的返回值
	 * @return 上下文对象
	 */
	protected Object createContext(ActionContext ac, Object obj) {
		Map<String, Object> context = new HashMap<String, Object>();

		//TODO
//		Object globalContext = ac.getServletContext().getAttribute(Loading.CONTEXT_NAME);
//		if (globalContext != null) {
//			//context.putAll((Context)globalContext);
//		}

		ServletRequestMap srm = new ServletRequestMap(ac.getRequest());
		context.put(ATTR_REQ_MAP, srm);
		
		context.put(ATTR_PATHARGS, ac.getPathArgs());

		HttpSession session = ac.getRequest().getSession(false);
		if (session != null) {
			HttpSessionMap hsm = new HttpSessionMap(session);
			context.put(ATTR_SES_MAP, hsm);
		}

		context.put(ATTR_ARGUMENTS, ac.getArguments());
		
		context.put(ATTR_OBJECT, obj);

		return context;
	}
}
