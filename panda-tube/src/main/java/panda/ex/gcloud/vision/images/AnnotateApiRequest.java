package panda.ex.gcloud.vision.images;

import java.util.ArrayList;
import java.util.List;

public class AnnotateApiRequest {
	private List<AnnotateImageRequest> requests;

	public List<AnnotateImageRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<AnnotateImageRequest> requests) {
		this.requests = requests;
	}
	
	public void addRequest(AnnotateImageRequest request) {
		if (requests == null) {
			requests = new ArrayList<AnnotateImageRequest>();
		}
		requests.add(request);
	}
}
