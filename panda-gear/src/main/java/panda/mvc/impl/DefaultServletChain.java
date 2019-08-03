package panda.mvc.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.mvc.filter.DecodingFilter;
import panda.mvc.filter.DispatchFilter;
import panda.mvc.filter.LoggingFilter;

@IocBean(type=ServletChain.class, singleton=false, create="initialize")
public class DefaultServletChain implements ServletChain {
	@IocInject
	private Ioc ioc;
	
	private List<ServletFilter> filters = new ArrayList<ServletFilter>();

	private int current = -1;

	/**
	 * @param filters the filters to set
	 */
	@IocInject(value=MvcConstants.MVC_FILTERS, required=false)
	public void setFilters(List<ServletFilter> filters) {
		this.filters.clear();
		this.filters.addAll(filters);
	}

	public void initialize() {
		if (filters.isEmpty()) {
			filters.add(ioc.get(DecodingFilter.class));
			filters.add(ioc.get(LoggingFilter.class));
			filters.add(ioc.get(DispatchFilter.class));
		}
	}

	@Override
	public boolean doChain(HttpServletRequest req, HttpServletResponse res) {
		return doNext(req, res);
	}

	@Override
	public boolean doNext(HttpServletRequest req, HttpServletResponse res) {
		current++;
		if (current >= filters.size()) {
			return false;
		}
		
		return filters.get(current).doFilter(req, res, this);
	}

	@Override
	public void addFilter(ServletFilter filter) {
		filters.add(filter);
	}
}
