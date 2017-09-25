package panda.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.loader.annotation.meta.BlackHorse;
import panda.ioc.loader.annotation.meta.Cat;
import panda.ioc.loader.annotation.meta.DogMaster;
import panda.ioc.loader.annotation.meta.Horse;
import panda.ioc.loader.annotation.meta.SingletonService;

public class SimpleIocTest {

	@Test(expected = IocException.class)
	public void test_error_bean() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(DogMaster.class.getPackage().getName()));
		try {
			ioc.get(DogMaster.class);
			fail("Never Success");
		}
		catch (IocException e) {
		}
		ioc.get(DogMaster.class);
	}

	@Test(expected = IocException.class)
	public void test_error_inject() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(Cat.class.getPackage().getName()));
		try {
			ioc.get(Cat.class);
			fail("Never Success");
		}
		catch (IocException e) {
		}
		ioc.get(DogMaster.class);
	}

	@Test
	public void test_override_inject() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(Horse.class.getPackage().getName()));

		Horse h = ioc.get(Horse.class);
		Assert.assertEquals("horse", h.getName());
		
		BlackHorse bh = ioc.get(BlackHorse.class);
		Assert.assertEquals("black horse", bh.getName());
		Assert.assertEquals("alias", bh.getAlias());
	}

	@Test
	public void test_no_singleton_depose() {
		SingletonService.CreateCount = 0;
		SingletonService.DeposeCount = 0;
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(SingletonService.class.getPackage().getName()));
		for (int i = 0; i < 100; i++) {
			ioc.get(SingletonService.class);
		}
		ioc.depose();
		System.gc();
		assertEquals(100, SingletonService.CreateCount);
		assertEquals(0, SingletonService.DeposeCount);

	}
}
