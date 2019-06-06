package panda.tube.gcloud.vision.images;

import java.util.List;

public class AnnotateApiResponse {
	private List<AnnotateImageResponse> responses;

	public List<AnnotateImageResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<AnnotateImageResponse> responses) {
		this.responses = responses;
	}
}
