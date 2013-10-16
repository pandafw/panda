package panda.mvc.fileupload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import panda.io.VfsUtils;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 */
@SuppressWarnings("serial")
public class UploadFile implements Serializable {

	protected static final Log log = Logs.getLog(UploadFile.class);

	/**
	 * constructor
	 */
	public UploadFile() {
	}

	protected FileObject file;
	protected String contentType;
	protected String fileName;
	
	protected byte[] data;
	protected Long dataLength;

	/**
	 * @return the file
	 */
	public FileObject getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(FileObject file) {
		this.file = file;
	}
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return the saveName
	 */
	public String getSaveName() {
		return file == null ? null : file.getName().getBaseName();
	}

	/**
	 * @param saveName the saveName to set
	 * @throws IOException if an I/O error occurs
	 */
	public void setSaveName(String saveName) throws IOException {
		if (Strings.isNotEmpty(saveName)) {
			this.file = UploadManager.resolveUploadFile(saveName);
		}
	}
	
	/**
	 * @return the saveTime
	 */
	public Long getSaveTime() {
		Long st = null;
		if (file != null) {
			try {
				st = file.getContent().getLastModifiedTime();
			}
			catch (IOException ex) {
			}
		}
		return st;
	}

	/**
	 * @return the data
	 * @throws FileNotFoundException if file not found
	 * @throws IOException if an I/O error occurs
	 */
	public byte[] getData() throws FileNotFoundException, IOException {
		if (data == null && file != null && file.exists()) {
			data = VfsUtils.toByteArray(file);
		}
		return data;
	}

	/**
	 * @param data the data to set
	 * @throws IOException IOException
	 */
	public void setData(byte[] data) throws IOException {
		this.data = data;
		if (this.file != null) {
			VfsUtils.write(this.data, this.file);
		}
		this.dataLength = (data == null ? null : (long)data.length);
	}

	/**
	 * @return the dataLength
	 */
	public Long getDataLength() {
		return dataLength;
	}

	/**
	 * @param dataLength the dataLength to set
	 */
	public void setDataLength(Long dataLength) {
		this.dataLength = dataLength;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		if (data != null) {
			return data.length;
		}
		else if (file != null) {
			try {
				if (file.exists()) {
					return file.getContent().getSize();
				}
			}
			catch (FileSystemException e) {
				log.warn("Failed to getFileSize", e);
			}
		}
		else if (dataLength != null) {
			return dataLength;
		}

		return 0L;
	}

	/**
	 * create a upload file
	 * @throws IOException IOException
	 */
	public void newFile() throws IOException {
		this.file = UploadManager.createUploadFile(this.fileName);
	}

	/**
	 * create a upload file
	 * @param baseName base name
	 * @throws IOException IOException
	 */
	public void newFile(String baseName) throws IOException {
		this.file = UploadManager.createUploadFile(baseName);
	}

	/**
	 * @return true if file exists
	 */
	public boolean isExist() {
		if (data != null) {
			return true;
		}
		
		if (file != null) {
			try {
				return file.exists();
			}
			catch (FileSystemException e) {
				log.warn("Failed to get file exist", e);
			}
		}
		
		if (dataLength != null && dataLength > 0) {
			return true;
		}
		
		return false;
	}
	
	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("file: ").append(file);
		sb.append(", ");
		sb.append("contentType: ").append(contentType);
		sb.append(", ");
		sb.append("fileName: ").append(fileName);
		sb.append(", ");
		sb.append("fileSize: ").append(getFileSize());
		sb.append(" }");
		
		return sb.toString();
	}
}
