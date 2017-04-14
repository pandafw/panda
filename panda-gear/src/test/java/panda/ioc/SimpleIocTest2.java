package panda.ioc;

import org.junit.Test;

import panda.ioc.a.B;
import panda.ioc.a.C;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.AnnotationIocLoader;

public class SimpleIocTest2 {
	@Test
	public void test_override_inject() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(B.class, C.class));

		ioc.get(C.class);
	}

}
