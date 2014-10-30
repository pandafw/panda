package panda.mvc.view.tag;


public class ListColumn {
	public static class Format {
		public String type;
		public String expression;
		public Object codemap;
	}
	
	public static class Filter {
		public String type;
	}

	public String name;
	public String type;
	public String header;
	public boolean hidden;
	public Format format;
	public Filter filter;
}
