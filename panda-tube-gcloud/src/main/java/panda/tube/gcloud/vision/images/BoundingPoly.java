package panda.tube.gcloud.vision.images;

import java.util.List;

public class BoundingPoly {
	private List<Vertex> vertices;

	public List<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vertex> vertices) {
		this.vertices = vertices;
	}
}
