package panda.ex.gcloud.vision.images;

import java.util.List;

public class ImageContext {
	private LatLongRect latLongRect;
	private List<String> languageHints;
	private List<CropHintsParam> cropHintsParams;

	public LatLongRect getLatLongRect() {
		return latLongRect;
	}
	public void setLatLongRect(LatLongRect latLongRect) {
		this.latLongRect = latLongRect;
	}
	public List<String> getLanguageHints() {
		return languageHints;
	}
	public void setLanguageHints(List<String> languageHints) {
		this.languageHints = languageHints;
	}
	public List<CropHintsParam> getCropHintsParams() {
		return cropHintsParams;
	}
	public void setCropHintsParams(List<CropHintsParam> cropHintsParams) {
		this.cropHintsParams = cropHintsParams;
	}
}
