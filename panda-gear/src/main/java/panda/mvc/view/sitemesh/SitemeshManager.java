package panda.mvc.view.sitemesh;

import javax.servlet.ServletContext;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.SetConstants;
import panda.mvc.view.sitemesh.SitemeshConfig.SitemeshDecorator;
import panda.servlet.HttpServlets;

@IocBean(create="initialize")
public class SitemeshManager {
	private static final Log log = Logs.getLog(SitemeshManager.class);

	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	@IocInject(value=MvcConstants.MVC_MAPPING_CASE_IGNORE, required=false)
	protected boolean ignoreCase;
	
	@IocInject(value=MvcConstants.SITEMESH, required=false)
	protected SitemeshConfig smcfg;
	
	@IocInject(value=MvcConstants.SITEMESH_DISABLE, required=false)
	protected boolean disabled;

	public void initialize() {
		if (smcfg != null && ignoreCase) {
			smcfg.initIgnoreCase();
		}
	}
	
	public SitemeshDecorator findDecorator(ActionContext ac) {
		if (smcfg == null) {
			return null;
		}

		if (Collections.isEmpty(smcfg.decorators)) {
			return null;
		}
		
		if (settings.getPropertyAsBoolean(SetConstants.MVC_SITEMESH_DISABLE, disabled)) {
			log.debug("Sitemesh disabled");
			return null;
		}
		
		if (Regexs.matches(smcfg.excludes, ac.getPath())) {
			return null;
		}
		
		for (SitemeshDecorator sd : smcfg.decorators) {
			if (Strings.isNotEmpty(sd.headName)) {
				String hv = ac.getRequest().getHeader(sd.headName);
				if (sd.headValue.equals(hv)) {
					return sd;
				}
			}
			if (Strings.isNotEmpty(sd.paraName)) {
				Object pv = ac.getRequest().getParameter(sd.paraName);
				if (sd.paraValue.equals(pv)) {
					return sd;
				}
			}
			if (sd.path != null) {
				if (sd.path.matcher(ac.getPath()).matches()) {
					return sd;
				}
			}
			if (sd.uri != null) {
				String uri = HttpServlets.getRequestURI(ac.getRequest());
				if (sd.uri.matcher(uri).matches()) {
					return sd;
				}
			}
		}
		
		return null;
	}
}
