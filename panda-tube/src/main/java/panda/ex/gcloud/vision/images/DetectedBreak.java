package panda.ex.gcloud.vision.images;

public class DetectedBreak {
	private BreakType type;
	private Boolean isPrefix;

	public BreakType getType() {
		return type;
	}
	public void setType(BreakType type) {
		this.type = type;
	}
	public Boolean getIsPrefix() {
		return isPrefix;
	}
	public void setIsPrefix(Boolean isPrefix) {
		this.isPrefix = isPrefix;
	}
}
