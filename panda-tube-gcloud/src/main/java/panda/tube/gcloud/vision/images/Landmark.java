package panda.tube.gcloud.vision.images;

public class Landmark {
	private LandmarkType type;
	private Position position;

	public LandmarkType getType() {
		return type;
	}
	public void setType(LandmarkType type) {
		this.type = type;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
	
}
