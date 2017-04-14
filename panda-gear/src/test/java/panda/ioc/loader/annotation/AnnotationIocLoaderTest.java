package panda.ioc.loader.annotation;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.loader.annotation.meta.ClassA;
import panda.ioc.loader.annotation.meta.ClassB;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;

public class AnnotationIocLoaderTest {

	IocLoader iocLoader = new AnnotationIocLoader(ClassA.class.getPackage().getName());

	@Test
	public void testGetName() {
		Assert.assertNotNull(iocLoader.getNames());
		Assert.assertTrue(iocLoader.getNames().size() > 0);
	}

	@Test
	public void testHas() {
		Assert.assertTrue(iocLoader.has(ClassA.class.getName()));
	}

	@Test
	public void testLoad() throws Throwable {
		IocObject iocObject = iocLoader.load(ClassB.class.getName());
		Assert.assertNotNull(iocObject);
		Assert.assertNotNull(iocObject.getFields());
		Assert.assertEquals(6, iocObject.getFields().size());
		Assert.assertEquals(IocValue.TYPE_REF, iocObject.getFields().values().iterator().next().getType());
	}

	@Test
	public void testGet() throws Throwable {
		Ioc ioc = new DefaultIoc(iocLoader);
		ClassB b = ioc.get(ClassB.class);
		
		Assert.assertNotNull(b);
		Assert.assertNotNull(b.a);
		Assert.assertEquals("default", b.xxx);
		Assert.assertNull(b.yyy);
		Assert.assertEquals(b.c, b.cc);
		Assert.assertEquals(b.c, b.cc2);
	}
}
