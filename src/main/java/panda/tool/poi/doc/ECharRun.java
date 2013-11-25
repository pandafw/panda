package panda.tool.poi.doc;



public class ECharRun {
	public final static String ECR_SPECIAL = "S";
	public final static String ECR_OBJECT = "O";
	public final static String ECR_OLE2 = "2";
	public final static String ECR_TEXT = "T";
	public final static String ECR_IGNORE = "I";
	public final static String ECR_MARK = "M";
	
	private String type;
	private String text;
	private String title;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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

}

