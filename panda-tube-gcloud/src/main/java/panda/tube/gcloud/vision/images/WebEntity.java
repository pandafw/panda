package panda.tube.gcloud.vision.images;

/**
 * https://cloud.google.com/vision/docs/reference/rest/v1/images/annotate#WebEntity
 */
public class WebEntity {
	private String entityId;
	private Float score;
	private String description;

	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
