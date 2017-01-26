package panda.wing.action.filepool;

import java.util.Map;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.vfs.dao.DaoFileItem;
import panda.wing.action.crud.GenericBulkAction;

public abstract class FilePoolBulkAction extends GenericBulkAction<DaoFileItem> {

	/**
	 * Constructor
	 */
	public FilePoolBulkAction() {
		setType(DaoFileItem.class);
		addDisplayFields(DaoFileItem.ID, DaoFileItem.NAME, DaoFileItem.SIZE, DaoFileItem.DATE, DaoFileItem.FLAG);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdelete
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

