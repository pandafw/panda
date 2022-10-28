package panda.mvc.filter;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.mvc.MvcConstants;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;

@IocBean
public class ResponseHeaderFilter implements ServletFilter {
	@IocInject(value=MvcConstants.RESPONSE_HEADERS, required=false)
	private Map<String, String> responseHeaders;
	
	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		if (Collections.isNotEmpty(responseHeaders)) {
			for (Entry<String, String> en : responseHeaders.entrySet()) {
				res.setHeader(en.getKey(), en.getValue());
			}
		}
		return sc.doNext(req, res);
	}
}
