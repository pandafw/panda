package panda.wing.action.filepool;

import java.util.ArrayList;
import java.util.List;

import panda.filepool.FileItem;
import panda.filepool.dao.DaoFileItem;
import panda.filepool.dao.FileDataQuery;
import panda.mvc.annotation.At;
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
