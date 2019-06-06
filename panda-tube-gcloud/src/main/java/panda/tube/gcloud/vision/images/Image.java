package panda.tube.gcloud.vision.images;

import panda.codec.binary.Base64;

public class Image {
	private String content;
	private ImageSource source;

	public Image() {
	}
	
	public Image(String content) {
		this.content = content;
	}

	public Image(ImageSource source) {
		this.source = source;
	}

	public static Image fromContent(byte[] content) {
		return new Image(Base64.encodeBase64String(content));
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public ImageSource getSource() {
		return source;
	}
	public void setSource(ImageSource source) {
		this.source = source;
	}
}
