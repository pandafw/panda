package panda.bind.xmlrpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import panda.bind.AbstractSerializer;
import panda.bind.json.JsonException;
import panda.lang.StringEscapes;
import panda.lang.codec.binary.Base64;
import panda.lang.time.DateTimes;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class XmlRpcSerializer extends AbstractSerializer {
	private String rootName = XmlRpcTags.T_METHOD_CALL;
	
	private List<String> nodes = new ArrayList<String>();
	
	/** current indent */
	private int indent;
	
	public XmlRpcSerializer() {
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

	public void setMethodCall(boolean isMethodCall) {
		rootName = isMethodCall ? XmlRpcTags.T_METHOD_CALL : XmlRpcTags.T_METHOD_RESPONSE;
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
			throw new XmlRpcException(e);
		}
	}

	private String writeEndTag() {
		String tag = nodes.remove(nodes.size() - 1);
		try {
			writer.append("</");
			writer.append(tag);
			writer.append('>');
			return tag;
		}
		catch (IOException e) {
			throw new XmlRpcException(e);
		}
	}

	@Override
	protected void endDocument(Object src) {
		indent = 0;
		writeIndent();
		writeEndTag();
	}

	@Override
	protected void startArray(String name, Object src) {
		if (nodes.size() > 2) {
			indent += indentFactor;
			writeIndent();
			writeBeginTag(XmlRpcTags.T_ARRAY);
			indent += indentFactor;
			writeIndent();
			writeBeginTag(XmlRpcTags.T_DATA);
		}
		indent += indentFactor;
	}

	@Override
	protected void endArray(String name, Object src, int len) {
		indent -= indentFactor;
		if (len > 0) {
			writeIndent();
		}
		
		if (nodes.size() > 2) {
			String tag = writeEndTag();
			if (XmlRpcTags.T_DATA.equals(tag)) {
				indent -= indentFactor;
				writeIndent();
				writeEndTag();
				indent -= indentFactor;
				writeIndent();
			}
		}
	}

	@Override
	protected void startArrayElement(String name, Object src, int index) {
		writeIndent();
		if (nodes.size() == 2) { 
			// param
			writeBeginTag(XmlRpcTags.T_PARAM);
			indent += indentFactor;
			writeIndent();
		}
		writeBeginTag(XmlRpcTags.T_VALUE);
	}
	
	@Override
	protected void endArrayElement(String name, Object src, int index) {
		writeEndTag();
		if (nodes.size() == 3) {
			// param
			indent -= indentFactor;
			writeIndent();
			writeEndTag();
		}
	}

	@Override
	protected void startObject(String name, Object src) {
		if (nodes.size() > 1) {
			indent += indentFactor;
			writeIndent();
			writeBeginTag(XmlRpcTags.T_STRUCT);
		}
	}
	
	@Override
	protected void endObject(String name, Object src, int len) {
		if (nodes.size() > 1) {
			if (len > 0) {
				writeIndent();
			}
			indent -= indentFactor;
			writeEndTag();
			writeIndent();
		}
	}

	@Override
	protected void startObjectProperty(String key, Object val, int index) {
		indent += indentFactor;
		writeIndent();

		if (nodes.size() == 1) {
			writeBeginTag(key);
		}
		else {
			writeBeginTag(XmlRpcTags.T_MEMBER);
	
			indent += indentFactor;
			writeIndent();
			writeName(key);
			
			writeIndent();
			writeBeginTag(XmlRpcTags.T_VALUE);
		}
	}
	
	@Override
	protected void endObjectProperty(String key, Object val, int index) {
		indent -= indentFactor;
		writeEndTag();

		if (nodes.size() > 1) {
			writeIndent();

			indent -= indentFactor;
			writeEndTag();
		}
	}

	@Override
	protected void serializeByteArray(String name, byte[] src) {
		String b64 = Base64.encodeBase64String(src);
		writeBeginTag(XmlRpcTags.T_BASE64);
		write(b64);
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
	
	private void writeName(String str) {
		try {
			writeBeginTag(XmlRpcTags.T_NAME);
			StringEscapes.escapeXml(str, writer);
			writeEndTag();
		}
		catch (IOException e) {
			throw new XmlRpcException(e);
		}
	}

	@Override
	protected void writeNull() {
		write("</");
		write(XmlRpcTags.T_NIL);
		write(">");
	}
	
	@Override
	protected void writeNumber(Number num) {
		if (num instanceof Double || num instanceof Float) {
			writeBeginTag(XmlRpcTags.T_DOUBLE);
		}
		else {
			writeBeginTag(XmlRpcTags.T_INT);
		}

		write(num.toString());
		writeEndTag();
	}
	
	@Override
	protected void writeCalendar(Calendar cal) {
		writeDate(cal.getTime());
	}
	
	@Override
	protected void writeDate(Date date) {
		writeBeginTag(XmlRpcTags.T_DATE);
		write(DateTimes.isoAltDatetimeFormat().format(date));
		writeEndTag();
	}
	
	@Override
	protected void writeBoolean(Boolean boo) {
		writeBeginTag(XmlRpcTags.T_BOOLEAN);
		write(boo ? "1" : "0");
		writeEndTag();
	}
	
	@Override
	protected void writeString(String str) {
		try {
			if (nodes.size() > 2) {
				writeBeginTag(XmlRpcTags.T_STRING);
				StringEscapes.escapeXml(str, writer);
				writeEndTag();
			}
			else {
				StringEscapes.escapeXml(str, writer);
			}
		}
		catch (IOException e) {
			throw new XmlRpcException(e);
		}
	}
	
	private void write(String str) {
		try {
			writer.append(str);
		}
		catch (IOException e) {
			throw new XmlRpcException(e);
		}
	}
}
