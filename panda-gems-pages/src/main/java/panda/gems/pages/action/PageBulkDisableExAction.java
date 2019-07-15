package panda.gems.pages.action;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.dao.query.DataQuery;
import panda.gems.pages.entity.Page;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageBulkDisableExAction extends PageBulkDisableAction {

	public PageBulkDisableExAction() {
		super();
	}

	@Override
	protected Page getBulkUpdateSample(List<Page> dataList, DataQuery<Page> dq) {
		dq.excludeAll().include(Page.STATUS);

		Page d = new Page();
		d.setStatus(VAL.STATUS_DISABLED);
		return d;
	}

}
