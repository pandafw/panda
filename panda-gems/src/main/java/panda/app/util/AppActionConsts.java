package panda.app.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import panda.app.constant.RES;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.mvc.util.ActionConsts;

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
		return getTextAsMap(RES.AUTH_PERMISSIONS);
	}
	
	/**
	 * getAuthRoleMap
	 * @return map
	 */
	public Map<String, String> getAuthRoleMap() {
		return getTextAsMap(RES.AUTH_ROLES);
	}
	
	/**
	 * getDataStatusMap
	 * @return map
	 */
	public Map<String, String> getDataStatusMap() {
		return getTextAsMap(RES.DATA_STATUS_MAP);
	}

	/**
	 * get app locale map
	 * @return map
	 */
	public Map<String, String> getAppLocaleMap() {
		return getTextAsMap(RES.APP_LOCALES);
	}

	/**
	 * get media tag map
	 * @return map
	 */
	public Map<String, String> getMediaTagMap() {
		return getTextAsMap(RES.MEDIA_TAGS);
	}

	/**
	 * get weekdays
	 * @return weekdays list
	 */
	public List<String> getWeekdays() {
		return getTextAsList(RES.CALENDAR_WEEKDAYS);
	}

	/**
	 * get weekdays abbreviation
	 * @return weekdays abbreviation list
	 */
	public List<String> getWeekdaysAbbr() {
		return getTextAsList(RES.CALENDAR_WEEKDAYS_ABBR);
	}

	/**
	 * get weekdays minimum abbreviation
	 * @return weekdays minimum abbreviation list
	 */
	public List<String> getWeekdaysMin() {
		return getTextAsList(RES.CALENDAR_WEEKDAYS_MIN);
	}

	/**
	 * get months
	 * @return month list
	 */
	public List<String> getMonths() {
		return getTextAsList(RES.CALENDAR_MONTHS);
	}

	/**
	 * get months abbreviation
	 * @return month list
	 */
	public List<String> getMonthsAbbr() {
		return getTextAsList(RES.CALENDAR_MONTHS_ABBR);
	}
}
