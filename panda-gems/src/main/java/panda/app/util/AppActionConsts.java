package panda.app.util;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.mvc.util.ActionConsts;

@SuppressWarnings("unchecked")
@IocBean(type=ActionConsts.class, scope=Scope.REQUEST)
public class AppActionConsts extends ActionConsts {
	@Override
	public Object get(Object key) {
		final String k = "avaliableCharsets";
		if (k.equals(key)) {
			return getAvaliableCharsets();
		}
		return super.get(key);
	}

	/**
	 * @return avaliable charset list
	 */
	public Set<String> getAvaliableCharsets() {
		final String k = "avaliableCharsets";
		
		Object v = cache.get(k);
		if (v instanceof Set) {
			return (Set<String>)v;
		}

		Map<String, Charset> map = Charset.availableCharsets();
		v = map.keySet();
		cache.put(k, v);
		return (Set<String>)v;
	}
	
	/**
	 * getAuthPermissionMap
	 * @return map
	 */
	public Map<String, String> getAuthPermissionMap() {
		return (Map<String, String>)get("authPermissionMap");
	}

	/**
	 * getAuthRoleMap
	 * @return map
	 */
	public Map<String, String> getAuthRoleMap() {
		return (Map<String, String>)get("authRoleMap");
	}
}
