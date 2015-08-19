package panda.wing.action.filepool;

import panda.filepool.dao.DaoFileItem;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validate;
import panda.wing.action.GenericEditAction;

public abstract class FilePoolEditAction extends GenericEditAction<DaoFileItem> {

	/**
	 * Constructor
	 */
	public FilePoolEditAction() {
		setType(DaoFileItem.class);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
}

