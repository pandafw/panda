package panda.gems.pages.action;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.pages.V;
import panda.gems.pages.entity.Page;
import panda.gems.tager.Tagger;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageEditExAction extends PageEditAction {
	@IocInject
	private Tagger tagger;
	
	/**
	 * Constructor
	 */
	public PageEditExAction() {
		super();
	}

	@Override
	protected void insertData(Page data) {
		super.insertData(data);
		tagger.update(getDao(), V.PAGE_TAG_KIND, data.getId().toString(), data.getTag());
	}

	@Override
	protected int updateData(Page udat, Page sdat) {
		int r = super.updateData(udat, sdat);
		tagger.update(getDao(), V.PAGE_TAG_KIND, udat.getId().toString(), udat.getTag());
		return r;
	}

	@Override
	protected void deleteData(Page data) {
		tagger.delete(getDao(), V.PAGE_TAG_KIND, data.getId().toString());
		super.deleteData(data);
	}

}

