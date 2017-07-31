package panda.idx;

import java.util.List;

public class IResult {
	private long totalHits;
	private List<IDocument> documents;
	
	/**
	 * @return the totalHits
	 */
	public long getTotalHits() {
		return totalHits;
	}
	/**
	 * @param totalHits the totalHits to set
	 */
	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}
	/**
	 * @return the documents
	 */
	public List<IDocument> getDocuments() {
		return documents;
	}
	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<IDocument> documents) {
		this.documents = documents;
	}
	
}
