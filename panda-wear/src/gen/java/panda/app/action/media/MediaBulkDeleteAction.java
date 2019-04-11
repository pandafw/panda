package panda.app.action.media;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.entity.Media;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

public abstract class MediaBulkDeleteAction extends GenericBulkAction<Media> {

	/**
	 * Constructor
	 */
	public MediaBulkDeleteAction() {
		setType(Media.class);
		addDisplayFields(Media.ID, Media.SLUG, Media.TAG, Media.FILE, Media.NAME, Media.SIZE, Media.WIDTH, Media.HEIGHT, Media.CREATED_AT, Media.CREATED_BY, Media.UPDATED_AT, Media.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdelete
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~bdelete")
	@TokenProtect
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

