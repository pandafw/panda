package panda.app.action.filepool;

import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validates;
import panda.vfs.dao.DaoFileItem;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
public class FilePoolListAction extends GenericListAction<DaoFileItem> {

	/**
	 * Constructor
	 */
	public FilePoolListAction() {
		setType(DaoFileItem.class);
		addDisplayFields(DaoFileItem.ID, DaoFileItem.NAME, DaoFileItem.SIZE, DaoFileItem.DATE, DaoFileItem.FLAG);
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
	@To(value=View.SFTL, error=View.SFTL)
	public Object list(@Param @Validates Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_print
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list_print(@Param @Validates Queryer qr) {
		return super.list_print(qr);
	}
	
	/**
	 * list_json
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(all=View.JSON)
	public Object list_json(@Param @Validates Queryer qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(all=View.XML)
	public Object list_xml(@Param @Validates Queryer qr) {
		return super.list_xml(qr);
	}
	
}

