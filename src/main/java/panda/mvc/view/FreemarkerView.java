package panda.mvc.view;

import panda.io.FileNames;
import panda.io.stream.StringBuilderWriter;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.RequestPath;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.mvc.view.sitemesh.Sitemesher;
import panda.net.http.HttpContentType;

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
public class FreemarkerView extends AbstractPathView {

	private static final String EXT = ".ftl";
	
	protected String encoding = Charsets.UTF_8;
	protected String contentType = HttpContentType.TEXT_HTML;

	public FreemarkerView(String location) {
		super(location);
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public void render(ActionContext ac) throws Exception {
		FreemarkerHelper fh = ac.getIoc().get(FreemarkerHelper.class);
		Sitemesher sm = ac.getIoc().get(Sitemesher.class);

		String path = evalPath(ac);

		// not defined
		if (Strings.isBlank(path)) {
			path = RequestPath.getRequestPath(ac.getRequest());
			path = FileNames.removeExtension(path) + EXT;
			if (!fh.hasTemplate(path)) {
				String base = ac.getAction().getClass().getName().replace('.', '/');
				path = '/' + base + '_' + ac.getMethodName() + EXT;
				if (!fh.hasTemplate(path)) {
					path = '/' + base + EXT;
				}
			}
		}
		else {
			if (!path.toLowerCase().endsWith(EXT)) {
				path += EXT;
			}
		}

		ac.getResponse().setCharacterEncoding(encoding);
		ac.getResponse().setContentType(contentType + "; charset=" + encoding);
		
		if (sm.needMesh()) {
			StringBuilderWriter sbw = new StringBuilderWriter();
			fh.execTemplate(sbw, path, ac.getResult());
			
			sm.meshup(ac.getResponse().getWriter(), sbw.toString());
		}
		else {
			fh.execTemplate(ac.getResponse().getWriter(), path, ac.getResult());
		}
	}
}


