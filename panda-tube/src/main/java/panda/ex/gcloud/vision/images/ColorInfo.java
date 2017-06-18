package panda.ex.gcloud.vision.images;

public class ColorInfo {
	private String color;
	private Float score;
	private Float pixelFraction;

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public Float getPixelFraction() {
		return pixelFraction;
	}
	public void setPixelFraction(Float pixelFraction) {
		this.pixelFraction = pixelFraction;
	}
}
