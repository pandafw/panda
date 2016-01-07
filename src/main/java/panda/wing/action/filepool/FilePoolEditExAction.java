package panda.wing.action.filepool;

import panda.filepool.dao.DaoFileItem;
import panda.filepool.dao.FileDataQuery;
import panda.mvc.annotation.At;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/filepool")
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
