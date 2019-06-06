package panda.tube.gcloud.vision.images;

import java.util.List;

public class Paragraph {

	private TextProperty property;
	private BoundingPoly boundingBox;
	private List<Word> words;

	public TextProperty getProperty() {
		return property;
	}
	public void setProperty(TextProperty property) {
		this.property = property;
	}
	public BoundingPoly getBoundingBox() {
		return boundingBox;
	}
	public void setBoundingBox(BoundingPoly boundingBox) {
		this.boundingBox = boundingBox;
	}
	public List<Word> getWords() {
		return words;
	}
	public void setWords(List<Word> words) {
		this.words = words;
	}
}
