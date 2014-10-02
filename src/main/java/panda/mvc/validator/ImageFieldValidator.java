package panda.mvc.validator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import panda.filepool.FileItem;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.image.JavaImageWrapper;
import panda.io.Streams;
import panda.mvc.ActionContext;

public class ImageFieldValidator extends AbstractFieldValidator {

	private boolean required;
	private Integer minWidth;
	private Integer minHeight;
	private Integer maxWidth;
	private Integer maxHeight;

	/**
	 * image width
	 */
	private Integer width;
	
	/**
	 * image height
	 */
	private Integer height;
	

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @return the minWidth
	 */
	public Integer getMinWidth() {
		return minWidth;
	}

	/**
	 * @param minWidth the minWidth to set
	 */
	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * @return the minHeight
	 */
	public Integer getMinHeight() {
		return minHeight;
	}

	/**
	 * @param minHeight the minHeight to set
	 */
	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}

	/**
	 * @return the maxWidth
	 */
	public Integer getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @param maxWidth the maxWidth to set
	 */
	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * @return the maxHeight
	 */
	public Integer getMaxHeight() {
		return maxHeight;
	}

	/**
	 * @param maxHeight the maxHeight to set
	 */
	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}

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
			else if (value instanceof FileItem) {
				FileItem f = (FileItem)value;
				img = Images.i().read(f.getData());
			}
			else {
				throw new ValidationException("The value type of '" + getName() + "' is invalid. (only RenderedImage/byte[]/File/URL/InputStream is allowed)");
			}
		}
		catch (Exception e) {
			// skip
		}
		return img;
	}

	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());
		if (value == null) {
			return !required;
		}

		ImageWrapper img = getImage(value);
		if (img == null) {
			return !required;
		}

		width = img.getWidth();
		height = img.getHeight();
		
		if ((minWidth != null && width < minWidth) 
				|| (minHeight != null && height < minHeight)
				|| (maxWidth != null && width > maxWidth) 
				|| (maxHeight != null && height > maxHeight)) {
			addFieldError(ac, getName(), value);
			return false;
		}
		
		return true;
	}
}
