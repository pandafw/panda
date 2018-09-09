package panda.vfs;

public abstract class FilePools {
	public static void safeDelete(FileItem f) {
		if (f == null || !f.isExists()) {
			return;
		}
		
		try {
			f.delete();
		}
		catch (Exception e) {
			// ignore
		}
	}
}
