package panda.doc.markdown.html;

import java.io.IOException;
import java.util.List;

import panda.doc.markdown.BlockEmitter;
import panda.lang.Exceptions;
import panda.lang.StringEscapes;

public class CodeBlockEmitter implements BlockEmitter {
	@Override
	public void emitBlock(StringBuilder out, List<String> lines, String meta) {
		try {
			out.append("<pre><code");
			if (meta.length() > 0) {
				out.append(" class=\"");
				StringEscapes.escapeHTML(meta, out);
				out.append('\"');
			}
			out.append('>');
	
			for (final String s : lines) {
				StringEscapes.escapeHTML(s, out);
				out.append('\n');
			}
			out.append("</code></pre>\n");
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

}
