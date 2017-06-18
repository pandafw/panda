package panda.ex.wordpress.bean;


public class TermFilter extends BaseFilter {

	public String orderby;
	public String order;
	
	/** Whether to return terms with count=0. */
	public boolean hide_empty;
	
	/** Restrict to terms with names that contain (case-insensitive) this value. */
	public String search;
}
