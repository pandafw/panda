package panda.wing.action.tool;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;

import com.opensymphony.xwork2.inject.Inject;

import panda.exts.fileupload.UploadManager;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.wing.action.work.AbstractSyncWorkAction;
import panda.wing.mvc.PandaStrutsConstants;


/**
 * delete upload temporary files
 */
public class FilePoolCleanupAction extends AbstractSyncWorkAction {
	/**
	 * fileSystemManager
	 */
	protected FileSystemManager fileSystemManager;
	
	/**
	 * cleanUpDir
	 */
	protected String cleanupDir;
	
	/**
	 * milliseconds since last modified. (default: 1h) 
	 */
	protected long expiredTime = 60 * 60 * 1000;

	/**
	 * @return fileSystemManager
	 * @throws FileSystemException if an error occurs
	 */
	public FileSystemManager getFileSystemManager() throws FileSystemException {
		if (fileSystemManager == null) {
			fileSystemManager = UploadManager.getFileSystemManager();
		}
		return fileSystemManager;
	}

	/**
	 * @param fileSystemManager the fileSystemManager to set
	 */
	@Inject(required=false)
	public void setManager(FileSystemManager fileSystemManager) {
		this.fileSystemManager = fileSystemManager;
	}

	/**
	 * @return the expiredTime
	 */
	public long getExpiredTime() {
		return expiredTime;
	}

	/**
	 * @param expiredTime the expiredTime to set
	 */
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	/**
	 * @return the cleanupDir
	 */
	public String getCleanupDir() {
		if (Strings.isEmpty(cleanupDir)) {
			return UploadManager.getSaveDir();
		}
		return cleanupDir;
	}

	/**
	 * @param cleanupDir the cleanupDir to set
	 */
	@Inject(value = PandaStrutsConstants.PANDA_MULTIPART_SAVEDIR, required = false)
	public void setCleanupDir(String cleanupDir) {
		this.cleanupDir = cleanupDir;
	}

	private static class CleanUpFileSelector implements FileSelector {
		private long expiredTime;
		
		public CleanUpFileSelector(long expiredTime) {
			super();
			this.expiredTime = expiredTime;
		}

		public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
			FileObject f = fileInfo.getFile();
			if (f.getType() == FileType.FILE) {
				long time = System.currentTimeMillis();
				if (time - f.getContent().getLastModifiedTime() > expiredTime) {
					return true;
				}
			}
			return false;
		}

		public boolean traverseDescendents(FileSelectInfo fileInfo)
				throws Exception {
			return true;
		}
	}
	
	@Override
	protected void doExecute() {
		try {
			FileObject repository = getFileSystemManager().resolveFile(getCleanupDir());
			
			if (repository.exists() && repository.getType() == FileType.FOLDER) {
				FileObject[] files = repository.findFiles(new CleanUpFileSelector(expiredTime));
				
				for (FileObject f : files) {
					if (f.delete()) {
						count++;
						if (log.isDebugEnabled()) {
							log.debug("File deleted - " + f.getName());
						}
					}
					else {
						if (log.isWarnEnabled()) {
							log.warn("Fail to delete file - " + f.getName());
						}
					}
				}
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
