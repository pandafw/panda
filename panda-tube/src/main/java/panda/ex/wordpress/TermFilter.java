package panda.ex.wordpress;


public class TermFilter extends Filter {

	public String orderby;
	public String order;
	
	/** Whether to return terms with count=0. */
	public boolean hide_empty;
	
	/** Restrict to terms with names that contain (case-insensitive) this value. */
	public String search;

	public TermFilter offset(Integer offset, Integer number) {
		return (TermFilter)super.offset(offset, number);
	}
	
	public TermFilter orderBy(String orderBy, String order) {
		this.orderby = orderBy;
		this.order = order;
		return this;
	}
}
