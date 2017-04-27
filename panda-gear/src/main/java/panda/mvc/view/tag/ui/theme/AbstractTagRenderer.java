package panda.mvc.view.tag.ui.theme;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.io.MimeType;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.tag.Escapes;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.ui.UIBean;

public abstract class AbstractTagRenderer<T extends UIBean> implements TagRenderer {
	protected ActionContext context;
	protected T tag;
	protected Writer writer;

	private Property property;

	@SuppressWarnings("unchecked")
	public AbstractTagRenderer(RenderingContext rc) {
		this.context = rc.getActionContext();
		this.tag = (T)rc.getTag();
		this.writer = rc.getWriter();
	}

	protected void addCss(String css) {
		if (Strings.isEmpty(css)) {
			return;
		}
		
		String cssClass = tag.getCssClass();
		if (Strings.isEmpty(cssClass)) {
			tag.setCssClass(css);
		}
		else {
			tag.setCssClass(cssClass + ' ' + css);
		}
	}

//	private String base;
//	protected String base() {
//		if (base == null) {
//			base = context.getBase();
//			if ("/".equals(base)) {
//				base = "";
//			}
//		}
//		return base;
//	}
//	protected String uri(String uri) {
//		return base() + uri;
//	}

	protected String button(String text, String icon) {
		return button(text, icon, null, null);
	}

	protected String button(String text, String icon, String cssClass) {
		Map<String, String> attrs = new HashMap<String, String>();
		if (cssClass != null) {
			attrs.put("class", cssClass);
		}
		return button(text, icon, null, attrs);
	}
	
	protected String button(String text, String icon, String sicon, Map<String, String> attrs) {
		String cssClass = null;
		if (attrs != null) {
			cssClass = attrs.remove("class");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<button class=\"btn btn-default")
		  .append(cssClass == null ? "" : " " + cssClass)
		  .append("\" type=\"submit\"");
		
		if (attrs != null) {
			for (Entry<String, String> en : attrs.entrySet()) {
				sb.append(" ")
				  .append(en.getKey())
				  .append("=\"")
				  .append(html(en.getValue()))
				  .append("\"");
			}
		}
		sb.append('>');

		if (icon != null) {
			sb.append(xicon(icon));
			sb.append(Strings.SPACE);
		}
		sb.append(text);

		if (sicon != null) {
			sb.append(Strings.SPACE);
			sb.append(xicon(icon));
		}
		sb.append("</button>");
		
		return sb.toString();
	}
	
	protected String ticon(String i) {
		if (i.startsWith("fa ")) {
			return i;
		}
		else if (i.startsWith("fa-")) {
			i = "fa " + i;
		}
		else {
			String[] ss = Strings.split(i);

			if ("icon".equals(ss[0])) {
				i = getText(ss[0], ss[0]);
			}
			else {
				if (ss[0].startsWith("icon-")) {
					ss[0] = ss[0].substring(5);
				}
				i = getText("icon-" + ss[0], ss[0]);
			}
			
			if (i.startsWith("fa ")) {
				ss[0] = i;
			}
			else if (i.startsWith("fa-")) {
				ss[0] = "fa " + i;
			}
			else {
				ss[0] = "fa fa-" + i;
			}
			
			i = Strings.join(ss, " ");
		}
		return i;
	}
	
	protected String xicon(String c) {
		return icon(ticon(c));
	}

	protected String icon(String c) {
		return "<i class=\"" + c + "\"></i>";
	}

	protected String icon(String c, String onclick) {
		return "<i class=\"" + c + " p-cpointer\" onclick=\"" + onclick + "\"></i>";
	}

	protected String defs(Object s) {
		return s == null ? "" : s.toString();
	}

	protected String join(String ... ss) {
		StringBuilder sb = new StringBuilder();
		for (String s : ss) {
			if (Strings.isNotEmpty(s)) {
				if (sb.length() > 0) {
					sb.append(' ');
				}
				sb.append(s);
			}
		}
		return sb.toString();
	}

	protected int defi(Object i) {
		return i instanceof Number ? ((Number)i).intValue() : 0;
	}

	protected String defs(String s) {
		return Strings.defaultString((String)s);
	}

	protected String defs(String val, String def) {
		return Strings.defaultIfEmpty((String)val, def);
	}

	protected String jsstr(Object s) {
		return StringEscapes.escapeJavaScript((String)s);
	}

	protected String jsstr(String s) {
		return StringEscapes.escapeJavaScript(s);
	}

	protected String html(Object s) {
		return StringEscapes.escapeHtml((String)s);
	}

	protected String html(String s) {
		return StringEscapes.escapeHtml(s);
	}

	protected String phtml(String s) {
		return StringEscapes.escapePhtml(s);
	}

	protected void writeJsc(String jsc) throws IOException {
		write("<script type=\"");
		write(MimeType.TEXT_JAVASCRIPT);
		write("\">");
		write(jsc);
		write("</script>\n");
	}
	protected void writeJs(String js) throws IOException {
		write("<script src=\"");
		write(js);
		write("\" type=\"");
		write(MimeType.TEXT_JAVASCRIPT);
		write("\"></script>\n");
	}

	protected void writeCss(String css) throws IOException {
		writeCss(css, null);
	}
	
	protected void writeCss(String css, String cls) throws IOException {
		write("<link");
		if (cls != null) {
			write(" class=\"");
			write(cls);
			write("\"");
		}
		write(" href=\"");
		write(css);
		write("\" rel=\"stylesheet\" type=\"text/css\"/>\n");
	}

	protected void writeln() throws IOException {
		writer.write('\n');
	}
	
	protected void write(String s) throws IOException {
		if (Strings.isNotEmpty(s)) {
			writer.write(s);
		}
	}
	
	protected void write(char c) throws IOException {
		writer.write(c);
	}
	
	protected void writeIfExists(String s, Object v) throws IOException {
		if (v != null) {
			write(s);
		}
	}
	
	protected void body(String text) throws IOException {
		body(text, true);
	}

	protected void body(String text, boolean encode) throws IOException {
		write(encode ? html(text) : text);
	}

	protected void xtag(String name) throws IOException {
		stag(name, null, true);
	}

	protected void xtag(String name, Attributes attrs) throws IOException {
		stag(name, attrs, true);
	}

	protected void stag(String name) throws IOException {
		stag(name, null, false);
	}
	
	protected void stag(String name, Attributes attrs) throws IOException {
		stag(name, attrs, false);
	}
	
	protected void stag(String name, Attributes attrs, boolean end) throws IOException {
		write("<");
		write(name);
		if (attrs != null) {
			for (Entry<String, String> en : attrs.entrySet()) {
				write(" ");
				write(en.getKey());
				write("=\"");
				write(en.getValue());
				write("\"");
			}
		}

		if (end) {
			write("/");
		}
		write(">");
	}

	public void etag(String name) throws IOException {
		write("</");
		write(name);
		write(">");
	}

	protected String getText(String key) {
		return context.getText().getText(key);
	}
	
	protected String getText(String key, String defaultValue) {
		return context.getText().getText(key, defaultValue);
	}
	
	protected void putInContext(String key, Object value) {
		context.getVars().put(key, value);
	}
	
	protected Object findInContext(String key) {
		return context.getVars().get(key);
	}

	protected <N> N newTag(Class<N> type) {
		return context.getIoc().get(type);
	}

	/**
	 * format and escape value
	 */
	public String formatValue(Object v) {
		return formatValue(v, null, null);
	}

	/**
	 * format and escape value
	 */
	public String formatValue(Object v, String format) {
		return formatValue(v, format, null);
	}
	
	/**
	 * format and escape value
	 */
	public String formatValue(Object value, String format, String escape) {
		return formatValue(value, format, null, escape);
	}
	
	/**
	 * format and escape value
	 */
	public String formatValue(Object value, String format, String pattern, String escape) {
		if (value != null) {
			if (property == null) {
				property = newTag(Property.class);
			}
			property.setValue(value);
			property.setFormat(format);
			property.setPattern(pattern);
			property.setEscape(Strings.defaultString(escape, Escapes.ESCAPE_HTML));
			return property.formatValue();
		}
		return "";
	}
}
