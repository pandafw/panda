package panda.mvc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.mvc.AbstractMvcTest;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.impl.processor.ViewProcessor;
import panda.mvc.view.VoidView;

public class ViewProcessorTest extends AbstractMvcTest {

	@Test
	public void test_error_processor() throws Throwable {
		ViewProcessor p = new EViewProcessor();
		ActionContext ac = new ActionContext();
		ac.setRequest(request).setResponse(response).setServletContext(servletContext);
		Throwable t = new Throwable();
		ac.setError(t);
		p.process(ac);
		Object obj = request.getAttribute(View.ATTR_OBJECT);
		assertNotNull(obj);
		assertTrue(obj instanceof Throwable);
		assertEquals(t, obj);
	}

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", "org.nutz.mvc.init.module.MainModule");
	}

}

class EViewProcessor extends ViewProcessor {

	public EViewProcessor() {
		view = new VoidView();
	}
}
