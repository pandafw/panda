package panda.mvc.adaptor.ejector;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.bind.json.JsonObject;
import panda.bind.xml.Xmls;
import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;

/**
 * XML Ejector
 */
@IocBean(singleton = false)
public class XmlParamEjector extends AbstractParamEjector {
	private JsonObject json;

	public XmlParamEjector() {
	}

	@Override
	protected Map<String, Object> getParams() {
		if (json == null) {
			try {
				HttpServletRequest req = ac.getRequest();
				String cs = HttpServlets.getEncoding(req);
				json = toJson(req.getInputStream(), cs);

				String qs = req.getQueryString();
				Map<String, Object> qss = URLHelper.parseQueryString(qs);
				for (Entry<String, Object> en : qss.entrySet()) {
					if (!json.containsKey(en.getKey())) {
						json.put(en.getKey(), en.getValue());
					}
				}
			} catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return json;
	}

	@Override
	public Object eject() {
		return getParams();
	}

	protected JsonObject toJson(InputStream is, String encoding) {
		return Xmls.fromXml(is, encoding, JsonObject.class);
	}

}
