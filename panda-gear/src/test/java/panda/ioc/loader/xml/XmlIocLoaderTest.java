package panda.ioc.loader.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map.Entry;

import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.XmlIocLoader;
import panda.ioc.loader.xml.meta.Bee;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocParam;
import panda.ioc.meta.IocValue;

public class XmlIocLoaderTest {

	IocLoader getNew(String fileName) {
		fileName = getClass().getPackage().getName().replace('.', '/') + '/' + fileName;
		return new XmlIocLoader(fileName);
	}

	@Test
	public void testXmlIocLoader() {
		IocLoader iocLoader = getNew("conf/offered.xml");
		assertTrue(iocLoader.getNames() != null);
		assertTrue(iocLoader.getNames().size() > 0);

		for (String name : iocLoader.getNames()) {
			assertNotNull(name);
			assertNotNull(iocLoader.load(name));
			IocObject iocObject = iocLoader.load(name);
			if (iocObject.getArgs() != null) {
				for (IocValue iocValue : iocObject.getArgs()) {
					iocValue.getKind();
					iocValue.getValue();
					checkValue(iocValue);
				}
			}
			if (iocObject.getFields() != null) {
				for (Entry<String, IocParam> en : iocObject.getFields().entrySet()) {
					assertNotNull(en.getKey());
					if (en.getValue() != null) {
						IocValue[] ivs = en.getValue().getValues();
						assertNotNull(ivs);
						assertTrue(ivs.length > 0);
						for (IocValue iv : ivs) {
							checkValue(iv);
						}
					}
				}
			}
		}
		
		for (IocParam ip : iocLoader.load("obj").getFields().values()) {
			for (IocValue iv : ip.getValues()) {
				iv.getValue();
			}
		}
	}

	private void checkValue(IocValue iocValue) {
		iocValue.getKind();
		if (iocValue.getValue() != null && iocValue.getValue() instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>)iocValue.getValue();
			for (Object object : collection) {
				assertNotNull(object);
			}
		}
	}

	@Test
	public void test_simple_case() {
		Ioc ioc = new DefaultIoc(getNew("conf/simple.xml"));
		Bee c = ioc.get(Bee.class, "C");
		assertEquals("TheC", c.getName());
		assertEquals(15, c.getAge());
		assertEquals("TheQueen", c.getMother().getName());
		assertEquals(3, c.getFriends().size());
		assertEquals("TheA", c.getFriends().get(0).getName());
		assertEquals("TheB", c.getFriends().get(1).getName());
		assertEquals(1, c.getMap().size());
		assertEquals("ABC", c.getMap().get("abc"));
	}

}
