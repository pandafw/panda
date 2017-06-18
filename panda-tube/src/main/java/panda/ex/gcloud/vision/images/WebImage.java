package panda.ex.gcloud.vision.images;

/**
 * https://cloud.google.com/vision/docs/reference/rest/v1/images/annotate#WebImage
 */
public class WebImage {
	private String url;
	private Float score;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
}
