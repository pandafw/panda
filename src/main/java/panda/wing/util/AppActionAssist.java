package panda.wing.util;

import java.util.Date;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
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
	 * @throws Exception if an error occurs
	 */
	public void loadSorterParams(Sorter sorter) throws Exception {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
				
		if (Strings.isEmpty(sorter.getColumn())) {
			String sc = (String)sp.loadState("list.sc");
			if (Strings.isEmpty(sc)) {
				String tx = getMethodName() + ActionRC.SORTER_COLUMN_SUFFIX;
				sc = getText(tx, (String)null);
				if (sc == null && !ActionRC.LIST_SORTER_COLUMN.equals(tx)) {
					sc = getText(ActionRC.LIST_SORTER_COLUMN, (String)null);
				}
			}
			if (Strings.isNotEmpty(sc)) {
				sorter.setColumn(sc);
			}
		}
		if (Strings.isNotEmpty(sorter.getColumn())) {
			if (Strings.isEmpty(sorter.getDirection())) {
				String sd = (String)sp.loadState("list.sd");
				if (Strings.isEmpty(sd)) {
					String tx = getMethodName() + ActionRC.SORTER_DIRECTION_SUFFIX;
					sd = getText(tx, (String)null);
					if (sd == null && !ActionRC.LIST_SORTER_DIRECTION.equals(tx)) {
						sd = getText(ActionRC.LIST_SORTER_DIRECTION, (String)null);
					}
					if (sd == null) {
						sd = Sorter.ASC;
					}
				}
				sorter.setDirection(sd);
			}
		}
	}
	
	/**
	 * load pager limit parameters from stateProvider
	 * @param pager pager
	 * @throws Exception if an error occurs
	 */
	public void loadLimitParams(Pager pager) throws Exception {
		if (pager.getLimit() == null || pager.getLimit() < 1) {
			pager.setLimit(Numbers.toLong((String)getState().loadState("list.pl"), 0L));
		}
		if (pager.getLimit() == null || pager.getLimit() < 1) {
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
	 * @throws Exception if an error occurs
	 */
	public void saveSorterParams(Sorter sorter) throws Exception {
		getState().saveState("list.sc", sorter.getColumn());
		getState().saveState("list.sd", sorter.getDirection());
	}
	
	/**
	 * save pager limit parameters to stateProvider
	 * @param pager pager
	 * @throws Exception if an error occurs
	 */
	public void saveLimitParams(Pager pager) throws Exception {
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
