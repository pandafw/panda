package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.servlet.FilteredHttpServletRequestWrapper;
import panda.servlet.FilteredHttpServletResponseWrapper;

@IocBean
public class DecodingProcessor extends AbstractProcessor {
	@IocInject(value=MvcConstants.REQUEST_ENCODING, required=false)
	private String encoding = Charsets.UTF_8;
	
	@Override
	public void process(ActionContext ac) {
		FilteredHttpServletRequestWrapper freq = new FilteredHttpServletRequestWrapper(ac.getRequest());
		FilteredHttpServletResponseWrapper fres = new FilteredHttpServletResponseWrapper(ac.getResponse());
		ac.setRequest(freq);
		ac.setResponse(fres);
		doNext(ac);
	}
}
