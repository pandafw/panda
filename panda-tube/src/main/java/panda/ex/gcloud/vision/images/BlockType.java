package panda.ex.gcloud.vision.images;

/** 
 * Type of a block (text, image etc) as identified by OCR.
 */
public enum BlockType {
	UNKNOWN,	// Unknown block type.
	TEXT,		// Regular text block.
	TABLE,		// Table block.
	PICTURE,	// Image block.
	RULER,		// Horizontal/vertical line box.
	BARCODE		//Barcode block.
}
