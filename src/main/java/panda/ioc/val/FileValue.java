package panda.ioc.val;

import java.io.File;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class FileValue implements ValueProxy {

	private String path;

	public FileValue(String path) {
		this.path = path;
	}

	public Object get(IocMaking ing) {
		return new File(path);
	}

}
