package panda.tool.poi.ppt;

import java.util.LinkedHashMap;
import java.util.Map;

public class ESlide {
	private EHeaderFooter header;
	private Map<String, EComment> comments = new LinkedHashMap<String, EComment>();
	private Map<String, String> texts = new LinkedHashMap<String, String>();
	private Map<String, String> notes = new LinkedHashMap<String, String>();
	
	/**
	 * @return the header
	 */
	public EHeaderFooter getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(EHeaderFooter header) {
		this.header = header;
	}
	/**
	 * @return the comments
	 */
	public Map<String, EComment> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(Map<String, EComment> comments) {
		this.comments = comments;
	}
	/**
	 * @return the texts
	 */
	public Map<String, String> getTexts() {
		return texts;
	}
	/**
	 * @param texts the texts to set
	 */
	public void setTexts(Map<String, String> texts) {
		this.texts = texts;
	}
	/**
	 * @return the notes
	 */
	public Map<String, String> getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(Map<String, String> notes) {
		this.notes = notes;
	}

}
