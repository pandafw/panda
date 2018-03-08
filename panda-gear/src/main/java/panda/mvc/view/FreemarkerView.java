package panda.mvc.view;

import panda.io.FileNames;
import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.ftl.FreemarkerHelper;

/**
 * <ul>
 * <li>'@Ok("ftl")' => /x/y/class_method.ftl -> /x/y/class.ftl -> /x/y/super_class_method.ftl -> /x/y/super_class.ftl
 * <li>'@Ok("ftl:~input")' => find template for input() method
 * <li>'@Ok("ftl:/abc/cbc")' => /abc/cbc.ftl
 * <li>'@Ok("ftl:/abc/cbc.ftl")' => /abc/cbc.ftl
 * </ul>
 */
@IocBean(singleton=false)
public class FreemarkerView extends AbstractView {
	public FreemarkerView() {
	}
	
	public FreemarkerView(String location) {
		setArgument(location);
	}
	
	/**
	 * alternative method
	 */
	public static final char ALT_PREFIX = '~';

	/**
	 * extension
	 */
	private static final String EXT = ".ftl";
	
	protected String encoding = Charsets.UTF_8;
	protected String contentType = MimeTypes.TEXT_HTML;

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

		String path = argument;

		// not defined
		if (Strings.isEmpty(path)) {
			path = ac.getPath();
			path = FileNames.removeExtension(path) + EXT;
			if (!fh.hasTemplate(path)) {
				Class<?> action = ac.getAction().getClass();
				path = findTemplate(fh, path, action, ac.getMethodName());
			}
		}
		else if (path.charAt(0) == ALT_PREFIX) {
			String method = path.substring(1);

			path = ac.getPath();
			path = FileNames.removeExtension(path) + ALT_PREFIX + method + EXT;
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


