package panda.bind.json;

import java.io.IOException;

import panda.bind.AbstractSerializer;
import panda.lang.Chars;
import panda.lang.StringEscapes;
import panda.lang.Strings;

/**
 * 
 *
 */
public class JsonSerializer extends AbstractSerializer {
	private final static char OBJECT_START_CHAR = '{';
	private final static char OBJECT_END_CHAR = '}';
	private final static char FIELD_NAME_VALUE_SEPARATOR = ':';
	private final static char FIELD_SEPARATOR = ',';
	private final static char ARRAY_START_CHAR = '[';
	private final static char ARRAY_END_CHAR = ']';
	private final static char STRING_QUOTE_CHAR = '"';

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
		write(ARRAY_START_CHAR);
		indent += indentFactor;
	}

	@Override
	protected void endArray(String name, Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
		write(ARRAY_END_CHAR);
	}

	@Override
	protected void startArrayElement(String name, Object src, int index) {
		if (index > 0) {
			writeSeparator(FIELD_SEPARATOR);
		}
		writeIndent();
	}
	
	@Override
	protected void endArrayElement(String name, Object src, int index) {
	}

	@Override
	protected void startObject(String name, Object src) {
		write(OBJECT_START_CHAR);
		indent += indentFactor;
	}
	
	@Override
	protected void endObject(String name, Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
		write(OBJECT_END_CHAR);
	}

	@Override
	protected void startObjectProperty(String key, Object val, int index) {
		if (index > 0) {
			writeSeparator(FIELD_SEPARATOR);
		}
		writeIndent();
		writeString(key);
		writeSeparator(FIELD_NAME_VALUE_SEPARATOR);
	}
	
	@Override
	protected void endObjectProperty(String key, Object val, int index) {
	}
	
	private void writeSeparator(char sep) {
		write(sep);
		if (indentFactor > 0) {
			write(Chars.SPACE);
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
				writer.append(STRING_QUOTE_CHAR);
				writer.append(STRING_QUOTE_CHAR);
				return;
			}
			
			writer.append(STRING_QUOTE_CHAR);
			StringEscapes.escapeJson(str, writer);
			writer.append(STRING_QUOTE_CHAR);
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
