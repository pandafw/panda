package panda.mvc.view.util;

import java.util.List;

import panda.mvc.view.tag.ui.ListView.ItemLink;


public class ListColumn {
	public static class Format {
		public String type;
		public String expr;
		public Object codemap;
		public String pattern;
		public String escape;
	}

	public String name;
	public String type;
	public String header;
	public String tooltip;
	public String cssClass;

	public boolean display = true;
	public boolean filterable = true;
	public boolean sortable = false;

	public boolean fixed;
	public boolean group;
	public boolean pkey;
	public boolean value;
	public boolean hidden;

	public Format format;
	public ListFilter filter;
	
	public Object link;
	public List<ItemLink> actions;
}
