package panda.wing.action.filepool;

import panda.mvc.annotation.At;
import panda.vfs.dao.DaoFileItem;
import panda.vfs.dao.FileDataQuery;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

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
