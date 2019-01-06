package panda.app.action.template;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.app.entity.Template;
import panda.dao.query.DataQuery;
import panda.mvc.annotation.At;

@At("${super_path}/template")
@Auth(AUTH.SUPER)
public class TemplateBulkDisableExAction extends TemplateBulkDisableAction {

	public TemplateBulkDisableExAction() {
		super();
	}

	@Override
	protected Template getBulkUpdateSample(List<Template> dataList, DataQuery<Template> dq) {
		Template d = new Template();
		d.setStatus(VAL.STATUS_DISABLED);

		dq.excludeAll().include(Template.STATUS);

		return d;
	}

}
