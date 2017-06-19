package panda.app.action.filepool;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;
import panda.app.action.filepool.FilePoolEditAction;
import panda.vfs.dao.DaoFileItem;
import panda.vfs.dao.FileDataQuery;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
public class FilePoolEditExAction extends FilePoolEditAction {
	/**
	 * Constructor
	 */
	public FilePoolEditExAction() {
	}

	/**
	 * startDelete
	 * @param data data
	 */
	@Override
	protected void startDelete(DaoFileItem data) {
		super.startDelete(data);

		FileDataQuery fdq = new FileDataQuery();
		fdq.fid().equalTo(data.getId());
		getDao().deletes(fdq);
	}
}
