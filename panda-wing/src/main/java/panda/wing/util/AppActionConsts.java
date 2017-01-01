package panda.wing.util;

import java.util.Map;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.mvc.util.ActionConsts;
import panda.wing.constant.RC;

@SuppressWarnings("unchecked")
@IocBean(type=ActionConsts.class, scope=Scope.REQUEST)
public class AppActionConsts extends ActionConsts {

	/**
	 * getPermissionMap
	 * @return map
	 */
	public Map<String, String> getPermissionMap() {
		return getTextAsMap(RC.PERMISSIONS);
	}
	
	/**
	 * getRoleMap
	 * @return map
	 */
	public Map<String, String> getRoleMap() {
		return getTextAsMap(RC.ROLES);
	}
	
	/**
	 * getDataStatusMap
	 * @return map
	 */
	public Map<String, String> getDataStatusMap() {
		Object v = cache.get(RC.DATA_STATUS_MAP);
		if (v instanceof Map) {
			return (Map)v;
		}

		Map<String, String> m = getTextAsMap(RC.DATA_STATUS_MAP, Collections.EMPTY_MAP);
		cache.put(RC.DATA_STATUS_MAP, m);
		return m;
	}

	/**
	 * get locale language map
	 * @return map
	 */
	public Map<String, String> getLocaleLanguageMap() {
		return getTextAsMap(RC.LOCALE_LANGUAGES);
	}

	/**
	 * get locale country map
	 * @return map
	 */
	public Map<String, String> getLocaleCountryMap() {
		return getTextAsMap(RC.LOCALE_COUNTRIES);
	}
}
