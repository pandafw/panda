package panda.tool.poi;

import org.apache.poi.hpsf.SummaryInformation;
import org.w3c.dom.Element;


public class ESummary {
	private String title;
	private String author;
	private String comments;
	private String keywords;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public void set(SummaryInformation si) {
		si.setTitle(getTitle());
		si.setAuthor(getAuthor());
		si.setComments(getComments());
		si.setKeywords(getKeywords());
	}
	
	public void copy(SummaryInformation si) {
		setTitle(si.getTitle());
		setAuthor(si.getAuthor());
		setComments(si.getComments());
		setKeywords(si.getKeywords());
	}
	
	public void copy(Element el) {
		setAuthor(el.getAttribute("author"));
		setComments(el.getAttribute("comments"));
		setKeywords(el.getAttribute("keywords"));
		setTitle(el.getAttribute("title"));
	}
}
