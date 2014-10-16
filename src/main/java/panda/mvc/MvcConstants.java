package panda.mvc;

import panda.ioc.IocConstants;

public interface MvcConstants extends IocConstants {
	public static final String FILEPOOL_LOCAL_PATH = "ref:panda.filepool.local.path";
	public static final String FILEPOOL_DAO_BLOCK_SIZE = "ref:panda.filepool.dao.block.size";
	public static final String FREEMARKER_TEMPLATE_PATH = "ref:panda.freemarker.path";
}
