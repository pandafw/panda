package panda.tool.poi.xls;

import org.apache.poi.ss.usermodel.HeaderFooter;


public class EHeaderFooter implements HeaderFooter {
	private String left;
	private String center;
	private String right;
	/**
	 * @return the left
	 */
	public String getLeft() {
		return left;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(String left) {
		this.left = left;
	}
	/**
	 * @return the center
	 */
	public String getCenter() {
		return center;
	}
	/**
	 * @param center the center to set
	 */
	public void setCenter(String center) {
		this.center = center;
	}
	/**
	 * @return the right
	 */
	public String getRight() {
		return right;
	}
	/**
	 * @param right the right to set
	 */
	public void setRight(String right) {
		this.right = right;
	}
	
}
