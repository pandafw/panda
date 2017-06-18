package panda.ex.gcloud.vision.images;

import java.util.List;

public class CropHintsParam {
	private List<Float> aspectRatios;

	public List<Float> getAspectRatios() {
		return aspectRatios;
	}

	public void setAspectRatios(List<Float> aspectRatios) {
		this.aspectRatios = aspectRatios;
	}
}
