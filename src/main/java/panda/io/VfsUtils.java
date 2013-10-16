package panda.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileType;

/**
 * VFS Utilities class.
 * @author yf.frank.wang@gmail.com
 */
public class VfsUtils {

	/**
	 * Removes a leading path from a second path.
	 * 
	 * @param lead The leading path, must not be null, must be absolute.
	 * @param path The path to remove from, must not be null, must be absolute.
	 * @return path's normalized absolute if it doesn't start with leading; path's path with
	 *         leading's path removed otherwise.
	 */
	public static String removeLeadingPath(FileObject lead, FileObject path) {
		return Files.removeLeadingPath(lead.getName().getPath(), path.getName().getPath());
	}

	/**
	 * read file content to byte array
	 * 
	 * @param file file
	 * @return byte array
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] toByteArray(FileObject file) throws IOException {
		InputStream is = null;
		try {
			is = file.getContent().getInputStream();
			byte[] b = Streams.toByteArray(is);
			return b;
		}
		finally {
			Streams.safeClose(is);
		}
	}

	/**
	 * Copy the contents of the given input File to the given output File.
	 * 
	 * @param in the file to Streams.copy from
	 * @param out the file to Streams.copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(FileObject in, FileObject out) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		
		try {
			is = in.getContent().getInputStream();
			os = out.getContent().getOutputStream();
			return Streams.copy(is, os);
		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	/**
	 * Copy the contents of the given input File to the given output File.
	 * 
	 * @param in the file to Streams.copy from
	 * @param os the output stream to Streams.copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(FileObject in, OutputStream os) throws IOException {
		InputStream is = null;
		
		try {
			is = in.getContent().getInputStream();
			return Streams.copy(is, os);
		}
		finally {
			Streams.safeClose(is);
		}
	}

	/**
	 * Write the contents of the given byte array to the given output File.
	 * 
	 * @param in the byte array to copy from
	 * @param out the file to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void write(byte[] in, FileObject out) throws IOException {
		OutputStream os = null;
		
		try {
			os = out.getContent().getOutputStream();
			Streams.write(in, os);
		}
		finally {
			Streams.safeClose(os);
		}
	}

	/**
	 * delete the file and sub files
	 * @param file file
	 * @return deleted file and folder count
	 * @throws IOException if an IO error occurs
	 */
	public static int deltree(FileObject file) throws IOException {
		return file.delete(new FileSelector() {
				public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
					return true;
				}

				public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
					return true;
				}
			});
	}
	
	/**
	 * zip folder
	 * @param src source file or folder
	 * @param zip output zip file
	 * @throws IOException if an IO error occurs
	 */
	public static void zip(final FileObject src, final FileObject zip) throws IOException {
		zip(src, zip, null);
	}
	
	/**
	 * zip folder
	 * @param src source file or folder
	 * @param zip output zip file
	 * @param depth depth
	 * @throws IOException if an IO error occurs
	 */
	public static void zip(final FileObject src, final FileObject zip, final int depth) throws IOException {
		zip(src, zip, new FileSelector() {
			public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
				return true;
			}

			public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
				return fileInfo.getDepth() < depth;
			}
		});
	}
	
	/**
	 * zip folder
	 * @param src source file or folder
	 * @param zip output zip file
	 * @param selector file selector
	 * @throws IOException if an IO error occurs
	 */
	public static void zip(final FileObject src, final FileObject zip, final FileSelector selector) throws IOException {
		OutputStream fos = zip.getContent().getOutputStream();
		final ZipOutputStream zos = new ZipOutputStream(fos);
		try {
			src.findFiles(new FileSelector() {
				public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
					FileObject fo = fileInfo.getFile();
					if (fo.getType().equals(FileType.FILE) && !fo.equals(zip)) {
						if (selector == null || selector.includeFile(fileInfo)) {
							zos.putNextEntry(new ZipEntry(removeLeadingPath(src, fo)));
							InputStream fis = fo.getContent().getInputStream();
							try {
								Streams.copy(fis, zos);
								zos.closeEntry();
							}
							finally {
								Streams.safeClose(fis);
							}
						}
					}
					return false;
				}
	
				public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
					return (selector == null || selector.traverseDescendents(fileInfo));
				}
			});
		}
		finally {
			Streams.safeClose(zos);
			Streams.safeClose(fos);
		}
	}

}
