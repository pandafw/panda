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

		List<FileItem> fis = new ArrayList<FileItem>();
		for (FileItem a : dataList) {
			FileItem fi = filePool.findFile(a.getId());
			if (fi != null && fi.isExists()) {
				fis.add(fi);
			}
		}
		return fis;
	}

	/**
	 * execBulkDelete
	 * @param dataList data list
	 * @param count deleted count
	 */
	@Override
	protected void execBulkDelete(final List<FileItem> dataList, final MutableInt count) {
		deleteDataList(dataList, count);
	}

	@Override
	protected int deleteData(FileItem data) {
		try {
			data.delete();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return 1;
	}
}
