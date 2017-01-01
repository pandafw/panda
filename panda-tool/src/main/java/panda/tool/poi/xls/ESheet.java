package panda.tool.poi.xls;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.HeaderFooter;

public class ESheet {
	private int index;
	private String name;
	private HeaderFooter header;
	private HeaderFooter footer;
	private Map<String, String> strings = new LinkedHashMap<String, String>();
	private Map<String, String> comments = new LinkedHashMap<String, String>();
	private Map<String, String> textboxs = new LinkedHashMap<String, String>();
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the header
	 */
	public HeaderFooter getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(HeaderFooter header) {
		this.header = header;
	}
	/**
	 * @return the footer
	 */
	public HeaderFooter getFooter() {
		return footer;
	}
	/**
	 * @param footer the footer to set
	 */
	public void setFooter(HeaderFooter footer) {
		this.footer = footer;
	}
	/**
	 * @return the strings
	 */
	public Map<String, String> getStrings() {
		return strings;
	}
	/**
	 * @param strings the strings to set
	 */
	public void setStrings(Map<String, String> strings) {
		this.strings = strings;
	}
	/**
	 * @return the comments
	 */
	public Map<String, String> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}
	/**
	 * @return the textboxs
	 */
	public Map<String, String> getTextboxs() {
		return textboxs;
	}
	/**
	 * @param textboxs the textboxs to set
	 */
	public void setTextboxs(Map<String, String> textboxs) {
		this.textboxs = textboxs;
	}
	
	public String toString() {
		return index + ": " + name;
	}
}

