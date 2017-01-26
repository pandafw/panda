package panda.mvc.testapp.classes.action.upload;

import panda.io.FileNames;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.vfs.FileItem;

@At
@To(View.RAW)
public class UploadTestModule {

	@At("/upload/(.*)$")
	public String test_upload(@Param("file") FileItem file) {
		return FileNames.getExtension(file.getName()) + "&" + file.getSize();
	}
}
