package panda.ioc.aop.config.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import panda.ioc.aop.impl.DefaultMirrors;
import panda.ioc.impl.DefaultIoc;

public class XmlAopConfigrationTest {

	@Test
	public void testGetMirror() throws ParserConfigurationException, SAXException, IOException {
		DefaultMirrors mirrorFactory = new DefaultMirrors();
		String xml = getClass().getPackage().getName().replace('.', '/') + "/xmlfile-aop.xml";
		mirrorFactory.setAopConfigration(new XmlAopConfigration(xml));
		assertNotNull(mirrorFactory.getMirror(null, XmlAopConfigration.class, null));
		assertNotNull(mirrorFactory.getMirror(null, DefaultMirrors.class, null));
		assertNotNull(mirrorFactory.getMirror(null, DefaultIoc.class, null));
	}

}
