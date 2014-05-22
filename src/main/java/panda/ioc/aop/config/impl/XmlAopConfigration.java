package panda.ioc.aop.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import panda.io.Streams;
import panda.lang.Doms;
import panda.lang.Exceptions;

/**
 * 通过Xml配置文件判断需要拦截哪些方法
 */
public class XmlAopConfigration extends AbstractAopConfigration {

	public XmlAopConfigration(String... files) throws ParserConfigurationException, SAXException, IOException {
		InputStream is = null;
		try {
			DocumentBuilder builder = Doms.builder();
			Document document;
			List<AopConfigrationItem> aopList = new ArrayList<AopConfigrationItem>();
			for (String file : files) {
				is = Streams.getStream(file);
				document = builder.parse(is);
				document.normalizeDocument();
				NodeList nodeListZ = ((Element)document.getDocumentElement()).getElementsByTagName("class");
				for (int i = 0; i < nodeListZ.getLength(); i++) {
					aopList.add(parse((Element)nodeListZ.item(i)));
				}
				is.close();
			}
			setAopItemList(aopList);
		}
		catch (Throwable e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(is);
		}

	}

	private AopConfigrationItem parse(Element item) {
		AopConfigrationItem aopItem = new AopConfigrationItem();
		aopItem.setClassName(item.getAttribute("name"));
		aopItem.setMethodName(item.getAttribute("method"));
		aopItem.setInterceptor(item.getAttribute("interceptor"));
		if (item.hasAttribute("singleton")) {
			aopItem.setSingleton(Boolean.parseBoolean(item.getAttribute("singleton")));
		}
		return aopItem;
	}

}
