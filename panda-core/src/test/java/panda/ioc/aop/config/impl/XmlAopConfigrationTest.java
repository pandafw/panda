package panda.ioc.aop.config.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import panda.ioc.aop.impl.DefaultMirrorFactory;
import panda.ioc.impl.DefaultIoc;

public class XmlAopConfigrationTest {

	@Test
	public void testGetMirror() throws ParserConfigurationException, SAXException, IOException {
		DefaultMirrorFactory mirrorFactory = new DefaultMirrorFactory(null);
		String xml = getClass().getPackage().getName().replace('.', '/') + "/xmlfile-aop.xml";
		mirrorFactory.setAopConfigration(new XmlAopConfigration(xml));
		assertNotNull(mirrorFactory.getMirror(XmlAopConfigration.class, null));
		assertNotNull(mirrorFactory.getMirror(DefaultMirrorFactory.class, null));
		assertNotNull(mirrorFactory.getMirror(DefaultIoc.class, null));
	}

}
