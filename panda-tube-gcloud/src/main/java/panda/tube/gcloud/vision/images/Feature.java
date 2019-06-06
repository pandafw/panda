package panda.tube.gcloud.vision.images;

public class Feature {
	private FeatureType type;
	private int maxResults;

	public Feature() {
	}
	
	public Feature(FeatureType type) {
		this.type = type;
	}

	public Feature(FeatureType type, int maxResults) {
		this.type = type;
		this.maxResults = maxResults;
	}

	public FeatureType getType() {
		return type;
	}
	public void setType(FeatureType type) {
		this.type = type;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}
