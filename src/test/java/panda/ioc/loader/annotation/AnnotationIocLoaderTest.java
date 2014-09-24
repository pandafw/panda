package panda.ioc.loader.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.ioc.IocLoader;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.loader.annotation.meta.ClassA;
import panda.ioc.loader.annotation.meta.ClassB;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;

public class AnnotationIocLoaderTest {

	IocLoader iocLoader = new AnnotationIocLoader(ClassA.class.getPackage().getName());

	@Test
	public void testGetName() {
		assertNotNull(iocLoader.getName());
		assertTrue(iocLoader.getName().length > 0);
	}

	@Test
	public void testHas() {
		assertTrue(iocLoader.has(ClassA.class.getName()));
	}

	@Test
	public void testLoad() throws Throwable {
		IocObject iocObject = iocLoader.load(null, ClassB.class.getName());
		assertNotNull(iocObject);
		assertNotNull(iocObject.getFields());
		assertTrue(iocObject.getFields().size() == 1);
		assertEquals(IocValue.TYPE_REF, iocObject.getFields().values().iterator().next().getType());
	}

}
