package panda.wing.action.filepool;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.vfs.dao.DaoFileItem;
import panda.wing.action.crud.GenericEditAction;

public abstract class FilePoolEditAction extends GenericEditAction<DaoFileItem> {

	/**
	 * Constructor
	 */
	public FilePoolEditAction() {
		setType(DaoFileItem.class);
		addDisplayFields(DaoFileItem.ID, DaoFileItem.NAME, DaoFileItem.SIZE, DaoFileItem.DATE, DaoFileItem.FLAG);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * view
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object view(@Param DaoFileItem key) {
		return super.view(key);
	}

	/**
	 * view_input
	 */
	@At
	@To(value="sftl:~view", error="sftl:~view")
	public Object view_input(@Param DaoFileItem data) {
		return super.view_input(data);
	}

	/**
	 * print
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object print(@Param DaoFileItem key) {
		return super.print(key);
	}

	/**
	 * print_input
	 */
	@At
	@To(value="sftl:~print", error="sftl:~print")
	public Object print_input(@Param DaoFileItem data) {
		return super.print_input(data);
	}

	/**
	 * delete
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object delete(@Param DaoFileItem key) {
		return super.delete(key);
	}

	/**
	 * delete_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~delete")
	public Object delete_execute(@Param DaoFileItem key) {
		return super.delete_execute(key);
	}

}

