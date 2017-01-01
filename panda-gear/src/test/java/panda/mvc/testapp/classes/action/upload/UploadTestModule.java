package panda.mvc.testapp.classes.action.upload;

import panda.io.FileNames;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;
import panda.vfs.FileItem;

@At
@Ok("raw")
public class UploadTestModule {

	@At("/upload/(.*)$")
	public String test_upload(@Param("file") FileItem file) {
		return FileNames.getExtension(file.getName()) + "&" + file.getSize();
	}
}
