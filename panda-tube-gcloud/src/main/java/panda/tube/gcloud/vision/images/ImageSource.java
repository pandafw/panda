package panda.tube.gcloud.vision.images;

public class ImageSource {
	private String gcsImageUri;
	private String imageUri;

	public String getGcsImageUri() {
		return gcsImageUri;
	}
	public void setGcsImageUri(String gcsImageUri) {
		this.gcsImageUri = gcsImageUri;
	}

	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	
	public static ImageSource fromUri(String uri) {
		ImageSource is = new ImageSource();
		is.setImageUri(uri);
		return is;
	}
	
	public static ImageSource fromGcs(String uri) {
		ImageSource is = new ImageSource();
		is.setGcsImageUri(uri);
		return is;
	}
}
