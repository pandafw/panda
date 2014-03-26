package panda.doc.markdown.html;

import java.util.List;
import java.util.Map;

import panda.doc.markdown.Plugin;

public class YumlPlugin extends Plugin {

	public YumlPlugin() {
		super("yuml");
	}

	@Override
	public void emit(final StringBuilder out, final List<String> lines, final Map<String, String> params) {
		StringBuffer sb2 = new StringBuffer();
		for (String line : lines) {
			line = line.trim();
			if (line.length() > 0) {
				if (sb2.length() > 0) {
					sb2.append(", ");
				}
				sb2.append(line);
			}
		}
		
		String type = params.get("type");
		if (type == null) {
			type = "class";
		}
		String style = params.get("style");
		if (style == null) {
			style = "scruffy";
		}
		String dir = params.get("dir");
		if (dir != null) {
			style = style + ";dir:" + dir + ";";
		}
		String scale = params.get("scale");
		if (scale != null) {
			style = style + ";scale:" + scale + ";";
		}

		out.append("<img src=\"http://yuml.me/diagram/").append(style).append("/").append(type).append("/");
		out.append(sb2);

		String format = params.get("format");
		if (format != null) {
			out.append(".").append(format);
		}
		out.append("\"/>");
	}

}
