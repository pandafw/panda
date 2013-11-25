package panda.tool.poi.doc;

import java.util.Map;

import panda.tool.poi.ESummary;


public class EDocument {
	private ESummary summary;
	private Map<Integer, EParagraph> main;
	private Map<Integer, EParagraph> comments;
	private Map<Integer, EParagraph> footnote;
	private Map<Integer, EParagraph> endnote;
	private Map<Integer, EParagraph> header;
	private Map<Integer, EParagraph> textbox;
	
	/**
	 * @return the summary
	 */
	public ESummary getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(ESummary summary) {
		this.summary = summary;
	}

	/**
	 * @return the main
	 */
	public Map<Integer, EParagraph> getMain() {
		return main;
	}

	/**
	 * @param main the main to set
	 */
	public void setMain(Map<Integer, EParagraph> main) {
		this.main = main;
	}

	/**
	 * @return the comments
	 */
	public Map<Integer, EParagraph> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(Map<Integer, EParagraph> comments) {
		this.comments = comments;
	}

	/**
	 * @return the footnote
	 */
	public Map<Integer, EParagraph> getFootnote() {
		return footnote;
	}

	/**
	 * @param footnote the footnote to set
	 */
	public void setFootnote(Map<Integer, EParagraph> footnote) {
		this.footnote = footnote;
	}

	/**
	 * @return the endnote
	 */
	public Map<Integer, EParagraph> getEndnote() {
		return endnote;
	}

	/**
	 * @param endnote the endnote to set
	 */
	public void setEndnote(Map<Integer, EParagraph> endnote) {
		this.endnote = endnote;
	}

	/**
	 * @return the header
	 */
	public Map<Integer, EParagraph> getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(Map<Integer, EParagraph> header) {
		this.header = header;
	}

	/**
	 * @return the textbox
	 */
	public Map<Integer, EParagraph> getTextbox() {
		return textbox;
	}

	/**
	 * @param textbox the textbox to set
	 */
	public void setTextbox(Map<Integer, EParagraph> textbox) {
		this.textbox = textbox;
	}

}

