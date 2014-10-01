package panda.mvc.validator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.vfs2.FileObject;

import panda.exts.fileupload.UploadFile;
import panda.exts.fileupload.UploadImage;
import panda.exts.vfs.Vfs;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.image.JavaImageWrapper;
import panda.io.Streams;

import com.opensymphony.xwork2.validator.ValidationException;

/**
 * image size field validator.
 */
public class ImageFieldValidator extends AbstractFieldValidator {

	protected ImageWrapper getImage(Object value) throws ValidationException {
		ImageWrapper img = null;
		try {
			if (value == null) {
			}
			else if (value instanceof byte[]) {
				img = Images.i().read((byte[])value);
			}
			else if (value instanceof InputStream) {
				byte[] data = Streams.toByteArray((InputStream)value);
				img = Images.i().read(data);
			}
			else if (value instanceof BufferedImage) {
				img = new JavaImageWrapper((BufferedImage)value);
			}
			else if (value instanceof ImageWrapper) {
				img = (ImageWrapper)value;
			}
			else if (value instanceof URL) {
				byte[] data = Streams.toByteArray((URL)value);
				img = Images.i().read(data);
			}
			else if (value instanceof File) {
				File f = (File)value;
				if (f.exists() && f.isFile()) {
					byte[] data = Streams.toByteArray(f);
					img = Images.i().read(data);
				}
			}
			else if (value instanceof FileObject) {
				FileObject f = (FileObject)value;
				if (f.exists()) {
					byte[] data = Vfs.toByteArray(f);
					img = Images.i().read(data);
				}
			}
			else if (value instanceof UploadImage) {
				img = ((UploadImage)value).getImage();
			}
			else if (value instanceof UploadFile) {
				byte[] data = ((UploadFile)value).getData();
				if (data != null) {
					img = Images.i().read(data);
				}
			}
			else {
				throw new ValidationException("The value type of [" + getFieldName() + "] is invalid. (only RenderedImage/byte[]/File/URL/InputStream is allowed)");
			}
		}
		catch (Exception e) {
			log.warn("Failed to read image of [" + getFieldName() + "]", e);
		}
		return img;
	}
	
	/**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			return;
		}
		else if (value instanceof UploadFile) {
			byte[] data;
			try {
				data = ((UploadFile)value).getData();
				if (data == null) {
					return;
				}
			}
			catch (Exception e) {
				return;
			}

			try {
				value = Images.i().read(data);
			}
			catch (Exception e) {
				addFieldError(ac, object, getName());
			}
		}
		
		if (getImage(value) == null) {
			addFieldError(ac, object, getName());
		}
	}
}
