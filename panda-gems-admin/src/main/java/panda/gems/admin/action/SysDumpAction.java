package panda.gems.admin.action;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.io.Settings;
import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/sys")
@Auth(AUTH.SUPER)
public class SysDumpAction extends BaseAction {
	@At
	@To(Views.SJSON)
	public Object dump_json(@Param Map arg) {
		return dump();
	}

	@At
	@To(Views.SXML)
	public Object dump_xml(@Param Map arg) {
		return dump();
	}

	@At
	@To(Views.SFTL)
	public Object dump(@Param Map arg) {
		return dump();
	}
	
	protected Object dump() {
		Map<String, Object> r = new LinkedHashMap<String, Object>();

		Map<String, Object> req = new LinkedHashMap<String, Object>();
		HttpServletRequest request = getRequest();
		req.put("AuthType", request.getAuthType());
		req.put("CharacterEncoding", request.getCharacterEncoding());
		req.put("ContentLength", request.getContentLength());
		req.put("ContentType", request.getContentType());
		req.put("ContextPath", request.getContextPath());
		req.put("isSecure", request.isSecure());
		req.put("isRequestedSessionIdFromCookie", request.isRequestedSessionIdFromCookie());
		req.put("isRequestedSessionIdFromURL", request.isRequestedSessionIdFromURL());
		req.put("isRequestedSessionIdValid", request.isRequestedSessionIdValid());
		req.put("Locale", request.getLocale().toString());
		req.put("LocalAddr", request.getLocalAddr());
		req.put("LocalName", request.getLocalName());
		req.put("LocalPort", request.getLocalPort());
		req.put("Method", request.getMethod());
		req.put("PathInfo", request.getPathInfo());
		req.put("PathTranslated", request.getPathTranslated());
		req.put("Protocol", request.getProtocol());
		req.put("QueryString", request.getQueryString());
		req.put("RemoteAddr", request.getRemoteAddr());
		req.put("RemotePort", request.getRemotePort());
		req.put("RequestedSessionId", request.getRequestedSessionId());
		req.put("RequestURI", request.getRequestURI());
		req.put("RequestURL", request.getRequestURL());
		req.put("Scheme", request.getScheme());
		req.put("ServerName", request.getServerName());
		req.put("ServerPort", request.getServerPort());
		req.put("ServletPath", request.getServletPath());

		req.put("headers", getReqHeader());
		req.put("parameters", getReqParams());
		req.put("attributes", toStringMap(getReq()));
		r.put("request", req);

		r.put("session", toStringMap(getSes()));
		r.put("servlet", toStringMap(getApp()));
		r.put("sysprops", System.getProperties());
		r.put("sysenv", System.getenv());
		
		Settings ss = getSettings();
		Map<String, Object> settings = new TreeMap<String, Object>();
		for (String k : ss.keySet()) {
			String f = ss.getFrom(k);
			if (Strings.isNotEmpty(k) && !Strings.startsWithChar(f, '$')) {
				settings.put(k, ss.get(k));
			}
		}
		r.put("settings", settings);

		return r;
	}

	protected Map<String, String> toStringMap(Map<String, Object> src) {
		Map<String, String> d = new TreeMap<String, String>();
		
		for (Entry<String, Object> e : src.entrySet()) {
			Object v = e.getValue();
			String s = Mvcs.castString(context, v);
			if (s == null) {
				s = (v == null ? "<NULL>" : "<" + v.getClass().getName() + ">");
			}
			d.put(e.getKey(), s);
		}
		return d;
	}
}
