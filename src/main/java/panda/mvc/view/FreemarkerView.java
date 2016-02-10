package panda.mvc.view;

import panda.io.FileNames;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.ftl.FreemarkerHelper;
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
	public static final FreemarkerView DEFAULT = new FreemarkerView(null);
	public static final FreemarkerView INPUT = new FreemarkerView("~input");

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

	protected String findTemplate(FreemarkerHelper fh, String path, Class<?> action, String method) {
		StringBuilder sb = new StringBuilder();
		while (true) {
			if (action == null) {
				throw new RuntimeException("Template \"" + path + "\" not found." + sb);
			}

			String base = Strings.replaceChars(action.getName(), '.', '/');
			String tpl = '/' + base + '_' + method + EXT;
			if (fh.hasTemplate(tpl)) {
				return tpl;
			}
			sb.append('\n').append(tpl);

			tpl = '/' + base + EXT;
			if (fh.hasTemplate(tpl)) {
				return tpl;
			}
			sb.append('\n').append(tpl);

			action = action.getSuperclass();
		}
	}

	@Override
	public void render(ActionContext ac) {
		FreemarkerHelper fh = ac.getIoc().get(FreemarkerHelper.class);

		String path = evalPath(ac);

		// not defined
		if (Strings.isEmpty(path)) {
			path = ac.getPath();
			path = FileNames.removeExtension(path) + EXT;
			if (!fh.hasTemplate(path)) {
				Class<?> action = ac.getAction().getClass();
				path = findTemplate(fh, path, action, ac.getMethodName());
			}
		}
		else if (path.charAt(0) == '~') {
			String method = path.substring(1);

			path = ac.getPath();
			path = FileNames.removeExtension(path) + '~' + method + EXT;
			if (!fh.hasTemplate(path)) {
				Class<?> action = ac.getAction().getClass();
				path = findTemplate(fh, path, action, method);
			}
		}
		else {
			if (!path.toLowerCase().endsWith(EXT)) {
				path += EXT;
			}
		}

		ac.getResponse().setCharacterEncoding(encoding);
		ac.getResponse().setContentType(contentType + "; charset=" + encoding);
		
		render(ac, path, fh);
	}
	
	protected void render(ActionContext ac, String path, FreemarkerHelper fh) {
		try {
			fh.execTemplate(ac.getResponse().getWriter(), path);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}


