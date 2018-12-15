package panda.app.action.media;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.entity.Media;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.vfs.FileItem;

public abstract class MediaBulkDeleteAction extends GenericBulkAction<Media> {
	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	protected FileItem file;

	/**
	 * Constructor
	 */
	public MediaBulkDeleteAction() {
		setType(Media.class);
		addDisplayFields(Media.ID, Media.TAG, Media.FILE, Media.NAME, Media.SIZE, Media.WIDTH, Media.HEIGHT, Media.CREATED_AT, Media.CREATED_BY, Media.CREATED_BY_USER, Media.UPDATED_AT, Media.UPDATED_BY, Media.UPDATED_BY_USER);
	}

	/**
	 * @return the file
	 */
	public FileItem getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(FileItem file) {
		this.file = file;
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

