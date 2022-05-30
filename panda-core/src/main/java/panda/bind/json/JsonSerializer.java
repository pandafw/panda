package panda.bind.json;

import java.io.IOException;

import panda.bind.AbstractSerializer;
import panda.lang.StringEscapes;
import panda.lang.Strings;

/**
 * 
 *
 */
public class JsonSerializer extends AbstractSerializer {
	/** current indent */
	private int indent;
	
	public JsonSerializer() {
	}

	@Override
	protected void startDocument(Object src) {
		indent = 0;
	}

	@Override
	protected void endDocument(Object src) {
	}

	@Override
	protected void startArray(String name, Object src) {
		write('[');
		indent += indentFactor;
	}

	@Override
	protected void endArray(String name, Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
		write(']');
	}

	@Override
	protected void startArrayElement(String name, Object src, int index) {
		if (index > 0) {
			writeSeparator(',');
		}
		writeIndent();
	}
	
	@Override
	protected void endArrayElement(String name, Object src, int index) {
	}

	@Override
	protected void startObject(String name, Object src) {
		write('{');
		indent += indentFactor;
	}
	
	@Override
	protected void endObject(String name, Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
		write('}');
	}

	@Override
	protected void startObjectProperty(String key, Object val, int index) {
		if (index > 0) {
			writeSeparator(',');
		}
		writeIndent();
		writeString(key);
		writeSeparator(':');
	}
	
	@Override
	protected void endObjectProperty(String key, Object val, int index) {
	}
	
	private void writeSeparator(char sep) {
		write(sep);
		if (indentFactor > 0) {
			write(' ');
		}
	}

	private void writeIndent() {
		try {
			writeIndent(indent);
		}
		catch (IOException e) {
			throw new JsonException(e);
		}
	}
	
	@Override
	protected void writeString(String str) {
		try {
			if (Strings.isEmpty(str)) {
				writer.append('"');
				writer.append('"');
				return;
			}
			
			writer.append('"');
			StringEscapes.escapeJSON(str, writer);
			writer.append('"');
		}
		catch (IOException e) {
			throw new JsonException(e);
		}
	}

	@Override
	protected void writeNull() {
		write("null");
	}
	
	@Override
	protected void writeNumber(Number num) {
		write(num.toString());
	}
	
	@Override
	protected void writeBoolean(Boolean boo) {
		write(boo.toString());
	}
	
	private void write(char ch) {
		try {
			writer.append(ch);
		}
		catch (IOException e) {
			throw new JsonException(e);
		}
	}
	
	private void write(String str) {
		try {
			writer.append(str);
		}
		catch (IOException e) {
			throw new JsonException(e);
		}
	}
}
