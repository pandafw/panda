package panda.mvc.fileupload;

import java.io.IOException;

import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.FileNames;
import panda.io.VfsUtils;
import panda.lang.Strings;

/**
 * Upload Image Bean Object
 */
@SuppressWarnings("serial")
public class UploadImage extends UploadFile {

	private ImageWrapper image;
	
	/**
	 * constructor
	 */
	public UploadImage() {
		setContentType("image");
	}

	/**
	 * @return the image
	 * @throws IOException if an I/O error occurs
	 */
	public ImageWrapper getImage() throws Exception {
		if (image == null) {
			byte[] data = getData();
			if (data != null) {
				image = Images.me().makeImage(data);
				String fm = FileNames.getExtension(getSaveName());
				if (Strings.isNotEmpty(fm)) {
					image.setFormat(fm);
				}
			}
		}
		return image;
	}

	/**
	 * resize the image
	 * @param scale scale
	 * @throws Exception if an error occurs
	 */
	public void resize(int scale) throws Exception {
		ImageWrapper img = getImage();
		if (img == null) {
			return;
		}

		image = img.resize(scale);

		data = image.getData();

		newFile("r." + image.getFormat());

		VfsUtils.write(data, file);
	}

	/**
	 * resize the image
	 * @param width width
	 * @param height height
	 * @throws Exception if an error occurs
	 */
	public void resize(int width, int height) throws Exception {
		ImageWrapper img = getImage();
		if (img == null) {
			return;
		}

		image = img.resize(width, height);

		data = image.getData();

		if (file != null) {
			VfsUtils.write(data, file);
		}
	}
}
