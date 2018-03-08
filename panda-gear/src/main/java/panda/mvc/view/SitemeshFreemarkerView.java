package panda.mvc.view;

import panda.io.stream.StringBuilderWriter;
import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.mvc.view.sitemesh.Sitemesher;

/**
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
@IocBean(singleton=false)
public class SitemeshFreemarkerView extends FreemarkerView {
	public SitemeshFreemarkerView() {
	}
	
	public SitemeshFreemarkerView(String location) {
		setArgument(location);
	}

	@Override
	protected void render(ActionContext ac, String path, FreemarkerHelper fh) {
		try {
			Sitemesher sm = ac.getIoc().get(Sitemesher.class);
			if (sm.needMesh()) {
				StringBuilderWriter sbw = new StringBuilderWriter();
				fh.execTemplate(sbw, path, ac.getResult());
				
				sm.meshup(ac.getResponse().getWriter(), sbw.toString());
			}
			else {
				fh.execTemplate(ac.getResponse().getWriter(), path, ac.getResult());
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to render template " + path, e);
		}
	}
}


