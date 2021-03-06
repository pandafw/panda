package panda.mvc.view.tag;

import java.io.Writer;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.mvc.util.MvcURLBuilder;

@IocBean(singleton=false)
public class CUrl extends ContextBean {
	@IocInject
	protected MvcURLBuilder urlbuilder;
	
	/**
	 * @see panda.mvc.view.tag.TagBean#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		if (Collections.isNotEmpty(params)) {
			if (urlbuilder.getParams() == null) {
				urlbuilder.setParams(params);
			}
			else {
				urlbuilder.addParams(params);
			}
		}

		String url = urlbuilder.build();
		writeOrSetVar(writer, url);

		return super.end(writer, "");
	}

	//-------------------------------
	public void setScheme(String scheme) {
		urlbuilder.setScheme(scheme);
	}
	
	public void setPort(int port) {
		urlbuilder.setPort(port);
	}

	public void setAction(String action) {
		urlbuilder.setAction(action);
	}

	public void setQuery(String query) {
		urlbuilder.setQuery(query);
	}
	
	public void setParams(Map params) {
		urlbuilder.setParams(params);
	}

	public void setIncludeParams(String includeParams) {
		urlbuilder.setIncludeParams(includeParams);
	}

	public void setIncludeContext(boolean includeContext) {
		urlbuilder.setIncludeContext(includeContext);
	}

	public void setAnchor(String anchor) {
		urlbuilder.setAnchor(anchor);
	}

	public void setEscapeAmp(boolean escapeAmp) {
		urlbuilder.setEscapeAmp(escapeAmp);
	}

	public void setSuppressParam(String suppress) {
		urlbuilder.setSuppressParam(suppress);
	}

	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort) {
		urlbuilder.setForceAddSchemeHostAndPort(forceAddSchemeHostAndPort);
	}
}
