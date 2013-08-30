package panda.bind.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.bind.AbstractSerializer;
import panda.bind.json.JsonException;
import panda.lang.StringEscapes;
import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class XmlSerializer extends AbstractSerializer {
	private String rootName = "doc";
	private String itemName = "i";
	
	private List<String> nodes = new ArrayList<String>();
	
	/** current indent */
	private int indent;
	
	public XmlSerializer() {
	}

	/**
	 * @return the rootName
	 */
	public String getRootName() {
		return rootName;
	}

	/**
	 * @param rootName the rootName to set
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	@Override
	protected void startDocument(Object src) {
		indent = 0;
		nodes.clear();
		writeBeginTag(rootName);
	}

	private void validateTagName(String tagName) {
		
	}
	
	private void writeBeginTag(String tagName) {
		validateTagName(tagName);
		nodes.add(tagName);

		try {
			writer.append('<');
			StringEscapes.escapeXml(tagName, writer);
			writer.append('>');
		}
		catch (IOException e) {
			throw new XmlException(e);
		}
	}

	private void writeEndTag() {
		String tagName = nodes.remove(nodes.size() - 1);
		try {
			writer.append("</");
			writer.append(tagName);
			writer.append('>');
		}
		catch (IOException e) {
			throw new XmlException(e);
		}
	}

	@Override
	protected void endDocument(Object src) {
		writeEndTag();
	}

	@Override
	protected void startArray(Object src) {
		indent += indentFactor;
	}

	@Override
	protected void endArray(Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
	}

	@Override
	protected void startArrayElement(Object src, int index) {
		writeIndent();
		writeBeginTag(itemName);
	}
	
	@Override
	protected void endArrayElement(Object src, int index) {
		writeEndTag();
	}

	@Override
	protected void startObject(Object src) {
		indent += indentFactor;
	}
	
	@Override
	protected void endObject(Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
	}

	@Override
	protected void startObjectProperty(String key, Object val, int index) {
		writeIndent();
		writeBeginTag(key);
	}
	
	@Override
	protected void endObjectProperty(String key, Object val, int index) {
		writeEndTag();
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
				return;
			}
			
			StringEscapes.escapeXml(str, writer);
		}
		catch (IOException e) {
			throw new JsonException(e);
		}
	}

	@Override
	protected void writeNull() {
	}
	
	@Override
	protected void writeNumber(Number num) {
		write(num.toString());
	}
	
	@Override
	protected void writeBoolean(Boolean boo) {
		write(boo.toString());
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
