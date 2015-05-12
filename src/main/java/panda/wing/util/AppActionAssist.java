package panda.wing.util;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import panda.bind.json.JsonObject;
import panda.bind.json.JsonSerializer;
import panda.cast.Castors;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
import panda.mvc.bind.filter.SorterPropertyFilter;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.PermissionProvider;
import panda.mvc.util.StateProvider;
import panda.wing.action.ActionRC;
import panda.wing.constant.VC;
import panda.wing.entity.ICreate;
import panda.wing.entity.IStatus;
import panda.wing.entity.IUpdate;


@IocBean(type=ActionAssist.class, scope=Scope.REQUEST)
public class AppActionAssist extends ActionAssist implements PermissionProvider {
	@IocInject
	protected AppSettings settings;

	@IocInject
	protected AppFreemarkerTemplateLoader ftlTemplateLoader;

	@IocInject
	protected AppResourceBundleLoader resBundleLoader;

	/**
	 * @return app version
	 */
	public String getAppVersion() {
		return settings.getAppVersion();
	}
	
	/**
	 * @return true if database resource loader is activated
	 */
	public boolean isDatabaseResourceLoader() {
		return resBundleLoader.getDatabaseResourceLoader() != null;
	}
	
	/**
	 * @return true if database template loader is activated
	 */
	public boolean isDatabaseTemplateLoader() {
		return ftlTemplateLoader.getDatabaseTemplateLoader() != null;
	}
	
	/**
	 * @return true if gae support
	 */
	public boolean isGaeSupport() {
		return Systems.IS_OS_APPENGINE;
	}

	/**
	 * hasPermission
	 * @param path path
	 * @return true if action has access permit
	 */
	@Override
	public boolean hasPermission(String path) {
		return true;
	}

	/**
	 * hasDataPermission
	 * @param path path
	 * @return true if action has access permit
	 */
	@Override
	public boolean hasDataPermission(Object data, String path) {
		return true;
	}

	public long getLoginUserId() {
		return VC.UNKNOWN_USID;
	}

	/**
	 * initialize common fields of data
	 * @param data data
	 */
	public void initCommonFields(Object data) {
		if (data instanceof IStatus) {
			if (((IStatus)data).getStatus() == null) {
				((IStatus)data).setStatus(VC.STATUS_0);
			}
		}
		
		Date now = DateTimes.getDate();
		if (data instanceof ICreate) {
			ICreate cb = (ICreate)data;
			cb.setCusid(getLoginUserId());
			cb.setCtime(now);
		}
		
		if (data instanceof IUpdate) {
			IUpdate ub = (IUpdate)data;
	
			ub.setUusid(getLoginUserId());
			ub.setUtime(now);
		}
	}

	
	/**
	 * initialize update fields of data
	 * @param data data
	 */
	public void initUpdateFields(Object data, Object srcData) {
		if (data instanceof IStatus) {
			if (((IStatus)data).getStatus() == null) {
				((IStatus)data).setStatus(VC.STATUS_0);
			}
		}
		
		if (data instanceof ICreate) {
			ICreate cb = (ICreate)data;
			ICreate sb = (ICreate)srcData;
	
			cb.setCusid(sb.getCusid());
			cb.setCtime(sb.getCtime());
		}
		
		if (data instanceof IUpdate) {
			IUpdate ub = (IUpdate)data;

			ub.setUusid(getLoginUserId());
			ub.setUtime(DateTimes.getDate());
		}
	}
	
	//-------------------------------------------------------------
	/**
	 * load sorter parameters from stateProvider
	 * @param sorter sorter
	 */
	public void loadSorterParams(Sorter sorter, Map<String, String> orders) {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
		
		// first check
		if (Strings.isNotEmpty(sorter.getColumn()) && orders.containsKey(sorter.getColumn())) {
			return;
		}
		
		String sc = (String)sp.loadState("sorter");
		if (Strings.isEmpty(sc)) {
			String tx = getMethodName() + ActionRC.SORTER_SUFFIX;
			sc = getText(tx, null);
			if (sc == null && !ActionRC.LIST_SORTER.equals(tx)) {
				sc = getText(ActionRC.LIST_SORTER, null);
			}
		}
		if (Strings.isNotEmpty(sc)) {
			JsonObject jo = JsonObject.fromJson(sc);
			Castors.scastTo(jo, sorter);
		}

		// second check
		if (Strings.isNotEmpty(sorter.getColumn()) && orders.containsKey(sorter.getColumn())) {
			return;
		}
		
		for (Entry<String, String> en : orders.entrySet()) {
			sorter.setColumn(en.getKey());
			sorter.setDirection(en.getValue());
			return;
		}
	}
	
	/**
	 * load pager limit parameters from stateProvider
	 * @param pager pager
	 */
	public void loadLimitParams(Pager pager) {
		if (!pager.hasLimit()) {
			StateProvider sp = getState();
			if (sp != null) {
				pager.setLimit(Numbers.toLong((String)sp.loadState("pager"), 0L));
			}
		}
		if (!pager.hasLimit()) {
			String tx = getMethodName() + ActionRC.PAGER_LIMIT_SUFFIX;
			Long l = getTextAsLong(tx);
			if (l == null && !ActionRC.LIST_PAGER_LIMIT.equals(tx)) {
				l = getTextAsLong(ActionRC.LIST_PAGER_LIMIT);
			}
			if (l == null) {
				l = ActionRC.DEFAULT_LIST_PAGER_LIMIT;
			}
			pager.setLimit(l);
		}
		limitPage(pager);
	}
	
	/**
	 * save sorter parameters to stateProvider
	 * @param sorter sorter
	 */
	public void saveSorterParams(Sorter sorter) {
		JsonSerializer js = new JsonSerializer();
		js.registerPropertyFilter(Sorter.class, new SorterPropertyFilter(true));
		String ss = js.serialize(sorter);

		getState().saveState("sorter", ss);
	}
	
	/**
	 * save pager limit parameters to stateProvider
	 * @param pager pager
	 */
	public void saveLimitParams(Pager pager) {
		getState().saveState("list.pl", pager.getLimit());
	}
	
	/**
	 * if pager.limit > maxLimit then set pager.limit = maxLimit
	 * @param pager pager
	 */
	public void limitPage(Pager pager) {
		long maxLimit = getTextAsLong(ActionRC.PAGER_MAX_LIMIT, 100L);
		if (pager.getLimit() == null 
				|| pager.getLimit() < 1 
				|| pager.getLimit() > maxLimit) {
			pager.setLimit(maxLimit);
		}
	}
	
}
