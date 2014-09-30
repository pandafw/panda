package panda.mvc.adaptor.ejector;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.net.http.URLHelper;
import panda.servlet.HttpServlets;

/**
 * JSON Parameter Ejector
 */
public class JsonParamEjector extends AbstractParamEjector {
	private JsonObject json;

	public JsonParamEjector(ActionContext ac) {
		super(ac);
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
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return json;
	}
	
	@Override
	public Object eject() {
		Object o = getParams().get(ALL);
		if (o != null) {
			return o;
		}
		return getParams();
	}
	
	protected JsonObject toJson(InputStream is, String encoding) {
		Object json = Jsons.fromJson(is, encoding);
		if (!(json instanceof JsonObject)) {
			JsonObject jo = new JsonObject();
			jo.put(ALL, json);
			return jo;
		}
		return (JsonObject)json;
	}
}
