package panda.wing.util;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.mvc.util.ActionConsts;
import panda.wing.constant.RC;

@SuppressWarnings("unchecked")
@IocBean(type=ActionConsts.class, scope=Scope.REQUEST)
public class AppActionConsts extends ActionConsts {
	public Set<String> getAvaliableCharsets() {
		final String name = "avaliable-charsets";
		
		Object v = cache.get(name);
		if (v instanceof Set) {
			return (Set<String>)v;
		}

		SortedMap<String,Charset> map = Charset.availableCharsets();
		v = map.keySet();
		cache.put(name, v);
		return (Set<String>)v;
	}

	/**
	 * getAuthPermissionMap
	 * @return map
	 */
	public Map<String, String> getAuthPermissionMap() {
		return getTextAsMap(RC.AUTH_PERMISSIONS);
	}
	
	/**
	 * getAuthRoleMap
	 * @return map
	 */
	public Map<String, String> getAuthRoleMap() {
		return getTextAsMap(RC.AUTH_ROLES);
	}
	
	/**
	 * getDataStatusMap
	 * @return map
	 */
	public Map<String, String> getDataStatusMap() {
		return getTextAsMap(RC.DATA_STATUS_MAP);
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
