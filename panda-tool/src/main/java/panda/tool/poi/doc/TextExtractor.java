package panda.tool.poi.doc;

/**
 * 
 */
public abstract class TextExtractor extends DocTextProcessor {
	protected boolean extractSummary = false;
	protected boolean extractHeader = false;
	protected boolean extractFooter = false;
	
	/**
	 * @return the extractSummary
	 */
	public boolean isExtractSummary() {
		return extractSummary;
	}

	/**
	 * @param extractSummary the extractSummary to set
	 */
	public void setExtractSummary(boolean extractSummary) {
		this.extractSummary = extractSummary;
	}

	/**
	 * @return the extractHeader
	 */
	public boolean isExtractHeader() {
		return extractHeader;
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	public void setExtractHeader(boolean extractHeader) {
		this.extractHeader = extractHeader;
	}

	/**
	 * @return the extractFooter
	 */
	public boolean isExtractFooter() {
		return extractFooter;
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	public void setExtractFooter(boolean extractFooter) {
		this.extractFooter = extractFooter;
	}

}
