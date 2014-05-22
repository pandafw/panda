package panda.ioc;

import static org.junit.Assert.*;

import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.IocException;
import panda.ioc.impl.PandaIoc;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.loader.xml.meta.DogMaster;
import panda.ioc.loader.xml.meta.SingletonService;

public class SimpleIocTest {

	@Test(expected = IocException.class)
	public void test_error_bean() {
		Ioc ioc = new PandaIoc(new AnnotationIocLoader(DogMaster.class.getPackage().getName()));
		try {
			ioc.get(DogMaster.class);
			fail("Never Success");
		}
		catch (IocException e) {
		}
		ioc.get(DogMaster.class);
	}

	@Test
	public void test_no_singleton_depose() {
		SingletonService.CreateCount = 0;
		SingletonService.DeposeCount = 0;
		Ioc ioc = new PandaIoc(new AnnotationIocLoader(SingletonService.class.getPackage().getName()));
		for (int i = 0; i < 100; i++) {
			ioc.get(SingletonService.class);
		}
		ioc.depose();
		System.gc();
		assertEquals(100, SingletonService.CreateCount);
		assertEquals(0, SingletonService.DeposeCount);

	}
}
