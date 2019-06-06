package panda.tube.gcloud.vision.images;

import java.util.List;

/**
 * https://cloud.google.com/vision/docs/reference/rest/v1/images/annotate#CropHintsAnnotation
 */
public class CropHintsAnnotation {
	private List<CropHint> cropHints;

	public List<CropHint> getCropHints() {
		return cropHints;
	}

	public void setCropHints(List<CropHint> cropHints) {
		this.cropHints = cropHints;
	}
}
