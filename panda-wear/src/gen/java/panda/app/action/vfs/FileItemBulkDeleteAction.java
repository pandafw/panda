package panda.app.action.vfs;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.vfs.FileItem;

public abstract class FileItemBulkDeleteAction extends GenericBulkAction<FileItem> {

	/**
	 * Constructor
	 */
	public FileItemBulkDeleteAction() {
		setType(FileItem.class);
		addDisplayFields(FileItem.NAME, FileItem.SIZE, FileItem.DATE);
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
	@At
	@To(value=Views.SFTL, error="sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

