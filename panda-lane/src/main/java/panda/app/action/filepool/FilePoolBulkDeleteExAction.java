package panda.app.action.filepool;

import java.util.ArrayList;
import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;
import panda.vfs.FileItem;
import panda.vfs.dao.DaoFileItem;
import panda.vfs.dao.FileDataQuery;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
public class FilePoolBulkDeleteExAction extends FilePoolBulkDeleteAction {
	/**
	 * Constructor
	 */
	public FilePoolBulkDeleteExAction() {
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
