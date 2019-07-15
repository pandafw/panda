package panda.gems.pages.action;

import java.util.ArrayList;
import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.pages.V;
import panda.gems.pages.entity.Page;
import panda.gems.tager.Tagger;
import panda.ioc.annotation.IocInject;
import panda.lang.mutable.MutableInt;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageBulkDeleteExAction extends PageBulkDeleteAction {

	@IocInject
	private Tagger tagger;
	
	public PageBulkDeleteExAction() {
		super();
	}

	@Override
	protected void deleteDataList(List<Page> dataList, MutableInt count) {
		List<String> ids = new ArrayList<String>(dataList.size());
		for (Page f : dataList) {
			ids.add(f.getId().toString());
		}

		tagger.deletes(getDao(), V.PAGE_TAG_KIND, ids);

		super.deleteDataList(dataList, count);
	}
}
