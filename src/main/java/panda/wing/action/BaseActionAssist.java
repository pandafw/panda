package panda.wing.action;

import java.util.Date;

import freemarker.cache.TemplateLoader;

import panda.io.Settings;
import panda.io.resource.ResourceBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.mvc.util.PermissionProvider;
import panda.wing.constant.VC;
import panda.wing.entity.ICreate;
import panda.wing.entity.IStatus;
import panda.wing.entity.IUpdate;
import panda.wing.mvc.ActionAssist;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;


public class BaseActionAssist extends ActionAssist implements PermissionProvider {
	@IocInject(type=Settings.class)
	protected AppSettings settings;

	@IocInject(type=TemplateLoader.class)
	protected AppFreemarkerTemplateLoader ftlTemplateLoader;

	@IocInject(type=ResourceBundleLoader.class)
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
}
