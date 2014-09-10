package panda.mvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.FileNames;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.RequestPath;

/**
 * 内部重定向视图
 * <p/>
 * 根据传入的视图名，决定视图的路径：
 * <ul>
 * <li>如果视图名以 '/' 开头， 则被认为是一个 全路径
 * <li>否则，将视图名中的 '.' 转换成 '/'，并加入前缀 "/WEB-INF/"
 * </ul>
 * 通过注解映射的例子：
 * <ul>
 * <li>'@Ok("forward:abc.cbc")' => /WEB-INF/abc/cbc
 * <li>'@Ok("forward:/abc/cbc")' => /abc/cbc
 * <li>'@Ok("forward:/abc/cbc.jsp")' => /abc/cbc.jsp
 * </ul>
 */
public class ForwardView extends AbstractPathView {

	public ForwardView(String dest) {
		super(dest == null ? null : dest.replace('\\', '/'));
	}

	public void render(ActionContext ac, Object obj) throws Exception {
		HttpServletRequest req = ac.getRequest();

		String path = evalPath(ac, obj);
		String args = "";
		if (path != null) {
			int q = path.indexOf("?");
			if (q >= 0) { // 将参数部分分解出来
				args = path.substring(q);
				path = path.substring(0, q);
			}
		}

		String ext = getExt();
		
		// 空路径，采用默认规则
		if (Strings.isBlank(path)) {
			path = RequestPath.getRequestPath(req);
			path = "/WEB-INF" + (path.startsWith("/") ? "" : "/") + FileNames.removeExtension(path) +  ext;
		}
		// 绝对路径 : 以 '/' 开头的路径不增加 '/WEB-INF'
		else if (path.charAt(0) == '/') {
			if (!path.toLowerCase().endsWith(ext)) {
				path += ext;
			}
		}
		else {
			if (!path.toLowerCase().endsWith(ext)) {
				path += ext;
			}
			path = "/WEB-INF/" + path;
		}

		// 执行 Forward
		forward(ac, path, args);
	}

	protected void forward(ActionContext ac, String path, String args) throws Exception {
		HttpServletRequest req = ac.getRequest();
		HttpServletResponse res = ac.getResponse();

		path = path + args;
		RequestDispatcher rd = req.getRequestDispatcher(path);
		if (rd == null) {
			throw Exceptions.makeThrow("Fail to find Forward '%s'", path);
		}
		
		// Do rendering
		rd.forward(req, res);
	}
	
	/**
	 * 子类可以覆盖这个方法，给出自己特殊的后缀,必须小写哦
	 * 
	 * @return 后缀
	 */
	protected String getExt() {
		return "";
	}

}
