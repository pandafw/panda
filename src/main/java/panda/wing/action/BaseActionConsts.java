package panda.wing.action;

import java.util.List;
import java.util.Map;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.mvc.util.ActionConsts;
import panda.wing.constant.RC;

/**
 */
@IocBean(type=ActionConsts.class, scope=Scope.REQUEST)
@SuppressWarnings("unchecked")
public class BaseActionConsts extends ActionConsts {

	/**
	 * @return map
	 */
	public Map<String, String> getBooleanMap() {
		return getTextAsMap("const-booleans");
	}

	/**
	 * @return list
	 */
	public List getBloodTypeList() {
		return getTextAsList("const-bloodTypes");
	}
	
	/**
	 * @return map
	 */
	public Map<String, String> getGenderMap() {
		return getTextAsMap("const-genders");
	}
	
	/**
	 * getPermissionMap
	 * @return map
	 */
	public Map<String, String> getPermissionMap() {
		return getTextAsMap(RC.PERMISSIONS);
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
