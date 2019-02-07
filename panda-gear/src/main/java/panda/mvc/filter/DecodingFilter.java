package panda.mvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.mvc.MvcConstants;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.servlet.FilteredHttpServletRequestWrapper;
import panda.servlet.FilteredHttpServletResponseWrapper;

@IocBean
public class DecodingFilter implements ServletFilter {
	@IocInject(value=MvcConstants.REQUEST_ENCODING, required=false)
	private String encoding = Charsets.UTF_8;
	
	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		FilteredHttpServletRequestWrapper freq = new FilteredHttpServletRequestWrapper(req);
		freq.setDefaultEncoding(encoding);
		FilteredHttpServletResponseWrapper fres = new FilteredHttpServletResponseWrapper(res);
		return sc.doNext(freq, fres);
	}
}
