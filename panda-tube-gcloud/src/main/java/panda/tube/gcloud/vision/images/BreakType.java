package panda.tube.gcloud.vision.images;

public enum BreakType {
	UNKNOWN,	// Unknown break label type.
	SPACE,		// Regular space.
	SURE_SPACE,		// Sure space (very wide).
	EOL_SURE_SPACE,	// Line-wrapping break.
	HYPHEN,			// End-line hyphen that is not present in text; does not co-occur with SPACE, LEADER_SPACE, or LINE_BREAK.
	LINE_BREAK		// Line break that ends a paragraph.
}
