package panda.mvc.testapp.classes.action.upload;

import panda.filepool.FileItem;
import panda.ioc.annotation.IocBean;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Ok;
import panda.mvc.annotation.param.Param;

@IocBean
@Ok("raw")
public class UploadTestModule {

	@At("/upload/*")
	public String test_upload(String type, @Param("file") FileItem file) {
		return type + "&" + file.getSize();
	}
}
