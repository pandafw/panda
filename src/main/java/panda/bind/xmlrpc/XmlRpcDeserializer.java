package panda.bind.xmlrpc;

import java.io.Reader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import panda.bean.BeanHandler;
import panda.bind.AbstractDeserializer;
import panda.lang.codec.binary.Base64;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;

/**
 * @author yf.frank.wang@gmail.com
 */
public class XmlRpcDeserializer extends AbstractDeserializer {
	public XmlRpcDeserializer() {
	}

	/**
	 * Creates a object from a XML, with a specific target class.<br>
	 */
	public <T> T deserialize(Reader xml, Type type) {
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();

			SaxHandler handler = new SaxHandler(new XomHandler(this, type));
			xr.setContentHandler(handler);

			xr.parse(new InputSource(xml));

			T obj = convertValue(handler.root(), type);
			return obj;
		}
		catch (Exception e) {
			throw wrapError(e);
		}
	}

	protected RuntimeException wrapError(Throwable e) {
		if (e instanceof XmlRpcException) {
			return (XmlRpcException)e;
		}
		return new XmlRpcException(e);
	}

	protected static class XomHandler {
		private static class Elem {
			private Type type;
			private String name;
			private Object value;
			private BeanHandler beanh;
			private int leaf = 0;
		}

		private XmlRpcDeserializer deser;
		private Type type;
		private Object root;
		private List<Elem> stack;

		public XomHandler(XmlRpcDeserializer deserializer, Type type) {
			this.deser = deserializer;
			this.type = type;
			stack = new ArrayList<Elem>();
		}

		protected XmlRpcException error(String message) {
			return new XmlRpcException(message + at());
		}

		protected XmlRpcException error(String message, Throwable e) {
			return new XmlRpcException(message + at(), e);
		}

		protected Object root() {
			return root;
		}

		protected String at() {
			StringBuilder sb = new StringBuilder(128);
			sb.append(" at ");
			for (Elem e : stack) {
				sb.append('/').append(e.name);
			}
			return sb.toString();
		}

		protected Elem lastElem() {
			return stack.get(stack.size() - 1);
		}

		public void changeToArray() {
			Elem ep = lastElem();
			if (deser.isArrayType(ep.type)) {
				return;
			}
			if (Object.class == ep.type) {
				ep.type = ArrayList.class;
				ep.beanh = deser.getBeanHandler(ArrayList.class);
				ep.value = ep.beanh.createObject();
				return;
			}
			error("Invalid array type: " + ep.type);
		}
		
		public void startElement(String name) {
			Elem em = new Elem();
			em.name = name;

			if (stack.isEmpty()) {
				em.type = type;
				stack.add(em);
				return;
			}

			Elem ep = lastElem();
			stack.add(em);

			if (ep.type == null) {
				return;
			}

			if (ep.beanh == null) {
				// initialize parent element
				if (deser.isArrayType(ep.type)) {
					ep.beanh = deser.getBeanHandler(ArrayList.class);
					ep.value = ep.beanh.createObject();
					em.type = deser.getArrayElementType(ep.type);
					em.name = String.valueOf(ep.leaf);
				}
				else if (Object.class == ep.type) {
					ep.beanh = deser.getBeanHandler(HashMap.class);
					ep.value = ep.beanh.createObject();
					em.type = Object.class;
				}
				else {
					ep.beanh = deser.getBeanHandler(ep.type);
					ep.value = ep.beanh.createObject();
				}
			}

			if (em.type == null) {
				if (deser.isArrayType(ep.type)) {
					em.type = deser.getArrayElementType(ep.type);
					em.name = String.valueOf(ep.leaf);
				}
				else if (Object.class == ep.type) {
					em.type = Object.class;
				}
				else {
					if (ep.beanh.canWriteProperty(em.name)) {
						em.type = ep.beanh.getPropertyType(em.name);
					}
					else {
						em.type = null;

						if (!deser.isIgnoreReadonlyProperty() || !deser.isIgnoreMissingProperty()) {
							if (ep.beanh.canReadProperty(em.name)) {
								if (deser.isIgnoreReadonlyProperty()) {
									return;
								}
								throw error("readonly property: " + em.name);
							}

							if (deser.isIgnoreMissingProperty()) {
								return;
							}
							throw error("missing property: " + em.name);
						}
					}
				}
			}

			ep.leaf++;
		}

		protected Object convertValue(Elem em) {
			if (em.type == null) {
				return null;
			}

			try {
				return deser.convertValue(em.value, em.type);
			}
			catch (Exception e) {
				throw error("Failed to convert value of property '" + em.name + "'", e);
			}
		}

		@SuppressWarnings("unchecked")
		public void endElement(Object value) {
			Elem em = stack.remove(stack.size() - 1);
			if (em.type == null) {
				return;
			}

			if (em.leaf == 0) {
				em.value = value;
			}

			if (stack.isEmpty()) {
				root = em.value;
				return;
			}

			Object ev = convertValue(em);
			Elem ep = lastElem();
			if (ep.beanh.setPropertyValue(ep.value, em.name, ev)) {
				return;
			}

			if (deser.isIgnoreReadonlyProperty() && deser.isIgnoreMissingProperty()) {
				return;
			}

			if (ep.beanh.canReadProperty(em.name)) {
				if (deser.isIgnoreReadonlyProperty()) {
					return;
				}
				throw error("readonly property: " + em.name);
			}

			if (deser.isIgnoreMissingProperty()) {
				return;
			}
			throw error("missing property: " + em.name);
		}
	}
	

	protected static class SaxHandler extends DefaultHandler {
		private static class XmlRpcElem {
			public static FastDateFormat[] DATE_FORMATS = {
				DateTimes.isoDatetimeFormat(),
				DateTimes.isoAltDatetimeFormat()
			};

			/** type of value: string, int, boolean, double, datetime, base64, name */
			private int type = 0;
			
			/** name of member */
			private String name;
			
			/** value */
			private Object value;

			private boolean shouldProcessText;

			/** The accumulated character data from the SAX driver. Is emptied when consumed */
			private StringBuilder text = new StringBuilder(128);

			public void reset() {
				type = 0;
				name = null;
				value = null;
				text.setLength(0);
				shouldProcessText = false;
			}

			public void startSaveText() {
				text.setLength(0);
				shouldProcessText = true;
			}

			public void characters(char[] data, int start, int length) {
				if (shouldProcessText) {
					text.append(data, start, length);
				}
			}

			private Date toDate(String text) throws XmlRpcException {
				ParseException ex = null;
				for (FastDateFormat df : DATE_FORMATS) {
					try {
						return df.parse(text);
					}
					catch (ParseException e) {
						ex = e;
					}
				}
				throw new XmlRpcException(ex);
			}

			/**
			 * Processes the character data supplied by the parser. Depending on the current type of
			 * the value, the data will be treated accordingly.
			 */
			public void processText() throws XmlRpcException {
				if (!shouldProcessText) {
					return;
				}
				
				if (type == XmlRpcTags.I_NAME) {
					name = text.toString();
				}
				else if (type == XmlRpcTags.I_STRING) {
					value = text.toString();
				}
				else if (type == XmlRpcTags.I_I4 || type == XmlRpcTags.I_INT) {
					value = new Integer(text.toString());
				}
				else if (type == XmlRpcTags.I_I8) {
					value = new Long(text.toString());
				}
				else if (type == XmlRpcTags.I_BOOLEAN) {
					String v = text.toString();
					value = v.equals("1");
				}
				else if (type == XmlRpcTags.I_DOUBLE) {
					value = new Double(text.toString());
				}
				else if (type == XmlRpcTags.I_DATE) {
					value = toDate(text.toString());
				}
				else if (type == XmlRpcTags.I_BASE64) {
					value = Base64.decodeBase64(text.toString());
				}
				else if (type == XmlRpcTags.I_NIL) {
					value = null;
				}
				
				shouldProcessText = false;
				text.setLength(0);
			}
		}

		private XomHandler xh;

		private XmlRpcElem elem = new XmlRpcElem();

		private List<Integer> tags;

		public SaxHandler(XomHandler xomHandler) {
			xh = xomHandler;
			tags = new ArrayList<Integer>();
		}

		protected Object root() {
			return xh.root();
		}

		protected XmlRpcException error(String message) {
			return new XmlRpcException(message + at());
		}

		protected XmlRpcException error(String message, Throwable e) {
			return new XmlRpcException(message + at(), e);
		}

		protected String at() {
			StringBuilder sb = new StringBuilder();
			sb.append(" at ");
			for (int p : tags) {
				sb.append('/').append(XmlRpcTags.name(p));
			}
			return sb.toString();
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			int tag = XmlRpcTags.hash(localName);
			tags.add(tag);

			if (tag == XmlRpcTags.I_METHOD_CALL || tag == XmlRpcTags.I_METHOD_RESPONSE) {
				elem.name = localName;
				xh.startElement(elem.name);
			}
			else if (tag == XmlRpcTags.I_FAULT) {
				elem.name = XmlRpcTags.T_FAULT;
				xh.startElement(elem.name);
			}
			else if (tag == XmlRpcTags.I_METHOD_NAME) {
				elem.type = XmlRpcTags.I_STRING;
				elem.name = XmlRpcTags.T_METHOD_NAME;
				elem.startSaveText();
				xh.startElement(elem.name);
			}
			else if (tag == XmlRpcTags.I_PARAMS || tag == XmlRpcTags.I_PARAM) {
				xh.startElement(localName);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_STRUCT || tag == XmlRpcTags.I_MEMBER) {
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_NAME) {
				elem.type = tag;
				elem.startSaveText();
			}
			else if (tag == XmlRpcTags.I_ARRAY) {
				xh.changeToArray();
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_DATA) {
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_VALUE) {
				int ptag = tags.get(tags.size() - 2);
				if (ptag == XmlRpcTags.I_MEMBER || ptag == XmlRpcTags.I_PARAM || ptag == XmlRpcTags.I_FAULT) {
				}
				else if (ptag == XmlRpcTags.I_DATA) {
					xh.startElement(XmlRpcTags.T_DATA);
				}
				else {
					throw error("Invalid XML-RPC format of <value>");
				}
				elem.type = XmlRpcTags.I_STRING;
				elem.startSaveText();
			}
			else if (tag == XmlRpcTags.I_STRING || tag == XmlRpcTags.I_I4 || tag == XmlRpcTags.I_I8
					|| tag == XmlRpcTags.I_INT || tag == XmlRpcTags.I_BOOLEAN || tag == XmlRpcTags.I_DOUBLE
					|| tag == XmlRpcTags.I_DATE || tag == XmlRpcTags.I_BASE64
					) {
				elem.type = tag;
				elem.startSaveText();
			}
			else if (tag == XmlRpcTags.I_NIL) {
				elem.type = tag;
			}
			else {
				throw error("Invalid XML-RPC element: " + localName);
			}
		}

		private int parent() {
			return tags.get(tags.size() - 1);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			tags.remove(tags.size() - 1);

			elem.processText();

			int tag = XmlRpcTags.hash(localName);

			if (tag == XmlRpcTags.I_METHOD_CALL || tag == XmlRpcTags.I_METHOD_RESPONSE) {
				xh.endElement(null);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_FAULT) {
				xh.endElement(null);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_METHOD_NAME) {
				xh.endElement(elem.value);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_PARAMS) {
				xh.endElement(null);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_PARAM) {
				xh.endElement(elem.value);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_STRUCT) {
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_MEMBER) {
				xh.endElement(elem.value);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_NAME) {
				xh.startElement(elem.name);
				elem.reset();
			}
			else if (tag == XmlRpcTags.I_ARRAY || tag == XmlRpcTags.I_DATA) {
				// skip
			}
			else if (tag == XmlRpcTags.I_VALUE) {
				int ptag = parent();
				if (ptag == XmlRpcTags.I_DATA) {
					xh.endElement(elem.value);
					elem.reset();
				}
			}
			else if (tag == XmlRpcTags.I_STRING || tag == XmlRpcTags.I_I4 || tag == XmlRpcTags.I_I8
					|| tag == XmlRpcTags.I_INT || tag == XmlRpcTags.I_BOOLEAN || tag == XmlRpcTags.I_DOUBLE
					|| tag == XmlRpcTags.I_DATE || tag == XmlRpcTags.I_BASE64
					) {
				//skip
			}
			else if (tag == XmlRpcTags.I_NIL) {
				//skip
			}
			else {
				throw error("Invalid XML-RPC element: " + localName);
			}
		}

		/**
		 * Called by the SAX driver when character data is available.
		 * <p>
		 * This implementation appends the data to an internal string buffer. The method is called
		 * for every element, wether characters are included int the element or not. This leads to
		 * the buffer being prepended with whitespace until actual character data is aquired. This
		 * is removed using the trim() method when the character data is consumed.
		 * </p>
		 * 
		 * @param data {@inheritDoc}
		 * @param start {@inheritDoc}
		 * @param length {@inheritDoc}
		 */
		public void characters(char[] data, int start, int length) {
			elem.characters(data, start, length);
		}
	}
}
