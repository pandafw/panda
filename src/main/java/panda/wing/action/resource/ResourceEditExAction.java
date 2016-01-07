package panda.wing.action.resource;

import java.util.Locale;

import panda.lang.Locales;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.constant.RC;
import panda.wing.constant.VC;
import panda.wing.entity.Resource;

@At("${super_context}/resource")
@Auth(AUTH.SUPER)
public class ResourceEditExAction extends ResourceEditAction {

	/**
	 * Constructor
	 */
	public ResourceEditExAction() {
	}

	/**
	 * isValidLocale
	 * @param language language
	 * @param country country
	 * @return true - if locale is valid
	 */
	public boolean isValidLocale(String language, String country) {
		if (Strings.isNotEmpty(language) && Strings.isNotEmpty(country)) {
			if (!VC.LOCALE_ALL.equals(country)) {
				return Locales.isAvailableLocale(new Locale(language, country));
			}
		}
		return true;
	}
	
	private void addReloadMessage() {
		addApplicationMessage(RC.MESSAGE_RESOURCE_UPDATED);
	}

	@Override
	protected void insertData(Resource data) {
		super.insertData(data);
		
//		if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
//			data.getLanguage(), data.getCountry(), null, data.getName(), data.getValue())) {
//			addReloadMessage();
//		}
		addReloadMessage();
	}

	@Override
	protected int updateData(Resource data, Resource srcData) {
		int cnt = super.updateData(data, srcData);
		
		if (cnt > 0) {
//			if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
//				data.getLanguage(), data.getCountry(), null, data.getName(), data.getValue())) {
//				addReloadMessage();
//			}
			addReloadMessage();
		}
		return cnt;
	}

	@Override
	protected void deleteData(Resource data) {
		super.deleteData(data);

//		if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
//			data.getLanguage(), data.getCountry(), null, data.getName(), null)) {
//			addReloadMessage();
//		}
		addReloadMessage();
	}
}