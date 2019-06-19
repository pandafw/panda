package panda.gems.files.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import panda.app.auth.Auth;
import panda.app.bean.ListResult;
import panda.app.constant.AUTH;
import panda.gems.files.action.FileItemListAction;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Comparators;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.Sorter;
import panda.vfs.FileItem;
import panda.vfs.FileStore;
import panda.vfs.dao.DaoFileStore;

@At("${!!admin_path|||'/admin'}/files")
@Auth(AUTH.ADMIN)
@IocBean(singleton=false, create="initialize")
public class FileItemListExAction extends FileItemListAction {
	@IocInject
	private FileStore fileStore;

	public void initialize() {
		setType(fileStore.getItemType());
	}

	@Override
	protected void queryList(final ListResult<FileItem> r, final Queryer qr, long defLimit, long maxLimit) {
		if (fileStore instanceof DaoFileStore) {
			super.queryList(r, qr, defLimit, maxLimit);
			return;
		}

		addLimitToPager(qr.getPager(), defLimit, maxLimit);

		List<FileItem> ds;
		try {
			ds = fileStore.listFiles();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}

		qr.getPager().setTotal(ds.size());
		qr.getPager().normalize();

		// TODO: filter
		
		// sort
		if (Strings.isNotEmpty(qr.getSorter().getColumn())) {
			Comparator<FileItem> c = Comparators.property(FileItem.class, qr.getSorter().getColumn());
			if (Sorter.DESC.equalsIgnoreCase(qr.getSorter().getDirection())) {
				c = Comparators.reverse(c);
			}
			
			Collections.sort(ds, c);
		}

		r.setPage(qr.getPager());
		
		// start, limit
		List<FileItem> list = new ArrayList<FileItem>();
		int start = qr.getPager().getStart().intValue();
		int limit = start + qr.getPager().getLimit().intValue();
		if (limit > ds.size()) {
			limit = ds.size();
		}
		for (int i = start; i < limit; i++) {
			list.add(ds.get(i));
		}
		
		r.setList(list);
	}

}
