package panda.wing.action.filepool;

import java.util.ArrayList;
import java.util.List;

import panda.filepool.FileItem;
import panda.filepool.dao.DaoFileItem;
import panda.filepool.dao.FileDataQuery;
import panda.mvc.annotation.At;

@At("${admin_context}/filepool")
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
