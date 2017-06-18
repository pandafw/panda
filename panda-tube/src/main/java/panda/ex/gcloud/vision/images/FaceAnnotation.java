package panda.ex.gcloud.vision.images;

import java.util.List;

public class FaceAnnotation {
	private BoundingPoly boundingPoly;
	private BoundingPoly fdBoundingPoly;
	private List<Landmark> landmarks;
	private Float rollAngle;
	private Float panAngle;
	private Float tiltAngle;
	private Float detectionConfidence;
	private Float landmarkingConfidence;
	private Likelihood joyLikelihood;
	private Likelihood sorrowLikelihood;
	private Likelihood angerLikelihood;
	private Likelihood surpriseLikelihood;
	private Likelihood underExposedLikelihood;
	private Likelihood blurredLikelihood;
	private Likelihood headwearLikelihood;

	public BoundingPoly getBoundingPoly() {
		return boundingPoly;
	}
	public void setBoundingPoly(BoundingPoly boundingPoly) {
		this.boundingPoly = boundingPoly;
	}
	public BoundingPoly getFdBoundingPoly() {
		return fdBoundingPoly;
	}
	public void setFdBoundingPoly(BoundingPoly fdBoundingPoly) {
		this.fdBoundingPoly = fdBoundingPoly;
	}
	public List<Landmark> getLandmarks() {
		return landmarks;
	}
	public void setLandmarks(List<Landmark> landmarks) {
		this.landmarks = landmarks;
	}
	public Float getRollAngle() {
		return rollAngle;
	}
	public void setRollAngle(Float rollAngle) {
		this.rollAngle = rollAngle;
	}
	public Float getPanAngle() {
		return panAngle;
	}
	public void setPanAngle(Float panAngle) {
		this.panAngle = panAngle;
	}
	public Float getTiltAngle() {
		return tiltAngle;
	}
	public void setTiltAngle(Float tiltAngle) {
		this.tiltAngle = tiltAngle;
	}
	public Float getDetectionConfidence() {
		return detectionConfidence;
	}
	public void setDetectionConfidence(Float detectionConfidence) {
		this.detectionConfidence = detectionConfidence;
	}
	public Float getLandmarkingConfidence() {
		return landmarkingConfidence;
	}
	public void setLandmarkingConfidence(Float landmarkingConfidence) {
		this.landmarkingConfidence = landmarkingConfidence;
	}
	public Likelihood getJoyLikelihood() {
		return joyLikelihood;
	}
	public void setJoyLikelihood(Likelihood joyLikelihood) {
		this.joyLikelihood = joyLikelihood;
	}
	public Likelihood getSorrowLikelihood() {
		return sorrowLikelihood;
	}
	public void setSorrowLikelihood(Likelihood sorrowLikelihood) {
		this.sorrowLikelihood = sorrowLikelihood;
	}
	public Likelihood getAngerLikelihood() {
		return angerLikelihood;
	}
	public void setAngerLikelihood(Likelihood angerLikelihood) {
		this.angerLikelihood = angerLikelihood;
	}
	public Likelihood getSurpriseLikelihood() {
		return surpriseLikelihood;
	}
	public void setSurpriseLikelihood(Likelihood surpriseLikelihood) {
		this.surpriseLikelihood = surpriseLikelihood;
	}
	public Likelihood getUnderExposedLikelihood() {
		return underExposedLikelihood;
	}
	public void setUnderExposedLikelihood(Likelihood underExposedLikelihood) {
		this.underExposedLikelihood = underExposedLikelihood;
	}
	public Likelihood getBlurredLikelihood() {
		return blurredLikelihood;
	}
	public void setBlurredLikelihood(Likelihood blurredLikelihood) {
		this.blurredLikelihood = blurredLikelihood;
	}
	public Likelihood getHeadwearLikelihood() {
		return headwearLikelihood;
	}
	public void setHeadwearLikelihood(Likelihood headwearLikelihood) {
		this.headwearLikelihood = headwearLikelihood;
	}
}
