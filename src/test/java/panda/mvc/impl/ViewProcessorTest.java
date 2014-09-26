package panda.mvc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.init.module.MainModule;
import panda.mvc.processor.ViewProcessor;

public class ViewProcessorTest extends AbstractMvcTestCase {

	@Test
	public void test_error_processor() throws Throwable {
		ViewProcessor p = new EViewProcessor();
		ActionContext ac = new ActionContext();
		ac.setRequest(request);
		ac.setResponse(response);
		ac.setServlet(servletContext);
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
		servletConfig.addInitParameter("modules", MainModule.class.getName());
	}

}

class VoidView implements View {

	public void render(ActionContext ac) throws Throwable {
	}

}

class EViewProcessor extends ViewProcessor {

	public EViewProcessor() {
		view = new VoidView();
	}
}
