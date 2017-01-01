package panda.bind.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.bind.AbstractSerializer;
import panda.lang.StringEscapes;
import panda.lang.Strings;

/**
 * 
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
	
	private void writeBeginTag(String tag) {
		validateTagName(tag);
		nodes.add(tag);

		try {
			writer.append('<');
			writer.append(tag);
			writer.append('>');
		}
		catch (IOException e) {
			throw new XmlException(e);
		}
	}

	private void writeEndTag() {
		String tag = nodes.remove(nodes.size() - 1);
		try {
			writer.append("</");
			writer.append(tag);
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
	protected void startArray(String name, Object src) {
		indent += indentFactor;
	}

	@Override
	protected void endArray(String name, Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
	}

	@Override
	protected void startArrayElement(String name, Object src, int index) {
		writeIndent();
		writeBeginTag(itemName);
	}
	
	@Override
	protected void endArrayElement(String name, Object src, int index) {
		writeEndTag();
	}

	@Override
	protected void startObject(String name, Object src) {
		indent += indentFactor;
	}
	
	@Override
	protected void endObject(String name, Object src, int len) {
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
			throw new XmlException(e);
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
			throw new XmlException(e);
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
			throw new XmlException(e);
		}
	}
}
