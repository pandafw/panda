package panda.net.ftp;

/**
 * Implements some simple FTPFileFilter classes.
 */
public class FTPFileFilters {

	/**
	 * Accepts all FTPFile entries, including null.
	 */
	public static final FTPFileFilter ALL = new FTPFileFilter() {
		// @Override
		public boolean accept(FTPFile file) {
			return true;
		}
	};

	/**
	 * Accepts all non-null FTPFile entries.
	 */
	public static final FTPFileFilter NON_NULL = new FTPFileFilter() {
		// @Override
		public boolean accept(FTPFile file) {
			return file != null;
		}
	};

	/**
	 * Accepts all (non-null) FTPFile directory entries.
	 */
	public static final FTPFileFilter DIRECTORIES = new FTPFileFilter() {
		// @Override
		public boolean accept(FTPFile file) {
			return file != null && file.isDirectory();
		}
	};

}
