package panda.mvc.testapp.classes.action.upload;

import panda.io.FileNames;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.vfs.FileItem;

@At
@To(Views.RAW)
public class UploadTestModule {

	@At("/upload/(.*)$")
	public String test_upload(@Param("file") FileItem file) {
		return FileNames.getExtension(file.getName()) + "&" + file.getSize();
	}
}
