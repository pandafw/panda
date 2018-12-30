package panda.vfs;

public abstract class FileStores {
	public static void safeDelete(FileItem f) {
		if (f == null || !f.isExists()) {
			return;
		}
		
		try {
			f.delete();
		}
		catch (Throwable e) {
			// ignore
		}
	}
}
