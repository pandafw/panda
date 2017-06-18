package panda.ex.gcloud.vision.images;

public class DetectedLanguage {
	private String languageCode;
	private Float confidence;

	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public Float getConfidence() {
		return confidence;
	}
	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}
}
