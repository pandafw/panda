package panda.app.action.filepool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
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
import panda.vfs.FilePool;
import panda.vfs.dao.DaoFilePool;
import panda.vfs.local.LocalFileItem;
import panda.vfs.local.LocalFilePool;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
@IocBean(singleton=false, create="initialize")
public class FilePoolListExAction extends FilePoolListAction {
	@IocInject
	private FilePool filePool;

	public void initialize() {
		setType(filePool.getItemType());
	}

	@Override
	protected void queryList(Queryer qr, long defLimit, long maxLimit) {
		if (filePool instanceof DaoFilePool) {
			super.queryList(qr, defLimit, maxLimit);
			return;
		}
		
		addLimitToPager(qr.getPager(), defLimit, maxLimit);

		if (filePool instanceof LocalFilePool) {
			try {
				List<LocalFileItem> ds = ((LocalFilePool)filePool).listFiles();
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

				// start, limit
				dataList = new ArrayList<FileItem>();
				int start = qr.getPager().getStart().intValue();
				int limit = start + qr.getPager().getLimit().intValue();
				if (limit > ds.size()) {
					limit = ds.size();
				}
				for (int i = start; i < limit; i++) {
					dataList.add(ds.get(i));
				}
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
	}

}
