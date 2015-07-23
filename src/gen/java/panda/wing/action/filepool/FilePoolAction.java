package panda.wing.action.filepool;

import java.util.List;
import panda.filepool.dao.DaoFileItem;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.validation.annotation.Validate;
import panda.wing.action.BaseEntityDaoAction;

public class FilePoolAction extends BaseEntityDaoAction<DaoFileItem> {

	/**
	 * Constructor
	 */
	public FilePoolAction() {
		setType(DaoFileItem.class);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list(@Param @Validate Queryer qr) {
		return super.list();
	}
	
	/**
	 * list_csv
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_csv(@Param @Validate Queryer qr) {
		return super.list_csv();
	}
	
	/**
	 * list_print
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_print(@Param @Validate Queryer qr) {
		return super.list_print();
	}
	
	/**
	 * list_popup
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_popup(@Param @Validate Queryer qr) {
		return super.list_popup();
	}
	
}

