package panda.mvc.filepool;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.vfs.FilePool;
import panda.vfs.gcs.GcsFilePool;

@IocBean(type=FilePool.class, create="initialize")
public class MvcGcsFilePool extends GcsFilePool {

	/**
	 * @param bucket the bucket to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILEPOOL_GCS_BUCKET, required=false)
	public void setBucket(String bucket) {
		super.setBucket(bucket);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILEPOOL_MAXAGE, required=false)
	public void setMaxAge(int maxAge) {
		super.setMaxAge(maxAge);
	}
}
