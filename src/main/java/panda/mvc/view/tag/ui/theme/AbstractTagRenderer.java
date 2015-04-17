package panda.mvc.view.tag.ui.theme;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.ui.CheckboxList;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.UIBean;
import panda.mvc.view.tag.ui.ViewField;

public abstract class AbstractTagRenderer<T extends UIBean> implements TagRenderer {
	protected ActionContext context;
	protected T tag;
	protected Writer writer;

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
		return button(text, icon, null, (Map<String, String>)null);
	}

	protected String button(String text, String icon, String sicon, String onclick) {
		Map<String, String> attrs = null;
		if (onclick != null) {
			attrs = new HashMap<String, String>();
			attrs.put("onclick", onclick);
		}
		return button(text, icon, sicon, attrs);
	}
	
	protected String button(String text, String icon, String sicon, Map<String, String> attrs) {
		String cssClass = null;
		if (attrs != null) {
			cssClass = attrs.remove("cssClass");
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
		if (i.startsWith("icon-")) {
			String[] ss = Strings.split(i);
	
			i = getText(ss[0], ss[0].substring(5));
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
		else if (i.startsWith("fa ")) {
			return i;
		}
		else if (i.startsWith("fa-")) {
			i = "fa " + i;
		}
		else {
			i = "fa fa-" + i;
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
		write("<script type=\"text/javascript\">");
		write(jsc);
		write("</script>\n");
	}
	protected void writeJs(String js) throws IOException {
		write("<script src=\"");
		write(js);
		write("\" type=\"text/javascript\"></script>\n");
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

	protected <N> N newTag(Class<N> type) {
		return context.getIoc().get(type);
	}

	protected void writeTextField(String cssClass, String name, String id, Object value) throws IOException {
		TextField tf = newTag(TextField.class);

		tf.setCssClass(cssClass);
		tf.setName(name);
		tf.setId(id);
		tf.setValue(value);
		
		tf.start(writer);
		tf.end(writer, "");
	}

	protected void writeSelect(String cssClass, String name, String id, Object list, String value) throws IOException {
		writeSelect(cssClass, name, id, list, value, null);
	}

	protected void writeSelect(String cssClass,
			String name, String id, Object list, String value,
			Boolean emptyOption) throws IOException {
		writeSelect(cssClass, name, id, list, value, emptyOption, null);
	}
	
	protected void writeSelect(String cssClass,
			String name, String id, Object list, String value,
			boolean emptyOption, String onchange) throws IOException {
		
		Select select = newTag(Select.class);

		select.setCssClass(cssClass);
		select.setName(name);
		select.setId(id);
		select.setList(list);
		select.setValue(value);
		select.setEmptyOption(emptyOption);
		select.setOnchange(onchange);

		select.start(writer);
		select.end(writer, "");
	}
	
	protected void writeCheckboxList(String cssClass, String name, String id, Object list, List<String> value)
			throws IOException {
		CheckboxList cl = newTag(CheckboxList.class);

		cl.setCssClass(cssClass);
		cl.setName(name);
		cl.setId(id);
		cl.setList(list);
		cl.setValue(value);

		cl.start(writer);
		cl.end(writer, "");
	}
	
	protected void writeRadio(String cssClass, String name, String id, Object list, String value) throws IOException {
		Radio r = newTag(Radio.class);

		r.setCssClass(cssClass);
		r.setName(name);
		r.setId(id);
		r.setList(list);
		r.setValue(value);

		r.start(writer);
		r.end(writer, "");
	}
	
	protected void writeDatePicker(String cssClass, String name, String id, String format, Date value)
			throws IOException {
		writeDatePicker(cssClass, name, id, format, value, null);
	}
	
	protected void writeDatePicker(String cssClass,
			String name, String id, String format,
			Date value, String options) throws IOException {

		DatePicker dp = newTag(DatePicker.class);

		dp.setCssClass(cssClass);
		dp.setName(name);
		dp.setId(id);
		dp.setFormat(format);
		dp.setValue(value);
		dp.setOptions(options);
		
		dp.start(writer);
		dp.end(writer, "");
	}
	
	protected void writeDateTimePicker(String cssClass, String name, String id, String format, Date value)
			throws IOException {

		DateTimePicker dp = newTag(DateTimePicker.class);

		dp.setCssClass(cssClass);
		dp.setName(name);
		dp.setId(id);
		dp.setFormat(format);
		dp.setValue(value);
		
		dp.start(writer);
		dp.end(writer, "");
	}
	
	protected void writeTimePicker(String cssClass, String name, String id, String format, Date value)
			throws IOException {
		
		TimePicker dp = newTag(TimePicker.class);

		dp.setCssClass(cssClass);
		dp.setName(name);
		dp.setId(id);
		dp.setFormat(format);
		dp.setValue(value);
		
		dp.start(writer);
		dp.end(writer, "");
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
		return context.text(key);
	}
	
	protected String getText(String key, String defaultValue) {
		return context.text(key, defaultValue);
	}
	
	protected void putInContext(String key, Object value) {
		context.getVars().put(key, value);
	}
	
	protected Object findInContext(String key) {
		return context.getVars().get(key);
	}

	public String formatValue(TextField tf) {
		return formatValue(tf, null);
	}
	
	public String formatValue(TextField tf, String escape) {
		Object value = tf.getValue();
		if (value != null) {
			String format = tf.getFormat();
			Property p = newTag(Property.class);
			p.setValue(value);
			p.setFormat(format);
			if (escape != null) {
				p.setEscape(escape);
			}
			return p.formatValue();
		}
		return "";
	}
	
	public String formatValue(ViewField tf) {
		return formatValue(tf, null);
	}
	
	public String formatValue(ViewField tf, String escape) {
		Object value = tf.getValue();
		if (value != null) {
			String format = tf.getFormat();
			Property p = newTag(Property.class);
			p.setValue(value);
			p.setFormat(format);
			if (escape != null) {
				p.setEscape(escape);
			}
			return p.formatValue();
		}
		return "";
	}

	public String escapeValue(Object value) {
		return escapeValue(value, null);
	}
	
	public String escapeValue(Object value, String escape) {
		if (value != null) {
			Property p = newTag(Property.class);
			p.setValue(value);
			if (escape != null) {
				p.setEscape(escape);
			}
			return p.formatValue();
		}
		return "";
	}
}
