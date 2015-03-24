package panda.mvc.view.sitemesh;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.view.sitemesh.SitemeshConfig.SitemeshDecorator;

@IocBean(create="initialize")
public class SitemeshManager {
	private static final Log log = Logs.getLog(SitemeshManager.class);

	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	@IocInject(value=MvcConstants.SITEMESH, required=false)
	protected SitemeshConfig smcfg;
	
	public void initialize() {
		if (settings.getPropertyAsBoolean("sitemesh.disable")) {
			log.info("Sitemesh disabled");
			smcfg = null;
		}
	}
	
	public SitemeshDecorator findDecorator(ActionContext ac) {
		if (smcfg == null) {
			return null;
		}
		
		if (Collections.isEmpty(smcfg.decorators)) {
			return null;
		}
		
		if (Collections.isNotEmpty(smcfg.excludes)) {
			for (Pattern p : smcfg.excludes) {
				if (p.matcher(ac.getPath()).matches()) {
					return null;
				}
			}
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
		}
		
		return null;
	}
}
