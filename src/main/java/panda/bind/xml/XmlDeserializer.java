package panda.bind.xml;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import panda.bean.BeanHandler;
import panda.bind.AbstractDeserializer;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class XmlDeserializer extends AbstractDeserializer {
	private boolean ignoreXmlAttributes = false;
	private Class<?> defaultXmlObjectType = LinkedHashMap.class;
	private Class<?> defaultXmlArrayType = ArrayList.class;

	public XmlDeserializer() {
	}

	/**
	 * @return the ignoreXmlAttributes
	 */
	public boolean isIgnoreXmlAttributes() {
		return ignoreXmlAttributes;
	}

	/**
	 * @param ignoreXmlAttributes the ignoreXmlAttributes to set
	 */
	public void setIgnoreXmlAttributes(boolean ignoreXmlAttributes) {
		this.ignoreXmlAttributes = ignoreXmlAttributes;
	}

	/**
	 * @return the defaultXmlObjectType
	 */
	public Class<?> getDefaultXmlObjectType() {
		return defaultXmlObjectType;
	}

	/**
	 * @param defaultXmlObjectType the defaultXmlObjectType to set
	 */
	public void setDefaultXmlObjectType(Class<?> defaultXmlObjectType) {
		this.defaultXmlObjectType = defaultXmlObjectType;
	}

	/**
	 * @return the defaultXmlArrayType
	 */
	public Class<?> getDefaultXmlArrayType() {
		return defaultXmlArrayType;
	}

	/**
	 * @param defaultXmlArrayType the defaultXmlArrayType to set
	 */
	public void setDefaultXmlArrayType(Class<?> defaultXmlArrayType) {
		this.defaultXmlArrayType = defaultXmlArrayType;
	}

	/**
	 * Creates a object from a XML, with a specific target class.<br>
	 */
	public <T> T deserialize(Reader xml, Type type) {
		try {
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			SAXHandler handler = new SAXHandler(this, type);
			parser.parse(new InputSource(xml), handler);
			T obj = convertValue(handler.root(), type);
			return obj;
		}
		catch (Exception e) {
			throw wrapError(e);
		}
	}
	
	protected XmlException wrapError(Throwable e) {
		if (e instanceof XmlException) {
			return (XmlException)e;
		}
		return new XmlException(e);
	}

	protected static class SAXHandler<T> extends DefaultHandler {
		private static class Elem {
			private String name;
			private Type type;
			private Object value;
			private StringBuilder text;
			private BeanHandler beanh;
			private boolean leaf = true;
		}
		
		private XmlDeserializer deser;
		private Type type;
		private Object root;
		private List<Elem> stack;

		public SAXHandler(XmlDeserializer deserializer, Type type) {
			this.deser = deserializer;
			this.type = type;
		}
		
		protected XmlException error(String message) {
			return new XmlException(message + at());
		}

		protected Object root() {
			return root;
		}
		
		protected String at() {
			StringBuilder sb = new StringBuilder();
			sb.append("at ");
			for (Elem e : stack) {
				sb.append('/').append(e.name);
			}
			return sb.toString();
		}

		@Override
		public void startDocument() throws SAXException {
			stack = new LinkedList<Elem>();
		}

		@Override
		public void endDocument() throws SAXException {
		}

		protected Elem lastElem() {
			return stack.get(stack.size() - 1);
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			Elem em = new Elem();
			em.name = qName;
			
			if (stack.isEmpty()) {
				em.type = type;
			}
			else {
				Elem pm = lastElem();
				if (pm.beanh == null) {
					// initialize parent element
					if (deser.isArrayType(pm.type)) {
						pm.beanh = deser.getBeanHandler(deser.getDefaultXmlArrayType());
						pm.value = pm.beanh.createObject();
						em.type = deser.getArrayElementType(pm.type);
					}
					else if (Object.class == pm.type) {
						pm.beanh = deser.getBeanHandler(deser.getDefaultXmlObjectType());
						pm.value = pm.beanh.createObject();
						em.type = Object.class;
					}
					else {
						pm.beanh = deser.getBeanHandler(pm.type);
						pm.value = pm.beanh.createObject();
					}
				}
				pm.leaf = false;

				if (em.type == null) {
					if (deser.isArrayType(pm.type)) {
						em.type = deser.getArrayElementType(pm.type);
					}
					else if (Object.class == pm.type) {
						em.type = Object.class;
					}
					else {
						em.type = pm.beanh.getPropertyType(qName);
					}
				}
			}
			stack.add(em);

			if (!deser.isIgnoreXmlAttributes()
					&& attributes != null 
					&& attributes.getLength() > 0
					) {
				if (deser.isArrayType(em.type)) {
					throw error("Xml attributes can not be serialized to the " + Types.typeToString(em.type));
				}

				// TODO
				for (int i = 0; i < attributes.getLength(); i++) {
//					vs.put(attributes.getQName(i), attributes.getValue(i));
				}
			}
		}

		protected Object convertText(Elem em) {
			if (em.text == null) {
				return null;
			}
			try {
				return deser.convertValue(em.text.toString(), em.type);
			}
			catch (RuntimeException e) {
				throw new XmlException("Failed to convert text of property '" + em.name + "'", e);
			}
		}

		protected Object convertValue(Elem em) {
			try {
				return deser.convertValue(em.value, em.type);
			}
			catch (RuntimeException e) {
				throw new XmlException("Failed to convert value of property '" + em.name + "'", e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Elem em = stack.remove(stack.size() - 1);
			if (em.leaf) {
				em.value = convertText(em);
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
			
			if (ep.beanh.canReadProperty(em.name)) {
				if (!deser.isIgnoreReadonlyProperty()) {
					throw error("readonly property: " + em.name);
				}
			}
			else {
				if (!deser.isIgnoreMissingProperty()) {
					throw error("missing property: " + em.name);
				}
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			Elem em = lastElem();
			if (em.leaf) {
				if (em.text == null) {
					em.text = new StringBuilder();
				}
				em.text.append(ch, start, length);
			}
		}
	}
}
