package panda.wing.action.resource;

import java.util.List;
import java.util.Locale;

import panda.lang.Locales;
import panda.lang.Strings;
import panda.wing.constant.RC;
import panda.wing.constant.VC;
import panda.wing.entity.Resource;

public class ResourceExAction extends ResourceAction {

	/**
	 * Constructor
	 */
	public ResourceExAction() {
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

	@Override
	protected int deleteDataList(List<Resource> dataList) {
		int cnt = super.deleteDataList(dataList);

		if (cnt > 0) {
	//		for (Resource data : dataList) {
	//			if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
	//				data.getLanguage(), data.getCountry(), null, data.getName(), data.getValue())) {
	//				addReloadMessage();
	//				break;
	//			}
	//		}
			addReloadMessage();
		}
		
		return cnt;
	}
}