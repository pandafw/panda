package panda.wing.action.filepool;

import java.util.ArrayList;
import java.util.List;

import panda.mvc.annotation.At;
import panda.vfs.FileItem;
import panda.vfs.dao.DaoFileItem;
import panda.vfs.dao.FileDataQuery;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/filepool")
@Auth(AUTH.SUPER)
public class FilePoolBulkExAction extends FilePoolBulkAction {
	/**
	 * Constructor
	 */
	public FilePoolBulkExAction() {
	}

	/**
	 * startBulkDelete
	 * @param dataList dataList
	 */
	@Override
	protected void startBulkDelete(List<DaoFileItem> dataList) {
		super.startBulkDelete(dataList);
		
		List<Long> ids = new ArrayList<Long>(dataList.size());
		for (FileItem f : dataList) {
			ids.add(f.getId());
		}

		FileDataQuery fdq = new FileDataQuery();
		fdq.fid().in(ids);
		getDao().deletes(fdq);
	}
}
