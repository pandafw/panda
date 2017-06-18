package panda.ex.gcloud.vision.images;

import java.util.List;

public class Page {
	private TextProperty property;
	private int width;
	private int height;
	private List<Block> blocks;

	public TextProperty getProperty() {
		return property;
	}
	public void setProperty(TextProperty property) {
		this.property = property;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public List<Block> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}
}
