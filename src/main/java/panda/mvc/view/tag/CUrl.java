package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.util.UrlBuilder;

@IocBean(singleton=false)
public class CUrl extends ContextBean {
	private static final Log log = Logs.getLog(CUrl.class);
	
	@IocInject
	protected UrlBuilder urlbuilder;
	
	/**
	 * @see panda.mvc.view.tag.Component#end(java.io.Writer, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean end(Writer writer, String body) {
		if (Collections.isNotEmpty(params)) {
			if (urlbuilder.getParams() == null) {
				urlbuilder.setParams(params);
			}
			else {
				urlbuilder.getParams().putAll(params);
			}
		}

		String url = urlbuilder.build();
		if (url != null) {
			try {
				if (getVar() == null) {
					writer.write(url);
				}
				else {
					putInVars(url);
				}
			}
			catch (IOException e) {
				log.warn("Could not write out URL tag", e);
			}
		}

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

	public void setIncludeParams(boolean includeParams) {
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

	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort) {
		urlbuilder.setForceAddSchemeHostAndPort(forceAddSchemeHostAndPort);
	}
}