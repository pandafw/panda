package panda.mvc.view.tag;

import java.util.List;

import panda.mvc.view.tag.ui.ListView.ItemLink;


public class ListColumn {
	public static class Format {
		public String type;
		public String expr;
		public Object codemap;
		public String escape;
	}
	
	public static class Filter {
		public String type;
		public boolean fixed;
		public String label;
		public String tooltip;
		public boolean display = true;
		public Object list;
	}

	public String name;
	public String type;
	public String header;
	public int width;
	public String tooltip;
	public boolean hidden;
	public boolean display = true;

	public boolean fixed;
	public boolean nowrap;
	public boolean group;
	public boolean pkey;
	public boolean enabled;
	public boolean value;

	public Format format;
	public Filter filter;
	
	public Object link;
	public List<ItemLink> actions;
	
	public boolean sortable = false;
	public boolean filterable = true;
}
