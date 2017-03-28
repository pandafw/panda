package panda.mvc.view.sitemesh;

public class Page {
	private String title;
	private String head;
	private String body;
	
	
	/**
	 * @param title the title
	 * @param head the head
	 * @param body the body
	 */
	public Page(String title, String head, String body) {
		super();
		this.title = title;
		this.head = head;
		this.body = body;
	}
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
	 * @return the head
	 */
	public String getHead() {
		return head;
	}
	/**
	 * @param head the head to set
	 */
	public void setHead(String head) {
		this.head = head;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	
}
