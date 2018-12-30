package panda.app.action.vfs;

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
import panda.vfs.FileStore;
import panda.vfs.dao.DaoFileStore;
import panda.vfs.dao.FileDataQuery;

@At("${super_path}/fileitem")
@Auth(AUTH.SUPER)
@IocBean(singleton=false, create="initialize")
public class FileItemBulkDeleteExAction extends FileItemBulkDeleteAction {
	@IocInject
	private FileStore fileStore;

	public void initialize() {
		setType(fileStore.getItemType());
	}

	@Override
	protected List<FileItem> selectDataList(List<FileItem> dataList, boolean filter) {
		if (fileStore instanceof DaoFileStore) {
			return super.selectDataList(dataList, filter);
		}

		try {
			List<FileItem> fis = new ArrayList<FileItem>();
			for (FileItem a : dataList) {
				FileItem fi = fileStore.getFile(a.getName());
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
		if (fileStore instanceof DaoFileStore) {
			super.execBulkDelete(dataList, count);
			return;
		}

		deleteDataList(dataList, count);
	}


	@Override
	protected void deleteDataList(List<FileItem> dataList, MutableInt count) {
		if (fileStore instanceof DaoFileStore) {
			List<String> nms = new ArrayList<String>(dataList.size());
			for (FileItem f : dataList) {
				nms.add(f.getName());
			}

			FileDataQuery fdq = new FileDataQuery();
			fdq.fnm().in(nms);
			getDao().deletes(fdq);
		}
		
		super.deleteDataList(dataList, count);
	}

	@Override
	protected int deleteData(FileItem data) {
		if (fileStore instanceof DaoFileStore) {
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
