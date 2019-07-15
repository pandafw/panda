package panda.gems.pages.action;

import panda.app.action.crud.GenericImportAction;
import panda.gems.pages.entity.Page;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

public abstract class PageImportAction extends GenericImportAction<Page> {

	/**
	 * Constructor
	 */
	public PageImportAction() {
		setType(Page.class);
		setDisplayFields(Page.ID, Page.THUMBNAIL, Page.SLUG, Page.TITLE, Page.TAG, Page.PUBLISH_DATE, Page.STATUS, Page.CREATED_AT, Page.CREATED_BY, Page.UPDATED_AT, Page.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * importx
	 * @param arg argument
	 * @return result or view
	 */
	@At("import")
	@To(value=Views.SFTL, error=Views.SFTL)
	@TokenProtect
	public Object importx(@Param Arg arg) {
		return super.importx(arg);
	}
	
}

