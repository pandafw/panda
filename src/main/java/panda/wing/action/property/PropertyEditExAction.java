package panda.wing.action.property;

import java.util.List;
import java.util.Locale;

import panda.lang.Locales;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.wing.constant.RC;
import panda.wing.constant.VC;
import panda.wing.entity.Property;

@At("${admin_context}/property")
public class PropertyEditExAction extends PropertyEditAction {

	/**
	 * Constructor
	 */
	public PropertyEditExAction() {
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
		addApplicationMessage(RC.MESSAGE_PROPERTY_UPDATED);
	}

	@Override
	protected void insertData(Property data) {
		super.insertData(data);
		
//		if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
//			data.getLanguage(), data.getCountry(), null, data.getName(), data.getValue())) {
//			addReloadMessage();
//		}
		addReloadMessage();
	}

	@Override
	protected int updateData(Property data, Property srcData) {
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
	protected void deleteData(Property data) {
		super.deleteData(data);

//		if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
//			data.getLanguage(), data.getCountry(), null, data.getName(), null)) {
//			addReloadMessage();
//		}
		addReloadMessage();
	}

	@Override
	protected int deleteDataList(List<Property> dataList) {
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