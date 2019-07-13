package panda.gems.pages.action;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.gems.pages.entity.Page;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

public abstract class PageBulkDisableAction extends GenericBulkAction<Page> {

	/**
	 * Constructor
	 */
	public PageBulkDisableAction() {
		setType(Page.class);
		setDisplayFields(Page.ID, Page.THUMBNAIL, Page.SLUG, Page.TITLE, Page.TAG, Page.PUBLISH_DATE, Page.STATUS, Page.CREATED_AT, Page.CREATED_BY, Page.UPDATED_AT, Page.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdisable
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object bdisable(@Param Map<String, String[]> args) {
		return super.bupdate(args);
	}

	/**
	 * bdisable_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~bdisable")
	@TokenProtect
	public Object bdisable_execute(@Param Map<String, String[]> args) {
		return super.bupdate_execute(args);
	}
	
}

