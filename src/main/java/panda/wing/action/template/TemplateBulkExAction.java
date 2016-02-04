package panda.wing.action.template;

import java.util.List;

import panda.mvc.annotation.At;
import panda.wing.auth.Auth;
import panda.wing.constant.APP;
import panda.wing.constant.AUTH;
import panda.wing.entity.Template;

@At("${super_context}/template")
@Auth(AUTH.SUPER)
public class TemplateBulkExAction extends TemplateBulkAction {
	/**
	 * Constructor
	 */
	public TemplateBulkExAction() {
	}

	private void addReloadMessage() {
		addApplicationMessage(APP.TEMPLATE_LOAD_DATE);
	}

	@Override
	protected int deleteDataList(List<Template> dataList) {
		int cnt = super.deleteDataList(dataList);

		if (cnt > 0) {
	//		for (Template data : dataList) {
	//			Application.getDatabaseTemplateLoader().putTemplate(data.getName(), data.getLanguage(),
	//				data.getCountry(), null, null);
	//		}
			addReloadMessage();
		}
		return cnt;
	}
}