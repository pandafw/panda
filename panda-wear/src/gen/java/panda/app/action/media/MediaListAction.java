package panda.app.action.media;

import panda.app.action.crud.GenericListAction;
import panda.app.entity.Media;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.view.Views;

public abstract class MediaListAction extends GenericListAction<Media> {

	/**
	 * Constructor
	 */
	public MediaListAction() {
		setType(Media.class);
		addDisplayFields(Media.ID, Media.SLUG, Media.TAG, Media.FILE, Media.NAME, Media.SIZE, Media.WIDTH, Media.HEIGHT, Media.CREATED_AT, Media.CREATED_BY, Media.CREATED_BY_USER, Media.UPDATED_AT, Media.UPDATED_BY, Media.UPDATED_BY_USER);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object list(@Param @VisitValidate Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_print
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object list_print(@Param @VisitValidate Queryer qr) {
		return super.list_print(qr);
	}
	
	/**
	 * list_json
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(Views.SJSON)
	public Object list_json(@Param @VisitValidate Queryer qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(Views.SXML)
	public Object list_xml(@Param @VisitValidate Queryer qr) {
		return super.list_xml(qr);
	}
	
}

