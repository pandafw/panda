package panda.tube.gcloud.vision.images;

import java.util.List;

public class Block {
	private TextProperty property;
	private BoundingPoly boudingBox;
	private List<Paragraph> paragraphs;

	public TextProperty getProperty() {
		return property;
	}
	public void setProperty(TextProperty property) {
		this.property = property;
	}
	public BoundingPoly getBoudingBox() {
		return boudingBox;
	}
	public void setBoudingBox(BoundingPoly boudingBox) {
		this.boudingBox = boudingBox;
	}
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}
	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}
}
