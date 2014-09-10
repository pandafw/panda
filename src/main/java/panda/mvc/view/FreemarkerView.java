package panda.mvc.view;

import panda.mvc.ActionContext;

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
 * <li>'@Ok("ftl:abc.cbc")' => /WEB-INF/abc/cbc.ftl
 * <li>'@Ok("ftl:/abc/cbc")' => /abc/cbc.ftl
 * <li>'@Ok("ftl:/abc/cbc.ftl")' => /abc/cbc.ftl
 * </ul>
 */
public class FreemarkerView extends ForwardView {

	public FreemarkerView(String dest) {
		super(dest);
	}

	protected void forward(ActionContext ac, String path, String args) throws Exception {
		//TODO
	}

	@Override
	protected String getExt() {
		return "";
	}

}
