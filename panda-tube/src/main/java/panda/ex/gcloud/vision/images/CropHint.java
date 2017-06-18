package panda.ex.gcloud.vision.images;

/**
 * https://cloud.google.com/vision/docs/reference/rest/v1/images/annotate#CropHint
 */
public class CropHint {
	private BoundingPoly boundingPoly;
	private Float confidence;
	private Float importanceFraction;

	public BoundingPoly getBoundingPoly() {
		return boundingPoly;
	}
	public void setBoundingPoly(BoundingPoly boundingPoly) {
		this.boundingPoly = boundingPoly;
	}
	public Float getConfidence() {
		return confidence;
	}
	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}
	public Float getImportanceFraction() {
		return importanceFraction;
	}
	public void setImportanceFraction(Float importanceFraction) {
		this.importanceFraction = importanceFraction;
	}
}
