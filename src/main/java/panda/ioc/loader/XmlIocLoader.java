package panda.ioc.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import panda.io.Files;
import panda.io.Streams;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Doms;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

/**
 * 使用XML做为Ioc配置文件 <br/>
 * 限制: <br/>
 * <li>必须是良构的XML文件 <li> <li>obj必须定义type,当前实现中IocObject是共享的 <li>
 */
public class XmlIocLoader extends AbstractIocLoader {

	private static final Log log = Logs.getLog(XmlIocLoader.class);

	protected Map<String, String> parentMap = new TreeMap<String, String>();

	protected static final String TAG_OBJ = "obj";
	protected static final String TAG_ARGS = "args";
	protected static final String TAG_FIELD = "field";
	protected static final String TAG_FACTORY = "factory";

	/**
	 * 仅用于标示内部obj的id,内部obj声明的id将被忽略 <br/>
	 * 该设计基于内部obj也可以使用继承顶层的obj
	 */
	private static int innerId;

	public XmlIocLoader(String... paths) {
		try {
			DocumentBuilder builder = Doms.builder();
			for (String p : paths) {
				loadFromPath(builder, p);
			}
			handleParent();
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
		
		if (log.isDebugEnabled()) {
			log.debugf("Loaded %d bean define from path=%s --> %s", 
				beans.size(), Arrays.toString(paths), beans.keySet());
		}
	}
	
	private void loadFromPath(DocumentBuilder builder, String path) throws IOException {
		File p = new File(path);
		if (Files.isDirectory(p)) {
			Collection<File> fs = Files.listFiles(p, new String[] { "xml" }, true);
			for (File f : fs) {
				InputStream is = new FileInputStream(f);
				loadFromStream(builder, is);
			}
		}
		else {
			InputStream is = Streams.getStream(path);
			loadFromStream(builder, is);
		}
	}
	
	private void loadFromStream(DocumentBuilder builder, InputStream is) throws IOException {
		try {
			Document document = builder.parse(is);
			document.normalizeDocument();
			NodeList nodeListZ = ((Element)document.getDocumentElement()).getChildNodes();
			for (int i = 0; i < nodeListZ.getLength(); i++) {
				if (nodeListZ.item(i) instanceof Element) {
					paserBean((Element)nodeListZ.item(i), false);
				}
			}
		}
		catch (Throwable e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(is);
		}
	}

	protected String paserBean(Element beanElement, boolean innerBean) throws Throwable {
		String beanId;
		if (innerBean) {
			beanId = "inner$" + innerId;
			innerId++;
		}
		else {
			beanId = beanElement.getAttribute("name");
		}
		
		if (beanId == null) {
			throw new RuntimeException("No name for one bean!");
		}
		
		if (beans.containsKey(beanId)) {
			throw new RuntimeException("Name of bean is not unique! name=" + beanId);
		}
		
		if (log.isDebugEnabled()) {
			log.debugf("Resolving bean define, name = %s", beanId);
		}
		
		IocObject iocObject = new IocObject();
		String beanType = beanElement.getAttribute("type");
		if (Strings.isNotBlank(beanType)) {
			iocObject.setType(Classes.getClass(beanType));
		}
		
		String beanScope = beanElement.getAttribute("scope");
		if (Strings.isNotBlank(beanScope)) {
			iocObject.setScope(beanScope);
		}
		
		String beanParent = beanElement.getAttribute("parent");
		if (Strings.isNotBlank(beanParent)) {
			parentMap.put(beanId, beanParent);
		}
		
		String factory = beanElement.getAttribute("factory");
		if (Strings.isNotBlank(factory)) {
			iocObject.setFactory(factory);
		}

		parseArgs(beanElement, iocObject);
		parseFields(beanElement, iocObject);
		parseEvents(beanElement, iocObject);

		beans.put(beanId, iocObject);
		if (log.isDebugEnabled()) {
			log.debugf("Resolved bean define, name = %s", beanId);
		}
		
		return beanId;
	}

	protected void parseArgs(Element beanElement, IocObject iocObject) throws Throwable {
		List<Element> list = getChildNodesByTagName(beanElement, TAG_ARGS);
		if (list.size() > 0) {
			Element argsElement = list.get(0);
			NodeList argNodeList = argsElement.getChildNodes();
			for (int i = 0; i < argNodeList.getLength(); i++) {
				if (argNodeList.item(i) instanceof Element) {
					iocObject.addArg(parseX((Element)argNodeList.item(i)));
				}
			}
		}
	}

	protected void parseFields(Element beanElement, IocObject iocObject) throws Throwable {
		List<Element> list = getChildNodesByTagName(beanElement, TAG_FIELD);
		for (Element fieldElement : list) {
			if (fieldElement.hasChildNodes()) {
				NodeList nodeList = fieldElement.getChildNodes();
				for (int j = 0; j < nodeList.getLength(); j++) {
					if (nodeList.item(j) instanceof Element) {
						String name = fieldElement.getAttribute("name");
						IocValue value = parseX((Element)nodeList.item(j));
						iocObject.addField(name, value);
						break;
					}
				}
			}
		}
	}

	protected static final String STR_TAG = "str";
	protected static final String ARRAY_TAG = "array";
	protected static final String MAP_TAG = "map";
	protected static final String ITEM_TAG = "item";
	protected static final String LIST_TAG = "list";
	protected static final String SET_TAG = "set";
	protected static final String OBJ_TAG = "obj";
	protected static final String INT_TAG = "int";
	protected static final String SHORT_TAG = "short";
	protected static final String LONG_TAG = "long";
	protected static final String FLOAT_TAG = "float";
	protected static final String DOUBLE_TAG = "double";
	protected static final String BOOLEAN_TAG = "bool";
	protected static final String REFER_TAG = "ref";
	protected static final String EL_TAG = "el";
	protected static final String JSON_TAG = "json";

	protected IocValue parseX(Element element) throws Throwable {
		IocValue iocValue = new IocValue(IocValue.TYPE_NORMAL);
		String type = element.getNodeName();
		if (JSON_TAG.equalsIgnoreCase(type)) {
			iocValue.setType(IocValue.TYPE_JSON);
			iocValue.setValue(element.getTextContent());
		}
		else if (EL_TAG.equalsIgnoreCase(type)) {
			iocValue.setType(IocValue.TYPE_EL);
			iocValue.setValue(element.getTextContent());
		}
		else if (REFER_TAG.equalsIgnoreCase(type)) {
			iocValue.setType(IocValue.TYPE_REF);
			iocValue.setValue(element.getTextContent());
			String req = element.getAttribute("required");
			if (Strings.isNotEmpty(req)) {
				iocValue.setRequired(Boolean.parseBoolean(req));
			}
		}
		else if (OBJ_TAG.equalsIgnoreCase(type)) {
			iocValue.setType(IocValue.TYPE_REF);
			iocValue.setValue(paserBean(element, true));
		}
		else if (MAP_TAG.equalsIgnoreCase(type)) {
			iocValue.setValue(paserMap(element));
		}
		else if (LIST_TAG.equalsIgnoreCase(type)) {
			iocValue.setValue(paserCollection(element));
		}
		else if (ARRAY_TAG.equalsIgnoreCase(type)) {
			iocValue.setValue(paserCollection(element).toArray());
		}
		else if (SET_TAG.equalsIgnoreCase(type)) {
			Set<Object> set = new HashSet<Object>();
			set.addAll(paserCollection(element));
			iocValue.setValue(set);
		}
		else {
			if (element.getFirstChild() != null) {
				iocValue.setValue(element.getFirstChild().getTextContent());
			}
		}
		return iocValue;
	}

	protected List<IocValue> paserCollection(Element element) throws Throwable {
		List<IocValue> list = new ArrayList<IocValue>();
		if (element.hasChildNodes()) {
			NodeList nodeList = element.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					list.add((IocValue)parseX((Element)node));
				}
			}
		}
		return list;
	}

	protected Map<String, ?> paserMap(Element element) throws Throwable {
		Map<String, Object> map = new HashMap<String, Object>();
		if (element.hasChildNodes()) {
			List<Element> elist = getChildNodesByTagName(element, ITEM_TAG);
			for (Element elementItem : elist) {
				String key = elementItem.getAttribute("key");
				if (map.containsKey(key))
					throw new IllegalArgumentException("key is not unique!");
				NodeList list = elementItem.getChildNodes();
				for (int j = 0; j < list.getLength(); j++) {
					if (list.item(j) instanceof Element) {
						map.put(key, parseX((Element)list.item(j)));
						break;
					}
				}
				if (!map.containsKey(key))
					map.put(key, null);
			}
		}
		return map;
	}

	protected void parseEvents(Element beanElement, IocObject iocObject) {
		List<Element> elist = getChildNodesByTagName(beanElement, "events");
		if (elist.size() > 0) {
			Element eventsElement = elist.get(0);
			IocEventSet iocEventSet = new IocEventSet();
			elist = getChildNodesByTagName(eventsElement, "fetch");
			if (elist.size() > 0) {
				iocEventSet.setFetch(elist.get(0).getTextContent());
			}
			
			elist = getChildNodesByTagName(eventsElement, "create");
			if (elist.size() > 0) {
				iocEventSet.setCreate(elist.get(0).getTextContent());
			}
			
			elist = getChildNodesByTagName(eventsElement, "depose");
			if (elist.size() > 0) {
				iocEventSet.setDepose(elist.get(0).getTextContent());
			}
			
			if (iocEventSet.getCreate() == null
					&& iocEventSet.getDepose() == null
					&& iocEventSet.getFetch() == null) {
				return;
			}
			iocObject.setEvents(iocEventSet);
		}
	}

	protected void handleParent() {
		// 检查parentId是否都存在.
		for (String parentId : parentMap.values()) {
			if (!beans.containsKey(parentId)) {
				throw Exceptions.makeThrow("发现无效的parent=%s", parentId);
			}
		}
		
		// 检查循环依赖
		List<String> parentList = new ArrayList<String>();
		for (Entry<String, String> entry : parentMap.entrySet()) {
			if (!check(parentList, entry.getKey())) {
				throw Exceptions.makeThrow("发现循环依赖! bean id=%s", entry.getKey());
			}
			parentList.clear();
		}

		while (parentMap.size() != 0) {
			Iterator<Entry<String, String>> it = parentMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				String beanId = entry.getKey();
				String parentId = entry.getValue();
				if (parentMap.get(parentId) == null) {
					IocObject newIocObject = Loaders.mergeWith(beans.get(beanId), beans.get(parentId));
					beans.put(beanId, newIocObject);
					it.remove();
				}
			}
		}
	}

	protected boolean check(List<String> parentList, String currentBeanId) {
		if (parentList.contains(currentBeanId)) {
			return false;
		}
		
		String parentBeanId = parentMap.get(currentBeanId);
		if (parentBeanId == null) {
			return true;
		}
		
		parentList.add(currentBeanId);
		return check(parentList, parentBeanId);
	}

	protected List<Element> getChildNodesByTagName(Element element, String tagName) {
		List<Element> list = new ArrayList<Element>();
		NodeList nList = element.getElementsByTagName(tagName);
		if (nList.getLength() > 0) {
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getParentNode().isSameNode(element) && node instanceof Element) {
					list.add((Element)node);
				}
			}
		}
		return list;
	}
}
