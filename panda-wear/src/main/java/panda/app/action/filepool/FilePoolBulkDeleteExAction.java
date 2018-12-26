package panda.app.action.filepool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.mutable.MutableInt;
import panda.mvc.annotation.At;
import panda.vfs.FileItem;
import panda.vfs.FilePool;
import panda.vfs.dao.DaoFilePool;
import panda.vfs.dao.FileDataQuery;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
@IocBean(singleton=false, create="initialize")
public class FilePoolBulkDeleteExAction extends FilePoolBulkDeleteAction {
	@IocInject
	private FilePool filePool;

	public void initialize() {
		setType(filePool.getItemType());
	}

	@Override
	protected List<FileItem> selectDataList(List<FileItem> dataList, boolean filter) {
		if (filePool instanceof DaoFilePool) {
			return super.selectDataList(dataList, filter);
		}

		try {
			List<FileItem> fis = new ArrayList<FileItem>();
			for (FileItem a : dataList) {
				FileItem fi = filePool.findFile(a.getId());
				if (fi != null && fi.isExists()) {
					fis.add(fi);
				}
			}
			return fis;
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * execBulkDelete
	 * @param dataList data list
	 * @param count deleted count
	 */
	@Override
	protected void execBulkDelete(final List<FileItem> dataList, final MutableInt count) {
		if (filePool instanceof DaoFilePool) {
			super.execBulkDelete(dataList, count);
			return;
		}

		deleteDataList(dataList, count);
	}


	@Override
	protected void deleteDataList(List<FileItem> dataList, MutableInt count) {
		if (filePool instanceof DaoFilePool) {
			List<String> ids = new ArrayList<String>(dataList.size());
			for (FileItem f : dataList) {
				ids.add(f.getId());
			}

			FileDataQuery fdq = new FileDataQuery();
			fdq.fid().in(ids);
			getDao().deletes(fdq);
		}
		
		super.deleteDataList(dataList, count);
	}

	@Override
	protected int deleteData(FileItem data) {
		if (filePool instanceof DaoFilePool) {
			return super.deleteData(data);
		}
		
		try {
			data.delete();
			return 1;
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
