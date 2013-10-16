package panda.mvc.fileupload;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

import panda.io.Files;
import panda.lang.Strings;

/**
 */
public class UploadManager {
	/**
	 * File System Manager
	 */
	private static FileSystemManager fileSystemManager;

	/**
	 * The directory in which uploaded files will be stored, if stored on disk.
	 */
	private static FileObject repository;

	/**
	 * The directory in which uploaded files will be stored, if stored on disk.
	 */
	private static String saveDir = System.getProperty("java.io.tmpdir");

	/**
	 * @return FileSystemManager
	 * @throws FileSystemException if an error occurs
	 */
	public static FileSystemManager getFileSystemManager() throws FileSystemException {
		if (fileSystemManager == null) {
			fileSystemManager = VFS.getManager();
		}
		return fileSystemManager;
	}

	/**
	 * @param fileSystemManager the fileSystemManager to set
	 */
	//TODO: @Inject(required=false)
	public static void setFileSystemManager(FileSystemManager fileSystemManager) {
		UploadManager.fileSystemManager = fileSystemManager;
	}

	/**
	 * @return the repository
	 * @throws IOException IOException
	 */
	public static FileObject getRepository() throws IOException {
		if (repository == null) {
			repository = getFileSystemManager().resolveFile(saveDir);
		}
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public static void setRepository(FileObject repository) {
		UploadManager.repository = repository;
	}

	/**
	 * @return the saveDir
	 */
	public static String getSaveDir() {
		return saveDir;
	}

	/**
	 * @param saveDir the saveDir to set
	 */
	//TODO: @Inject(value=NutsStrutsConstants.NUTS_MULTIPART_SAVEDIR, required=false)
	public static void setSaveDir(String saveDir) {
		UploadManager.saveDir = saveDir;
	}

	/**
	 * @param baseName base file name
	 * @return A unique file name
	 */
	public static String createUniqueFileName(String baseName) {
		String uniqueFileName = UUID.randomUUID().toString();
		if (baseName != null) {
			uniqueFileName +=  "." + Strings.remove(Files.getFileNameExtension(baseName), ' ');
		}
		return uniqueFileName;
	}
	
	/**
	 * Create a upload file
	 * @param baseName baseName
	 * @return a FileObject
	 * @throws IOException IOException
	 */
	public static FileObject createUploadFile(String baseName) throws IOException {
		String fileName = createUniqueFileName(baseName);
		return getFileSystemManager().resolveFile(getRepository(), fileName);
	}
	
	/**
	 * Create a upload file
	 * @param baseName baseName
	 * @return a FileObject
	 * @throws IOException IOException
	 */
	public static FileObject resolveUploadFile(String baseName) throws IOException {
		return getFileSystemManager().resolveFile(getRepository(), baseName);
	}
}
