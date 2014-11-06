package panda.ioc.val;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.annotation.IocBean;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.impl.ScopeIocContext;
import panda.ioc.loader.MapIocLoader;

public class RefValueTest {

	@IocBean("obj")
	public static class TestReferContext {
		IocContext ic;
	}

	@Test
	public void test_refer_context() {
		IocContext context = new ScopeIocContext("abc");
		String json = "{obj:{singleton:false,fields:{ic:'ref:$ictx'}}}";
		Ioc ioc = new DefaultIoc(new MapIocLoader(json), context, "abc");
		TestReferContext trc = ioc.get(TestReferContext.class);
		assertTrue(context == trc.ic);
	}
}
