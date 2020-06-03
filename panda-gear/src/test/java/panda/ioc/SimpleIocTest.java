package panda.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.a.A;
import panda.ioc.a.B;
import panda.ioc.a.C;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.loader.annotation.meta.Cat;
import panda.ioc.loader.annotation.meta.DogMaster;
import panda.ioc.loader.annotation.meta.Horse;
import panda.ioc.loader.annotation.meta.HorseMP;
import panda.ioc.loader.annotation.meta.SingletonService;
import panda.ioc.loader.annotation.meta.WhiteHorse;

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
		
		WhiteHorse bh = ioc.get(WhiteHorse.class);
		Assert.assertEquals("white horse", bh.getName());
		Assert.assertEquals("alias", bh.getAlias());
	}

	@Test
	public void test_override_inject_abc() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(C.class.getPackage().getName()));
		
		A a = ioc.get(A.class);
		B b = ioc.get(B.class);
		C c = ioc.get(C.class);
		
		Assert.assertEquals(a, b);
		Assert.assertEquals(c.a, a);
		Assert.assertEquals(c.b, b);
		Assert.assertEquals("{\"a\":0,\"b\":1}", b.jo.toString());
		Assert.assertEquals("[0,1]", b.ja.toString());
	}

	@Test
	public void test_multi_params_inject() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(HorseMP.class.getPackage().getName()));

		HorseMP h = ioc.get(HorseMP.class);
		Assert.assertEquals("horse", h.getName());
		Assert.assertEquals("alias", h.getAlias());
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
