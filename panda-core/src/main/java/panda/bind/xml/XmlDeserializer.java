package panda.bind.xml;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

/**
 * 
 *
 */
public class XmlDeserializer extends AbstractDeserializer {
	private Class<?> defaultXmlObjectType = HashMap.class;
	private Class<?> defaultXmlArrayType = ArrayList.class;

	public XmlDeserializer() {
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
			XMLReader xr = XMLReaderFactory.createXMLReader();

			SaxHandler handler = new SaxHandler(this, type);
			xr.setContentHandler(handler);
			
			xr.parse(new InputSource(xml));
			
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

	protected static class SaxHandler extends DefaultHandler {
		private static class Elem {
			private Type type;
			private String name;
			private Object value;
			private StringBuilder text;
			private BeanHandler beanh;
			private int leaf = 0;
		}
		
		private XmlDeserializer deser;
		private Type type;
		private Object root;
		private List<Elem> elems;

		public SaxHandler(XmlDeserializer deserializer, Type type) {
			this.deser = deserializer;
			this.type = type;
			elems = new ArrayList<Elem>();
		}
		
		protected XmlException error(String message) {
			return new XmlException(message + at());
		}

		protected XmlException error(String message, Throwable e) {
			return new XmlException(message + at(), e);
		}

		protected Object root() {
			return root;
		}
		
		protected String at() {
			StringBuilder sb = new StringBuilder(128);
			sb.append(" at ");
			for (Elem e : elems) {
				sb.append('/').append(e.name);
			}
			return sb.toString();
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
		}

		protected Elem lastElem() {
			return elems.get(elems.size() - 1);
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			Elem em = new Elem();
			em.name = localName;
			
			if (elems.isEmpty()) {
				em.type = type;
				elems.add(em);
				return;
			}

			Elem ep = lastElem();
			elems.add(em);

			if (ep.type == null) {
				return;
			}
			
			if (ep.beanh == null) {
				// initialize parent element
				if (deser.isArrayType(ep.type)) {
					ep.beanh = deser.getBeanHandler(deser.getDefaultXmlArrayType());
					ep.value = ep.beanh.createObject();
					em.type = deser.getArrayElementType(ep.type);
					em.name = String.valueOf(ep.leaf);
				}
				else if (Object.class == ep.type) {
					ep.beanh = deser.getBeanHandler(deser.getDefaultXmlObjectType());
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
								throw error("readonly property [" + em.name + "] of " + ep.type);
							}
	
							if (deser.isIgnoreMissingProperty()) {
								return;
							}
							throw error("missing property [" + em.name + "] of " + ep.type);
						}
					}
				}
			}

			ep.leaf++;
		}

		protected Object convertText(Elem em) {
			Object v = null;
			if (em.text != null && em.text.length() > 0) {
				v = em.text.toString();
			}

			try {
				return deser.convertValue(v, em.type);
			}
			catch (Exception e) {
				throw error("Failed to convert text of property '" + em.name + "'", e);
			}
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
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Elem em = elems.remove(elems.size() - 1);
			if (em.type == null) {
				return;
			}
			
			if (em.leaf == 0) {
				em.value = convertText(em);
			}
			
			if (elems.isEmpty()) {
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

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			Elem em = lastElem();
			if (em.leaf == 0) {
				if (em.text == null) {
					em.text = new StringBuilder(length);
				}
				em.text.append(ch, start, length);
			}
		}
	}
}
