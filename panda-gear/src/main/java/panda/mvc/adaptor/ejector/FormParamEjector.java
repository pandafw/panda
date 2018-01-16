package panda.mvc.adaptor.ejector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.MvcConstants;

/**
 * Form parameter ejector (Default)
 */
@IocBean(singleton=false)
public class FormParamEjector extends AbstractParamEjector {
	@IocInject(value=MvcConstants.REQUEST_ENCODING, required=false)
	private String encoding = Charsets.UTF_8;
	
	@IocInject(value=MvcConstants.REQUEST_EMPTY_PARAMS, required=false)
	private boolean emptyParams = true;
	
	private Map<String, Object> params;
	
	public FormParamEjector() {
	}

	@Override
	protected Map<String, Object> getParams() {
		if (params == null) {
			HttpServletRequest req = ac.getRequest();
			
			params = new HashMap<String, Object>();

			if (emptyParams) {
				for (Entry<String, String[]> en : req.getParameterMap().entrySet()) {
					String[] vs = en.getValue();
					Object v = (vs != null && vs.length == 1) ? vs[0] : vs;
					params.put(en.getKey(), v);
				}
			}
			else {
				for (Entry<String, String[]> en : req.getParameterMap().entrySet()) {
					String[] vs = en.getValue();
					if (Arrays.isEmpty(vs)) {
						continue;
					}
					
					List<String> ss = new ArrayList<String>();
					for (String v : vs) {
						if (Strings.isNotEmpty(v)) {
							ss.add(v);
						}
					}
					if (Collections.isEmpty(ss)) {
						continue;
					}
					
					params.put(en.getKey(), ss.size() == 1 ? ss.get(0) : ss);
				}
			}
		}
		return params;
	}
}
